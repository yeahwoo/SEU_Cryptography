package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import edu.seu.utils.DataParser;
import edu.seu.utils.AlgUtils;
import edu.seu.utils.BytesUtils;
import edu.seu.enums.Algorithm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.Map;

import static edu.seu.CipherApplication.algUtils;
import static edu.seu.constant.MessageConstant.*;

public class ReceiveData {
    private final JPanel panelDataReceive;

    /**
     * 获取接收到的密文文本框，供发送方调用显示密文
     *
     * @return JTextArea
     */
    public static JTextArea getTextAreaCiphertext() {
        return textAreaCiphertext;
    }

    private static JTextArea textAreaCiphertext;
    private final JTextArea textAreaPlaintext;
    private final JTextArea textAreaSessionKey;
    private final JTextArea textAreaSig;
    private final JButton buttonDecrypt;
    private final JButton buttonVerify;
    private final JButton buttonSave;

    // 接收到的密文
    private byte[] ciphertext;
    // 解密数据
    Map<String, byte[]> fileDecrypt;
    // 是否解密
    private boolean isDecrypt = false;

    /**
     * 初始化密文接收面板
     */
    public ReceiveData() {
        panelDataReceive = new JPanel();
        panelDataReceive.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataReceive.setBorder(BorderFactory.createTitledBorder(null, "接收到的消息密文", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataReceive.add(panel21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel21.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaCiphertext = new JTextArea();
        textAreaCiphertext.setEditable(false);
        textAreaCiphertext.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaCiphertext.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPane2.setViewportView(textAreaCiphertext);
        JPanel panelDataDecrypt = new JPanel();
        panelDataDecrypt.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelDataReceive.add(panelDataDecrypt, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelDataDecrypt.setBorder(BorderFactory.createTitledBorder(null, "解密后的消息及各项参数", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JPanel panelPlaintext = new JPanel();
        panelPlaintext.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataDecrypt.add(panelPlaintext, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelPlaintext.setBorder(BorderFactory.createTitledBorder(null, "消息明文", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane3 = new JScrollPane();
        panelPlaintext.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaPlaintext = new JTextArea();
        textAreaPlaintext.setEditable(false);
        textAreaPlaintext.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaPlaintext.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPane3.setViewportView(textAreaPlaintext);
        JPanel panelSessionKey = new JPanel();
        panelSessionKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataDecrypt.add(panelSessionKey, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSessionKey.setBorder(BorderFactory.createTitledBorder(null, "会话密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane4 = new JScrollPane();
        panelSessionKey.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSessionKey = new JTextArea();
        textAreaSessionKey.setEditable(false);
        textAreaSessionKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSessionKey.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPane4.setViewportView(textAreaSessionKey);
        JPanel panelSignature = new JPanel();
        panelSignature.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataDecrypt.add(panelSignature, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSignature.setBorder(BorderFactory.createTitledBorder(null, "数字签名", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane5 = new JScrollPane();
        panelSignature.add(scrollPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSig = new JTextArea();
        textAreaSig.setEditable(false);
        textAreaSig.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSig.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPane5.setViewportView(textAreaSig);
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 5, 0), -1, -1));
        panelDataReceive.add(panel22, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        buttonVerify = new JButton();
        buttonVerify.setText("认证");
        panel22.add(buttonVerify, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        buttonSave = new JButton();
        buttonSave.setText("保存");
        panel22.add(buttonSave, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        buttonDecrypt = new JButton();
        buttonDecrypt.setText("解密");
        panel22.add(buttonDecrypt, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));

        addListener();
    }

    /**
     * 获取数据接收面板
     *
     * @return JPanel
     */
    public JPanel getPanelDataReceive() {
        return panelDataReceive;
    }

    /**
     * 监听器
     */
    private void addListener() {
        // 解密
        buttonDecrypt.addActionListener(e -> {
            String message = textAreaCiphertext.getText();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(null, NO_MESSAGE, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 从文件读取数据，则从文本框中获取密文文件路径
            if (SendData.msgSource.equals("file")) {
                String filePath = message.substring(0, message.lastIndexOf("\\"));
                String fileName = message.substring(message.lastIndexOf("\\") + 1, message.lastIndexOf("."));
                ciphertext = BytesUtils.getBytes(message);
                try {
                    fileDecrypt = algUtils.receiverProcess(ciphertext);
                    if (fileDecrypt.get("errorMessage") != null) {
                        JOptionPane.showMessageDialog(null, new String(fileDecrypt.get("errorMessage")), "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    byte[] plaintext = fileDecrypt.get("plainText");
                    byte[] signature = fileDecrypt.get("signature");
                    byte[] key = fileDecrypt.get("sessionKey");
                    BytesUtils.saveFile(plaintext, filePath, "decrypted_" + fileName);
                    textAreaPlaintext.setText(filePath + "\\decrypted_" + fileName);
                    textAreaSig.setText(DataParser.byte2String(signature));
                    textAreaSessionKey.setText(DataParser.byte2String(key));
                    JOptionPane.showMessageDialog(null, "解密成功！\n明文已保存至" + filePath + "目录下", "TIP", JOptionPane.INFORMATION_MESSAGE);
                    isDecrypt = true;
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, DECRYPT_ERR, "ERROR", JOptionPane.WARNING_MESSAGE);
                    textAreaPlaintext.setText("");
                    textAreaSig.setText("");
                    textAreaSessionKey.setText("");
                    isDecrypt = false;
                }
            } else {
                // 直接从文本框中获取密文
                try {
                    ciphertext = DataParser.string2Byte(message);
                    fileDecrypt = algUtils.receiverProcess(ciphertext);
                    if (fileDecrypt.get("errorMessage") != null) {
                        JOptionPane.showMessageDialog(null, new String(fileDecrypt.get("errorMessage")), "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    byte[] plaintext = fileDecrypt.get("plainText");
                    byte[] signature = fileDecrypt.get("signature");
                    byte[] key = fileDecrypt.get("sessionKey");
                    textAreaPlaintext.setText(new String(plaintext));
                    textAreaSig.setText(DataParser.byte2String(signature));
                    textAreaSessionKey.setText(DataParser.byte2String(key));
                    JOptionPane.showMessageDialog(null, "解密成功！", "TIP", JOptionPane.INFORMATION_MESSAGE);
                    isDecrypt = true;
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, DECRYPT_ERR, "ERROR", JOptionPane.WARNING_MESSAGE);
                    textAreaPlaintext.setText("");
                    textAreaSig.setText("");
                    textAreaSessionKey.setText("");
                    isDecrypt = false;
                }
            }
        });

        // 认证
        buttonVerify.addActionListener(e -> {
            if (!isDecrypt) {
                JOptionPane.showMessageDialog(null, DATA_NOT_ENCRYPT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String plaintext = textAreaPlaintext.getText();
            byte[] signature;
            Algorithm hashAlg = Algorithm.valueOf(algUtils.getParameter().get("hashAlg"));
            try {
                signature = DataParser.string2Byte(textAreaSig.getText());
            } catch (Exception e3) {
                JOptionPane.showMessageDialog(null, VERIFY_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (SendData.msgSource.equals("file")) {
                // 从文件读数据
                byte[] fileBytes = BytesUtils.getBytes(plaintext);
                try {
                    boolean pass = AlgUtils.authentication(algUtils.getSenderPublicKey(), fileBytes, signature, hashAlg);
                    if (pass) {
                        JOptionPane.showMessageDialog(null, VERIFY_SUCCESS, "TIP", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, VERIFY_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e3) {
                    JOptionPane.showMessageDialog(null, VERIFY_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // 从文本框读数据
                byte[] dataBytes = plaintext.getBytes();
                boolean pass = AlgUtils.authentication(algUtils.getSenderPublicKey(), dataBytes, signature, hashAlg);
                if (pass) {
                    JOptionPane.showMessageDialog(null, VERIFY_SUCCESS, "TIP", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, VERIFY_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 保存解密后的密钥参数
        buttonSave.addActionListener(e -> {
            if (!isDecrypt) {
                JOptionPane.showMessageDialog(null, DATA_NOT_ENCRYPT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 创建文件选择器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择保存路径");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            // 显示保存文件的对话框
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                // 用户选择了文件夹路径
                File selectedFile = fileChooser.getSelectedFile();
                String savePath = selectedFile.getAbsolutePath() + "\\";
                // 将数据保存到指定路径下
                // 获取会话密钥以及数字签名
                String signature = textAreaSig.getText();
                String sessionKey = textAreaSessionKey.getText();
                String fileSignature = "ciphertext.sig";
                String fileKey = "session.key";
                if (SendData.msgSource.equals("file")) {
                    String message = textAreaCiphertext.getText();
                    String substring = message.substring(message.lastIndexOf("\\") + 1, message.lastIndexOf("."));
                    fileSignature = substring.concat(".sig");
                    fileKey = substring.concat(".key");
                }
                BytesUtils.saveFile(DataParser.string2Byte(signature), savePath, fileSignature);
                BytesUtils.saveFile(sessionKey.getBytes(), savePath, fileKey);
                JOptionPane.showMessageDialog(null, "数字签名和对称密钥已保存至" + selectedFile + "目录下！", "TIP", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}

