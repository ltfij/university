package com.hjwylde.uni.comp103.assignment09.deliverySystem;

/*
 * Code for Assignment 9, COMP 103 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Package implements Comparable<Package> {

    private final String destination;
    private final String pkgType;

    private int deliveryDue;
    private int deliveredTime;

    /*
     * Constructors
     */

    public Package(String destination, String pkgType) {
        this(destination, pkgType, PackageDeliverer.curTime);
    }

    public Package(String destination, String pkgType, int dropOffTime) {
        this.destination = destination;
        this.pkgType = pkgType;

        if (!PackageDeliverer.placeTimes.containsKey(destination))
            throw new IllegalArgumentException(
                    "destination must be a valid destination from the places.txt file.");
        if (!pkgType.matches("[PSB]"))
            throw new IllegalArgumentException("pkgType must match the regex [PSB].");

        deliveryDue = dropOffTime;
        if (pkgType.equalsIgnoreCase("P"))
            deliveryDue += 60;
        else if (pkgType.equalsIgnoreCase("S"))
            deliveryDue += 180;
        else
            deliveryDue = PackageDeliverer.DAY_END;
        deliveredTime = -1;
    }

    /*
     * Getters
     */

    /**
     * Used for priority queues. A package has a greater priority based on the minimum leaving time
     * in order to deliver that package on time.
     */
    @Override
    public int compareTo(Package pkg) {
        return (int) ((deliveryDue - getDeliveryTime()) - (pkg.deliveryDue - pkg.getDeliveryTime()));
    }

    public void delivered() {
        deliveredTime = PackageDeliverer.curTime; // Set the time that this package
                                                  // was delivered
    }

    public boolean deliveredOnTime() {
        if (deliveredTime == -1) // If delivered() has not been called...
            return false; // ...then it has not been delivered

        return deliveredTime <= deliveryDue;
    }

    public int getDeliveryDue() {
        return deliveryDue;
    }

    /*
     * Operations
     */

    public double getDeliveryTime() {
        return PackageDeliverer.placeTimes.get(destination);
    }

    public String getDestination() {
        return destination;
    }

    public String getPackageType() {
        return pkgType;
    }

    /*
     * Inherited
     */

    @Override
    public String toString() {
        return "{T: " + pkgType + "; D: " + destination + "}";
    }

    /**
     * Validates this package in terms of package specifications. A package is a valid package if
     * the current time is less than specified "accept" times for various package types. Note: only
     * needs to be checked once when the package has been dropped off at the depot.
     */
    public boolean validate() {
        if (pkgType.equalsIgnoreCase("P")
                && (PackageDeliverer.curTime >= (PackageDeliverer.DAY_END - 60)))
            return false;
        else if (pkgType.equalsIgnoreCase("S")
                && (PackageDeliverer.curTime >= (PackageDeliverer.DAY_END - 180)))
            return false;
        else if (pkgType.equalsIgnoreCase("B")
                && (PackageDeliverer.curTime >= (PackageDeliverer.DAY_END - 300)))
            return false;

        return true;
    }
}
