// ANSWER: "Jim is: A Cat!, Bob is: A Kitten!"
// ANSWER: "Jim is: A Cat!, Bob is: A Cat!"
// ANSWER: "Jim is: A Kitten!, Bob is: A Kitten!"
package com.hjwylde.uni.swen221.lab03.polyquiz;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Test1 {

    public static void main(String[] args) {
        Cat jim = new Cat();
        Cat bob = new Kitten();
        System.out.println("Jim is: " + jim.whatAmI() + ", bob is " + bob.whatAmI());
    }

    static class Cat {

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
