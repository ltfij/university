package com.hjwylde.uni.comp103.assignment09.deliverySystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/*
 * Code for Assignment 9, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class PackageDeliverer {

    // Constants / Global variables
    protected static int curTime; // The current time of day in minutes

    protected static final int DAY_START = 0; // 9 a.m. is specified as 0

    protected static final int DAY_END = PackageDeliverer.DAY_START + 480; // 5 p.m. is 480 minutes
    // after 9 a.m.

    protected static final double DRIVING_COST = 0.5; // dollars per minute

    protected static final double PREMIUM_RATE = 12; // dollars per premium

    // package
    protected static final double STANDARD_RATE = 8; // dollars per standard

    // package
    protected static final double BUDGET_RATE = 4; // dollars per budget package

    protected static final double EMPLOYEE_RATE = 12; // dollars per day (includes
                                                      // cost of scooter)

    private static final String ROOT = PackageDeliverer.class.getProtectionDomain().getCodeSource()
            .getLocation().getPath().replaceAll("%20", " ");

    public static HashMap<String, Double> placeTimes; // Place to Minutes map
    private final int speed = 40; // kilometers per hour

    private Set<Deliverer> deliverers; // Set of deliverers

    private Queue<Package> undeliveredPackages; // Packages that have been
    // accepted
    private Map<Integer, Package> packagesList; // TimeOfDay to Package map for
                                                // packages in a day

    private final List<Package> refusedPackages; // Packages the company refused

    boolean debug = true; // Show debug messages

    /*
     * Main
     */

    public PackageDeliverer() {
        setupPlaceTimes();

        deliverers = new HashSet<>();

        undeliveredPackages = new PriorityQueue<>();
        packagesList = new HashMap<>();

        refusedPackages = new ArrayList<>();
    }

    /*
     * Constructors
     */

    public void addDeliverer(String deliverer) {
        if ((deliverer == null) || deliverer.equals("")) // Validate deliverer
            throw new IllegalArgumentException();

        deliverers.add(new Deliverer(deliverer));
    }

    /*
     * Initialization
     */

    /**
     * Outputs useful information about the days income and packages that the deliverers accepted or
     * refused etc.
     */
    public void printDaysIncome() {
        System.out.println("---");
        System.out.println("Computing days income...");
        System.out.println("---");
        double income = 0;

        Package pkg;
        for (Deliverer d : deliverers) {
            System.out.println("\n\nDeliverer: " + d.getName());

            System.out.println("\nEmployee rate: " + PackageDeliverer.EMPLOYEE_RATE);
            income -= PackageDeliverer.EMPLOYEE_RATE;

            System.out.println("\nDeliverer was on the road for " + d.getDrivingTime()
                    + " minutes.");
            income -= d.getDrivingCost();

            pkg = d.getCurrentPackage();
            if (pkg != null) {
                System.out.println("Deliverer currently has a package: " + pkg.toString());

                income -= 2;
                income += PackageDeliverer.calculatePackageIncome(pkg);
            }

            System.out.println("\nDeliverer delivered " + d.getDeliveredPackages().size()
                    + " package(s).");
            for (Package dPkg : d.getDeliveredPackages()) {
                System.out.print(dPkg.toString() + "  ");

                income -= 2;
                income += PackageDeliverer.calculatePackageIncome(dPkg);
            }
        }

        System.out.println("\n" + undeliveredPackages.size() + " package(s) went undelivered.");
        for (Package uPkg : undeliveredPackages)
            System.out.print(uPkg.toString() + "  ");

        System.out.println("\nCompany refused " + refusedPackages.size() + " package(s).");
        for (Package rPkg : refusedPackages)
            System.out.print(rPkg.toString() + "  ");

        System.out.println("\n" + packagesList.size()
                + " package(s) arrived after the end of the day.");
        for (Package rPkg : packagesList.values())
            System.out.print(rPkg + "  ");

        System.out.println("\n\nTotal of days income: " + income);
    }

    /*
     * Setters
     */

    public void setDeliverers(String... deliverers) {
        if ((deliverers == null) || (deliverers.length == 0)) // Validate deliverers
            return;

        this.deliverers = new HashSet<>();
        for (int i = 0; i < deliverers.length; i++)
            addDeliverer(deliverers[i]);
    }

    public void setPackageList(String path) {
        File packagesFile = new File(PackageDeliverer.ROOT, path);
        packagesList = new HashMap<>();

        if (!packagesFile.exists()) { // If we can't find the packages file...
            JFileChooser fc = new JFileChooser(); // ...then ask the user for a valid
                                                  // one
            fc.setCurrentDirectory(new File(PackageDeliverer.ROOT));

            if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
                packagesFile = fc.getSelectedFile();
            else
                // If the user clicked cancel...
                return; // ...then return
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(packagesFile));

            String line;
            while ((line = br.readLine()) != null) { // For each package in
                                                     // packagesFile...
                if (line.equals("")) // Skip if the line is empty
                    continue;

                StringTokenizer newPkg = new StringTokenizer(line);

                int pkgTime = Integer.parseInt(newPkg.nextToken());
                String pkgType = newPkg.nextToken();
                String destination = newPkg.nextToken();

                // Add each package in the packagesFile to a map, with the key being the
                // time of day that it
                // is delivered to the depot
                packagesList.put(pkgTime, new Package(destination, pkgType, pkgTime));
            }

            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return;
    }

    /**
     * Simulates a day for the package delivery company with the given deliverers and package list
     * for that day.
     */
    public void stepThroughDay() {
        if (packagesList.isEmpty() || PackageDeliverer.placeTimes.isEmpty()) {
            System.err.println("A suitable package or places list has not been selected.");
            return;
        }

        undeliveredPackages = new PriorityQueue<>(); // Packages that get accepted at the depot

        Package pkg;
        for (PackageDeliverer.curTime = PackageDeliverer.DAY_START; PackageDeliverer.curTime <= PackageDeliverer.DAY_END; PackageDeliverer.curTime++) { // Go
                                                                                                                                                        // through
                                                                                                                                                        // the
            // day a minute
            // at a
            // time...
            if (debug) {
                System.out.print("Current time: " + PackageDeliverer.curTime);

                for (Deliverer d : deliverers)
                    System.out.print(" {" + d.getName() + " is "
                            + (d.onJob() ? "unavailable" : "available") + "}");

                System.out.println();
            }

            if (packagesList.containsKey(PackageDeliverer.curTime)) { // If the packagesList has a
                // package for the current
                // time...
                pkg = packagesList.remove(PackageDeliverer.curTime); // ...then remove from
                                                                     // packagesList
                // and store reference

                if (debug)
                    System.out.print("Package for current time: " + pkg.toString());

                if (pkg.validate() && validate(pkg)) { // Check that the package is
                                                       // valid
                    if (debug)
                        System.out.println(" was offered.");

                    undeliveredPackages.offer(pkg); // Add the package to the priority
                                                    // queue to be delivered
                } else { // ...else not a valid package
                    if (debug)
                        System.out.println(" was refused.");

                    refusedPackages.add(pkg); // Add to a list of refused packages
                }
            }

            for (Deliverer d : deliverers) { // For every deliverer...
                d.update(); // Update the deliverer

                if (!d.onJob()) // If the deliverer is at the depot...
                    for (Package uPkg : undeliveredPackages) { // ...then search through
                                                               // all undelivered
                                                               // packages in priority
                                                               // order for one the
                                                               // deliverer can accept
                        if (debug)
                            System.out.print("Checking " + uPkg.toString() + "... " + d.getName()
                                    + " ");

                        if (d.validate(uPkg)) { // If the deliverer can take the package...
                            if (debug)
                                System.out.println("accepted it.");

                            d.setPackage(uPkg); // Set the deliverers package to pkg
                            undeliveredPackages.remove(uPkg); // Remove from package queue

                            break;
                        } else if (debug)
                            System.out.println("refused it.");
                    }
            }
        }
    }

    /*
     * Operations
     */

    private void setupPlaceTimes() {
        File places = new File(PackageDeliverer.ROOT, "deliverySystem/places.txt");
        PackageDeliverer.placeTimes = new HashMap<>();

        if (!places.exists()) { // If we can't find places.txt...
            JFileChooser fc = new JFileChooser(); // ...then ask the user for a
                                                  // places.txt
            fc.setCurrentDirectory(new File(PackageDeliverer.ROOT));
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

                @Override
                public boolean accept(File f) {
                    int ext = f.getName().lastIndexOf(".");

                    if (ext == -1)
                        return f.isDirectory();

                    return (f.getName().equalsIgnoreCase("places.txt"));
                }

                @Override
                public String getDescription() {
                    return "places.txt";
                }
            });

            if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
                places = fc.getSelectedFile();
            else
                // If the user clicks cancel...
                return; // ...then return
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(places));

            String line;
            StringTokenizer place;
            while ((line = br.readLine()) != null) { // For each line in places.txt...
                if (line.equals("")) // If the line is empty...
                    continue; // ...then return

                place = new StringTokenizer(line);
                // ...add that place to a map with it's time to reach as the value
                PackageDeliverer.placeTimes.put(place.nextToken(),
                        (Double.parseDouble(place.nextToken()) * 60) / speed);
            }

            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return;
    }

    /**
     * Validates a package in terms of the company. Returns true if the company can make a profit
     * from the package and a simple check on if there is an available driver to take it.
     * 
     * @param pkg
     * @return
     */
    private boolean validate(Package pkg) {
        double profit;
        if (pkg.getPackageType().equalsIgnoreCase("P"))
            profit = 12;
        else if (pkg.getPackageType().equalsIgnoreCase("S"))
            profit = 8;
        else
            profit = 4;

        if (((pkg.getDeliveryTime() * 2 * PackageDeliverer.DRIVING_COST) + 2) >= profit) // Check if
                                                                                         // the
            // packages cost
            // is
            // greater than
            // possible
            // profit
            return false;

        for (Deliverer d : deliverers)
            if (!d.onJob())
                return true;
            else if (d.getReturnTime() <= (pkg.getDeliveryDue() - pkg.getDeliveryTime()))
                return true;

        return false;
    }

    /*
     * Helper Methods
     */

    public static void main(String[] args) {
        PackageDeliverer p = new PackageDeliverer();

        p.setDeliverers("Carl", "Anna"); // Set some deliverers
        p.setPackageList("deliverySystem/Tests/Bigtest.txt");
        p.stepThroughDay();
        p.printDaysIncome();
    }

    /**
     * Calculates how much income was earned from a package, returns either a positive value for how
     * much was paid or negative 3 times this if a refund was applicable because it wasn't delivered
     * on time.
     */
    private static double calculatePackageIncome(Package pkg) {
        if (pkg == null)
            throw new IllegalArgumentException();

        double multiplier;
        if (pkg.deliveredOnTime()) // Check whether package was delivered on time
            multiplier = 1;
        else
            multiplier = -3; // ...if package was not delivered on time, the refund is
                             // 3 times its value

        String pkgType = pkg.getPackageType();
        if (pkgType.equalsIgnoreCase("P"))
            return PackageDeliverer.PREMIUM_RATE * multiplier;
        else if (pkgType.equalsIgnoreCase("S"))
            return PackageDeliverer.STANDARD_RATE * multiplier;
        else
            return PackageDeliverer.BUDGET_RATE * multiplier;
    }
}
