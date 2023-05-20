import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Decoding of input file in huffman coding and writing output into new file.
 */
public class Decode {
    public void decode(Path compressedHuffmanInput, Path huffmanCodeCollection, Path decompress) throws IOException{
        byte[] bytes = fileToByteArray(compressedHuffmanInput);

        String bitString = getBitString(bytes);
        bitString = everythingUpToLastOne(bitString);

        Map<String, Character> code = loadCodeCollection(Files.readString(huffmanCodeCollection, StandardCharsets.US_ASCII).trim());
        String decoded = decodeBitString(bitString, code);

        Files.writeString(decompress, decoded);
    }
    /**
     * Read file into byte array.
     */
    public byte[] fileToByteArray(Path compressedHuffmanInput) throws IOException {
        return Files.readAllBytes(compressedHuffmanInput);
    }

    /**
     * Convert byte array to bit string.
     */
    public String getBitString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte aByte : bytes) {
            String bitString = Integer.toBinaryString(Byte.toUnsignedInt(aByte));

            // Putting missing leading 0s back in the front
            bitString = String.format("%8s", bitString).replace(' ', '0');
            sb.append(bitString);
        }
        return sb.toString();
    }

    /**
     * Cut off last 1 and everything after from a bit string.
     */
    public String everythingUpToLastOne(String bitString) {
        return bitString.substring(0, bitString.lastIndexOf("1"));
    }

    /**
     * Converts huffman code collection string into map.
     * The string format:
     * {ascii decimal value}:{path}-{ascii decimal value}:{path}
     * e.g.: 10:1010111-13:1010110
     */
    public Map<String, Character> loadCodeCollection(String codeCollection) {
        String[] codesFromCollection = codeCollection.split("-");

        return Arrays.stream(codesFromCollection)
                .map(x -> x.split(":"))
                .collect(Collectors.toMap(x -> x[1], x -> (char) Integer.valueOf(x[0]).intValue()));
    }

    /**
     * Decode the given bit string with a huffman code collection.
     */
    public String decodeBitString(String inputString, Map<String, Character> codeCollection) {
        StringBuilder match = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {
            match.append(inputString.charAt(i));
            if (codeCollection.containsKey(match.toString())) {
                result.append(codeCollection.get(match.toString()));
                match = new StringBuilder();
            }
        }

        return result.toString();
    }
}
