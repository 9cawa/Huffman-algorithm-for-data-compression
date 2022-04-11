package aksenov.coursework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
//        String text = "aabaacca";
//
//        //Считаем сколько раз каждый символ встречается в сообщении
//        TreeMap<Character, Integer> frequencies = countFrequency(text);
//
//        //Список узлов для листов дерева
//        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
//
//        //Запонлняем список
//        for (Character c : frequencies.keySet()) {
//            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
//        }
//
//        //Дерево без кодов
//        CodeTreeNode tree = huffman(codeTreeNodes);
//
//        //Формируем префиксные коды для каждого символа
//        TreeMap<Character, String> codes = new TreeMap<>();
//        for (Character c : frequencies.keySet()) {
//            codes.put(c, tree.getCodeForCharacter(c, ""));
//        }
//
//        System.out.println("Таблица префиксных кодов: " + codes);
//
//        StringBuilder encoded = new StringBuilder();
//        for (int i = 0; i < text.length(); i++) {
//            encoded.append(codes.get(text.charAt(i)));
//        }
//
//        System.out.println("Размер исходной строки: " + text.getBytes().length * 8 + " бит");
//        System.out.println("Размер сжатой строки: " + encoded.length() + " бит");
//        System.out.println("Биты сжатой строки: " + encoded);
//
//        String decoded = huffmanDecode(encoded.toString(), tree);
//        System.out.println("\nРасшифровано: " + decoded);

        if (args[0].equals("compress")) {
            WorkWithFiles.fileCompress(args[1], args[2]);
        } else if (args[0].equals("decompress")) {
            if (args[2].endsWith(".huf"))
                WorkWithFiles.fileDecompress(args[1], args[2]);
            else
                System.out.println("Such file can't be decompressed, please rerun!");
        } else {
            System.out.println("There is no such command, please rerun!");
        }
    }

    /**
     * Метод, который считает, сколько раз каждый символ встречается в сообщении
    */
    static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> freqMap = new TreeMap<>();

        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = freqMap.get(c);
            freqMap.put(c, count != null ? count + 1 : 1);
        }

        return freqMap;
    }

    /**
     * Алгоритм Хаффмана
    */
    static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {

        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            //left и right - два узла с наименьшей частотой
            CodeTreeNode right = codeTreeNodes.remove(codeTreeNodes.size() - 1);
            CodeTreeNode left = codeTreeNodes.remove(codeTreeNodes.size() - 1);

            //Связываем два этих узла с промежуточным
            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right);
            codeTreeNodes.add(parent);
        }

        return codeTreeNodes.get(0);
    }

    /**
     * Декодирование алгоритма
    */
    static String huffmanDecode(String encoded, CodeTreeNode tree) {
        //Здесь будем накапливать расшифрованные данные
        StringBuilder decoded = new StringBuilder();

        //Здесь будем хранить текущий узел
        CodeTreeNode node = tree;

        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if (node.content != null) {
                decoded.append(node.content);
                node = tree;
            }
        }

        return decoded.toString();
    }

}
