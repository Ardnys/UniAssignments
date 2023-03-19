package inputoutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyGUITest extends JPanel {

    public MyGUITest() {
        super(new GridLayout(3, 3));

        /*
            THIS IS GONNA BE INSIDE THE FileChooserDemo2.java
            Like the landing page of the file chooser.
            That class is going to connect the compressions too somehow
         */

        // them buts
        JButton huffmanCompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("huffman compressed");
                // TODO calls huffman compression
            }
        });
        JButton lzwCompressionButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("lzw compressed");
                // TODO calls lzw compression
            }
        });
        JButton huffmanDecompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("huffman decompressed");
                // TODO calls huffman decompression
            }
        });
        JButton lzwDecompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("lzw decompressed");
                // TODO calls lzw decompression
            }
        });

        JButton chooseFileButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO initialise fc here
            }
        });

        huffmanCompressButton.setText("Huffman Compression");
        huffmanDecompressButton.setText("Huffman Decompression");
        lzwCompressionButton.setText("LZW Compression");
        lzwDecompressButton.setText("LZW Decompression");
        chooseFileButton.setText("Choose File for Compression");

        JLabel fileLabel = new JLabel("File to be compressed");
        JLabel compressedLabel = new JLabel("Compressed file");

        add(huffmanCompressButton);
        add(lzwCompressionButton);
        add(huffmanDecompressButton);
        add(lzwDecompressButton);
        add(fileLabel);
        add(compressedLabel);
        add(chooseFileButton);

    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame();


        frame.add(new MyGUITest(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sketchy Compressor");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
