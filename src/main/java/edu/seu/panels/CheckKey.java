package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.seu.CipherApplication.algUtils;
import static edu.seu.constant.MessageConstant.INFO_UNSET;

public class CheckKey {

    private final JPanel panelKeyCheck;
    private final JButton buttonRefresh;
    private final JTextArea textAreaSessionKey;
    private final JTextArea textAreaSenderPubKey;
    private final JTextArea textAreaSenderPriKey;
    private final JTextArea textAreaReceiverPubKey;
    private final JTextArea textAreaReceiverPriKey;

    /**
     * 初始化查看密钥面板
     */
    public CheckKey() {
        JPanel panelSymKey;
        JPanel panelAysKey;
        JPanel panelSenderKey;
        JPanel panelReceiverKey;
        JPanel panelSenPubKey;
        JPanel panelSenPriKey;
        JPanel panelRecPubKey;
        JPanel panelRecPriKey;
        JPanel panelRefresh;
        JScrollPane scrollPaneSenderPubKey;
        JScrollPane scrollPaneSenderPriKey;
        JScrollPane scrollPaneReceiverPubKey;
        JScrollPane scrollPaneReceiverPriKey;
        JScrollPane scrollPaneSessionKey;

        panelKeyCheck = new JPanel();
        panelKeyCheck.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelSymKey = new JPanel();
        panelSymKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelKeyCheck.add(panelSymKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSymKey.setBorder(BorderFactory.createTitledBorder(null, "对称密钥及哈希算法", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

        scrollPaneSessionKey = new JScrollPane();
        panelSymKey.add(scrollPaneSessionKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSessionKey = new JTextArea();
        textAreaSessionKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSessionKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaSessionKey.setEditable(false);
        scrollPaneSessionKey.setViewportView(textAreaSessionKey);
        panelAysKey = new JPanel();
        panelAysKey.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelKeyCheck.add(panelAysKey, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelAysKey.setBorder(BorderFactory.createTitledBorder(null, "非对称密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelSenderKey = new JPanel();
        panelSenderKey.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAysKey.add(panelSenderKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSenderKey.setBorder(BorderFactory.createTitledBorder(null, "发送方密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelSenPubKey = new JPanel();
        panelSenPubKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSenderKey.add(panelSenPubKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSenPubKey.setBorder(BorderFactory.createTitledBorder(null, "公钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPaneSenderPubKey = new JScrollPane();
        panelSenPubKey.add(scrollPaneSenderPubKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSenderPubKey = new JTextArea();
        textAreaSenderPubKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSenderPubKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaSenderPubKey.setEditable(false);
        scrollPaneSenderPubKey.setViewportView(textAreaSenderPubKey);
        panelSenPriKey = new JPanel();
        panelSenPriKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSenderKey.add(panelSenPriKey, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelSenPriKey.setBorder(BorderFactory.createTitledBorder(null, "私钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPaneSenderPriKey = new JScrollPane();
        panelSenPriKey.add(scrollPaneSenderPriKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSenderPriKey = new JTextArea();
        textAreaSenderPriKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaSenderPriKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaSenderPriKey.setEditable(false);
        scrollPaneSenderPriKey.setViewportView(textAreaSenderPriKey);
        panelReceiverKey = new JPanel();
        panelReceiverKey.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAysKey.add(panelReceiverKey, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelReceiverKey.setBorder(BorderFactory.createTitledBorder(null, "接收方密钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelRecPubKey = new JPanel();
        panelRecPubKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelReceiverKey.add(panelRecPubKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelRecPubKey.setBorder(BorderFactory.createTitledBorder(null, "公钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPaneReceiverPubKey = new JScrollPane();
        panelRecPubKey.add(scrollPaneReceiverPubKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaReceiverPubKey = new JTextArea();
        textAreaReceiverPubKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaReceiverPubKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaReceiverPubKey.setEditable(false);
        textAreaReceiverPubKey.setText("");
        scrollPaneReceiverPubKey.setViewportView(textAreaReceiverPubKey);
        panelRecPriKey = new JPanel();
        panelRecPriKey.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelReceiverKey.add(panelRecPriKey, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelRecPriKey.setBorder(BorderFactory.createTitledBorder(null, "私钥", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPaneReceiverPriKey = new JScrollPane();
        panelRecPriKey.add(scrollPaneReceiverPriKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaReceiverPriKey = new JTextArea();
        textAreaReceiverPriKey.setWrapStyleWord(true);  // 使换行时不会在单词中间断开
        textAreaReceiverPriKey.setLineWrap(true);       // 使文本超出宽度时换行
        textAreaReceiverPriKey.setEditable(false);
        scrollPaneReceiverPriKey.setViewportView(textAreaReceiverPriKey);
        panelRefresh = new JPanel();
        panelRefresh.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelKeyCheck.add(panelRefresh, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        buttonRefresh = new JButton();
        buttonRefresh.setText("刷新");
        panelRefresh.add(buttonRefresh, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), new Dimension(100, -1), 0, false));
        addListener();
    }

    /**
     * 获取密钥设置面板
     *
     * @return JPanel
     */
    public JPanel getPanelKeyCheck() {
        return panelKeyCheck;
    }

    /**
     * 监听器
     */
    private void addListener() {
        // 判断是否选中AES算法
        buttonRefresh.addActionListener(new ActionListener() {
            @Override
            // 获取密钥参数值
            public void actionPerformed(ActionEvent e) {
                // 根据下拉框的选项判断
                if (SetKey.isSave()) {
                    textAreaSessionKey.setText(algUtils.getParameterInfo().get("symmetricKey"));
                    textAreaSenderPubKey.setText(algUtils.getParameterInfo().get("senderPublicKey"));
                    textAreaSenderPriKey.setText(algUtils.getParameterInfo().get("senderPrivateKey"));
                    textAreaReceiverPubKey.setText(algUtils.getParameterInfo().get("receiverPublicKey"));
                    textAreaReceiverPriKey.setText(algUtils.getParameterInfo().get("receiverPrivateKey"));
                }else{
                    JOptionPane.showMessageDialog(null, INFO_UNSET, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
