import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

/**
 * Methods to encode data with huffman encoding.
 */
public class Encode {
    public void encode(Path asciiInputPath, Path huffmanDataFile, Path codeCollection) throws IOException {
        //todo
        String inputFromFile = readFile(asciiInputPath);
        int[] frequencyTable = getFrequencyTable(inputFromFile);
        //createMiniHeap(frequencyTable);
                // Create a tree from the frequency table. All leaves are single characters,
                // their parent node has the added frequency of its two children.
        //createHuffmanCode(minHeapRoot);
                //Create the huffman code from a tree.
                //e.g. every branch to the left adds a 0 to the way, every one to the right a 1.
                //Once a node has no left and no right node, it's a leaf -> its path and character are added to the code collection.
        //writeHuffmanToFile(code, codeBook);

        //createBitString(code, input);
                //Create bit string from ascii input and a huffman code book.
                //Encoded bit string, with an additional 1 and filled up with 0 until the next multiple of 8.
        //createByteArray(bitString); bitString must be multiple of 8
                //8 consecutive characters are 1 byte
        //writeData(bytes, huffmanDataFile); huffmanDataFile being the output file
    }


    /**
     * Read a file to a string.
     */
    public String readFile(Path pathAsciiFile) throws IOException {
        return Files.readString(pathAsciiFile, StandardCharsets.US_ASCII);
    }

    /**
     * Create a frequency table of every char from input String
     * In total there are 128 ascii chars, so length of array = 128.
     * The index represents the character (ASCII value) and the value
     * at that index represents the frequency of that character in
     * the input string.
     */

    public int[] getFrequencyTable(String input) {
        int[] fTable = new int[128];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            fTable[c]++;
        }

        int[] streamlinedTable = Arrays.stream(fTable)
                .map(i -> (int) (Math.ceil ((double) i / input.length() * 100))) // Math.ceil -> So that frequencies between 0 and 1 are not automatically shown as 0.
                .toArray();

        return streamlinedTable;
    }



}
