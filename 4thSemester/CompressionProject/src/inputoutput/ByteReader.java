package inputoutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ByteReader {

    private String path;

    public ByteReader(String path) {
        this.path = path;
    }

    public void readFileBetterPerformance(String fileName) {

        try (FileInputStream fis = new FileInputStream(new File(fileName))) {

            // remaining bytes that can be read
            System.out.println("Remaining bytes that can be read : " + fis.available());

            // 8k a time
            byte[] bytes = new byte[8192];

            // reads 8192 bytes at a time, if end of the file, returns -1
            while (fis.read(bytes) != -1) {

                // convert bytes to string for demo
                System.out.println(new String(bytes, StandardCharsets.UTF_8));

                System.out.println("Remaining bytes that can be read : " + fis.available());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] readFile(File file) {
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);

            //System.out.println(new String(bytes, StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public void readFolder() {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                readFile(file);
            }
        }
    }

}
