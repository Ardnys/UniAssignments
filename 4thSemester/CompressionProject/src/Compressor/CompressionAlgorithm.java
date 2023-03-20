package Compressor;

public interface CompressionAlgorithm {
    byte[] compress(byte[] bytes);
    byte[] decompress(byte[] bytes);

    String getFileFormat();
}
