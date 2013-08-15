package com.hjwylde.uni.comp103.assignment9.deliverySystem;

import java.util.ArrayList;
import java.util.List;

/*
 * Code for Assignment 9, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Deliverer {
    
    private String name;
    
    private Package pkg; // Current package this deliverer is holding
    private final List<Package> deliveredPackages; // List of all delivered
                                                   // packages.
    
    private int leftTime; // Time the deliverer left the depot
    private double drivingTime; // Total time the deliverer has been driving for
    
    /*
     * Constructors
     */
    
    public Deliverer(String name) {
        this.name = name;
        
        deliveredPackages = new ArrayList<>();
        
        leftTime = 0;
        drivingTime = 0;
    }
    
    /*
     * Getters
     */
    
    public Package getCurrentPackage() {
        return pkg;
    }
    
    public List<Package> getDeliveredPackages() {
        return deliveredPackages;
    }
    
    public double getDrivingCost() {
        return drivingTime * PackageDeliverer.DRIVING_COST;
    }
    
    public double getDrivingTime() {
        return drivingTime;
    }
    
    public String getName() {
        return name;
    }
    
    public double getReturnTime() {
        if (pkg == null)
            return leftTime
                + (deliveredPackages.get(deliveredPackages.size() - 1)
                    .getDeliveryTime() * 2);
        
        return leftTime + (pkg.getDeliveryTime() * 2);
    }
    
    /*
     * Setters
     */
    
    public boolean onJob() {
        if (pkg != null) // If deliverer has a package...
            return true; // ...then deliverer is delivering.
        if (deliveredPackages.size() == 0) // If no packages has ever been delivered
                                           // (and given from
                                           // above, not carrying any package)...
            return false; // ...then deliverer is not delivering a package
            
        return getReturnTime() > PackageDeliverer.curTime; // If the calculated
                                                           // return time of
                                                           // deliverer
                                                           // is
        // greater than the current time, return true
    }
    
    /*
     * Operations
     */
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean setPackage(Package pkg) {
        if ((this.pkg != null) || (pkg == null)) // If deliverer already has a
                                                 // package or package given
                                                 // is invalid...
            return false; // ...then return
            
        this.pkg = pkg; // Set current package
        leftTime = PackageDeliverer.curTime; // Deliverer has left the depot
        drivingTime += pkg.getDeliveryTime() * 2; // Increment time on road to total
                                                  // time for delivering
                                                  // package and returning
        
        return true;
    }
    
    @Override
    public String toString() {
        if (pkg != null)
            return ("{N: " + name + "; C: " + pkg.toString() + "; D: "
                + deliveredPackages.toString() + "}");
        
        return ("{N: " + name + "; C: NULL; D: " + deliveredPackages.toString() + "}");
    }
    
    public void update() {
        if (!onJob()) // Nothing to update...
            return;
        
        if (pkg == null) // Is on a job and is returning, thus nothing to update...
            return;
        
        if (PackageDeliverer.curTime >= (leftTime + pkg.getDeliveryTime())) { // If
            // current
            // time
            // reaches
            // delivery
            // time
            // for
            // package...
            pkg.delivered(); // ...then deliver the package
            deliveredPackages.add(pkg); // Add the package to a list of delivered
                                        // packages
            pkg = null; // Deliverer is no longer holding a package
        }
    }
    
    /*
     * Inherited
     */
    
    /**
     * Validates a package in terms of the deliverer. A package is a valid package
     * if the deliverer
     * can return to the depot by the <code>DAY_END</code> time specified in the
     * <code>PackageDeliverer</code> class.
     * 
     * @param pkg the package.
     * @return true if deliverer can return before <code>DAY_END</code>
     */
    public boolean validate(Package pkg) {
        return (PackageDeliverer.curTime + (pkg.getDeliveryTime() * 2)) <= PackageDeliverer.DAY_END;
    }
}
