package Compressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FileAyo {
    /*
    THIS IS WHERE READING AND WRITING IS DONE. IT'S DIFFERENT FOR LZW AND HUFFMAN SO IDK HOW IT'S GONNA WORK IN THIS HIERARCHY BUT YE
     */
    private String fileFormat;
    private String sourcePath;
    private String compressedPath;
    private String decompressPath;
    //private File sourceFile;
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

        stuff(path, fileFormat);

    }
    public static FileAyo getInstance(String path, String fileFormat) {
        if (instance == null) {
            instance = new FileAyo(path, fileFormat);
        } else {
            instance.setFileFormat(fileFormat);
            instance.stuff(path, fileFormat);
        }
        return instance;
    }

    private void stuff(String path, String fileFormat) {
        sourcePath = path;
        //sourceFile = new File(path);
        compressedPath = path + fileFormat;
        System.out.println("compressed path: " + compressedPath);
        compressedFile = new File(compressedPath);

        int idx = path.indexOf('.')-1;
        decompressPath = path.substring(0, idx+1) + "decompressed" + path.substring(idx+1);
        decompressedFile = new File(decompressPath);
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    /*
    This reads a file for compression. Works for both Huffman and LZW
    This is also used for reading file for LZW decompression
     */
    public byte[] readFile(boolean isCompressed) {
        try {
            if (isCompressed)
                return Files.readAllBytes(Paths.get(compressedPath));
            else
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
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(compressedFile))) {
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

    public void writeFile(byte[] bytes, boolean isCompressed) {
        File file;
        if (isCompressed)
            file = compressedFile;
        else
            file = decompressedFile;
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("werroe while writing decmopressed bop");
        }
    }

    public void initStreamForHuffman() {
        try {
            objInStream = new ObjectInputStream(new FileInputStream(compressedPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("init broke");
        }
    }

    public int readHuffmanHeader() {
        int len = 0;

        try {
            len = objInStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading huffman header");
        }
        return len;
    }

    public Map<String, Byte> readHuffmanTable() {
        try {
            return (Map<String, Byte>) objInStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("io error while reading huffman map");
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
            System.out.println("what do you mean class not found");
        }
        return null;
    }

    public byte[] readHuffmanBytes(int len) {
        byte[] bytes = new byte[len];

        try {
            objInStream.read(bytes);
            objInStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading huffman bytes");
        }
        return bytes;
    }


}
