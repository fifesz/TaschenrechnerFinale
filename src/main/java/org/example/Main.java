package org.example;
import org.example.TaschenrechnerGUI;

import javax.swing.*;

public class Main{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaschenrechnerGUI::new);
    }
}