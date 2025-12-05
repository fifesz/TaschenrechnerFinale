package org.example;

import java.text.DecimalFormat;

public class TaschenrechnerLogic {
    // Taschenrechner Logik
    // Diese Klasse bildet die Operationen des Taschenrechners ab
    public static final String ERROR = "Error";
    //Der Zahlenformatierer zeigt bis zu 8 Nachkommastellen an
    private static final DecimalFormat FORMAT = new DecimalFormat("#.########");
    //displayText ist ein String der aktuell auf dem Display angezeigt wird
    private String displayText = "0";
    //currentValue ist der aktuell gespeicherte Zwischenwert
    private double currentValue = 0;
    //currentOperator ist der Operator, der zuletzt gedrückt wurde
    private String currentOperator = null;
    //lastOperator der Operator der nach erneutes Drücken der '=' erneut verwendet wird
    private String lastOperator = null;
    //mit lastOperand wird der letzte Operand erneut verwendet
    private double lastOperand = 0;
    //mit enteringNewNumber kann eine neue Zahl eingegeben werden
    private boolean enteringNewNumber = true;
    //errorState zeigt an,ob der Taschenrechner sich in den Fehlerzustand befindet
    private boolean errorState = false;
    //Kennzeichnet den BIN-Mode, die arithmetische Operationen sind deaktiviert
    //BIN-Mode endet durch 'C', eine neue Zifferneingabe
    private boolean binaryMode = false;

    //Zeigt den aktuellen Display-Text an
    public String getDisplayText() {
        return displayText;
    }
//Zeigt an,ob der Taschenrechner sich aktuell in ein Error-Zustand befindet
    public boolean isErrorState() {
        return errorState;
    }

    //Ermöglicht die Eingabe einer negativen Zahl zur Beginn
    public void startNegativeNumber() {
        if (errorState) return;
        displayText = "-";
        enteringNewNumber = false;
    }

    //Fügt einen neuen Ziffer an den aktuellen Wert
    public void appendDigit(String digit) {
        if (binaryMode) {
            clearAll();          //Beim BIN-Mode wird alles zurückgesetzt, eine neue Eingabe startet
        }
        if (errorState) return;
        if (enteringNewNumber) {
            displayText = digit;
            enteringNewNumber = false;
        } else {
            // wenn aktuell nur "-" steht (negative Zahl), weitere Eingabe ist erlaubt
            if (displayText.equals("-")) displayText = "-" + digit;
            else displayText = displayText + digit;
        }
    }
//Eine Dezimalpunkt wird hinzugefügt
    //In BIN-Mode wird alles zurückgesetzt, eine neue Eingabe startet
    public void appendDot() {
        if (errorState) return;
        if (binaryMode) {
            clearAll();
        }
        if (enteringNewNumber) {
            displayText = "0.";
            enteringNewNumber = false;
        } else if (!displayText.contains(".")) {
            displayText = displayText + ".";
        }
    }
//Löscht das letzte Zeichen
    public void backspace() {
        if (errorState) return;
        if (enteringNewNumber) return;
//Wenn nur noch ein Zeichen übrig ist, wird auf '0' gesetzt
        if (displayText.length() <= 1) {
            displayText = "0";
            enteringNewNumber = true;
        } else {
            displayText = displayText.substring(0, displayText.length() - 1);
            //Wenn nur noch '-' übrig ist, kann weiter eine negative Zahl eingegeben werden
            if (displayText.equals("-")) enteringNewNumber = false;
        }
    }
//Setzt den Taschenrechner komplett auf den Anfangszustand zurück
    public void clearAll() {
        displayText = "0";
        currentValue = 0;
        currentOperator = null;
        lastOperator = null;
        lastOperand = 0;
        enteringNewNumber = true;
        errorState = false;
        binaryMode = false;
    }

