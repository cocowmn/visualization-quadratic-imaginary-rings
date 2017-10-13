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

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Alonso del Arte
 */
public final class RingWindowDisplay extends Canvas {
    
    /**
     * The default pixels per unit interval. Hopefully in later versions of the program there will be the capability to change pixels per unit interval.
     */
    public static final int DEFAULT_PIXELS_PER_UNIT_INTERVAL = 40;
    /**
     * The minimum pixels per unit interval. Trying to set pixels per unit interval below this value will cause an exception.
     */
    public static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL = 2;
    /**
     * The maximum pixels per unit interval. Even on an 8K display, this value might be much too large. Trying to set pixels per unit interval above this value will cause an exception.
     */
    public static final int MAXIMUM_PIXELS_PER_UNIT_INTERVAL = 6400;
    
    public static final int RING_CANVAS_HORIZ_MIN = 100;
    public static final int RING_CANVAS_VERTIC_MIN = 178;
    public static final int RING_CANVAS_DEFAULT_HORIZ_MAX = 1280;
    public static final int RING_CANVAS_DEFAULT_VERTIC_MAX = 720;
    
    public static final int DEFAULT_RING_D = -1;
    
    public static final int DEFAULT_DOT_RADIUS = 5;
    
    public static final Color DEFAULT_CANVAS_BACKGROUND_COLOR = new Color(2107440); // A dark blue
    
    public static final Color DEFAULT_HALF_INTEGER_GRID_COLOR = Color.DARK_GRAY;
    public static final Color DEFAULT_INTEGER_GRID_COLOR = Color.BLACK;
    
    public static final Color DEFAULT_ZERO_COLOR = Color.BLACK;
    public static final Color DEFAULT_UNIT_COLOR = Color.WHITE;
    public static final Color DEFAULT_INERT_PRIME_COLOR = Color.CYAN;
    public static final Color DEFAULT_SPLIT_PRIME_COLOR = Color.BLUE;
    public static final Color DEFAULT_RAMIFIED_PRIME_COLOR = Color.GREEN;
    
    /**
     * The actual pixels per unit interval setting, should be initialized to DEFAULT_PIXELS_PER_UNIT_INTERVAL in the constructor. Use setPixelsPerUnitInterval(int pixelLength) to change, making sure pixelLength is greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL.
     */
    protected int pixelsPerUnitInterval;
    protected ImaginaryQuadraticRing imagQuadRing;
    protected ImaginaryQuadraticInteger basicImaginaryInterval;
    protected int pixelsPerBasicImaginaryInterval;
    protected Canvas ringCanvas;
    protected Graphics ringCanvasGraphics;
    
    private java.util.List<ImagQuadrIntDisplayComplexPlanePoint> windowIntegers;
    private int windowIntegersLength;
    private int halfIntegersMark;
    private int ringCanvasHorizMax;
    private int ringCanvasVerticMax;
    
    private int chosenRingD;
    
    private int dotRadius;
    
    private Color backgroundColor;
    
    private Color halfIntegerGridColor;
    private Color integerGridColor;
    
    private Color zeroColor;
    private Color unitColor;
    private Color inertPrimeColor;
    private Color splitPrimeColor;
    private Color ramifiedPrimeColor;
    
    private int zeroCoordX;
    private int zeroCoordY;
    
    /**
     * Change how many pixels there are per unit interval.
     * @param pixelLength An integer greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL. A value outside of this range will cause an IllegalArgumentException.
     */
    public void setPixelsPerUnitInterval(int pixelLength) {
        if (pixelLength < MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new CoordinateSystemMismatchException("Pixels per unit interval needs to be set to greater than " + (MINIMUM_PIXELS_PER_UNIT_INTERVAL - 1), false);
        }
        if (pixelLength > MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new CoordinateSystemMismatchException("Pixels per unit interval needs to be set to less than " + (MAXIMUM_PIXELS_PER_UNIT_INTERVAL + 1), false);
        }
        pixelsPerUnitInterval = pixelLength;
    }
    
