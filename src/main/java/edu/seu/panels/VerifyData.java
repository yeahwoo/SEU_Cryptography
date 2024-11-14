package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import edu.seu.utils.DataParser;
import edu.seu.algorithm.RSAKey;
import edu.seu.utils.AlgUtils;
import edu.seu.utils.BytesUtils;
import edu.seu.enums.Algorithm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigInteger;

import static edu.seu.constant.MessageConstant.*;

public class VerifyData {
    private final JPanel panelDataVerify;
    private final JButton buttonVerify;
    private final JButton buttonPublicKey;
    private final JButton buttonPlainText;
    private final JButton buttonSignature;
    private final JTextArea textAreaPlainText;
    private final JTextArea textAreaSignature;
    private final JTextArea textAreaPublicKey;
    private final JCheckBox checkBoxPlainText;
    private final JCheckBox checkBoxSignature;

    // 待验证数据
    byte[] data;
    // 公钥
    byte[] key;
    // 签名
    byte[] signature;

    /**
     * 初始化数据验证面板
     */
    public VerifyData() {
        JLabel labelPlainText;
        JPanel panelPlainText;
        JPanel panelInputData;
        JLabel labelSignature;
        JLabel labelSenderPublicKey;
        JScrollPane scrollPaneSignature;
        JScrollPane scrollPaneData;
        JScrollPane scrollPaneSenderPublicKey;

        panelDataVerify = new JPanel();
        panelDataVerify.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelPlainText = new JPanel();
        panelPlainText.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelDataVerify.add(panelPlainText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelPlainText.setBorder(BorderFactory.createTitledBorder(null, "选择待验证的明文", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelPlainText.add(panel21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        labelPlainText = new JLabel();
        labelPlainText.setText("待验证明文");
        panel21.add(labelPlainText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxPlainText = new JCheckBox();
        checkBoxPlainText.setText("从文件输入");
        panel21.add(checkBoxPlainText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPlainText = new JButton();
        buttonPlainText.setEnabled(false);
        buttonPlainText.setText("打开文件");
        panel21.add(buttonPlainText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPlainText.add(panel22, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 100), null, null, 0, false));
        scrollPaneSenderPublicKey = new JScrollPane();
        panel22.add(scrollPaneSenderPublicKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaPlainText = new JTextArea();
        textAreaPlainText.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaPlainText.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPaneSenderPublicKey.setViewportView(textAreaPlainText);
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 5, 0), -1, -1));
        panelDataVerify.add(panel23, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        buttonVerify = new JButton();
        buttonVerify.setText("认证");
        panel23.add(buttonVerify, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        panelInputData = new JPanel();
        panelInputData.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDataVerify.add(panelInputData, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 100), null, null, 0, true));
        panelInputData.setBorder(BorderFactory.createTitledBorder(null, "输入发送方公钥和数字签名", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panelInputData.add(panel24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel24.add(panel25, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelSignature = new JLabel();
        labelSignature.setText("数字签名");
        panel25.add(labelSignature, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSignature = new JButton();
        buttonSignature.setEnabled(false);
        buttonSignature.setText("打开文件");
        panel25.add(buttonSignature, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPaneData = new JScrollPane();
        panel25.add(scrollPaneData, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSignature = new JTextArea();
        textAreaSignature.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSignature.setLineWrap(true);       // 使文本超出宽度时换行
        scrollPaneData.setViewportView(textAreaSignature);
        checkBoxSignature = new JCheckBox();
        checkBoxSignature.setText("从文件输入");
        panel25.add(checkBoxSignature, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel24.add(panel26, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelSenderPublicKey = new JLabel();
        labelSenderPublicKey.setText("发送方公钥");
        panel26.add(labelSenderPublicKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPublicKey = new JButton();
        buttonPublicKey.setText("打开文件");
        panel26.add(buttonPublicKey, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPaneSignature = new JScrollPane();
        panel26.add(scrollPaneSignature, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaPublicKey = new JTextArea();
        textAreaPublicKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaPublicKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaPublicKey.setEditable(false);
        scrollPaneSignature.setViewportView(textAreaPublicKey);

        addListener();
    }

    /**
     * 获取数据验证面板
     *
     * @return JPanel
     */
    public JPanel getPanelDataVerify() {
        return panelDataVerify;
    }

    /**
     * 监听器
     */
    private void addListener() {
        // 读取明文
        buttonPlainText.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择待验证明文文件");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String dataPath = fileChooser.getSelectedFile().getPath();
                if (!dataPath.isEmpty()) {
                    data = BytesUtils.getBytes(dataPath);
                    textAreaPlainText.setText(dataPath);
                }
            }
        });

        // 是否从文件输入明文
        checkBoxPlainText.addActionListener(e -> {
            if (checkBoxPlainText.isSelected()) {
                buttonPlainText.setEnabled(true);
                textAreaPlainText.setText("");
                textAreaPlainText.setEditable(false);
            } else {
                buttonPlainText.setEnabled(false);
                textAreaPlainText.setEditable(true);
            }
        });

        // 读取公钥文件
        buttonPublicKey.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择发送方公钥文件");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String keyPath = fileChooser.getSelectedFile().getPath();
                if (!keyPath.isEmpty()) {
                    key = BytesUtils.getBytes(keyPath);
                    textAreaPublicKey.setText(new String(key));
                }
            }
        });

        // 读取数字签名
        buttonSignature.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择签名文件");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String signaturePath = fileChooser.getSelectedFile().getPath();
                if (!signaturePath.isEmpty()) {
                    signature = BytesUtils.getBytes(signaturePath);
                    textAreaSignature.setText(DataParser.byte2String(signature));
                }
            }
        });

        // 是否从文件输入数字签名
        checkBoxSignature.addActionListener(e -> {
            if (checkBoxSignature.isSelected()) {
                buttonSignature.setEnabled(true);
                textAreaSignature.setText("");
                textAreaSignature.setEditable(false);
            } else {
                buttonSignature.setEnabled(false);
                textAreaSignature.setEditable(true);
            }
        });

        // 认证
        buttonVerify.addActionListener(actionEvent -> {
            // 获取明文和签名
            if (!checkBoxPlainText.isSelected()) {
                data = textAreaPlainText.getText().trim().getBytes();
            }
            if (!checkBoxSignature.isSelected()) {
                signature = DataParser.string2Byte(textAreaSignature.getText().trim());
            }
            if (key == null || key.length == 0) {
                JOptionPane.showMessageDialog(null, KEY_NOT_INPUT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (signature == null || signature.length == 0) {
                JOptionPane.showMessageDialog(null, SIG_NOT_INPUT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (data == null || data.length == 0) {
                JOptionPane.showMessageDialog(null, MSG_NOT_INPUT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String rawKey = new String(key);
                String[] keyArray = rawKey.split("\n");
                Algorithm hashAlg = Algorithm.valueOf(keyArray[0]);
                BigInteger n = new BigInteger(keyArray[1]);
                BigInteger e = new BigInteger(keyArray[2]);
                RSAKey.PublicKey publicKey = new RSAKey.PublicKey(n, e);
                boolean pass = AlgUtils.authentication(publicKey, data, signature, hashAlg);
                if (pass) {
                    JOptionPane.showMessageDialog(null, VERIFY_SUCCESS, "TIP", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, VERIFY_ERR, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, ERROR, "ERROR", JOptionPane.ERROR_MESSAGE);
                throw e;
            }
        });
    }
}
