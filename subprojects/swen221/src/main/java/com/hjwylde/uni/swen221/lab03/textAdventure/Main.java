package com.hjwylde.uni.swen221.lab03.textAdventure;

import java.util.Scanner;

/*
 * Code for Laboratory 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Main {

    public static void main(String[] args) {
        // ==============================================================
        // 1) Construct the rooms
        // ==============================================================

        Room hallway =
                new Room("The Hallway",
                        "A dimly lit room, littered with strange paintings and grotesque statues.");
        Room dinningRoom =
                new Room(
                        "The Dinning Room",
                        "A classic 14th centuary dinning room featuring a long table and wooden furnishings.  Here, the family gathers every evening for a feast!");
        Room study =
                new Room(
                        "The Study",
                        "A small room with numerous bookshelves filled with many old and crumbling books.  The count likes to work in here, since it's very quiet.");
        Room kitchen =
                new Room(
                        "The Kitchen",
                        "A simple kitchen fitted with a rusting oven and the obligitory kitching table.  Who knows how anyone can cook in here.");
        Room lounge =
                new Room(
                        "The Sitting Room",
                        "A large room with a ridiculously big chandelier and excellent view of the garden.  This is where the family gathers to discuss their next act of evil.");

        // ==============================================================
        // 2) Connect rooms together by doors
        // ==============================================================

        Key loungeDiningKey = new Key();
        Door hallwayKitchenDoor = new Door("North Exit", hallway, kitchen);
        Door hallwayLoungeDoor = new Door("East Exit", hallway, lounge);
        Door hallwayStudyDoor = new FakeDoor("South Exit", hallway, study);
        Door loungeDinningDoor = new LockedDoor("South Exit", lounge, dinningRoom, loungeDiningKey);
        Door studyDinningDoor = new Door("East Exit", study, dinningRoom);

        hallway.getItems().add(hallwayKitchenDoor);
        hallway.getItems().add(hallwayLoungeDoor);
        hallway.getItems().add(hallwayStudyDoor);
        kitchen.getItems().add(hallwayKitchenDoor);
        lounge.getItems().add(hallwayLoungeDoor);
        lounge.getItems().add(loungeDinningDoor);
        study.getItems().add(hallwayStudyDoor);
        study.getItems().add(studyDinningDoor);
        dinningRoom.getItems().add(studyDinningDoor);
        dinningRoom.getItems().add(loungeDinningDoor);

        // ==============================================================
        // 3) Add some other items to the rooms
        // ==============================================================

        hallway.getItems().add(loungeDiningKey);
        hallway.getItems().add(new Book("I'm a title!", "I'm a blurb!", "Woooo a clue!"));
        lounge.getItems().add(
                new HeavyItem("A very heavy item.", "Nope, my bad. It's extremely heavy."));
        hallway.getItems().add(new MagicItem("Desc", "Description"));
        lounge.getItems().add(new Coin(30));
        study.getItems().add(new Coin(10));
        study.getItems().add(
                new Furniture("A book case",
                        "An old wooden book case, laden with books about Vampires."));
        study.getItems()
                .add(new Furniture("A book case",
                        "An broken wooden book case.  Books are falling off left right and center!"));
        study.getItems()
                .add(new Furniture("A book case",
                        "An old wooden book case.  There's just one book in it, and it's about Calculus ... yikes!"));
        kitchen.getItems()
                .add(new Furniture("A kitchen table",
                        "An old wooden kitchen table.  You know, the kind found in Farm kitchens all over the country."));
        dinningRoom.getItems().add(
                new Furniture("A long table",
                        "An smart wooden dinning set, with eight chairs around it."));
        lounge.getItems().add(
                new Furniture("A Sheep",
                        "It's white and fluffy and going baaaa.  How did it get here?"));
        lounge.getItems()
                .add(new Furniture("A Light",
                        "A long reading light, with an icky flowery design. Yuk."));
        // ==============================================================
        // 4) Begin the game
        // ==============================================================

        System.out.println("Welcome to the Haunted House Adventure!!!!!");
        System.out.println("-------------------------------------------");
        System.out.println("Written by David J. Pearce (in a rush)\n\n");

        try (Scanner scanner = new Scanner(System.in)) {
            Player p = Main.inputPlayer(scanner);

            Main.playGame(p, hallway, scanner);
        }
    }

    public static void playGame(Player player, Room room, Scanner scanner) {
        boolean finished = false;

        while (!finished) {
            System.out.println("================================");
            System.out.println(player.getName() + ", you are in " + room.getName()
                    + ".  You see:\n");

            Main.printDescription(room.getDescription(), 80);

            if (room.getItems().isEmpty())
                System.out.println("\nThere are no items in " + room.getName() + ".");
            else {
                System.out.println("\nYou can see the following things in " + room.getName() + ":");
                int idx = 0;
                for (Item i : room.getItems())
                    System.out.println("\t" + ++idx + ") " + i.shortDescription());
            }

            Room oldRoom = room;
            while (room == oldRoom) {
                System.out
                        .println("\nDo you want to go (E)ast, (W)est, (N)orth, (S)outh, (P)ick up something,\n (D)rop something, P(R)od something, (L)ook at something or (Q)uit? (E/W/N/S/P/R/L/Q):");

                String action = scanner.nextLine();

                if (action.equals("E") || action.equals("W") || action.equals("N")
                        || action.equals("S")) {
                    room = Main.walkInto(player, room, action);
                    Main.pause();
                } else if (action.equals("P")) {
                    System.out.println("Pick up which item?");
                    int item = Main.inputNumber(1, room.getItems().size(), scanner);
                    room.getItems().get(item - 1).pickUp(player, room);
                    Main.pause();
                } else if (action.equals("D")) {
                    System.out.println("You're carrying: ");
                    int idx = 0;
                    for (Item i : player.getItems())
                        System.out.println("\t" + ++idx + ") " + i.shortDescription());
                    System.out.println("Drop which item?");
                    Item item =
                            player.getItems().get(
                                    Main.inputNumber(1, room.getItems().size(), scanner) - 1);
                    room.getItems().add(item);
                    player.getItems().remove(item);
                    System.out.println("You dropped " + item.shortDescription());
                    Main.pause();
                } else if (action.equals("R")) {
                    System.out.println("Prod up which item?");
                    int item = Main.inputNumber(1, room.getItems().size(), scanner);
                    room.getItems().get(item - 1).prod(player, room);
                    Main.pause();
                } else if (action.equals("L")) {
                    System.out.println("Look at which item?");
                    int item = Main.inputNumber(1, room.getItems().size(), scanner);
                    System.out.println(room.getItems().get(item - 1).longDescription());
                    Main.pause();
                } else if (action.equals("Q"))
                    finished = true;
            }
        }
    }

    /**
     * Input a number from the keyboard. The number must be between the min and max parameters.
     * 
     * @param min
     * @param max
     * @param scanner
     * @return
     */
    private static int inputNumber(int min, int max, Scanner scanner) {
        while (true) {
            String x = scanner.nextLine();
            try {
                int answer = Integer.parseInt(x);
                if ((answer >= min) && (answer <= max))
                    return answer;
            } catch (NumberFormatException e) {
            }
            System.out.println("Invalid input!");
        }
    }

    private static Player inputPlayer(Scanner scanner) {
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        System.out.println("Who do you want to be?\n");
        System.out.println("1) The strong warrior");
        System.out.println("2) The wiley wizard");
        System.out.println("3) The average archer");
        System.out.println("Enter 1-3:");
        int type = Main.inputNumber(1, 3, scanner);

        switch (type) {
            case 1:
                return new Player(name, 10, 1);
            case 2:
                return new Player(name, 1, 10);
            case 3:
                return new Player(name, 5, 5);
        }

        return null; // unreachable code
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Print a string using a fixed-width display width.
     * 
     * @param description
     * @param lineLength
     */
    private static void printDescription(String description, int lineLength) {
        int l = 0;
        for (int i = 0; i != description.length(); ++i, ++l) {
            if (l >= lineLength) {
                System.out.println("");
                l = 0;
            }
            System.out.print(description.charAt(i));
        }
        System.out.println();
    }

    private static Room walkInto(Player player, Room room, String direction) {
        String exit = "";

        // first, determine item name
        if (direction.equals("E"))
            exit = "East Exit";
        else if (direction.equals("W"))
            exit = "West Exit";
        else if (direction.equals("N"))
            exit = "North Exit";
        else if (direction.equals("S"))
            exit = "South Exit";

        // second, find the door (this could be made more efficient)
        for (Item i : room.getItems())
            if (i instanceof Door) {
                Door d = (Door) i;
                if (d.name().equals(exit))
                    return d.walkInto(player, room);
            }

        System.out.println("You can't go that way!");
        Main.pause();
        return room;
    }
}
