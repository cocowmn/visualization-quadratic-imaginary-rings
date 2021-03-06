/*
 * Copyright (C) 2018 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package imaginaryquadraticinteger;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the ImaginaryQuadraticRing class.
 * @author Alonso del Arte, from template generated by NetBeans IDE.
 */
public class ImaginaryQuadraticRingTest {
    
    private static ImaginaryQuadraticRing ringGaussian;
    private static ImaginaryQuadraticRing ringZi2;
    private static ImaginaryQuadraticRing ringEisenstein;
    private static ImaginaryQuadraticRing ringOQi7;
    private static ImaginaryQuadraticRing ringRandom;
    
    private static int randomDiscr;
    private static boolean ringRandomd1mod4;
    
    /**
     * The delta value to use when assertEquals() requires a delta value.
     * Is this an appropriate value, or does it need to be smaller?
     */
    public static final double TEST_DELTA = 0.00001;
    
    /**
     * Sets up five ImaginaryQuadraticRing objects, corresponding to 
     * <b>Z</b>[<i>i</i>], <b>Z</b>[&radic;-2], <b>Z</b>[&omega;], 
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub> and a randomly chosen ring.
     * The randomly chosen ring <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> is 
     * determined by <i>d</i> being at most -5. It is unlikely but not 
     * impossible that this could turn out to be 
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub>, which would be just fine if it 
     * just made some of the tests redundant, but since it could make {@link 
     * #testEquals()} fail, it is necessary to guard against this unlikely 
     * eventuality.
     */
    @BeforeClass
    public static void setUpClass() {
        randomDiscr = NumberTheoreticFunctionsCalculator.randomNegativeSquarefreeNumber(RingWindowDisplay.MINIMUM_RING_D);
        if (randomDiscr > -5) {
            randomDiscr = -5; // This is just in case we get -3 or -1, which we are already testing for and which require special treatment in some of the tests.
        }
        if (randomDiscr == -7) {
            randomDiscr = -11; // Resetting -7 to -11 since -7 is being tested regardless
        }
        ringRandomd1mod4 = (randomDiscr % 4 == -3);
        ringGaussian = new ImaginaryQuadraticRing(-1);
        ringZi2 = new ImaginaryQuadraticRing(-2);
        ringEisenstein = new ImaginaryQuadraticRing(-3);
        ringOQi7 = new ImaginaryQuadraticRing(-7);
        ringRandom = new ImaginaryQuadraticRing(randomDiscr);
        System.out.println(ringRandom.toASCIIString() + " has been randomly chosen for testing purposes.");
    }
    
