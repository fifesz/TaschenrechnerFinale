package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaschenrechnerTest {

    // ----------------------------------------------------------
    // Hilfsmethode: neues Objekt erstellen
    // ----------------------------------------------------------
    private TaschenrechnerLogic newCalc() {
        return new TaschenrechnerLogic();
    }

    // ----------------------------------------------------------
    // Grundlegendes Verhalten
    // ----------------------------------------------------------
    @Test
    void testInitialState() {
        TaschenrechnerLogic calc = newCalc();
        assertEquals("0", calc.getDisplayText());
        assertFalse(calc.isErrorState());
    }

    @Test
    void testAppendDigits() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("5");
        calc.appendDigit("2");
        assertEquals("52", calc.getDisplayText());
    }

    @Test
    void testAppendDot() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("3");
        calc.appendDot();
        calc.appendDigit("5");
        assertEquals("3.5", calc.getDisplayText());
    }

    @Test
    void testPreventDoubleDot() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("7");
        calc.appendDot();
        calc.appendDot();   // ignored
        assertEquals("7.", calc.getDisplayText());
    }

    // ----------------------------------------------------------
    // Operatoren
    // ----------------------------------------------------------
    @Test
    void testAddition() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("4");
        calc.operatorPressed("+");
        calc.appendDigit("6");
        calc.equalsPressed();
        assertEquals("10", calc.getDisplayText());
    }

    @Test
    void testSubtraction() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.operatorPressed("-");
        calc.appendDigit("5");
        calc.equalsPressed();
        assertEquals("4", calc.getDisplayText());
    }

    @Test
    void testMultiplication() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("3");
        calc.operatorPressed("*");
        calc.appendDigit("7");
        calc.equalsPressed();
        assertEquals("21", calc.getDisplayText());
    }

    @Test
    void testDivision() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("8");
        calc.operatorPressed("/");
        calc.appendDigit("2");
        calc.equalsPressed();
        assertEquals("4", calc.getDisplayText());
    }

    @Test
    void testDivisionByZero() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("5");
        calc.operatorPressed("/");
        calc.appendDigit("0");
        calc.equalsPressed();

        assertTrue(calc.isErrorState());
        assertEquals("Error", calc.getDisplayText());
    }

    // ----------------------------------------------------------
    // Wiederholtes Drücken von "="
    // ----------------------------------------------------------
    @Test
    void testRepeatedEquals() {
        TaschenrechnerLogic calc = newCalc();

        calc.appendDigit("2");
        calc.operatorPressed("+");
        calc.appendDigit("3");
        calc.equalsPressed();     // = 5

        calc.equalsPressed();     // = 8
        calc.equalsPressed();     // = 11

        assertEquals("11", calc.getDisplayText());
    }

    // ----------------------------------------------------------
    // Spezialfunktionen
    // ----------------------------------------------------------
    @Test
    void testSquare() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("4");
        calc.squarePressed();
        assertEquals("16", calc.getDisplayText());
    }

    @Test
    void testSqrt() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.sqrtPressed();
        assertEquals("3", calc.getDisplayText());
    }

    @Test
    void testSqrtOfNegative() {
        TaschenrechnerLogic calc = newCalc();
        calc.startNegativeNumber();
        calc.appendDigit("9");
        calc.sqrtPressed();

        assertTrue(calc.isErrorState());
        assertEquals("Error", calc.getDisplayText());
    }

    @Test
    void testBinary() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("6");
        calc.binaryPressed();
        assertEquals("110", calc.getDisplayText());
    }

    @Test
    void testBinaryFailsOnNonInteger() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("5");
        calc.appendDot();
        calc.appendDigit("2");
        calc.binaryPressed();

        assertTrue(calc.isErrorState());
        assertEquals("Error", calc.getDisplayText());
    }

    // ----------------------------------------------------------
    // Backspace
    // ----------------------------------------------------------
    @Test
    void testBackspace() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.appendDigit("8");
        calc.backspace();
        assertEquals("9", calc.getDisplayText());
    }

    @Test
    void testBackspaceToZero() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("7");
        calc.backspace();
        assertEquals("0", calc.getDisplayText());
    }

    // ----------------------------------------------------------
    // Negative Zahlen starten
    // ----------------------------------------------------------
    @Test
    void testStartNegativeNumber() {
        TaschenrechnerLogic calc = newCalc();
        calc.startNegativeNumber();
        calc.appendDigit("5");
        assertEquals("-5", calc.getDisplayText());
    }
}