    private void drawGrids(Graphics graphicsForGrids) {
        
        int verticalGridDistance;
        int currPixelPos, currReflectPixelPos;
        boolean withinBoundaries = true;
        
        verticalGridDistance = this.pixelsPerBasicImaginaryInterval;
        if (this.imagQuadRing.d1mod4) {
            // Draw horizontal lines of half integer grid
            currPixelPos = this.zeroCoordY + verticalGridDistance;
            currReflectPixelPos = this.zeroCoordY - verticalGridDistance;
            graphicsForGrids.setColor(halfIntegerGridColor);
            verticalGridDistance *= 2;
            while (withinBoundaries) {
                withinBoundaries = (currPixelPos < this.ringCanvasVerticMax) && (currReflectPixelPos > -1);
                if (withinBoundaries) {
                    graphicsForGrids.drawLine(0, currPixelPos, this.ringCanvasHorizMax, currPixelPos);
                    graphicsForGrids.drawLine(0, currReflectPixelPos, this.ringCanvasHorizMax, currReflectPixelPos);
                    currPixelPos += verticalGridDistance;
                    currReflectPixelPos -= verticalGridDistance;
                }
            }
            // Draw vertical lines of half integer grid
            int halfHorizontalGridDistance = this.pixelsPerUnitInterval;
            if (halfHorizontalGridDistance % 2 == 1) {
                halfHorizontalGridDistance--;
            }
            halfHorizontalGridDistance /= 2;
            withinBoundaries = true;
            currPixelPos = this.zeroCoordX + halfHorizontalGridDistance;
            currReflectPixelPos = this.zeroCoordX - halfHorizontalGridDistance;
            while (withinBoundaries) {
                withinBoundaries = (currPixelPos < ringCanvasHorizMax) && (currReflectPixelPos > -1);
                if (withinBoundaries) {
                    graphicsForGrids.drawLine(currPixelPos, 0, currPixelPos, this.ringCanvasVerticMax);
                    graphicsForGrids.drawLine(currReflectPixelPos, 0, currReflectPixelPos, this.ringCanvasVerticMax);
                    currPixelPos += this.pixelsPerUnitInterval;
                    currReflectPixelPos -= this.pixelsPerUnitInterval;
                }
            }
        } 
        // Draw horizontal lines of integer grid
        withinBoundaries = true;
        graphicsForGrids.setColor(integerGridColor);
        graphicsForGrids.drawLine(0, this.zeroCoordY, this.ringCanvasHorizMax, this.zeroCoordY);
        currPixelPos = this.zeroCoordY;
        currReflectPixelPos = currPixelPos;
        while (withinBoundaries) {
            currPixelPos += verticalGridDistance;
            currReflectPixelPos -= verticalGridDistance;
            withinBoundaries = (currPixelPos < ringCanvasVerticMax) && (currReflectPixelPos > -1);
            if (withinBoundaries) {
                graphicsForGrids.drawLine(0, currPixelPos, this.ringCanvasHorizMax, currPixelPos);
                graphicsForGrids.drawLine(0, currReflectPixelPos, this.ringCanvasHorizMax, currReflectPixelPos);
            }
        }
        // Draw vertical lines of integer grid
        graphicsForGrids.drawLine(this.zeroCoordX, 0, this.zeroCoordX, this.ringCanvasVerticMax);
        currPixelPos = this.zeroCoordX;
        currReflectPixelPos = currPixelPos;
        withinBoundaries = true;
        while (withinBoundaries) {
            currPixelPos += this.pixelsPerUnitInterval;
            currReflectPixelPos -= this.pixelsPerUnitInterval;
            withinBoundaries = (currPixelPos < ringCanvasHorizMax) && (currReflectPixelPos > -1);
            if (withinBoundaries) {
                graphicsForGrids.drawLine(currPixelPos, 0, currPixelPos, this.ringCanvasVerticMax);
                graphicsForGrids.drawLine(currReflectPixelPos, 0, currReflectPixelPos, this.ringCanvasVerticMax);
            }
        }
                
    }
    
    private void drawPoints(Graphics graphicsForPoints) {
        
        int currPointX, currPointY;
        int currNegPointX, currNegPointY;
        
        int dotDiameter = 2 * this.dotRadius;
        
        int maxX = (int) Math.floor((this.ringCanvasHorizMax - this.zeroCoordX)/this.pixelsPerUnitInterval);
        int maxY;
        int verticalGridDistance = this.pixelsPerBasicImaginaryInterval;
        
        ImaginaryQuadraticInteger currIQI;
        Color currColor;
        
        int currSplitPrime, currSplitPrimePointX, currNegSplitPrimePointX;
        
        if (this.imagQuadRing.d1mod4) {
            maxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/(2 * this.pixelsPerBasicImaginaryInterval));
            verticalGridDistance *= 2;
        } else {
            maxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
        }
        