    /**
     * Test of getNegRad method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testGetNegRad() {
        System.out.println("getNegRad");
        assertEquals(-1, ringGaussian.getNegRad());
        assertEquals(-2, ringZi2.getNegRad());
        assertEquals(-3, ringEisenstein.getNegRad());
        assertEquals(-7, ringOQi7.getNegRad());
        assertEquals(randomDiscr, ringRandom.getNegRad());
    }

    /**
     * Test of getAbsNegRad method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testGetAbsNegRad() {
        System.out.println("getAbsNegRad");
        assertEquals(1, ringGaussian.getAbsNegRad());
        assertEquals(2, ringZi2.getAbsNegRad());
        assertEquals(3, ringEisenstein.getAbsNegRad());
        assertEquals(7, ringOQi7.getAbsNegRad());
        assertEquals(-randomDiscr, ringRandom.getAbsNegRad());
    }

    /**
     * Test of getAbsNegRadSqrt method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testGetAbsNegRadSqrt() {
        System.out.println("getAbsNegRadSqrt");
        assertEquals(1.0, ringGaussian.getAbsNegRadSqrt(), TEST_DELTA);
        assertEquals(Math.sqrt(2), ringZi2.getAbsNegRadSqrt(), TEST_DELTA);
        assertEquals(Math.sqrt(3), ringEisenstein.getAbsNegRadSqrt(), TEST_DELTA);
        assertEquals(Math.sqrt(7), ringOQi7.getAbsNegRadSqrt(), TEST_DELTA);
        assertEquals(Math.sqrt(-randomDiscr), ringRandom.getAbsNegRadSqrt(), TEST_DELTA);
    }
    
    /**
     * Test of hasHalfIntegers method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testHasHalfIntegers() {
        System.out.println("hasHalfIntegers");
        String assertionMessage = ringGaussian.toASCIIString() + " should not be said to have half-integers.";
        assertFalse(assertionMessage, ringGaussian.hasHalfIntegers());
        assertionMessage = ringZi2.toASCIIString() + " should not be said to have half-integers.";
        assertFalse(assertionMessage, ringZi2.hasHalfIntegers());
        assertionMessage = ringEisenstein.toASCIIString() + " should be said to have half-integers.";
        assertTrue(assertionMessage, ringEisenstein.hasHalfIntegers());
        assertionMessage = ringOQi7.toASCIIString() + " should be said to have half-integers.";
        assertTrue(assertionMessage, ringOQi7.hasHalfIntegers());
        assertionMessage = ringRandom.toASCIIString() + " should ";
        if (ringRandomd1mod4) {
            assertionMessage = assertionMessage + "be said to have half-integers.";
        } else {
            assertionMessage = assertionMessage + "not be said to have half-integers.";
        }
        assertEquals(assertionMessage, ringRandomd1mod4, ringRandom.hasHalfIntegers());
    }

    /**
     * Test of preferBlackboardBold method, of class ImaginaryQuadraticRing.
     * Without arguments, preferBlackboardBold is the getter method. With 
     * arguments, preferBlackboardBold is the setter method. This is perhaps an 
     * unnecessary test. The results of {@link #testToString()} and {@link 
     * #testToHTMLString()} are far more important.
     */
    @Test
    public void testPreferBlackboardBold() {
        System.out.println("preferBlackboardBold");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        assertTrue(ImaginaryQuadraticRing.preferBlackboardBold());
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        assertFalse(ImaginaryQuadraticRing.preferBlackboardBold());
    }
    
