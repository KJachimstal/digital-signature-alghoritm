import dsa.*;
import dsa.exceptions.CorruptedDataException;
import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
//    Frame
    JFrame frame;
//    Main panel
    public JPanel mainPanel;

    private JButton inputFile;
    private JButton importPublicKeyButton;
    private JTextArea log;
    private JLabel infoInputFile;
    private JTextArea publicKeyTextarea;
    private JButton exportPublicKeyButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton inputText;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton importPrivateKeyButton;
    private JButton exportPrivateKeyButton;
    private JTextArea privateKeyTextarea;
    private JTextArea signatureTextArea;
    private JButton inputSignatureFile;
    private JButton inputSignatureText;
    private JLabel infoSignatureFile;
    private JButton setAsSignatureButton;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    Model
    private byte[] data;
    private byte[] signature;
    private byte[] results;
    private JFileChooser inputChooser;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private boolean canProcess = false;
    private boolean loadedFromFile = false;

    public Application(JFrame frame) {
        this.frame = frame;
        createMenu();

        DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

//        Buttons
        setIcon(inputFile, "file_in.png");
        setIcon(inputText, "keyboard.png");
        setIcon(inputSignatureFile, "file_in.png");
        setIcon(inputSignatureText, "keyboard.png");
        setIcon(importPublicKeyButton, "cipher_key.png");
        setIcon(exportPublicKeyButton, "cipher_key.png");
        setIcon(importPrivateKeyButton, "cipher_key.png");
        setIcon(exportPrivateKeyButton, "cipher_key.png");

        encryptButton.setEnabled(canProcess);
        decryptButton.setEnabled(canProcess);

//        Actions
        inputFile.addActionListener(e -> inputFileDialog());
        inputText.addActionListener(e -> inputTextDialog());
        inputSignatureFile.addActionListener(e -> inputSignatureFileDialog());
        inputSignatureText.addActionListener(e -> inputSignatureTextDialog());
        exportPublicKeyButton.addActionListener(e -> exportCipherKey(true));
        importPublicKeyButton.addActionListener(e -> importCipherKey(true));
        exportPrivateKeyButton.addActionListener(e -> exportCipherKey(false));
        importPrivateKeyButton.addActionListener(e -> importCipherKey(false));
        setAsSignatureButton.addActionListener(e -> setAsSignature());
        encryptButton.addActionListener(e -> sign());
        decryptButton.addActionListener(e -> verify());
    }

    public void inputFileDialog() {
        data = _loadFile(infoInputFile);
        updateButtons();
    }

    public void inputSignatureFileDialog() {
        signature = _loadFile(infoSignatureFile);
        updateButtons();
    }

    private byte[] _loadFile(JLabel fileInfo) {
        inputChooser = new JFileChooser();
        int returnValue = inputChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = inputChooser.getSelectedFile().getPath();
            try {
                byte[] input_data = DataUtils.loadBytes(selectedFile);

                String message = input_data.length + " bytes have been loaded.";
                log("File " + inputChooser.getSelectedFile().getName() + " loaded.");
                log(message);
                fileInfo.setText(inputChooser.getSelectedFile().getName());
                loadedFromFile = true;
                return input_data;
            } catch (Exception ex) {
                String message = "Could not load file: " + selectedFile;
                log(message);
                JOptionPane.showMessageDialog(frame, message, "Loading error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    public void inputTextDialog() {
        data = _loadText(inputTextArea);
        updateButtons();
    }

    public void inputSignatureTextDialog() {
        signature = _loadText(signatureTextArea);
        updateButtons();
    }

    public byte[] _loadText(JTextArea textarea) {
        String input = textarea.getText();
        byte[] input_data = input.getBytes();
        log("Text has been loaded.");
        log(input_data.length + " bytes have been loaded.");
        loadedFromFile = false;

        return input_data;
    }

    private void importCipherKey(boolean type) {
//        true - public, false - private
        String pattern_string = (type ? PublicKey.getPattern() : PrivateKey.getPattern());

        JFileChooser keyChooser = new JFileChooser();
        int returnValue = keyChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = keyChooser.getSelectedFile().getPath();
            try {
                byte[] bytes = DataUtils.loadBytes(selectedFile);
                String key_string = new String(bytes);
                Pattern pattern = Pattern.compile(pattern_string);
                Matcher matcher = pattern.matcher(key_string);
                boolean valid = matcher.matches();
                if (!valid) {
                    throw new Exception("Invalid key");
                }

                if (type) {
                    BigInteger p = new BigInteger(matcher.group(1));
                    BigInteger h = new BigInteger(matcher.group(2));
                    BigInteger q = new BigInteger(matcher.group(3));
                    BigInteger b = new BigInteger(matcher.group(4));
                    publicKey = new PublicKey(p, h, q, b);
                    log("Public key imported.");
                } else {
                    BigInteger a = new BigInteger(matcher.group(1));
                    BigInteger p = new BigInteger(matcher.group(2));
                    BigInteger q = new BigInteger(matcher.group(3));
                    BigInteger h = new BigInteger(matcher.group(4));
                    privateKey = new PrivateKey(a, p, q, h);
                    log("Private key imported.");
                }

                updateKeys();

            } catch (IOException ex) {
                String message = "Could not load file: " + selectedFile;
                log(message);
                JOptionPane.showMessageDialog(frame, message, "Loading error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Loading error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCipherKey(boolean type) {
//        true - public, false - private
        Key key = (type ? publicKey : privateKey);
        if (key != null) {
            JFileChooser keyChooser = new JFileChooser();
            int returnValue = keyChooser.showSaveDialog(mainPanel);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String selectedPath = keyChooser.getSelectedFile().getAbsolutePath();
                try {
                    DataUtils.saveBytes(key.getBytes(), selectedPath);
                    log("Cipher key exported.");
                } catch (Exception ex) {
                    String message = "Could not save file: " + selectedPath;
                    log(message);
                    JOptionPane.showMessageDialog(frame, message, "Save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame,"Key doesn't exists. Please first import key.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setAsSignature() {
        if (results != null) {
            signature = results;
            log("Results are now signature.");
            updateButtons();
        }
    }

    private void generateKey() {
        String number = JOptionPane.showInputDialog("Prime number length: ", "512");
        try {
            int length = Integer.parseInt(number);
            if (length < 512 || length > 1024) {
                throw new Exception("Invalid prime length (only in range 512 - 1024).");
            }
            KeyGenerator keygen = new KeyGenerator(length, 160);
            keygen.generate();
            publicKey = keygen.getPublicKey();
            log("Public key generated.");
            privateKey = keygen.getPrivateKey();
            log("Private key generated.");
            updateKeys();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,"Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKeys() {
        if (publicKey != null) {
            publicKeyTextarea.setText(publicKey.toString());
        }
        if (privateKey != null) {
            privateKeyTextarea.setText(privateKey.toString());
        }
        updateButtons();
    }

    private void sign() {
        if (privateKey != null) {
            canProcess = false;
            _updateButtons();

            log("Preparing signature...");
            Block[] blocks = Operations.generateBlocks(data, privateKey.getMaxLength());
            Sign signature = new Sign(blocks, privateKey);
            log("Starting signature...");
            signature.sign();
            log("Signature completed successfully.");


            byte[] bytes = Operations.blocksToBytes(signature.getResults(), privateKey.getFillSize());
            results = bytes;

            if (loadedFromFile) {
                JFileChooser keyChooser = new JFileChooser();
                int returnValue = keyChooser.showSaveDialog(mainPanel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String selectedPath = keyChooser.getSelectedFile().getAbsolutePath();
                    try {
                        DataUtils.saveBytes(bytes, selectedPath);
                        log("Signature data saved: " + selectedPath);
                    } catch (Exception ex) {
                        String message = "Could not save file: " + selectedPath;
                        log(message);
                        JOptionPane.showMessageDialog(frame, message, "Save error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                outputTextArea.setText(new String(bytes));
            }
            canProcess = true;
            _updateButtons();
        }
    }

    private void verify() {
        if (publicKey != null) {
            canProcess = false;
            _updateButtons();

            log("Preparing decryption...");
            System.out.println(Arrays.toString(data));
            Block[] blocks = Operations.generateBlocks(data, publicKey.getMaxLength());
            Block[] data = Operations.generateBlocks(signature, publicKey.getFillSize());
            Verify verify = new Verify(data, blocks, publicKey);
            log("Starting decryption...");

            try {
                boolean check = verify.check();
                log("Verify completed successfully.");

                if (check) {
                    JOptionPane.showMessageDialog(frame,
                            "Signature is valid.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE,
                            getIcon("check.png", 45, 45));
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Signature is invalid.",
                            "Invalid",
                            JOptionPane.INFORMATION_MESSAGE,
                            getIcon("cross.png", 45, 45));
                }
            } catch (CorruptedDataException e) {
                String message = "Corrupted data.";
                log(message);
                JOptionPane.showMessageDialog(frame, message, "Verify error", JOptionPane.ERROR_MESSAGE);
            }

            canProcess = true;
            _updateButtons();
        }
    }

    private void setIcon(JButton button, String path) {
        try {
            Image img = ImageIO.read(getClass().getResource(path));
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon imgIcon = new ImageIcon(img);
            button.setIcon(imgIcon);
        } catch (Exception ex) {
//            ...
            System.out.println(ex);
        }
    }

    private ImageIcon getIcon(String path, int width, int height) {
        try {
            Image img = ImageIO.read(getClass().getResource(path));
            img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
//            ...
            System.out.println(ex);
        }
        return null;
    }

    private void updateButtons() {
        _updateButtons();
    }

    private void _updateButtons() {
        if (data != null && data.length > 0) {
            encryptButton.setEnabled(privateKey != null);
            decryptButton.setEnabled(publicKey != null && signature != null && signature.length > 0);
        } else {
            encryptButton.setEnabled(false);
            decryptButton.setEnabled(false);
        }
        setAsSignatureButton.setEnabled(results != null && results.length > 0);
    }

    private void log(String message) {
        Date date = new Date();
        log.append('[' + dateFormat.format(date) + "]: " + message + '\n');
    }

    public void dispose() {}

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

//        File
        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem file_item_1 = new JMenuItem("Load file");
        file_item_1.addActionListener(e -> inputFileDialog());

        file.add(file_item_1);

//        Key
        JMenu key = new JMenu("Key");
        menuBar.add(key);

        JMenuItem key_item_1 = new JMenuItem("Generate key");
        key_item_1.addActionListener(e -> generateKey());

        key.add(key_item_1);

        frame.setJMenuBar(menuBar);
    }
}
