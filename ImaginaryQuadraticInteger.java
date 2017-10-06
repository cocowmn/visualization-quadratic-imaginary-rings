/*
 * Copyright (C) 2017 Alonso del Arte
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

import java.util.*;

/**
 *
 * @author Alonso del Arte
 */

public class ImaginaryQuadraticInteger implements AlgebraicInteger {
    
    protected int realPartMult;
    protected int imagPartMult;
    protected ImaginaryQuadraticRing imagQuadRing;
    
    /**
     * If d1mod4 is true, then denominator may be 1 or 2, otherwise denominator should be 1.
     */
    protected int denominator;

    /**
     * Gives the algebraic degree of the algebraic integer. Should not be higher than 2.
     * @return 0 if the algebraic integer is 0, 1 if it's a purely real integer, 2 otherwise.
     */
    @Override
    public int algebraicDegree() {
        if (imagPartMult == 0) {
            if (realPartMult == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }
    
    /**
     * Calculates the trace of the imaginary quadratic integer (real part plus real integer times square root of a negative integer)
     * @return Twice the real part
     */
    
    @Override
    public int trace() {
        if (imagQuadRing.d1mod4 && denominator == 2) {
            return realPartMult;
        } else {
            return 2 * realPartMult;
        }
    }
    
    /**
     * Calculates the norm of the imaginary quadratic integer (real part plus real integer times square root of a negative integer)
     * @return Square of the real part minus square of the imaginary part. May be 0 but never negative.
     */
    @Override
    public int norm() {
        int N;
        if (imagQuadRing.d1mod4 && denominator == 2) {
            N = (realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult)/4;
        } else {
            N = realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult;
        }
        return N;
    }
    
    /**
     * Gives the coefficients for the minimal polynomial of the algebraic integer
     * @return An array of three integers. If the algebraic integer is of degree 2, the array will be {norm, negative trace, 1}; if of degree 1, then {number, 1, 0}, and for 0, {0, 0, 0}.
     */
    @Override
    public int[] minPolynomial() {
        int[] coeffs = {0, 0, 0};
        switch (algebraicDegree()) {
            case 0: 
                break;
            case 1: 
                coeffs[0] = -1 * realPartMult;
                coeffs[1] = 1;
                break;
            case 2: 
                coeffs[0] = norm();
                coeffs[1] = -trace();
                coeffs[2] = 1;
                break;
        }
        return coeffs;
    }
    
    @Override
    public String minPolynomialString() {
        String polString = "";
        int[] polCoeffs = minPolynomial();
        switch (algebraicDegree()) {
            case 0:
                break;
            case 1:
                if (polCoeffs[0] < 0) {
                    polString = "x - " + ((-1) * polCoeffs[0]);
                } else {
                    polString = "x + " + polCoeffs[0];
                }
                break;
            case 2:
                polString = "x^2 ";
                if (polCoeffs[1] < -1) {
                    polString += ("- " + ((-1) * polCoeffs[1]) + "x ");
                }
                if (polCoeffs[1] == -1) {
                    polString += "- x ";
                }
                if (polCoeffs[1] == 1) {
                    polString += "+ x ";
                }
                if (polCoeffs[1] > 1) {
                    polString += ("+ " + polCoeffs[1] + "x ");
                }
                if (polCoeffs[0] < 0) {
                    polString += ("- " + ((-1) * polCoeffs[0]));
                } else {
                    polString += ("+ " + polCoeffs[0]);
                }
                break;
        }
        return polString;
    }
    
    @Override
    public String toString() {
        String IQIString;
        if (this.denominator == 2) {
            IQIString = this.realPartMult + "/2 ";
            if (this.imagPartMult < -1) {
                IQIString += (("- " + ((-1) * this.imagPartMult)) + "\u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult == -1) {
                IQIString += ("- \u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult == 1) {
                IQIString += ("+ \u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult > 1) {
                IQIString += ("+ " + this.imagPartMult + "\u221A(" + this.imagQuadRing.negRad + ")/2");
            } 
        } else {
            IQIString = this.realPartMult + " ";
            if (this.imagPartMult < -1) {
                IQIString += (("- " + ((-1) * this.imagPartMult)) + "\u221A(" + this.imagQuadRing.negRad + ")");
            }
            if (this.imagPartMult == -1) {
                IQIString += ("- \u221A(" + this.imagQuadRing.negRad + ")");
            }
            if (this.imagPartMult == 1) {
                IQIString += ("+ \u221A(" + this.imagQuadRing.negRad + ")");
            }
            if (this.imagPartMult > 1) {
                IQIString += ("+ " + this.imagPartMult + "\u221A(" + this.imagQuadRing.negRad + ")");
            }
        }
        return IQIString;
    }
    
    /**
     * Addition operation, since operator + (plus) can't be overloaded.
     * @param summand The imaginary quadratic integer to be added.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come from different quadratic rings, the result of the sum will be an algebraic integer of degree 4 and this exception will be thrown.
     */
    public ImaginaryQuadraticInteger plus(ImaginaryQuadraticInteger summand) throws AlgebraicDegreeOverflowException {
        if (this.imagQuadRing.negRad != summand.imagQuadRing.negRad) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        int sumRealPart = 0;
        int sumImagPart = 0;
        int sumDenom = 1;
        if (this.imagQuadRing.d1mod4) {
            if (this.denominator == 1 && summand.denominator == 2) {
                sumRealPart = 2 * this.realPartMult + summand.realPartMult;
                sumImagPart = 2 * this.imagPartMult + summand.imagPartMult;
                sumDenom = 2;
            }
            if (this.denominator == 2 && summand.denominator == 1) {
                sumRealPart = this.realPartMult + 2 * summand.realPartMult;
                sumImagPart = this.imagPartMult + 2 * summand.imagPartMult;
                sumDenom = 2;
            }
            if (this.denominator == summand.denominator) {
                sumRealPart = this.realPartMult + summand.realPartMult;
                sumImagPart = this.imagPartMult + summand.imagPartMult;
                sumDenom = this.denominator;
            }
        } else {
            sumRealPart = this.realPartMult + summand.realPartMult;
            sumImagPart = this.imagPartMult + summand.imagPartMult;
            sumDenom = 1;
        }
        return new ImaginaryQuadraticInteger(sumRealPart, sumImagPart, this.imagQuadRing, sumDenom);
    }
    
    /**
     * Class constructor
     * @param a The real part of the imaginary quadratic integer, multiplied by 2 when applicable
     * @param b The part to be multiplied by sqrt(d), multiplied by 2 when applicable
     * @param R The ring to which this algebraic integer belongs to
     * @param denom In most cases 1, but may be 2 if a and b have the same parity and d = 1 mod 4
     */
    public ImaginaryQuadraticInteger(int a, int b, ImaginaryQuadraticRing R, int denom) {
        
        boolean abParityMatch;
        
        if (denom < 1 || denom > 2) {
            throw new IllegalArgumentException("Parameter denom must be 1 or 2.");
        }
        if (denom == 2) {
            abParityMatch = Math.abs(a % 2) == Math.abs(b % 2);
            if (!abParityMatch) {
                throw new IllegalArgumentException("Parity of parameter a must match parity of parameter b.");
            }
            if (a % 2 == 0) {
                this.realPartMult = a/2;
                this.imagPartMult = b/2;
                this.denominator = 1;
            } else {
                if (R.d1mod4) {
                    this.realPartMult = a;
                    this.imagPartMult = b;
                    this.denominator = 2;
                } else {
                    throw new IllegalArgumentException("Either parameter a and parameter b need to both be even, or parameter denom needs to be 1.");
                }
            }
        } else {
            this.realPartMult = a;
            this.imagPartMult = b;
            this.denominator = 1;
        }
        this.imagQuadRing = R;
        
    }
    
    public static int getIntFromConsole(Scanner input) {
        int enteredInteger = 0;
        boolean invalidInput = true;
        
        while (invalidInput) {
            try {
                enteredInteger = input.nextInt();
                invalidInput = false;
            } catch (InputMismatchException inputMismatch) {
                System.out.println("Please enter an integer.");
                input.nextLine();
            }
        }
        return enteredInteger;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner inputScanner = new Scanner(System.in);
        int chosenRingD = RingWindowDisplay.DEFAULT_RING_D;
        int chosenRealPartMult;
        int chosenImagPartMult;
        int chosenDenom;
        
        ImaginaryQuadraticRing imR;
        ImaginaryQuadraticInteger currIQI;
        int[] currPolCoeffs;
        
        /*
        The idea here is that the user will be able to choose between a graphical interface and a text interface.
        But the graphical interface is not ready yet, so for now there's only the text interface.
        
        if (args.length > 0) {
            if (args[0] == "-gui" || args[0] == "-GUI") {
                RingWindowDisplay(chosenRingD)
            }
            if (args[0] == "-text" || args[0] == "-TEXT") {
                text stuff
            }
        }
        */        

        while (chosenRingD != 0) {
            System.out.print("Please enter a negative squarefree integer d for the ring discriminant (or 0 to quit): ");
            chosenRingD = getIntFromConsole(inputScanner);
            if (chosenRingD > 0) {
                System.out.print("Taking " + chosenRingD + " to be ");
                chosenRingD *= -1;
                System.out.println(chosenRingD);
            }
            if (NumberTheoreticFunctionsCalculator.isSquareFree(chosenRingD)) {
                imR = new ImaginaryQuadraticRing(chosenRingD);
                if (imR.d1mod4) {
                    System.out.println("Given that " + chosenRingD + " is congruent to 1 mod 4, please enter real and imaginary parts multiplied by 2.");
                    chosenDenom = 2;
                } else {
                    chosenDenom = 1;
                }
                chosenImagPartMult = 1;
                while (chosenImagPartMult != 0) {
                    System.out.print("Please enter real part of quadratic integer: ");
                    chosenRealPartMult = getIntFromConsole(inputScanner);
                    System.out.print("Please enter imaginary part of quadratic integer (or 0 to change ring): ");
                    chosenImagPartMult = getIntFromConsole(inputScanner);
                    if (imR.d1mod4 && (Math.abs(chosenRealPartMult % 2) != Math.abs(chosenImagPartMult % 2))) {
                        chosenImagPartMult++;
                    }
                    currIQI = new ImaginaryQuadraticInteger(chosenRealPartMult, chosenImagPartMult, imR, chosenDenom);
                    System.out.println(currIQI.toString());
                    System.out.println("Algebraic degree is " + currIQI.algebraicDegree());
                    System.out.println("Trace is " + currIQI.trace());
                    System.out.println("Norm is " + currIQI.norm());
                    System.out.println("Minimal polynomial is " + currIQI.minPolynomialString());
                    System.out.println(" ");
                }
            }
        }
    }
    
}