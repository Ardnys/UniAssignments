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
import javax.swing.*;
import javax.swing.SwingUtilities;

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
                              implements ActionListener {
    static private String newline = "\n";
    private JTextArea log;
    private JFileChooser fc;

    public CompressionGUI() {
        super(new GridLayout(3,3));

        JButton huffmanCompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("huffman compressed");
                log.append("initiate huffman compression\n");
                // TODO calls huffman compression
            }
        });
        JButton lzwCompressionButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("lzw compressed");
                log.append("initiate lzw compression\n");
                // TODO calls lzw compression
            }
        });
        JButton huffmanDecompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("huffman decompressed");
                log.append("initiate huffman decompression\n");
                // TODO calls huffman decompression
            }
        });
        JButton lzwDecompressButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("lzw decompressed");
                log.append("initiate lzw decompression\n");
                // TODO calls lzw decompression
            }
        });

        JButton chooseFileButton = new JButton("Choose file for compression");
        chooseFileButton.addActionListener(this);

        huffmanCompressButton.setText("Huffman Compression");
        huffmanDecompressButton.setText("Huffman Decompression");
        lzwCompressionButton.setText("LZW Compression");
        lzwDecompressButton.setText("LZW Decompression");
        // chooseFileButton.setText("Choose File for Compression");

//        JLabel fileLabel = new JLabel("File to be compressed");
//        JLabel compressedLabel = new JLabel("Compressed file");

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        add(huffmanCompressButton);
        add(lzwCompressionButton);
        add(huffmanDecompressButton);
        add(lzwDecompressButton);
        add(logScrollPane);
//        add(fileLabel);
//        add(compressedLabel);
        add(chooseFileButton);

        // below is the code from docs
        //        super(new BorderLayout());

        //Create the log first, because the action listener
        //needs to refer to it.
//        log = new JTextArea(5,20);
//        log.setMargin(new Insets(5,5,5,5));
//        log.setEditable(false);
//        JScrollPane logScrollPane = new JScrollPane(log);

//        JButton sendButton = new JButton("Choose File...");
//        sendButton.addActionListener(this);
//
//        add(sendButton, BorderLayout.PAGE_START);
//        add(logScrollPane, BorderLayout.CENTER);
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
            File file = fc.getSelectedFile();
            log.append("Attaching file: " + file.getName()
                       + "." + newline);
        } else {
            log.append("Attachment cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

        //Reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);
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
