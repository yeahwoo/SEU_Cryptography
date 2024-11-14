package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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


public class SetKey {
    private final JPanel panelKeySetting;
    private final JComboBox<String> comboBoxHash;
    private final JCheckBox checkBoxSelectSeed;
    private final JTextArea textAreaInputSeed;
    private final JRadioButton radioButtonDES;
    private final JRadioButton radioButtonAES;
    private final JTextField textFieldKeySizeSender;
    private final JTextField textFieldKeySizeReceiver;
    private final JButton buttonSenderKeyGen;
    private final JButton buttonReceiverKeyGen;
    private final JButton buttonSave;
    private final JButton buttonSelectPath;
    private final JTextField textFieldPath;

    private Algorithm algFlag;
    private Algorithm hashFlag;
    private String keySeed;
    private String rsaKeySizeSender;
    private String rsaKeySizeReceiver;
    private String keyPath;
    private boolean isSenderKeyGen;
    private boolean isReceiverKeyGen;
    // 参数是否均设置，传递给其他模块
    private static boolean isSave = false;

    /**
     * 获取保存状态
     *
     * @return boolean
     */
    public static boolean isSave() {
        return isSave;
    }

    /**
     * 初始化密钥设置面板
     */
    public SetKey() {
        JPanel labelSelectAlg;
        JPanel panelAsyKeySetting;
        JPanel panelSymKeySetting;
        JPanel panelSenderKeySetting;
        JPanel panelReceiverKeySetting;
        JPanel panelAlg;
        JPanel panelKeyGen;
        JLabel panelKeyGenMethod;
        JPanel panelHash;
        JLabel panelSelectHash;
        JPanel panelSavePath;

        algFlag = Algorithm.AES;
        hashFlag = Algorithm.MD5;
        panelKeySetting = new JPanel();
        textAreaInputSeed = new JTextArea();
        textAreaInputSeed.setEnabled(false);
        isSenderKeyGen = false;
        isReceiverKeyGen = false;

        textFieldPath = new JTextField();
        panelKeySetting.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelKeySetting.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelAsyKeySetting = new JPanel();
        panelAsyKeySetting.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelKeySetting.add(panelAsyKeySetting, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelAsyKeySetting.setBorder(BorderFactory.createTitledBorder(null, "非对称密钥设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelSenderKeySetting = new JPanel();
        panelSenderKeySetting.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAsyKeySetting.add(panelSenderKeySetting, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSenderKeySetting.setBorder(BorderFactory.createTitledBorder(null, "发送方密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSenderKeySetting.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldKeySizeSender = new JTextField();
        textFieldKeySizeSender.setText("");
        panel2.add(textFieldKeySizeSender, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        buttonSenderKeyGen = new JButton();
        buttonSenderKeyGen.setText("确认");
        panel2.add(buttonSenderKeyGen, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 25), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("指定密钥模长（1024-2048位）");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelReceiverKeySetting = new JPanel();
        panelReceiverKeySetting.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAsyKeySetting.add(panelReceiverKeySetting, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelReceiverKeySetting.setBorder(BorderFactory.createTitledBorder(null, "接收方密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelReceiverKeySetting.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldKeySizeReceiver = new JTextField();
        textFieldKeySizeReceiver.setText("");
        panel5.add(textFieldKeySizeReceiver, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        buttonReceiverKeyGen = new JButton();
        buttonReceiverKeyGen.setText("确认");
        panel5.add(buttonReceiverKeyGen, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 25), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("指定密钥模长（1024-2048位）");
        panel6.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSymKeySetting = new JPanel();
        panelSymKeySetting.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelKeySetting.add(panelSymKeySetting, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSymKeySetting.setBorder(BorderFactory.createTitledBorder(null, "对称密钥设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelAlg = new JPanel();
        panelAlg.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSymKeySetting.add(panelAlg, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelAlg.setBorder(BorderFactory.createTitledBorder(null, "加密算法", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        labelSelectAlg = new JPanel();
        labelSelectAlg.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAlg.add(labelSelectAlg, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, -1), new Dimension(150, -1), 0, true));
        final JLabel label3 = new JLabel();
        label3.setText("选择加密算法");
        labelSelectAlg.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 250), -1, -1));
        panelAlg.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup = new ButtonGroup();
        radioButtonAES = new JRadioButton();
        radioButtonAES.setText("AES");
        radioButtonAES.setSelected(true);
        radioButtonDES = new JRadioButton();
        radioButtonDES.setText("DES");
        buttonGroup.add(radioButtonDES);
        buttonGroup.add(radioButtonAES);
        panel7.add(radioButtonAES, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        panel7.add(radioButtonDES, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelKeyGen = new JPanel();
        panelKeyGen.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSymKeySetting.add(panelKeyGen, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelKeyGen.setBorder(BorderFactory.createTitledBorder(null, "密钥生成", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));

        panelKeyGen.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelKeyGenMethod = new JLabel();
        panelKeyGenMethod.setText("对称密钥生成方式（默认随机生成）");
        panel8.add(panelKeyGenMethod, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxSelectSeed = new JCheckBox();
        checkBoxSelectSeed.setText("指定密钥种子");
        panel8.add(checkBoxSelectSeed, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelKeyGen.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textAreaInputSeed.setText("使用默认密钥种子");
        panel9.add(textAreaInputSeed, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelKeySetting.add(panel10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelHash = new JPanel();
        panelHash.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(panelHash, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelHash.setBorder(BorderFactory.createTitledBorder(null, "哈希算法", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelSelectHash = new JLabel();
        panelSelectHash.setText("选择哈希算法");
        panelHash.add(panelSelectHash, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.comboBoxHash = new JComboBox<String>();
        final DefaultComboBoxModel<String> comboBoxHash = new DefaultComboBoxModel<String>();
        comboBoxHash.addElement("MD5");
        comboBoxHash.addElement("SHA224");
        comboBoxHash.addElement("SHA256");
        comboBoxHash.addElement("SHA384");
        comboBoxHash.addElement("SHA512");
        this.comboBoxHash.setModel(comboBoxHash);
        panelHash.add(this.comboBoxHash, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(200, -1), 0, false));
        panelSavePath = new JPanel();
        panelSavePath.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(panelSavePath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSavePath.setBorder(BorderFactory.createTitledBorder(null, "保存密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelSavePath.add(panel11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSelectPath = new JButton();
        buttonSelectPath.setText("选择保存路径");
        panel11.add(buttonSelectPath, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldPath.setEditable(false);
        textFieldPath.setText("");
        panel11.add(textFieldPath, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, 25), new Dimension(200, -1), 0, false));
        buttonSave = new JButton();
        buttonSave.setText("保存");
        panel11.add(buttonSave, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));

        addListener();
    }

    /**
     * 获取密钥设置面板
     *
     * @return JPanel
     */
    public JPanel getPanelKeySetting() {
        return panelKeySetting;
    }

    /**
     * 为各组件添加事件监听
     */
    private void addListener() {
        // 判断是否选中AES算法
        radioButtonAES.addActionListener(e -> {
            // 根据下拉框的选项判断
            if (radioButtonAES.isSelected()) {
                algFlag = Algorithm.AES;
            }
        });

        // 判断是否选中DES算法
        radioButtonDES.addActionListener(e -> {
            // 根据下拉框的选项判断
            if (radioButtonDES.isSelected()) {
                algFlag = Algorithm.DES;
            }
        });

        // 是否指定密钥种子
        checkBoxSelectSeed.addActionListener(e -> {
            if (checkBoxSelectSeed.isSelected()) {
                textAreaInputSeed.setEnabled(true);
                textAreaInputSeed.setText("");
            } else {
                textAreaInputSeed.setEnabled(false);
                textAreaInputSeed.setText("使用默认密钥种子");
            }
        });

        // 指定哈希算法
        comboBoxHash.addActionListener(e -> {
            switch (comboBoxHash.getSelectedIndex()) {
                case 0: {
                    hashFlag = Algorithm.MD5;
                }
                break;
                case 1: {
                    hashFlag = Algorithm.SHA224;
                }
                break;
                case 2: {
                    hashFlag = Algorithm.SHA256;
                }
                break;
                case 3: {
                    hashFlag = Algorithm.SHA384;
                }
                break;
                case 4: {
                    hashFlag = Algorithm.SHA512;
                }
                break;
            }
        });

        // 获取发送方密钥模长
        buttonSenderKeyGen.addActionListener(e -> {
            if (!isSenderKeyGen) {
                rsaKeySizeSender = textFieldKeySizeSender.getText().trim();
                if (rsaKeySizeSender.isEmpty() || (Integer.parseInt(rsaKeySizeSender) < 1024) || (Integer.parseInt(rsaKeySizeSender) > 2048))
                    JOptionPane.showMessageDialog(null, INVALID_LENGTH, "ERROR", JOptionPane.ERROR_MESSAGE);
                else {
                    isSenderKeyGen = true;
                    textFieldKeySizeSender.setEditable(false);
                    buttonSenderKeyGen.setText("重置");
                }
            } else {
                isSenderKeyGen = false;
                textFieldKeySizeSender.setEditable(true);
                textFieldKeySizeSender.setText("");
                buttonSenderKeyGen.setText("确认");
            }
        });

        // 获取接收方密钥模长
        buttonReceiverKeyGen.addActionListener(e -> {
            if (!isReceiverKeyGen) {
                rsaKeySizeReceiver = textFieldKeySizeReceiver.getText().trim();
                if (rsaKeySizeReceiver.isEmpty() || (Integer.parseInt(rsaKeySizeReceiver) < 1024) || (Integer.parseInt(rsaKeySizeReceiver) > 2048))
                    JOptionPane.showMessageDialog(null, INVALID_LENGTH, "ERROR", JOptionPane.ERROR_MESSAGE);
                else {
                    isReceiverKeyGen = true;
                    textFieldKeySizeReceiver.setEditable(false);
                    buttonReceiverKeyGen.setText("重置");
                }
            } else {
                isReceiverKeyGen = false;
                textFieldKeySizeReceiver.setEditable(true);
                textFieldKeySizeReceiver.setText("");
                buttonReceiverKeyGen.setText("确认");
            }
        });

        // 选择保存路径
        buttonSelectPath.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // 只允许选择目录
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("选择保存路径");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                keyPath = file.getAbsolutePath();
                textFieldPath.setText(keyPath);
            }
        });

        // 保存密钥参数
        buttonSave.addActionListener(e -> {
            // 指定种子，则获取用户输入的种子
            if (checkBoxSelectSeed.isSelected()) {
                keySeed = textAreaInputSeed.getText().trim();
                if (keySeed.isEmpty()) {
                    JOptionPane.showMessageDialog(null, SEED_NOT_INPUT, "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // 否则种子置空，随机生成
                keySeed = null;
            }

            // 判断RSA密钥是否生成
            if (!(isSenderKeyGen && isReceiverKeyGen)) {
                JOptionPane.showMessageDialog(null, KEY_NOT_GEN, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 判断保存路径是否选择
            if (keyPath == null) {
                JOptionPane.showMessageDialog(null, PATH_NOT_SELECT, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 创建算法工具类
            if (algFlag != null && hashFlag != null && rsaKeySizeSender != null && rsaKeySizeReceiver != null) {
                algUtils = new AlgUtils(algFlag, hashFlag, keySeed, rsaKeySizeSender, rsaKeySizeReceiver);
                isSave = true;
                // 保存到指定目录下
                Map<String, String> parameters = algUtils.getParameter();
                String publicKey = parameters.get("hashAlg") + "\n"
                        + parameters.get("senderKeyM") + "\n"
                        + parameters.get("senderPublicKeyE") + "\n";
                BytesUtils.saveFile(publicKey.getBytes(), keyPath, "public.key");
                JOptionPane.showMessageDialog(null, "发送方公钥已保存到" + keyPath + "目录下", "TIP", JOptionPane.INFORMATION_MESSAGE);

            }
        });


    }
}
