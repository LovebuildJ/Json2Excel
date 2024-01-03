package com.jason.frame;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class UiFrame {

    private static final Map<String, String> HEAD_MAP = new LinkedHashMap<>(16);

    public static void createUI() {
        // 创建主窗口
        JFrame frame = new JFrame("Json2Excel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JLabel label1 = new JLabel("【绑定属性】将字段和名称绑定 例如 name:名字,class:班级 使用逗号分隔");
        label1.setMinimumSize(new Dimension(100, 0));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area1 = new JTextArea();
        area1.setMinimumSize(new Dimension(100, 0));
        area1.setAlignmentX(Component.LEFT_ALIGNMENT);
        area1.setText("name:名字,class:班级");


        JLabel label2 = new JLabel("【JSON数据】需要转换的Json数据 请使用数组形式");
        label2.setMinimumSize(new Dimension(100, 0));
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area2 = new JTextArea();
        area2.setMinimumSize(new Dimension(100, 0));
        area2.setAlignmentX(Component.LEFT_ALIGNMENT);
        area2.setText("[{\"name\":\"张三\",\"class\":\"高一(2)班\"}]");

        JButton button = new JButton("开始转换");
        button.setMinimumSize(new Dimension(200, 0));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标点击校验数据
                String bindTableHead = area1.getText();
                if (StrUtil.isBlank(bindTableHead)) {
                    // 显示弹框提示
                    JOptionPane.showMessageDialog(frame, "绑定数据为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String enStr = bindTableHead.replaceAll("，", ",").replaceAll("：", ":");

                String[] split = enStr.split(",");
                for (String s : split) {
                    String[] kv = s.split(":");
                    HEAD_MAP.put(kv[0], kv[1]);
                }

                String jsonData = area2.getText();
                if (StrUtil.isBlank(jsonData)) {
                    // 显示弹框提示
                    JOptionPane.showMessageDialog(frame, "JSON数据为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // 导出数据
                export2Excel(jsonData, frame);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });


        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        jPanel.add(label1);
        jPanel.add(area1);

        jPanel.add(label2);
        jPanel.add(area2);

        jPanel.add(button);

        frame.getContentPane().add(jPanel);

        // 计算居中位置
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;

        // 设置窗口位置
        frame.setLocation(x, y);
        // 显示窗口
        frame.setVisible(true);
    }

    private static void export2Excel(String jsonData, JFrame frame) {

        String time = DateUtil.format(new Date(), "yyyyMMddHHmmSSS");
        String fileName = getUserHome() + "json转excel-" + time + ".xlsx";

        List<List<String>> head = new ArrayList<>();
        Collection<String> values = HEAD_MAP.values();

        for (String value : values) {
            List<String> col = new ArrayList<>();
            col.add(value);
            head.add(col);
        }

        List<List<Object>> rows = new ArrayList<>();
        try {

            JSONArray arr = JSON.parseArray(jsonData);
            for (Object o : arr) {
                JSONObject jb = (JSONObject) o;
                List<Object> row = new ArrayList<>();
                for (String key : jb.keySet()) {
                    row.add(jb.get(key));
                }
                rows.add(row);
            }
        } catch (Exception e) {
            // 显示弹框提示
            JOptionPane.showMessageDialog(frame, "JSON数据格式错误", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 底层使用 easy excel导出
        EasyExcel.write(fileName).head(head).sheet().doWrite(rows);
        JOptionPane.showMessageDialog(frame, "Json转Excel成功(文件在桌面)", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String getUserHome() {
        // 获取用户主目录
        String userHome = System.getProperty("user.home");

        // 构建桌面路径
        String desktopPath = "";
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            desktopPath = userHome + "\\Desktop\\";
        } else if (os.contains("mac")) {
            desktopPath = userHome + "/Desktop/";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("sunos")) {
            desktopPath = userHome + "/Desktop/";
        }

        return desktopPath;
    }
}
