// ANSWER: "Jim is: A Cat! Bob is: A Kitten!"
// ANSWER: "Jim is: A Cat! Bob is: A Cat!"
// ANSWER: "Jim is: A Kitten! Bob is: A Kitten!"
package com.hjwylde.uni.swen221.lab03.polyquiz;

/*
 * Code for Laboratory 3, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Test2 {
    
    public static void main(String[] args) {
        Cat jim = new Cat();
        Cat bob = new Kitten();
        System.out.print("Jim is: ");
        jim.printWhatAmI();
        System.out.print(" Bob is: ");
        bob.printWhatAmI();
    }
    
    static class Cat {
        
        public void printWhatAmI() {
            System.out.print(whatAmI());
        }
        
        public String whatAmI() {
            return "A Cat!";
        }
    }
    
    static class Kitten extends Cat {
        
        @Override
        public String whatAmI() {
            return "A Kitten!";
        }
    }
}
