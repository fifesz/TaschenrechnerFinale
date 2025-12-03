package org.example;

import javax.swing.*;
import java.awt.*;

public class TaschenrechnerGUI extends JFrame {

    private final JTextField display = new JTextField("0");
    private final TaschenrechnerLogic logic = new TaschenrechnerLogic();

    public TaschenrechnerGUI() {
        super("Taschenrechner");

        // Display
        display.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        display.setPreferredSize(new Dimension(0, 70));
        add(display, BorderLayout.NORTH);

        // Buttons grid (5x4)
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
                "√","x²","BIN","C",
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                ".","0","←","+"
        };

        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            btn.addActionListener(e -> handleButton(b));
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Big equals button
        JButton equals = new JButton("=");
        equals.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        equals.setPreferredSize(new Dimension(0, 60));
        equals.addActionListener(e -> {
            logic.equalsPressed();
            updateDisplay();
        });
        add(equals, BorderLayout.SOUTH);

        // frame
        setSize(360, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        updateDisplay(); // ensure display in sync
        setVisible(true);
    }

    private void handleButton(String label) {
        // special: start negative number if "-" pressed on zero
        if (label.equals("-")) {
            // If current display is "0" (or logic says entering new) start negative input
            if (logic.getDisplayText().equals("0") || logic.isErrorState()) {
                // If error currently shown, clear first
                if (logic.isErrorState()) {
                    logic.clearAll();
                }
                logic.startNegativeNumber();
                updateDisplay();
                return;
            }
        }

        // normal handling
        switch (label) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                logic.appendDigit(label);
                break;
            case ".":
                logic.appendDot();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                logic.operatorPressed(label);
                break;
            case "C":
                logic.clearAll();
                break;
            case "√":
                logic.sqrtPressed();
                break;
            case "x²":
                logic.squarePressed();
                break;
            case "BIN":
                logic.binaryPressed();
                break;
            case "←":
                logic.backspace();
                break;
            default:
                break;
        }

        updateDisplay();
    }

    private void updateDisplay() {
        display.setText(logic.getDisplayText());
    }

}