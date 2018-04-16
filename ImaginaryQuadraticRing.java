/*
 * Copyright (C) 2018 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package imaginaryquadraticinteger;

/**
 * An object to represent an imaginary quadratic integer ring. A ring of 
 * quadratic integers contains infinitely many numbers. But, for the sake of 
 * this program, objects of type ImaginaryQuadraticRing are held by objects of 
 * type ImaginaryQuadraticInteger rather than the other way around.
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticRing {

    /**
     * Ought to be a squarefree negative integer. For example, -7.
     */
    protected final int negRad;
    
    /**
     * A convenient holder for the absolute value of negRad. For example, 7.
     */
    protected final int absNegRad;
    
    /**
     * A convenient holder for the square root of absNegRad. For example, 
     * approximately 2.645751311. This is a machine precision number that will 
     * hopefully be adequate for graphics applications.
     */
    protected final double absNegRadSqrt;
    
    /**
     * Should be true only if negRad is congruent to 1 modulo 4. For example, 
     * given negRad = -7, d1mod4 is true; given negRad = -10, d1mod4 is false.
     */
    protected final boolean d1mod4;
    
    /**
     * Determines whether certain functions use blackboard bold or not. It is 
     * true by default. Getter is <code>preferBlackboardBold()</code> without 
     * arguments, setter is <code>preferBlackboardBold(true)</code> or 
     * <code>preferBlackboardBold(false)</code> as desired.
     */
    private static boolean preferenceForBlackboardBold = true;
    
    /**
     * Gets the value of the discriminant, divided by 4 when d1mod4 is true. For 
     * example, the discriminant of Z[sqrt(-10)] is -40; the discriminant of 
     * O_(Q(sqrt(-7))) is -7.
     * @return The negative integer d for Q(sqrt(d)). For instance, for Z[-10], 
     * returns -10 (which is -40 divided by 4); for O_(Q(sqrt(-7))) it returns 
     * -7.
     */
    public int getNegRad() {
        return this.negRad;
    }
    
    /**
     * Gets the value of the discriminant, divided by -4 when d1mod4 is true. 
     * For example, the discriminant of Z[sqrt(-10)] is -40; the discriminant of 
     * O_(Q(sqrt(-7))) is -7.
     * @return The positive integer -d for Q(sqrt(d)). For instance, for Z[-10], 
     * returns 10 (which is -40 divided by -4); for O_(Q(sqrt(-7))) it returns 
     * 7.
     */
    public int getAbsNegRad() {
        return this.absNegRad;
    }

    /**
     * Gets the square root of the absolute value of the discriminant (divided 
     * by 4 when necessary), but always multiplied by -1.
     * @return sqrt(abs(d)). For example, for Z[i] this would 1, for Z[sqrt(-2)] 
     * this would be approximately 1.414213562373.
     */
    public double getAbsNegRadSqrt() {
        return this.absNegRadSqrt;
    }
    
    /**
     * Tells whether the ring has what are imprecisely called "half-integers."
     * These are numbers of the form a/2 + b sqrt(d)/2, with both a and b odd 
     * integers. This is essentially a public getter for the immutable protected 
     * property d1mod4.
     * @return True if d is congruent to 1 modulo 4, false otherwise. For 
     * example, given negRad = -7, this returns true; given negRad = -10, this 
     * returns false.
     */
    public boolean hasHalfIntegers() {
        return this.d1mod4;
    }
    
    /**
     * Query the setting of the preference for blackboard bold.
     * @return true if blackboard bold is preferred, false if plain bold is 
     * preferred.
     */
    public static boolean preferBlackboardBold() {
        return preferenceForBlackboardBold;
    }
    
    /**
     * Set preference for blackboard bold or plain bold. This is only relevant 
     * for the functions toTeXString() and toHTMLString().
     * @param preferenceForBB true if you prefer blackboard bold, false if you 
     * prefer plain bold. Note that this is a static property that will apply to 
     * other runtime instances unless it is explicitly changed.
     */
    public static void preferBlackboardBold(boolean preferenceForBB) {
        preferenceForBlackboardBold = preferenceForBB;
    }
    
    /**
     * A text representation of the ring's label. In some contexts, 
     * toASCIIString(), toTeXString(), toHTMLString() or toFilenameString may be 
     * preferable.
     * @return A String representing the imaginary quadratic ring which can be 
     * output to the console. Examples: for d = -7, the result is 
     * "O_(Q(\u221A-7))"; for d = -5, the result is "Z[\u221A-5]"; for d = -3, 
     * the result is "Z[\u03C9]". This presupposes that the console can display 
     * the "\u221A" and "\u03C9" characters.
     */
    @Override
    public String toString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "Z[i]"; // i is the imaginary unit, sqrt(-1)
                break;
            case -3:
                IQRString = "Z[\u03C9]"; // omega = -1/2 + sqrt(-3)/2 is a complex cubic root of 1
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "O_(Q(\u221A" + this.negRad + "))";
                } else {
                    IQRString = "Z[\u221A" + this.negRad + "]";
                }
        }
        return IQRString;
    }
    
    /**
     * A text representation of the ring's label. The main difference between 
     * this function and toString() is that this function does not use the 
     * "\u221A" character, using "sqrt" instead.
     * @return A String representing the imaginary quadratic ring which can be 
     * output to the console. Examples: for d = -7, the result is 
     * "O_(Q(sqrt(-7)))"; for d = -5, the result is "Z[sqrt(-5)]"; for d = -3, 
     * the result is "Z[omega]". Then you don't have to worry about the ability 
     * to display the "\u221A" and "\u03C9" characters.
     */
    public String toASCIIString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "Z[i]"; // i is the imaginary unit, sqrt(-1)
                break;
            case -3:
                IQRString = "Z[omega]"; // omega = -1/2 + sqrt(-3)/2 is a complex cubic root of 1
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "O_(Q(sqrt(" + this.negRad + ")))";
                } else {
                    IQRString = "Z[sqrt(" + this.negRad + ")]";
                }
        }
        return IQRString;
    }

    /**
     * A text representation of the ring's label suitable for use in a TeX 
     * document. The representation uses blackboard bold unless 
     * <code>preferBlackboardBold(false)</code> is in effect.
     * I have not tested this function in the context of outputting to a TeX 
     * document.
     * @return A String suitable for use in a TeX document, if I haven't made 
     * any mistakes. Examples: assuming the default preference for blackboard 
     * bold, for d = -7, the result is "\mathcal O_{\mathbb Q(\sqrt{-7})}"; for 
     * d = -5, the result is "\mathbb Z[\sqrt{-5})]"; for d = -3, the result is 
     * "\mathbb Z[\omega]".
     */
    public String toTeXString() {
        String IQRString;
        String QChar;
        String ZChar;
        if (preferenceForBlackboardBold) {
            QChar = "\\mathbb Q";
            ZChar = "\\mathbb Z";
        } else {
            QChar = "\\textbf Q";
            ZChar = "\\textbf Z";
        }
        switch (this.negRad) {
            case -1:
                IQRString = ZChar + "[i]";
                break;
            case -3:
                IQRString = ZChar + "[\\omega]";
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "\\mathcal O_{" + QChar + "(\\sqrt{" + this.negRad + "})}";
                } else {
                    IQRString = ZChar + "[\\sqrt{" + this.negRad + "}]";
            }
        }
        return IQRString;
    }
    
    /**
     * A text representation of the ring's label suitable for use in an HTML 
     * document.
     * I have not tested this function in the context of outputting to an HTML 
     * document.
     * @return A String suitable for use in an HTML document, if I haven't made 
     * any mistakes. If preferenceForBlackboardBold is true, this also assumes 
     * the font chosen by the browser has the relevant Unicode characters. 
     * Examples: with preference for blackboard bold on, we should get 
     * "<i>O</i><sub>\u211A(&radic;-7)</sub>" for d = -7; for d = -5, the result 
     * is "\u2124[&radic;-5]"; for d = -3, the result is "\u2124[\u03C9]". If 
     * instead preference for blackboard bold is off, we should get 
     * "<i>O</i><sub><b>Q</b>(&radic;-7)</sub>" for d = -7; for d = -5, the 
     * result is "<b>Z</b>[&radic;-5]"; for d = -3, the result is 
     * "<b>Z</b>[\u03C9]".
     */
    public String toHTMLString() {
        String IQRString;
        String QChar;
        String ZChar;
        if (preferenceForBlackboardBold) {
            QChar = "\u211A"; // Double-struck capital Q
            ZChar = "\u2124"; // Double-struck capital Z
        } else {
            QChar = "<b>Q</b>";
            ZChar = "<b>Z</b>";
        }
        switch (this.negRad) {
            case -1:
                IQRString = ZChar + "[<i>i</i>]";
                break;
            case -3:
                IQRString = ZChar + "[\u03C9]";
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "<i>O</i><sub>" + QChar + "(&radic;(" + this.negRad + ")</sub>";
                } else {
                    IQRString = ZChar + "[&radic;" + this.negRad + "]";
                }
        }
        return IQRString;
    }
    
    /**
     * A text representation of the ring's label suitable for use in a filename.
     * With our modern operating systems, it may be unnecessary to worry about 
     * illegal characters in toString(). But just in case, here is a function 
     * whose output will hopefully help the calling program conform to the old 
     * MS-DOS 8.3 standard. There is one context in which I have definitely 
     * found this function useful, and that is in the output of the test suite, 
     * for which the font seems to lack Greek letters.
     * @return A string suitable for use in a filename. Examples: for d = -1, 
     * returns "ZI"; for d = -2, returns "ZI2"; for d = -3, returns "ZW"; for d 
     * = -7, returns "OQI7".
     */
    public String toFilenameString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "ZI";
                break;
            case -3:
                IQRString = "ZW"; // It is frowned upon to use "w" as a makeshift omega, but for the purpose here it is acceptable.
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "OQI" + this.absNegRad;
                } else {
                    IQRString = "ZI" + this.absNegRad;
                }
        }
        return IQRString;
    }
    
    /**
     * Class constructor. Its task is, after validating the parameter, simply to 
     * set the appropriate protected final properties.
     * @param d A negative, squarefree integer.
     * @throws IllegalArgumentException If d is 0 or any positive integer, or if 
     * d is negative but not squarefree.
     */
    public ImaginaryQuadraticRing(int d) {
        if (d > -1) {
            throw new IllegalArgumentException("Negative integer required for parameter d.");
        }
        if (!NumberTheoreticFunctionsCalculator.isSquareFree(d)) {
            throw new IllegalArgumentException("Squarefree integer required for parameter d.");
        }
        this.d1mod4 = (d % 4 == -3); // For whatever reason, odd negative numbers modulo 4 are given as -3 and -1 rather than 1 and 3.
        this.negRad = d;
        this.absNegRad = Math.abs(negRad);
        this.absNegRadSqrt = Math.sqrt(absNegRad);
    }
    
}