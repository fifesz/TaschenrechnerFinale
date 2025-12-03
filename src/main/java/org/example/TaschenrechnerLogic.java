package org.example;

import java.text.DecimalFormat;

public class TaschenrechnerLogic {

    // Konstanten
    public static final String ERROR = "Error";
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##########");

    // Zustand
    private String displayText = "0";
    private double currentValue = 0;
    private String currentOperator = null;
    private String lastOperator = null;
    private double lastOperand = 0;
    private boolean enteringNewNumber = true;
    private boolean errorState = false;

    // --- GETTER / SETTER für GUI & Tests
    public String getDisplayText() {
        return displayText;
    }

    public boolean isErrorState() {
        return errorState;
    }

    /** Ermöglicht GUI, eine neue negative Zahl zu starten (setzt "-" im Display). */
    public void startNegativeNumber() {
        if (errorState) return;
        displayText = "-";
        enteringNewNumber = false;
    }

    // --------------------------------
    // Grundoperationen für Eingaben
    // --------------------------------
    public void appendDigit(String digit) {
        if (errorState) return;

        if (enteringNewNumber) {
            displayText = digit;
            enteringNewNumber = false;
        } else {
            // wenn aktuell nur "-" steht (negativer Start), erlauben wir Anfügen
            if (displayText.equals("-")) displayText = "-" + digit;
            else displayText = displayText + digit;
        }
    }

    public void appendDot() {
        if (errorState) return;

        if (enteringNewNumber) {
            displayText = "0.";
            enteringNewNumber = false;
        } else if (!displayText.contains(".")) {
            displayText = displayText + ".";
        }
    }

    public void backspace() {
        if (errorState) return;
        if (enteringNewNumber) return;

        if (displayText.length() <= 1) {
            displayText = "0";
            enteringNewNumber = true;
        } else {
            displayText = displayText.substring(0, displayText.length() - 1);
            // wenn nur "-" übrig bleibt, setzen wir enteringNewNumber = false so that user can continue "-x"
            if (displayText.equals("-")) enteringNewNumber = false;
        }
    }

    public void clearAll() {
        displayText = "0";
        currentValue = 0;
        currentOperator = null;
        lastOperator = null;
        lastOperand = 0;
        enteringNewNumber = true;
        errorState = false;
    }

    // --------------------------------
    // Operatoren / Rechnen
    // --------------------------------
    public void operatorPressed(String op) {
        if (errorState) return;

        try {
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

    public void equalsPressed() {
        if (errorState) return;

        try {
            double displayed = parseDisplay();

            if (currentOperator != null) {
                double result = calculate(currentValue, displayed, currentOperator);

                lastOperator = currentOperator;
                lastOperand = displayed;

                currentValue = result;
                displayText = formatNumber(result);

                currentOperator = null;
                enteringNewNumber = true;
            } else if (lastOperator != null) {
                double base = parseDisplay();
                double result = calculate(base, lastOperand, lastOperator);

                currentValue = result;
                displayText = formatNumber(result);
                enteringNewNumber = true;
            }
        } catch (ArithmeticException ae) {
            showError();
        } catch (Exception e) {
            showError();
        }
    }

    // --------------------------------
    // Spezialfunktionen
    // --------------------------------
    public void sqrtPressed() {
        if (errorState) return;
        try {
            double v = parseDisplay();
            if (v < 0) { showError(); return; }
            displayText = formatNumber(Math.sqrt(v));
            enteringNewNumber = true;
        } catch (Exception e) { showError(); }
    }

    public void squarePressed() {
        if (errorState) return;
        try {
            double v = parseDisplay();
            displayText = formatNumber(v * v);
            enteringNewNumber = true;
        } catch (Exception e) { showError(); }
    }

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
        } catch (Exception e) { showError(); }
    }

    // --------------------------------
    // Hilfsmethoden
    // --------------------------------
    private double parseDisplay() {
        // support "-" alone? if user typed just "-" treat as 0? we disallow: parse will throw -> caught
        if (displayText.equals("-")) throw new NumberFormatException("Incomplete negative number");
        return Double.parseDouble(displayText);
    }

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
                if (b == 0.0) throw new ArithmeticException("Division by zero");
                return a / b;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    private String formatNumber(double v) {
        String s = FORMAT.format(v);
        if (s.equals("-0")) return "0";
        return s;
    }

    private void showError() {
        displayText = ERROR;
        errorState = true;
    }
}
