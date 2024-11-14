package edu.seu;

import com.formdev.flatlaf.FlatIntelliJLaf;
import edu.seu.panels.RootPanel;
import edu.seu.utils.AlgUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CipherApplication {
    // 算法工具
    public static AlgUtils algUtils;

    public static void main(String[] args) {
        URL url = CipherApplication.class.getClassLoader().getResource("SEU_logo.png");
        FlatIntelliJLaf.setup();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("模拟加密通信系统");
        if(url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            frame.setIconImage(image);
        }else{
            System.out.println("未找到图标");
        }
        frame.setContentPane(new RootPanel().getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        // 设置窗体居中
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算窗体的位置
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        // 设置窗体的位置
        frame.setLocation(x, y);
        frame.setResizable(false);
        frame.setSize(600, 480);
        frame.setVisible(true);
    }
}
