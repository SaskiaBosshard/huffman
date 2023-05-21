import java.io.IOException;
import java.nio.file.Path;

public class Main {
    /**
     * Path to the huffman code.
     */
    private final static Path HUFFMAN_PATH = Path.of("dec_tab-mada.txt");

    /**
     * Path to the compressed input.
     */
    private final static Path COMPRESSED_INPUT_PATH = Path.of("output-mada.dat");

    /**
     * Path to ascii input.
     */
    private final static Path ASCII_INPUT_PATH = Path.of("input.txt");

    /**
     * Path to decompressed input.
     */
    private final static Path DECOMPRESSED_INPUT_PATH = Path.of("decompress.txt");

    /**
     * By using methods in class Encode, read ASCII_INPUT_PATH. Calculate huffman from input and save it in HUFFMAN_PATH.
     * Then, save the compressed bytes into COMPRESSED_INPUT_PATH.
     *
     * By using methods in class Decode, read the files COMPRESSED_INPUT_PATH and HUFFMAN_PATH. Then reconstruct
     * the original text from it and save the result to DECOMPRESSED_INPUT_PATH.
     */
    public static void main(String[] args) throws IOException {
        Encode encode = new Encode();
        //encode.encode(ASCII_INPUT_PATH, COMPRESSED_INPUT_PATH, HUFFMAN_PATH);

        Decode decode = new Decode();
        decode.decode(COMPRESSED_INPUT_PATH, HUFFMAN_PATH, DECOMPRESSED_INPUT_PATH);
    }
}
