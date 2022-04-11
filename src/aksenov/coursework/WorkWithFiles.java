package aksenov.coursework;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static aksenov.coursework.Main.*;

public class WorkWithFiles {

    /**
     * Сохранение таблицы частот и сжатой информации в файл
     */
    private static void saveToFile(File output, Map<Character, Integer> frequencies, String bits) {
        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(output));
            os.writeInt(frequencies.size());
            for (Character character: frequencies.keySet()) {
                os.writeChar(character);
                os.writeInt(frequencies.get(character));
            }
            int compressedSizeBits = bits.length();
            BitArray bitArray = new BitArray(compressedSizeBits);
            for (int i = 0; i < bits.length(); i++) {
                bitArray.set(i, bits.charAt(i) != '0' ? 1 : 0);
            }

            os.writeInt(compressedSizeBits);
            os.write(bitArray.bytes, 0, bitArray.getSizeInBytes());
            os.flush();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загрузка сжатой информации и таблицы частот из файла
     */
    private static void loadFromFile(File input, Map<Character, Integer> frequencies, StringBuilder bits) {
        try {
            DataInputStream os = new DataInputStream(new FileInputStream(input));
            int frequencyTableSize = os.readInt();
            for (int i = 0; i < frequencyTableSize; i++) {
                frequencies.put(os.readChar(), os.readInt());
            }
            int dataSizeBits = os.readInt();
            BitArray bitArray = new BitArray(dataSizeBits);
            os.read(bitArray.bytes, 0, bitArray.getSizeInBytes());
            os.close();

            for (int i = 0; i < bitArray.size; i++) {
                bits.append(bitArray.get(i) != 0 ? "1" : 0);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Алгоритм сжатия текстового файла
     */
    static void fileCompress(String path, String name) {
        try {
            // загрузка содержимого файла в виде строки
            String content = new String(Files.readAllBytes(Paths.get(path + "\\" + name)));

            // вычисление таблицы частот с которыми встречаются символы в тексте
            TreeMap<Character, Integer> frequencies = countFrequency(content);

            ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();

            // генерация листов будущего дерева для символов текста
            for(Character c: frequencies.keySet()) {
                codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
            }
            // построение кодового дерева алгоритмом Хаффмана
            CodeTreeNode tree = huffman(codeTreeNodes);

            // постоение таблицы префиксных кодов для символов исходного текста
            TreeMap<Character, String> codes = new TreeMap<>();
            for (Character c: frequencies.keySet()) {
                codes.put(c, tree.getCodeForCharacter(c, ""));
            }

            // кодирование текста префиксными кодами
            StringBuilder encoded = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                encoded.append(codes.get(content.charAt(i)));
            }

            // сохранение сжатой информации в файл
            File file = new File(path + "\\compressed.huf");
            saveToFile(file, frequencies, encoded.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Алгоритм декомпрессии данных текстового файла
     */
    static void fileDecompress(String path, String name) {
        try {
            File file = new File(path + "\\" + name);

            TreeMap<Character, Integer> frequencies2 = new TreeMap<>();
            StringBuilder encoded2 = new StringBuilder();
            ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();

            // извлечение сжатой информации из файла
            loadFromFile(file, frequencies2, encoded2);

            // генерация листов и построение кодового дерева Хаффмана на основе таблицы частот сжатого файла
            for (Character c : frequencies2.keySet()) {
                codeTreeNodes.add(new CodeTreeNode(c, frequencies2.get(c)));
            }
            CodeTreeNode tree2 = huffman(codeTreeNodes);

            // декодирование обратно исходной информации из сжатой
            String decoded = huffmanDecode(encoded2.toString(), tree2);

            // сохранение в файл декодированной информации
            Files.write(Paths.get(path + "\\decompressed.txt"), decoded.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
