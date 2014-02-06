// ANSWER: "43.80133"
// ANSWER: "42.221"
// ANSWER: "49.00022"
package com.hjwylde.uni.swen221.lab03.polyquiz;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Test10 {
    
    public static void main(String[] args) {
        Shape[] shapes = new Shape[5];
        shapes[0] = new Circle(1.0);
        shapes[1] = new Square(3);
        shapes[2] = new Circle(1.23);
        shapes[3] = shapes[1];
        shapes[4] = new Square(1);
        
        double totalArea = 0;
        for (Shape s : shapes)
            totalArea += s.area();
        System.out.println(totalArea);
    }
    
    static class Circle implements Shape {
        
        private double radius;
        
        public Circle(double radius) {
            this.radius = radius;
        }
        
        @Override
        public double area() {
            return Math.pow(radius * Math.PI, 2);
        }
    }
    
    static interface Shape {
        
        public double area();
    }
    
    static class Square implements Shape {
        
        private int sidelength;
        
        public Square(int sidelength) {
            this.sidelength = sidelength;
        }
        
        @Override
        public double area() {
            return sidelength * sidelength;
        }
    }
}
