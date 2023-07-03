import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DefaultHighlighter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class Editor extends JFrame implements ActionListener {
    JTextArea textArea;
    JFrame frame;
    JFileChooser fileChooser;
    Highlighter highlighter;
    HighlightPainter highlightPainter;
    HashSet<String> dictionary;
    boolean dictionaryLoaded;
    Hash hash = new Hash(10);

    Editor() {
        System.setProperty("file.encoding", "UTF-8");
        frame = new JFrame("Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a text area
        textArea = new JTextArea();

        DefaultContextMenu.addDefaultContextMenu(textArea, hash);

        // Create a scroll pane and add the text area to it
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create the File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem printMenuItem = new JMenuItem("Print");
        JMenuItem closeMenuItem = new JMenuItem("Close");

        // Add action listeners to menu items
        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        closeMenuItem.addActionListener(this);

        // Add menu items to the File menu
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.add(closeMenuItem);

        // Create the Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        JMenuItem copyMenuItem = new JMenuItem("Copy");
        JMenuItem pasteMenuItem = new JMenuItem("Paste");

        // Add action listeners to menu items
        cutMenuItem.addActionListener(this);
        copyMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);

        // Add menu items to the Edit menu
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);

        // Add the menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Set the size of the frame
        frame.setSize(500, 500);

        // Initialize the highlighter
        highlighter = textArea.getHighlighter();
        highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        // Load the dictionary
        loadDictionary();

        // Add document listener to the text area
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateWords();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateWords();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateWords();
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    // // Load the dictionary from file
    // private void loadDictionary() {

    // try {
    // BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
    // String word;
    // dictionary = new HashSet<>();
    // while ((word = reader.readLine()) != null) {
    // hash.inserir(new Palavra(word.toLowerCase()));
    // dictionary.add(word.toLowerCase());
    // }
    // reader.close();
    // dictionaryLoaded = true;
    // } catch (IOException e) {
    // JOptionPane.showMessageDialog(frame, "Dictionary File Not Found");
    // dictionaryLoaded = false;
    // }
    // }

    // Load the dictionary from file
    private void loadDictionary() {
        try {
            Charset cs = Charset.forName("UTF-8");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("dictionary.txt"), cs));
            String word;
            dictionary = new HashSet<>();
            while ((word = reader.readLine()) != null) {
                hash.inserir(new Palavra(word.toLowerCase()));
                dictionary.add(word.toLowerCase());
            }
            reader.close();
            dictionaryLoaded = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Dictionary File Not Found");
            dictionaryLoaded = false;
        }
    }

    // Validate words in the text and highlight the ones not in the dictionary
    private void validateWords() {
        if (dictionaryLoaded) {
            String text = textArea.getText();

            highlighter.removeAllHighlights();

            String[] words = text.split("\\s");

            System.out.println("###### WORDS");
            for (String word : words) {
                System.out.println(word);
            }
            System.out.println("######");

            for (String word : words) {
                if (!hash.buscar(word.toLowerCase(getLocale()))) {
                    try {

                        List<Integer> ocorrencias = countWord(word, text);

                        for (int item : ocorrencias) {
                            int startOcorrencia = item;
                            int endOcorrencia = startOcorrencia + word.length();
                            highlighter.addHighlight(startOcorrencia, endOcorrencia, highlightPainter);
                        }
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(hash);
        }
    }

    public List<Integer> countWord(String palavra, String texto) {
        Pattern pattern = Pattern.compile("\\b" + palavra + "\\b");
        Matcher matcher = pattern.matcher(texto);

        List<Integer> ocorrencias = new ArrayList<>();
        while (matcher.find()) {
            Integer indiceInicio = matcher.start();
            ocorrencias.add(indiceInicio);
        }
        return ocorrencias;
    }

    // Handle menu item actions
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("New")) {
            newFile();
        } else if (action.equals("Open")) {
            openFile();
        } else if (action.equals("Save")) {
            saveFile();
        } else if (action.equals("Print")) {
            printFile();
        } else if (action.equals("Cut")) {
            textArea.cut();
        } else if (action.equals("Copy")) {
            textArea.copy();
        } else if (action.equals("Paste")) {
            textArea.paste();
        } else if (action.equals("Close")) {
            frame.setVisible(false);
        }
    }

    // Create a new file
    private void newFile() {
        textArea.setText("");
    }

    // Open a file
    // private void openFile() {
    // fileChooser = new JFileChooser();
    // int option = fileChooser.showOpenDialog(frame);
    // if (option == JFileChooser.APPROVE_OPTION) {
    // File file = fileChooser.getSelectedFile();
    // try {
    // BufferedReader reader = new BufferedReader(new FileReader(file));
    // StringBuilder text = new StringBuilder();
    // String line;
    // while ((line = reader.readLine()) != null) {
    // text.append(line).append("\n");
    // }
    // reader.close();
    // textArea.setText(text.toString());
    // } catch (IOException e) {
    // JOptionPane.showMessageDialog(frame, "Failed to open file: " +
    // e.getMessage());
    // }
    // }
    // }

    // Open a file
    private void openFile() {
        fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
                reader.close();
                textArea.setText(text.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to open file: " + e.getMessage());
            }
        }
    }

    // Save the file
    private void saveFile() {
        fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to save file: " + e.getMessage());
            }
        }
    }

    // Print the file
    private void printFile() {
        try {
            textArea.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(frame, "Failed to print file: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String args[]) {
        Editor editor = new Editor();
    }
}