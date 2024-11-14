package edu.seu.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RootPanel {

    private final JPanel rootPanel;

    public RootPanel() {
        SetKey setKey = new SetKey();
        CheckKey checkKey = new CheckKey();
        SendData sendData = new SendData();
        ReceiveData receiveData = new ReceiveData();
        VerifyData verifyData = new VerifyData();

        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JTabbedPane tabbedPane = new JTabbedPane();
        rootPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));

        tabbedPane.addTab("密钥设置", setKey.getPanelKeySetting());
        tabbedPane.addTab("密钥查看", checkKey.getPanelKeyCheck());
        tabbedPane.addTab("密文发送", sendData.getPanelDataSend());
        tabbedPane.addTab("密文接收",receiveData.getPanelDataReceive());
        tabbedPane.addTab("数据验证", verifyData.getPanelDataVerify());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