        // The central point, 0
        currPointX = this.zeroCoordX;
        currPointY = this.zeroCoordY;
        graphicsForPoints.setColor(zeroColor);
        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        
        // The purely real unit points, -1 and 1
        currNegPointX = currPointX - this.pixelsPerUnitInterval;
        currPointX += this.pixelsPerUnitInterval;
        graphicsForPoints.setColor(unitColor);
        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        
        // The other purely real integer points
        for (int x = 2; x <= maxX; x++) {
            currPointX += this.pixelsPerUnitInterval;
            currNegPointX -= this.pixelsPerUnitInterval;
            if (NumberTheoreticFunctionsCalculator.isPrime(x)) {
                if (NumberTheoreticFunctionsCalculator.euclideanGCD(x, this.imagQuadRing.negRad) > 1) {
                    currColor = this.ramifiedPrimeColor;
                } else {
                    currColor = this.inertPrimeColor; // Assume the prime to be inert for now
                }
                graphicsForPoints.setColor(currColor);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
            }
        }
        
        // The purely imaginary integer points other than 0
        if (this.imagQuadRing.negRad == -1) {
            // Take care to color the units in Z[i]
            currPointX = this.zeroCoordX;
            // currPointY = this.zeroCoordY; ???currPointY should not have changed from before, right????
            currPointY = this.zeroCoordY + verticalGridDistance;
            currNegPointY = this.zeroCoordY - verticalGridDistance;
            graphicsForPoints.setColor(this.unitColor);
            graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
            graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
            graphicsForPoints.setColor(this.inertPrimeColor);
            for (int y = 2; y <= maxY; y++) {
                currPointY += verticalGridDistance;
                currNegPointY -= verticalGridDistance;
                if (NumberTheoreticFunctionsCalculator.isPrime(y)) {
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                }
            }
        } else {
            currPointX = this.zeroCoordX;
            // currPointY = this.zeroCoordY; ???currPointY should not have changed from before, right????
            currNegPointY = currPointY;
            for (int y = 1; y <= maxY; y++) {
                currPointY += verticalGridDistance;
                currNegPointY -= verticalGridDistance;
                currIQI = new ImaginaryQuadraticInteger(0, y, this.imagQuadRing, 1);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                    if (NumberTheoreticFunctionsCalculator.euclideanGCD(currIQI.norm(), this.imagQuadRing.negRad) > 1) {
                        currColor = this.ramifiedPrimeColor;
                    } else {
                        currColor = this.inertPrimeColor;
                    }
                    graphicsForPoints.setColor(currColor);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                }
            }
        }
        
        // Now the complex integer points, but not the "half-integers" yet
        for (int x = 1; x <= maxX; x++) {
            currPointX = this.zeroCoordX + (x * this.pixelsPerUnitInterval);
            currNegPointX = this.zeroCoordX - (x * this.pixelsPerUnitInterval);
            for (int y = 1; y <= maxY; y++) {
                currPointY = this.zeroCoordY + (y * verticalGridDistance);
                currNegPointY = this.zeroCoordY - (y * verticalGridDistance);
                currIQI = new ImaginaryQuadraticInteger(x, y, this.imagQuadRing, 1);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                    graphicsForPoints.setColor(this.inertPrimeColor);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    currSplitPrime = currIQI.norm();
                    if (currSplitPrime <= maxX) {
                        currSplitPrimePointX = this.zeroCoordX + (currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordX - (currSplitPrime * this.pixelsPerUnitInterval);
                        graphicsForPoints.setColor(this.splitPrimeColor);
                        graphicsForPoints.fillOval(currSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                    }
                    if (currSplitPrime <= maxY && this.imagQuadRing.negRad == -1) {
                        currSplitPrimePointX = this.zeroCoordY + (currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordY - (currSplitPrime * this.pixelsPerUnitInterval);
                        graphicsForPoints.fillOval(this.zeroCoordX - this.dotRadius, currSplitPrimePointX - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(this.zeroCoordX - this.dotRadius, currNegSplitPrimePointX - this.dotRadius, dotDiameter, dotDiameter);
                    }
                    
                }
            }
        }
        
        // Last but not least, the "half-integers"
        if (this.imagQuadRing.d1mod4) {
            int halfUnitInterval = pixelsPerUnitInterval;
            if (halfUnitInterval % 2 == 1) {
                halfUnitInterval--;
            }
            halfUnitInterval /= 2;
            int halfMaxX = 2 * maxX;
            int halfMaxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
            currPointX = this.zeroCoordX + halfUnitInterval;
            currNegPointX = this.zeroCoordX - halfUnitInterval;
            currPointY = this.zeroCoordY + pixelsPerBasicImaginaryInterval;
            currNegPointY = this.zeroCoordY - pixelsPerBasicImaginaryInterval;
            // Take care of the other units among the Eisenstein integers
            if (this.imagQuadRing.negRad == -3) {
                graphicsForPoints.setColor(unitColor);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);        
            }
            // And now to seek the primes
            for (int x = 1; x < halfMaxX; x += 2) {
                currPointX = this.zeroCoordX + (x * halfUnitInterval);
                currNegPointX = this.zeroCoordX - (x * halfUnitInterval);
                for (int y = 1; y <= halfMaxY; y += 2) {
                    currPointY = this.zeroCoordY + (y * this.pixelsPerBasicImaginaryInterval);
                    currNegPointY = this.zeroCoordY - (y * this.pixelsPerBasicImaginaryInterval);
                    currIQI = new ImaginaryQuadraticInteger(x, y, this.imagQuadRing, 2);
                    if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                        graphicsForPoints.setColor(this.inertPrimeColor);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        currSplitPrime = currIQI.norm();
                        if (currSplitPrime <= maxX) {
                            currSplitPrimePointX = this.zeroCoordX + (currSplitPrime * this.pixelsPerUnitInterval);
                            currNegSplitPrimePointX = this.zeroCoordX - (currSplitPrime * this.pixelsPerUnitInterval);
                            graphicsForPoints.setColor(this.splitPrimeColor);
                            graphicsForPoints.fillOval(currSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                            graphicsForPoints.fillOval(currNegSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        }
                    }
                }
            }
        }
        
    }
    
    private void collectWindowIntegers() {
        
        int minX, maxX, minY, maxY;
        int halfIntMinX, halfIntMaxX, halfIntMinY, halfIntMaxY;
        int currX, currY;
        ImagQuadrIntDisplayComplexPlanePoint currAlgInteger;
        
        currX = 0;
        currY = 0;
        
        maxX = (int) Math.floor((ringCanvasHorizMax/pixelsPerUnitInterval)/2);
        minX = (-1) * maxX;
        if (imagQuadRing.d1mod4) {
            maxY = (int) Math.floor(ringCanvasVerticMax/pixelsPerBasicImaginaryInterval);
        } else {
            maxY = (int) Math.floor((ringCanvasVerticMax/pixelsPerBasicImaginaryInterval)/2);
        }
        minY = (-1) * maxY;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < minY; y++) {
                currAlgInteger = new ImagQuadrIntDisplayComplexPlanePoint(x, y, imagQuadRing, 1, currX, currY);
                windowIntegers.add(currAlgInteger);
            }
        }
        halfIntegersMark = windowIntegers.size();
        if (imagQuadRing.d1mod4) {
            // TODO: Collect the "half-integers"
        }
        windowIntegersLength = windowIntegers.size();
        
    }
    
    private ImaginaryQuadraticInteger integerLookUp(int coordX, int coordY) {
        int currIndex = 0;
        if (coordX < 0 || coordX > ringCanvasHorizMax || coordY < 0 || coordY > ringCanvasVerticMax) {
            throw new IllegalArgumentException("Coordinates with negative values are not allowed.");
        }
        if (coordX < 0 || coordX > ringCanvasHorizMax || coordY < 0 || coordY > ringCanvasVerticMax) {
            throw new IllegalArgumentException("Coordinates exceed given canvas size.");
        }
        // TODO: Lookup logic goes here
        return windowIntegers.get(currIndex);
    }
    
    public void changeRingWindowDimensions(int newHorizMax, int newVerticMax) {
        if (newHorizMax < RING_CANVAS_HORIZ_MIN || newVerticMax < RING_CANVAS_VERTIC_MIN) {
            throw new IllegalArgumentException("New window dimensions need to be equal or greater than supplied minimums.");
        }
        this.ringCanvasHorizMax = newHorizMax;
        this.ringCanvasVerticMax = newVerticMax;
    }
    
    public void changeBackgroundColor(Color newBackgroundColor) {
        this.backgroundColor = newBackgroundColor;
    }
    
    public void changeGridColors(Color newHalfIntegerGridColor, Color newIntegerGridColor) {
        this.halfIntegerGridColor = newHalfIntegerGridColor;
        this.integerGridColor = newIntegerGridColor;
    }
    
    public void changeDotRadius(int newDotRadius) {
        if (newDotRadius < 1) {
            throw new IllegalArgumentException("Dot radius must be at least 1 pixel.");
        }
        this.dotRadius = newDotRadius;
    }
    
    public void changePointColors(Color newZeroColor, Color newUnitColor, Color newInertPrimeColor, Color newSplitPrimeColor, Color newRamifiedPrimeColor) {
        this.zeroColor = newZeroColor;
        this.unitColor = newUnitColor;
        this.inertPrimeColor = newInertPrimeColor;
        this.splitPrimeColor = newSplitPrimeColor;
        this.ramifiedPrimeColor = newRamifiedPrimeColor;
    }
    
    public void changeZeroCoords(int newCoordX, int newCoordY) {
        this.zeroCoordX = newCoordX;
        this.zeroCoordY = newCoordY;
    }
    
    @Override
    public void paint(Graphics g) {
        drawGrids(g);
        drawPoints(g);
    }   
    
    /**
     * Set the imaginary quadratic ring for which to draw a window of
     * @param iR The imaginary quadratic integer ring to work in
     */
    private void setRing(ImaginaryQuadraticRing iR) {
        double imagInterval;
        this.imagQuadRing = iR;
        imagInterval = this.pixelsPerUnitInterval * this.imagQuadRing.absNegRadSqrt;
        if (imagQuadRing.d1mod4) {
            imagInterval /= 2;
        }
        this.pixelsPerBasicImaginaryInterval = (int) Math.floor(imagInterval);
    }
    
    public RingWindowDisplay(int ringChoice) {
        
        ImaginaryQuadraticRing imR;
        
        this.pixelsPerUnitInterval = DEFAULT_PIXELS_PER_UNIT_INTERVAL;
        this.ringCanvas = new Canvas();
        this.ringCanvasHorizMax = RING_CANVAS_DEFAULT_HORIZ_MAX;
        this.ringCanvasVerticMax = RING_CANVAS_DEFAULT_VERTIC_MAX;
        this.backgroundColor = DEFAULT_CANVAS_BACKGROUND_COLOR;
        this.halfIntegerGridColor = DEFAULT_HALF_INTEGER_GRID_COLOR;
        this.integerGridColor = DEFAULT_INTEGER_GRID_COLOR;
        this.zeroColor = DEFAULT_ZERO_COLOR;
        this.unitColor = DEFAULT_UNIT_COLOR;
        this.inertPrimeColor = DEFAULT_INERT_PRIME_COLOR;
        this.splitPrimeColor = DEFAULT_SPLIT_PRIME_COLOR;
        this.ramifiedPrimeColor = DEFAULT_RAMIFIED_PRIME_COLOR;
        
        this.zeroCoordX = (int) Math.floor(this.ringCanvasHorizMax/2);
        this.zeroCoordY = (int) Math.floor(this.ringCanvasVerticMax/2);
        
        this.dotRadius = DEFAULT_DOT_RADIUS;
        
        if (ringChoice > 0) {
            ringChoice *= -1;
        }
        if (NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            imR = new ImaginaryQuadraticRing(ringChoice);
        } else {
            imR = new ImaginaryQuadraticRing(DEFAULT_RING_D);
        }
        this.setRing(imR);
        
        this.setBackground(this.backgroundColor);
        this.setSize(this.ringCanvasHorizMax, this.ringCanvasVerticMax); 
                               
    }
    
    public static void startRingWindowDisplay(int ringChoice) {
        
        if (ringChoice > -1) {
            ringChoice *= -1;
        }
        if (!NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            ringChoice = DEFAULT_RING_D;
        }
        
        JFrame ringFrame = new JFrame("Ring Diagram");  
        ringFrame.add(new RingWindowDisplay(ringChoice));
        ringFrame.setLayout(null);  
        ringFrame.setSize(RING_CANVAS_DEFAULT_HORIZ_MAX + 20, RING_CANVAS_DEFAULT_VERTIC_MAX + 20);
        ringFrame.setVisible(true);
        ringFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        
        
        
    }

    
}
