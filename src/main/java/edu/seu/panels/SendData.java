package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import edu.seu.utils.DataParser;
import edu.seu.utils.BytesUtils;

import static edu.seu.CipherApplication.algUtils;
import static edu.seu.constant.MessageConstant.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class SendData {

    private final JPanel panelDataSend;
    private final JTextArea textAreaRawData;
    private final JTextArea textAreaDigest;
    private final JTextArea textAreaSig;
    private final JButton buttonChooseFromFile;
    private final JButton buttonSend;
    private final JButton buttonSavePath;
    private final JCheckBox checkBoxChooseFromFile;


    // 待加密明文文件
    private File rawFile;
    // 数据来源
    public static String msgSource = "text";
    // 数据摘要
    byte[] digest;
    // 数字签名
    byte[] signature;
    // 密文
    byte[] cipherData;
    // 密文保存路径
    private String savePath;
    // 是否已经发送
    private boolean hasSend = false;

    /**
     * 初始化数据发送面板
     */
    public SendData() {
        JLabel panelSelectFile;
        JLabel panelDigestGen;
        JScrollPane textScrollDigest;
        JScrollPane textScrollSignature;

        panelDataSend = new JPanel();
        panelDataSend.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataSend.add(panel13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel13.add(panel14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel14.add(panel15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel15.add(panel16, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSelectFile = new JLabel();
        panelSelectFile.setText("输入需要加密的数据或文件");
        panel16.add(panelSelectFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxChooseFromFile = new JCheckBox();
        checkBoxChooseFromFile.setText("从文件输入");
        panel16.add(checkBoxChooseFromFile, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonChooseFromFile = new JButton();
        buttonChooseFromFile.setText("打开文件");
        buttonChooseFromFile.setEnabled(false);
        panel16.add(buttonChooseFromFile, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel15.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaRawData = new JTextArea();
        textAreaRawData.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaRawData.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPane1.setViewportView(textAreaRawData);
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 2, new Insets(5, 50, 5, 50), -1, -1));
        panel13.add(panel17, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        buttonSavePath = new JButton();
        buttonSavePath.setText("保存");
        panel17.add(buttonSavePath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 25), new Dimension(100, -1), 0, false));
        buttonSend = new JButton();
        buttonSend.setText("发送");
        panel17.add(buttonSend, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 25), new Dimension(100, -1), 0, false));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel13.add(panel18, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel18.add(panel19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelDigestGen = new JLabel();
        panelDigestGen.setText("生成的数据摘要和数字签名为");
        panel19.add(panelDigestGen, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel19.add(panel20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textScrollDigest = new JScrollPane();
        panel20.add(textScrollDigest, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaDigest = new JTextArea();
        textAreaDigest.setEditable(false);
        textAreaDigest.setText("");
        textAreaDigest.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaDigest.setLineWrap(true);       // 使文本超出宽度时换行
        textScrollDigest.setViewportView(textAreaDigest);
        textScrollSignature = new JScrollPane();
        panel20.add(textScrollSignature, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSig = new JTextArea();
        textAreaSig.setEditable(false);
        textAreaSig.setText("");
        textAreaSig.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSig.setLineWrap(true);       // 使文本超出宽度时换行
        textScrollSignature.setViewportView(textAreaSig);

        addListener();
    }

    /**
     * 获取数据发送面板
     *
     * @return JPanel
     */
    public JPanel getPanelDataSend() {
        return panelDataSend;
    }

    /**
     * 监听器
     */
    private void addListener() {
        // 是否从文件输入数据
        checkBoxChooseFromFile.addActionListener(e -> {
            if (checkBoxChooseFromFile.isSelected()) {
                msgSource = "file";
                buttonChooseFromFile.setEnabled(true);
                textAreaRawData.setText("");
                textAreaRawData.setEditable(false);
            } else {
                msgSource = "text";
                buttonChooseFromFile.setEnabled(false);
                textAreaRawData.setEditable(true);
            }
        });

        // 从文件输入待加密数据
        buttonChooseFromFile.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("打开文件");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                rawFile = fileChooser.getSelectedFile();
                textAreaRawData.setText(rawFile.toString());
            }
        });

        // 选择密文保存路径
        buttonSavePath.addActionListener(e -> {
            if (!hasSend) {
                JOptionPane.showMessageDialog(null, DATA_NOT_SEND, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择保存路径");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            // 显示保存文件的对话框
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                savePath = file.getAbsolutePath() + "\\";
                String fileSig = "ciphertext.sig";
                if (msgSource.equals("text")) {
                    BytesUtils.saveFile(signature, savePath, fileSig);
                    JOptionPane.showMessageDialog(null, "数字签名已保存至：" + savePath + fileSig, "TIP", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String fileInput = textAreaRawData.getText().trim();
                    String fileName = fileInput.substring(fileInput.lastIndexOf("\\") + 1);
                    fileSig = fileName.concat(".sig");
                    BytesUtils.saveFile(signature, savePath, fileSig);
                    JOptionPane.showMessageDialog(null, "数字签名已保存至：" + savePath + fileSig, "TIP", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 发送文件
        buttonSend.addActionListener(e -> {
            if (SetKey.isSave()) {
                if (msgSource.equals("text")) {
                    // 从输入框输入数据
                    String msg = textAreaRawData.getText();
                    if (msg.isEmpty()) {
                        JOptionPane.showMessageDialog(null, DATA_NOT_INPUT, "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Map<String, byte[]> dataMap;
                    try {
                        dataMap = algUtils.senderProcess(msg.getBytes());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    // 将数据摘要和数字签名取出，填充到文本框中
                    digest = dataMap.get("digest");
                    signature = dataMap.get("signature");
                    cipherData = dataMap.get("cipherData");
                    textAreaDigest.setText("数据摘要为：\n" + DataParser.byte2String(digest));
                    textAreaSig.setText("数字签名为：\n" + DataParser.byte2String(signature));

                    // 将密文填充到接收方的文本框中
                    JTextArea textAreaCipher = ReceiveData.getTextAreaCiphertext();
                    if (textAreaCipher != null) {
                        textAreaCipher.setText(DataParser.byte2String(cipherData));
                    }
                    hasSend = true;
                    JOptionPane.showMessageDialog(null, SEND_SUCCESS, "TIP", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // 从文件输入数据
                    String file = textAreaRawData.getText();
                    // 读取数据
                    byte[] fileBytes = BytesUtils.getBytes(file);
                    if (fileBytes == null) {
                        JOptionPane.showMessageDialog(null, READ_FILE_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Map<String, byte[]> dataMap;
                        try {
                            dataMap = algUtils.senderProcess(fileBytes);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        // 将数据摘要和数字签名取出，填充到文本框中
                        digest = dataMap.get("digest");
                        signature = dataMap.get("signature");
                        cipherData = dataMap.get("cipherData");
                        textAreaDigest.setText("数据摘要为：\n" + DataParser.byte2String(digest));
                        textAreaSig.setText("数字签名为：\n" + DataParser.byte2String(signature));
                        // 将密文填充到接收方的文本框中
                        JTextArea textAreaCipher = ReceiveData.getTextAreaCiphertext();
                        if (textAreaCipher != null) {
                            String filePath = textAreaRawData.getText().trim().substring(0, textAreaRawData.getText().trim().lastIndexOf("\\"));
                            String fileName = textAreaRawData.getText().trim().substring(textAreaRawData.getText().trim().lastIndexOf("\\") + 1);
                            String fileEnc = fileName.concat(".enc");
                            textAreaCipher.setText(filePath + "\\" + fileEnc);
                            BytesUtils.saveFile(cipherData, filePath, fileEnc);
                        }
                        hasSend = true;
                        JOptionPane.showMessageDialog(null, SEND_SUCCESS, "TIP", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, INFO_UNSET, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}