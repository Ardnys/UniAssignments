/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package Compressor;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;

/*
 * FileChooserDemo2.java requires these files:
 *   ImageFileView.java
 *   ImageFilter.java
 *   ImagePreview.java
 *   Utils.java
 *   images/jpgIcon.gif (required by ImageFileView.java)
 *   images/gifIcon.gif (required by ImageFileView.java)
 *   images/tiffIcon.gif (required by ImageFileView.java)
 *   images/pngIcon.png (required by ImageFileView.java)
 */
public class CompressionGUI extends JPanel
                              implements ActionListener, ItemListener {
    static private String newline = "\n";
    private JTextArea log;
    private JFileChooser fc;
    private File file;
    private CompressionAlgorithm algorithm;
    private Huffman huffmanAlgorithm;
    private LZW lzwAlgorithm;
    private JToggleButton toggleButton;
    private FileAyo FIO;

    public CompressionGUI() {
        super(new GridLayout(3,3));

        GridLayout l = (GridLayout) super.getLayout();
        l.setVgap(10);
        l.setHgap(10);
        JButton compressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO compress here
                if (file != null) {
                    if (!toggleButton.isSelected()) {
                        log.append("initiate huffman compression\n");
                        algorithm = new Huffman();
                        initFileIO(algorithm.getFileFormat());
                        // huffman is messy i'll do it later
                        byte[] bytes = FIO.readFile(false);
                        byte[] compressedBytes = algorithm.compress(bytes);
                        // however, compressedBytes is not enough: we need the hashtable we used to encode the messages
                        // as well as the length of the encoded messages
                        // FIO.writeFile();
                        /*
                        HUFFMAN WORKS ON ITS OWN BECAUSE OF MY DESIGN FLAW
                        BUT ALSO DOESN'T WORK AFTER SOME POINT I COULDN'T FIGURE IT OUT
                         */




                    }
                    else if (toggleButton.isSelected()) {
                        log.append("initiate lzw compression\n");
                        algorithm = new LZW();
                        initFileIO(algorithm.getFileFormat());
                        // System.out.println(algorithm.getFileFormat());
                        // this one works
                        byte[] bytes = FIO.readFile(false);
                        byte[] compressedBytes = algorithm.compress(bytes);
                        short[] shorts = new short[compressedBytes.length/2];
                        ByteBuffer.wrap(compressedBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                        FIO.writeFile(compressedBytes, true);

                    }
                } else {
                    log.append("you haven't chosen a file ya dingus\n");
                }
            }
        });
        compressButton.setHorizontalAlignment(SwingConstants.CENTER);
        JButton decompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO decompression here
                if (file != null) {
                    if (!toggleButton.isSelected()) {
                        log.append("initiate huffman decompression\n");
                        // there's another case but we'll just not right now
                        assert FIO.getFileFormat().equals(algorithm.getFileFormat());
                        FIO.initStreamForHuffman();                                // first initialise the stream because different objects will be read
                        int bodyLength = FIO.readHuffmanHeader();                  // second, read huffman header
                        Map<String, Byte> decodingMap = FIO.readHuffmanTable();    // third, read decoding table
                        byte[] bytes = FIO.readHuffmanBytes(bodyLength);           // finally, read the encoded bytes

                    }
                    else if (toggleButton.isSelected()){
                        log.append("initiate lzw decompression\n");
                        //algorithm = new LZW();
                        //initFileIO(algorithm.getFileFormat());
                        //assert algorithm instanceof LZW; // it's gonna blow up :D
                        assert FIO.getFileFormat().equals(algorithm.getFileFormat()); // nukes out

                        byte[] bytes = FIO.readFile(true);
                        byte[] decompressedBytes = algorithm.decompress(bytes);
                        FIO.writeFile(decompressedBytes, false);
                    }

                } else {
                    log.append("you haven't chosen a file ya dingus\n");
                }
            }
        });
        decompressButton.setHorizontalAlignment(SwingConstants.CENTER);


        JButton chooseFileButton = new JButton("open file explorer");
        chooseFileButton.addActionListener(this);
        chooseFileButton.setHorizontalAlignment(SwingConstants.CENTER);

        toggleButton = new JToggleButton("Huffman (Click to Change)");
        toggleButton.addItemListener(this);


        compressButton.setText("Compress File");
        decompressButton.setText("Decompress File");
        // chooseFileButton.setText("Choose File for Compression");

//        JLabel fileLabel = new JLabel("File to be compressed");
//        JLabel compressedLabel = new JLabel("Compressed file");

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        JLabel label = new JLabel("HUFFMAN DOESN'T WORK");
        add(toggleButton);
        add(decompressButton);
        add(compressButton);
        add(logScrollPane);
        add(chooseFileButton);
        add(label);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (toggleButton.isSelected()) {
            toggleButton.setText("LZW (Click to Change)");
        } else {
            toggleButton.setText("Huffman (Click to Change) DOESN'T WORK DON'T PRESS");
        }

    }
    public void actionPerformed(ActionEvent e) {
        //Set up the file chooser.
        if (fc == null) {
            String rootPath = System.getProperty("user.dir");

            fc = new JFileChooser(rootPath);

	    //Add a custom file filter and disable the default
	    //(Accept All) file filter.
            fc.addChoosableFileFilter(new ImageFilter());
            fc.setAcceptAllFileFilterUsed(false);

	    //Add custom icons for file types.
            fc.setFileView(new ImageFileView());

	    //Add the preview pane.
            fc.setAccessory(new ImagePreview(fc));
        }

        //Show it.
        int returnVal = fc.showDialog(CompressionGUI.this,
                                      "Choose");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile(); // this is the file
            log.append("Attaching file: " + file.getName()
                       + "." + newline);
        } else {
            log.append("Attachment cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

        //Reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);
    }

    private void initFileIO(String format) {
        FIO = FileAyo.getInstance(file.getPath(), format);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new CompressionGUI());

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
