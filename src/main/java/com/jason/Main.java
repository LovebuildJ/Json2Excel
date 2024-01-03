package com.jason;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.jason.frame.UiFrame;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        // 设置外观为FlatLaf以获得现代化的外观
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        UiFrame.createUI();
    }
}