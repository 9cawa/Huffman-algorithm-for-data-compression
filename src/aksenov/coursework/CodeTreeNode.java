package aksenov.coursework;

/**
 * Класс для представления кодового дерева
 */
public class CodeTreeNode implements Comparable<CodeTreeNode> {

    Character content;
    int weight;
    CodeTreeNode left;
    CodeTreeNode right;

    public CodeTreeNode(Character content, int weight) {
        this.content = content;
        this.weight = weight;
    }

    public CodeTreeNode(Character content, int weight, CodeTreeNode left, CodeTreeNode right) {
        this.content = content;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(CodeTreeNode o) {
        return o.weight - weight;
    }

    @Override
    public String toString() {
        return "CodeTreeNode{" +
                "content=" + content +
                ", weight=" + weight +
                '}' + "\n";
    }

    /**
     * Метод, выполняющий проход по кодовому дереву от корня до конкретного символа, и,
     в зависимости от поворота направо/налево вычисляющий последовательность нулей и единиц для каждого символа.

     * Входные параметры: символ для которого вычисляется код и путь в виде нулей и единиц
       (при сворачивании влево - пишем ноль, вправо - единицу)
     **/
    public String getCodeForCharacter(Character ch, String parentPath) {

        if (content == ch) {
            return parentPath;
        } else {
            if (left != null) {
                String path = left.getCodeForCharacter(ch, parentPath + "0");
                if (path != null) {
                    return path;
                }
            }
            if (right != null) {
                String path = right.getCodeForCharacter(ch, parentPath + "1");
                if (path != null) {
                    return path;
                }
            }
        }

        return null;
    }
}
