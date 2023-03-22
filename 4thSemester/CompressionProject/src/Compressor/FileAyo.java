package Compressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FileAyo {
    /*
    THIS IS WHERE READING AND WRITING IS DONE. IT'S DIFFERENT FOR LZW AND HUFFMAN SO IDK HOW IT'S GONNA WORK IN THIS HIERARCHY BUT YE
     */
    private String sourcePath;
    private String decompressPath;
    private File sourceFile;
    private File compressedFile;
    private File decompressedFile;
    private FileOutputStream outputStream;
    private FileOutputStream decompressionOutputStream;
    private ObjectOutputStream objOutStream;
    private ObjectInputStream objInStream;
    private FileInputStream inputStream;
    private FileInputStream decompressionInputStream;

    // S I N G L E T O N
    private static FileAyo instance;

    private FileAyo(String path, String fileFormat) {

        sourceFile = new File(path);
        compressedFile = new File(path + fileFormat);

        int idx = path.indexOf('.')-1;
        decompressPath = path.substring(0, idx) + "decompressed" + path.substring(idx+1);
        decompressedFile = new File(decompressPath);

        try {
            outputStream = new FileOutputStream(compressedFile);
            objOutStream = new ObjectOutputStream(outputStream);
            decompressionOutputStream = new FileOutputStream(decompressedFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the compression output streams");
        }

        try {
            inputStream = new FileInputStream(sourceFile);
            decompressionInputStream = new FileInputStream(compressedFile);
            objInStream = new ObjectInputStream(decompressionInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the compression input streams");
        }
    }
    public static FileAyo getInstance(String path, String fileFormat) {
        if (instance == null) {
            instance = new FileAyo(path, fileFormat);
        }
        return instance;
    }

    /*
    This reads a file for compression. Works for both Huffman and LZW
    This is also used for reading file for LZW decompression
     */
    public byte[] readFile() {
        try {
            return Files.readAllBytes(Paths.get(sourcePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading the file for compression");
        }
        return null;
    }
    /*
    This is for writing Huffman Compression
     */
    public void writeFile(byte[] bytes, int codeLength, Map<?, ?> codingMap) {
        try (ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(compressedFile))) {
            objOutStream.writeInt(codeLength);
            objOutStream.writeObject(codingMap);
            // writing the body for the compressed file
            objOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while writing to compressed file");
        }
    }
    /*
    This is for writing LZW compression. We use shorts here for better compression
     */
    public void writeFile(short[] shorts) {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            for (short code: shorts) {
                System.out.println(code);
                dos.writeShort(code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("problem while writing shorts");
        }
    }
     /*
    ----------------------------------------------------------------------
    |                                                                    |
    |                                                                    |
    |             D  E  C  O  M  P  R  E  S  S  I  O  N                  |
    |                                                                    |
    |                                                                    |
    ----------------------------------------------------------------------
     */

}
