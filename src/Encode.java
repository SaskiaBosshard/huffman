import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Encoding of input file in huffman coding and writing output into new file.
 */
public class Encode {
    public void encode(Path asciiInputPath, Path huffmanDataFile, Path codeCollection) throws IOException {
        String inputFromFile = readFile(asciiInputPath);

        int[] frequencyTable = getFrequencyTable(inputFromFile);
        TreeNode huffmanTree = createHuffmanTree(frequencyTable);
        Map<Character, String> code = createHuffmanCode(huffmanTree);

        writeHuffmanToFile(code, codeCollection);
        String bitString = createBitString(code, inputFromFile);
        byte[] b = toByteArray(bitString);
        writeData(b, huffmanDataFile);
    }

    /**
     * Read a file to a string.
     */
    public String readFile(Path pathAsciiFile) throws IOException {
        return Files.readString(pathAsciiFile, StandardCharsets.US_ASCII);
    }

    /**
     * Create a frequency table of every char from input String
     * Length of array = number of ascii chars = 128.
     * The index represents the character (ASCII value) and the value
     * at that index represents the frequency of that character in
     * the input string.

     * @return streamlined frequency table
     */

    public int[] getFrequencyTable(String input) {
        int[] fTable = new int[128];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            fTable[c]++;
        }

        int[] streamlinedTable = Arrays.stream(fTable) // Source: ChatGPT
                .map(i -> (int) (Math.ceil ((double) i / input.length() * 100))) // Math.ceil -> So that frequencies between 0 and 1 are not automatically shown as 0.
                .toArray();

        return streamlinedTable;
    }

    /**
     * Create a tree from a frequency table. All leaves are single characters,
     * their parent node has the added frequency of its two children.
     * The algorithm is taken from this source:
     * https://www.techiedelight.com/huffman-coding/
     *
     * @return root TreeNode of the created tree
     */

    public static TreeNode createHuffmanTree(int[] fTable) {
        PriorityQueue<TreeNode> minHeap = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        // Step 1: Create leaf nodes for characters and add them to the priority queue
        for (int i = 0; i < fTable.length; i++) {
            if (fTable[i] > 0) {
                char c = (char) i;
                TreeNode leafNode = new TreeNode(c, fTable[i]);
                minHeap.offer(leafNode);
            }
        }

        // Step 2: Build the Huffman tree by combining nodes until only one node is left in the heap
        while (minHeap.size() > 1) {
            TreeNode leftChild = minHeap.poll();
            TreeNode rightChild = minHeap.poll();

            int combinedFrequency = leftChild.frequency + rightChild.frequency;
            TreeNode parentNode = new TreeNode('\0', combinedFrequency);
            parentNode.left = leftChild;
            parentNode.right = rightChild;

            minHeap.offer(parentNode);
        }

        // Step 3: Return the root node of the Huffman tree
        return minHeap.poll();
    }

    /**
     * Create huffman code from a previously created Huffman tree.
     * Left branches adds a 0 and right branches a 1.
     * An ascii-Character and its specific path are added to the code collection
     * when they are a leaf (treeNode with no left or right node).
     *
     * Returns the ascii-character's specific path.
     */
    public Map<Character, String> createHuffmanCode(TreeNode rootOfTree) {
        Map<Character, String> map = new HashMap<>();
        traceTree(map, rootOfTree, "");
        return map;
    }

    /**
     * Method which allows to trace along the Huffman tree
     */
    private void traceTree(Map<Character, String> map, TreeNode rootOfTree, String way) {
        if (rootOfTree.left == null && rootOfTree.right == null) {
            map.put(rootOfTree.ascii, way);
        } else {
            traceTree(map, rootOfTree.left, way + "0");
            traceTree(map, rootOfTree.right, way + "1");
        }
    }

    /**
     * Writes a huffman code collection to a file.
     * Example of the created format: 10:1010111-13:1010110
     */
    public void writeHuffmanToFile(Map<Character, String> code, Path outPath) throws IOException {
        String output = code.keySet().stream()
                .sorted()
                .map(x -> String.format("%d:%s", (int) x, code.get(x)))
                .collect(Collectors.joining("-"));

        Files.writeString(outPath, output, StandardCharsets.US_ASCII);
    }

    /**
     * Creates a bit string from ascii input file and a huffman code collection.
     * @return Encoded bit string, with an additional 1 and filled up with 0 until the next multiple of 8
     */
    public String createBitString(Map<Character, String> code, String inputFromFile) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < inputFromFile.length(); i++) {
            char charFromFile = inputFromFile.charAt(i);
            String value = code.get(charFromFile);
            sb.append(value);
        }
        sb.append("1");

        int zeroLength = 8 - sb.length() % 8;
        for (int i = 0; i < zeroLength; i++) {
            sb.append("0");
        }

        //assert (sb.length() % 8 == 0); // simple test, it always has to be a multiple of BYTE_LENGTH.

        return sb.toString();
    }

    /**
     * Creates a byte array from a bit string.
     * @return Byte array which represents the input bit string.
     */
    public byte[] toByteArray(String bitString) {
        int numberOfBytes = bitString.length() / 8;
        byte[] byteArray = new byte[numberOfBytes];

        for (int i = 0; i < bitString.length(); i += 8) {
            String bits = bitString.substring(i, i + 8);
            Integer x = Integer.parseInt(bits, 2);
            byte b = x.byteValue();
            byteArray[i / 8] = b;
        }
        return byteArray;
    }

    public void writeData(byte[] data, Path outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath.toString())) {
            fos.write(data);
        }
    }


}
