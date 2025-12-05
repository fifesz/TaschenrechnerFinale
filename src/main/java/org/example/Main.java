package org.example;
import org.example.TaschenrechnerGUI;

import javax.swing.*;
// Das ist die Main Methode, es startet den Programm
public class Main{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaschenrechnerGUI::new);
    }
}