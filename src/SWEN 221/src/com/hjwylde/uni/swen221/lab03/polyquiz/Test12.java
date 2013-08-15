// ANSWER: "54.0"
// ANSWER: "56.556"
// ANSWER: "77.88"
package com.hjwylde.uni.swen221.lab03.polyquiz;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Test12 {
    
    public static void main(String[] args) {
        Shape[] shapes = new Shape[5];
        shapes[0] = new Rectangle(2, 3);
        shapes[1] = new Triangle(5, 6);
        shapes[2] = new Rectangle(2, 3);
        shapes[3] = new Triangle(6, 7);
        shapes[4] = new Rectangle(2, 3);
        
        double totalArea = 0;
        for (Shape s : shapes)
            totalArea += s.area();
        System.out.println(totalArea);
    }
    
    static class Rectangle implements Shape {
        
        private int aSideLength;
        private int bSideLength;
        
        public Rectangle(int aSideLength, int bSideLength) {
            this.aSideLength = aSideLength;
            this.bSideLength = bSideLength;
        }
        
        @Override
        public double area() {
            return aSideLength * bSideLength;
        }
    }
    
    static interface Shape {
        
        public double area();
    }
    
    static class Triangle extends Rectangle {
        
        public Triangle(int base, int height) {
            super(base, height);
        }
        
        @Override
        public double area() {
            return super.area() / 2.0;
        }
    }
}
