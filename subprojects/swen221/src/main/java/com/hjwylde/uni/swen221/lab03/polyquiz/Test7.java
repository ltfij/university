// ANSWER: "Jim claws Bob\n Bob bites back Jim"
// ANSWER: "Jim bites Bob\n Bob claws back Jim"
// ANSWER: "Jim claws Bob\n Bob claws back Jim"
package com.hjwylde.uni.swen221.lab03.polyquiz;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Test7 {

    public static void main(String[] args) {
        Cat jim = new Cat("Jim");
        Cat bob = new RoughCat("Bob");
        jim.fight(bob);
    }

    static class Cat {

        public String name;

        public Cat(String name) {
            this.name = name;
        }

        public void fight(Cat target) {
            System.out.println(name + " claws " + target.name);
            target.fightBack(this);
        }

        public void fightBack(Cat target) {
            System.out.println(name + " claws back " + target.name);
        }
    }

    static class RoughCat extends Cat {

        public RoughCat(String name) {
            super(name);
        }

        @Override
        public void fight(Cat target) {
            System.out.println(name + " bites " + target.name);
            target.fightBack(this);
        }

        @Override
        public void fightBack(Cat target) {
            System.out.println(name + " bites back " + target.name);
        }
    }
}