    //Rechnen mit den Operatoren '+','-','*','/'
    //In BIN-Mode sind die Operatoren deaktiviert
    public void operatorPressed(String op) {
        if (binaryMode) return;
        if (errorState) return;
        try {
            //Wenn ein Operator aktiv ist,wird die Operation berechnet
            double displayed = parseDisplay();
            if (currentOperator != null && !enteringNewNumber) {
                double result = calculate(currentValue, displayed, currentOperator);
                currentValue = result;
                displayText = formatNumber(result);
            } else {
                currentValue = displayed;
            }
            currentOperator = op;
            enteringNewNumber = true;
        } catch (ArithmeticException ae) {
            // z. B. Division durch 0 — setze Error
            showError();
        } catch (Exception e) {
            showError();
        }
    }
//Behandelt das Drücken der '=' Taste
    //In BIN-Mode ist '=' deaktiviert
    public void equalsPressed() {
        if (binaryMode) return;
        if (errorState) return;
        try {
            //Wenn ein Operator aktiv ist, wird die Rechenoparation ausgeführt
            double displayed = parseDisplay();
            if (currentOperator != null) {
                double result = calculate(currentValue, displayed, currentOperator);

                lastOperator = currentOperator;
                lastOperand = displayed;

                currentValue = result;
                displayText = formatNumber(result);

                currentOperator = null;
                enteringNewNumber = true;
                //Hier wird das Verhalten angegeben, wenn die '=' erneut gedrückt wird
                //Der letzte Rechenoperation wird erneut durchgeführt
            } else if (lastOperator != null) {
                double base = parseDisplay();
                double result = calculate(base, lastOperand, lastOperator);
                currentValue = result;
                displayText = formatNumber(result);
                enteringNewNumber = true;
            }
            // Fehlermeldung, auf dem Display wird 'Error' angezeigt
        } catch (ArithmeticException ae) {
            showError();
        } catch (Exception e) {
            showError();
        }
    }
    //Berechnet die Quadratwurzel
    public void sqrtPressed() {
        if (errorState) return;
        try {
            double v = parseDisplay();
            //Fehlerzustand bei negative Werte
            if (v < 0) { showError(); return; }
            displayText = formatNumber(Math.sqrt(v));
            enteringNewNumber = true;
        } catch (Exception e) { showError(); }
    }
//Berechnet das Quadrat
    public void squarePressed() {
        if (errorState) return;
        try {
            double v = parseDisplay();
            displayText = formatNumber(v * v);
            enteringNewNumber = true;
        } catch (Exception e) { showError(); }
    }
//Wandelt den aktuellen Zahl in eine binäre Zahl
    //Nur ganze Zahlen sind zulässig
    public void binaryPressed() {
        if (errorState) return;
        try {
            double v = parseDisplay();
            if (v != Math.floor(v)) { showError(); return; }
            long val = Math.round(v);
            String bin = Long.toBinaryString(Math.abs(val));
            if (val < 0) bin = "-" + bin;
            displayText = bin;
            enteringNewNumber = true;
            //BIN-Mode wird hier aktiviert
            binaryMode = true;
        }
        catch (Exception e) { showError(); }
    }

    //Wandelt den aktuellen Text auf dem Display in einem double Wert um
    private double parseDisplay() {
        //Ein einzelne '-' ist keine gültige Eingabe, es entsteht eine Ausnahme
        if (displayText.equals("-")) throw new NumberFormatException("Zahl eingeben");
        return Double.parseDouble(displayText);
    }
//Führt die Operationen aus
    //In die 4 verschiedenen cases sind die Operationen '+','-','*','/'
    private double calculate(double a, double b, String op) {
        if (op == null) return b;
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                //Division durch Null erzeugt einen Fehlermeldung
                if (b == 0.0) throw new ArithmeticException("Division durch null");
                return a / b;
            default:
                throw new IllegalArgumentException("Unbekannte Operator: " + op);
        }
    }
//Wandelt einen double in einen String um
    private String formatNumber(double v) {
        String s = FORMAT.format(v);
        //'-0' wird zu '0' umgewandelt
        if (s.equals("-0")) return "0";
        return s;
    }
//Taschenrechner wird in Error-Zustand gesetzt
    private void showError() {
        displayText = ERROR;
        errorState = true;
    }
}
