import java.io.File;

public class DirReader {
    private final File[] textFiles;

    public DirReader(String path) {
        File file = new File(path); // all docs file
        textFiles = file.listFiles();
    }
    public File[] getTextFiles() {
        return textFiles;
    }

}