    /**
     * Test of hashCode method, of class ImaginaryQuadraticRing. The purpose 
     * here isn't to test that any specific ring maps to any specific hash code,  
     * but rather that two rings that are equal get the same hash code, and two 
     * rings that are not equal get different hash codes.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        ImaginaryQuadraticRing someRing = new ImaginaryQuadraticRing(-1);
        int expResult = ringGaussian.hashCode();
        System.out.println("BeforeClass-initialized " + ringGaussian.toASCIIString() + " hashed as " + expResult);
        int result = someRing.hashCode();
        System.out.println("Test-initialized " + someRing.toASCIIString() + " hashed as " + expResult);
        String assertionMessage = "BeforeClass-initialized and test-initialized Z[i] should hash the same.";
        assertEquals(assertionMessage, expResult, result);
        assertionMessage = ringGaussian.toASCIIString() + " and " + ringZi2.toASCIIString() + " should hash differently.";
        assertNotEquals(assertionMessage, someRing.hashCode(), ringZi2.hashCode());
        someRing = new ImaginaryQuadraticRing(-2);
        expResult = ringZi2.hashCode();
        System.out.println("BeforeClass-initialized " + ringZi2.toASCIIString() + " hashed as " + expResult);
        assertEquals(ringZi2.hashCode(), someRing.hashCode());
        result = someRing.hashCode();
        System.out.println("Test-initialized " + someRing.toASCIIString() + " hashed as " + expResult);
        assertionMessage = "BeforeClass-initialized and test-initialized Z[i] should hash the same.";
        assertEquals(assertionMessage, expResult, result);
        assertionMessage = ringGaussian.toASCIIString() + " and " + ringZi2.toASCIIString() + " should hash differently.";
        assertNotEquals(assertionMessage, someRing.hashCode(), ringGaussian.hashCode());
    }
    
    /**
     * Test of equals method, of class ImaginaryQuadraticRing. The reflexive, 
     * symmetric and transitive properties are tested for rings that should 
     * register as equal. Then five different rings are tested to check that 
     * they're not registering as equal.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        ImaginaryQuadraticRing someRing = new ImaginaryQuadraticRing(-1);
        ImaginaryQuadraticRing transitiveHold = new ImaginaryQuadraticRing(-1);
        assertTrue(ringGaussian.equals(ringGaussian)); // Reflexive test
        assertEquals(ringGaussian, someRing);
        assertEquals(someRing, ringGaussian); // Symmetric test
        assertEquals(someRing, transitiveHold);
        assertEquals(transitiveHold, ringGaussian); // Transitive test
        // Now to test that rings that are not equal are reported as not equal
        assertNotEquals(ringGaussian, ringZi2);
        assertNotEquals(ringZi2, ringEisenstein);
        assertNotEquals(ringEisenstein, ringOQi7);
        assertNotEquals(ringOQi7, ringRandom);
    }

    /**
     * Test of toString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult = "Z[i]";
        String result = ringGaussian.toString();
        assertEquals(expResult, result);
        expResult = "Z[\u221A-2]";
        result = ringZi2.toString();
        assertEquals(expResult, result);
        expResult = "Z[\u03C9]";
        result = ringEisenstein.toString();
        assertEquals(expResult, result);
        expResult = "O_(Q(\u221A-7))";
        result = ringOQi7.toString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "O_(Q(\u221A" + randomDiscr + "))";
        } else {
            expResult = "Z[\u221A" + randomDiscr + "]";
        }
        result = ringRandom.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toASCIIString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToASCIIString() {
        System.out.println("toASCIIString");
        String expResult = "Z[i]";
        String result = ringGaussian.toASCIIString();
        assertEquals(expResult, result);
        expResult = "Z[sqrt(-2)]";
        result = ringZi2.toASCIIString();
        assertEquals(expResult, result);
        expResult = "Z[omega]";
        result = ringEisenstein.toASCIIString();
        assertEquals(expResult, result);
        expResult = "O_(Q(sqrt(-7)))";
        result = ringOQi7.toASCIIString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "O_(Q(sqrt(" + randomDiscr + ")))";
        } else {
            expResult = "Z[sqrt(" + randomDiscr + ")]";
        }
        result = ringRandom.toASCIIString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toTeXString method, of class ImaginaryQuadraticRing. Note that 
     * the blackboard preference has an effect on the output.
     */
    @Test
    public void testToTeXString() {
        System.out.println("toTeXString");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        String expResult = "\\mathbb Z[i]";
        String result = ringGaussian.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathbb Z[\\sqrt{-2}]";
        result = ringZi2.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathbb Z[\\omega]";
        result = ringEisenstein.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathcal O_{\\mathbb Q(\\sqrt{-7})}";
        result = ringOQi7.toTeXString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "\\mathcal O_{\\mathbb Q(\\sqrt{" + randomDiscr + "})}";
        } else {
            expResult = "\\mathbb Z[\\sqrt{" + randomDiscr + "}]";
        }
        result = ringRandom.toTeXString();
        assertEquals(expResult, result);
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        expResult = "\\textbf Z[i]";
        result = ringGaussian.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\textbf Z[\\sqrt{-2}]";
        result = ringZi2.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\textbf Z[\\omega]";
        result = ringEisenstein.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathcal O_{\\textbf Q(\\sqrt{-7})}";
        result = ringOQi7.toTeXString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "\\mathcal O_{\\textbf Q(\\sqrt{" + randomDiscr + "})}";
        } else {
            expResult = "\\textbf Z[\\sqrt{" + randomDiscr + "}]";
        }
        result = ringRandom.toTeXString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toHTMLString method, of class ImaginaryQuadraticRing. Note that 
     * the blackboard preference has an effect on the output.
     */
    @Test
    public void testToHTMLString() {
        System.out.println("toHTMLString");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        String expResult = "\u2124[<i>i</i>]";
        String result = ringGaussian.toHTMLString();
        assertEquals(expResult, result);
        expResult = "\u2124[&radic;-2]";
        result = ringZi2.toHTMLString();
        assertEquals(expResult, result);
        expResult = "\u2124[\u03C9]";
        result = ringEisenstein.toHTMLString();
        assertEquals(expResult, result);
        expResult = "<i>O</i><sub>\u211A(&radic;(-7)</sub>";
        result = ringOQi7.toHTMLString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "<i>O</i><sub>\u211A(&radic;(" + randomDiscr + ")</sub>";
        } else {
            expResult = "\u2124[&radic;" + randomDiscr + "]";
        }
        result = ringRandom.toHTMLString();
        assertEquals(expResult, result);
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        expResult = "<b>Z</b>[<i>i</i>]";
        result = ringGaussian.toHTMLString();
        assertEquals(expResult, result);
        expResult = "<b>Z</b>[&radic;-2]";
        result = ringZi2.toHTMLString();
        assertEquals(expResult, result);
        expResult = "<b>Z</b>[\u03C9]";
        result = ringEisenstein.toHTMLString();
        assertEquals(expResult, result);
        expResult = "<i>O</i><sub><b>Q</b>(&radic;(-7)</sub>";
        result = ringOQi7.toHTMLString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "<i>O</i><sub><b>Q</b>(&radic;(" + randomDiscr + ")</sub>";
        } else {
            expResult = "<b>Z</b>[&radic;" + randomDiscr + "]";
        }
        result = ringRandom.toHTMLString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toFilenameString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToFilenameString() {
        System.out.println("toFilenameString");
        // Preference for blackboard bold is irrelevant for this particular test.
        String expResult = "ZI";
        String result = ringGaussian.toFilenameString();
        assertEquals(expResult, result);
        expResult = "ZI2";
        result = ringZi2.toFilenameString();
        assertEquals(expResult, result);
        expResult = "ZW";
        result = ringEisenstein.toFilenameString();
        assertEquals(expResult, result);
        expResult = "OQI7";
        result = ringOQi7.toFilenameString();
        assertEquals(expResult, result);
        if (ringRandomd1mod4) {
            expResult = "OQI" + (-1 * randomDiscr);
        } else {
            expResult = "ZI" + (-1 * randomDiscr);
        }
        result = ringRandom.toFilenameString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of ImaginaryQuadraticRing class constructor. The main thing we're 
     * testing here is that an invalid argument triggers an 
     * IllegalArgumentException. That the other tests pass makes us plenty 
     * confident that the constructor works correctly on valid arguments.
     */
    @Test
    public void testConstructor() {
        System.out.println("ImaginaryQuadraticRing (constructor)");
        ImaginaryQuadraticRing ringZi10 = new ImaginaryQuadraticRing(-10); // This should work fine
        System.out.println("Created " + ringZi10.toASCIIString() + " without problem.");
        ImaginaryQuadraticRing ringOQi11 = new ImaginaryQuadraticRing(-11); // This should also work fine
        System.out.println("Created " + ringOQi11.toASCIIString() + " without problem.");
        try {
            ImaginaryQuadraticRing ringZi12 = new ImaginaryQuadraticRing(-12);
            System.out.println("Somehow created " + ringZi12.toASCIIString() + " without problem.");
            fail("Attempt to use -12 should have caused an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Attempt to use -12 correctly triggered IllegalArgumentException \"" + iae.getMessage() + "\"");
        }
        try {
            ImaginaryQuadraticRing ringZ7 = new ImaginaryQuadraticRing(7);
            System.out.println("Somehow created " + ringZ7.toASCIIString() + " without problem.");
            fail("Attempt to use 7 should have caused an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Attempt to use 7 correctly triggered IllegalArgumentException \"" + iae.getMessage() + "\"");
        }
    }
    
}
