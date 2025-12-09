package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaschenrechnerTest {

    private TaschenrechnerLogic newCalc() {
        return new TaschenrechnerLogic();
    }
   //Testet ob Display initial '0' anzeigt
    @Test
    void testInitialState() {
        TaschenrechnerLogic calc = newCalc();
        assertEquals("0", calc.getDisplayText());
        assertFalse(calc.isErrorState());
    }
//Testet ob Ziffern korrekt hintereinander angehängt werden
    @Test
    void testAppendDigits() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("5");
        calc.appendDigit("2");
        assertEquals("52", calc.getDisplayText());
    }
//Testet ob Dezimalpunkt korrekt gesetzt wird
    @Test
    void testAppendDot() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("3");
        calc.appendDot();
        calc.appendDigit("5");
        assertEquals("3.5", calc.getDisplayText());
    }
//Verhindert das Setzen eines doppelten Dezimalpunktes
    @Test
    void testPreventDoubleDot() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("7");
        calc.appendDot();
        calc.appendDot();
        assertEquals("7.", calc.getDisplayText());
    }
//Testet die korrekte Addition
    @Test
    void testAddition() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("4");
        calc.operatorPressed("+");
        calc.appendDigit("6");
        calc.equalsPressed();
        assertEquals("10", calc.getDisplayText());
    }
//Testet die korrekte Subtraktion
    @Test
    void testSubtraction() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.operatorPressed("-");
        calc.appendDigit("5");
        calc.equalsPressed();
        assertEquals("4", calc.getDisplayText());
    }
//Testet die korrekte Multiplikation
    @Test
    void testMultiplication() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("3");
        calc.operatorPressed("*");
        calc.appendDigit("7");
        calc.equalsPressed();
        assertEquals("21", calc.getDisplayText());
    }
//Testet die korrekte Division
    @Test
    void testDivision() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("8");
        calc.operatorPressed("/");
        calc.appendDigit("2");
        calc.equalsPressed();
        assertEquals("4", calc.getDisplayText());
    }
//Erzeugt einen Fehlerzustand bei Division durch Null
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
//Testet wiederholte Ausführung derselben Operation über die Gleich Taste
    @Test
    void testRepeatedEquals() {
        TaschenrechnerLogic calc = newCalc();

        calc.appendDigit("2");
        calc.operatorPressed("+");
        calc.appendDigit("3");
        calc.equalsPressed();

        calc.equalsPressed();
        calc.equalsPressed();

        assertEquals("11", calc.getDisplayText());
    }
//Testet die korrekte Quadratrechnung
    @Test
    void testSquare() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("4");
        calc.squarePressed();
        assertEquals("16", calc.getDisplayText());
    }
//Testet die korrekte Quadratwurzelberechnung
    @Test
    void testSqrt() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.sqrtPressed();
        assertEquals("3", calc.getDisplayText());
    }
//Erzeugt einen Fehlerzustand bei Quadratwurzel einer negativen Zahl
    @Test
    void testSqrtOfNegative() {
        TaschenrechnerLogic calc = newCalc();
        calc.startNegativeNumber();
        calc.appendDigit("9");
        calc.sqrtPressed();

        assertTrue(calc.isErrorState());
        assertEquals("Error", calc.getDisplayText());
    }
//Testet die korrekte Uwandlung einer Dezimalzahl in eine Binärzahl
    @Test
    void testBinary() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("6");
        calc.binaryPressed();
        assertEquals("110", calc.getDisplayText());
    }
//Erzeugt einen Fehlerzustand bei der Umwandlung nicht-ganzzahligen Werte
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
//Testet die korrekte Backspace Funktion
    @Test
    void testBackspace() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("9");
        calc.appendDigit("8");
        calc.backspace();
        assertEquals("9", calc.getDisplayText());
    }
//Testet ob nach Löschen der letzten Ziffer eine Null angezeigt wird
    @Test
    void testBackspaceToZero() {
        TaschenrechnerLogic calc = newCalc();
        calc.appendDigit("7");
        calc.backspace();
        assertEquals("0", calc.getDisplayText());
    }
//Testet die Eingabe einer negativer Zahl zu Beginn
    @Test
    void testStartNegativeNumber() {
        TaschenrechnerLogic calc = newCalc();
        calc.startNegativeNumber();
        calc.appendDigit("5");
        assertEquals("-5", calc.getDisplayText());
    }
}
