package org.example;

import javax.swing.*;
import java.awt.*;
//Diese Klasse erzeugt die grafische Oberfläche des Taschenrechners
//Hier werden die grafische Elemente des Programms definiert
//Die folgenden Methoden verbinden die Logik mit der grafischen Oberfläche
public class TaschenrechnerGUI extends JFrame {

    private final JTextField display = new JTextField("0");
    private final TaschenrechnerLogic logic = new TaschenrechnerLogic();
//Diese Konstruktor baut das Display und die Buttons auf
    public TaschenrechnerGUI() {
        super("Taschenrechner");

        //Eigenschaften des Displays:
        //Schriftart,Textgröße,Rahmen und Ausrichtung
        //Display ist im oberen Bereich positioniert
        display.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        display.setPreferredSize(new Dimension(0, 70));
        add(display, BorderLayout.NORTH);

        //Eigenschaften und Beschriftunden der Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        String[] buttons = {
                "√","x²","BIN","C",
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                ".","0","←","+"
        };
//Schriftart und Größe der Buttons
        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            btn.addActionListener(e -> handleButton(b));
            buttonPanel.add(btn);
        }
        //Das Panel für die Buttons wird mittig,zentral positioniert
        add(buttonPanel, BorderLayout.CENTER);

        //Eigenschaften für '=' Button
        //Schriftart,Größe und Positionierung
        JButton equals = new JButton("=");
        equals.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        equals.setPreferredSize(new Dimension(0, 60));
        equals.addActionListener(e -> {
            logic.equalsPressed();
            updateDisplay();
        });
        add(equals, BorderLayout.SOUTH);

        //Fenstergröße wird definiert
        setSize(360, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Anzeige wird aktualisiert, damit das Dsiplay mit dem Logik synchron ist
        updateDisplay();
        setVisible(true);
    }
//Behandlung der verschiedenen Eingaben, wird für die Logik weiterleitet
    private void handleButton(String label) {
        //Negative Zahl als Starteingabe
        if (label.equals("-")) {
            //Wenn '0' oder 'Error' angezeigt wird
            if (logic.getDisplayText().equals("0") || logic.isErrorState()) {
                //Wenn 'Error' angezeigt wird, werden alle Werte zurückgesetzt
                if (logic.isErrorState()) {
                    logic.clearAll();
                }
                //Die Eingabe einer neuen negativen Zahl beginnt
                logic.startNegativeNumber();
                updateDisplay();
                return;
            }
        }

        //Eingabe von Ziffern,die Zahl wird erweitert
        switch (label) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                logic.appendDigit(label);
                break;
                //Ein Dezimalpunkt wird zur aktuellen Zahl hinzugefügt
            case ".":
                logic.appendDot();
                break;
                //Ein Operator wird gedrückt
            case "+":
            case "-":
            case "*":
            case "/":
                logic.operatorPressed(label);
                break;
                //'C' Clear All löscht alle eingaben
            case "C":
                logic.clearAll();
                break;
                //Quadratwurzel Funktion wird ausgeführt
            case "√":
                logic.sqrtPressed();
                break;
                //Quadratfunktion wird ausgeführt
            case "x²":
                logic.squarePressed();
                break;
                //Der Zahl wird in eine Binärzahl umgewandelt
            case "BIN":
                logic.binaryPressed();
                break;
                //Die letzte Eingabe wird gelöscht
            case "←":
                logic.backspace();
                break;
            default:
                break;
        }
//Die Anzeige wird aktualisiert
        updateDisplay();
    }
//Aktualisiert den Text im Display
    private void updateDisplay() {
        display.setText(logic.getDisplayText());
    }

}