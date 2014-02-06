package com.hjwylde.uni.comp103.assignment01.fightClub;

import java.awt.Color;
import java.util.*;

import com.hjwylde.uni.comp103.util.DrawingCanvas;

/*
 * Code for Assignment 1, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class Creature {
    
    // Fields to store the canvas
    private final DrawingCanvas canvas;
    private final int lowlimit, highlimit, leftlimit, rightlimit;
    
    private final int numHiddenUnits = 4;
    
    // Physical properties shared by all Creatures.
    private static final double INERTIA = 0.95;
    
    private static final int bodySize = 45;
    private static final double Dfightable = 3 * Creature.bodySize; // body space.
    private static final double longScale = 20 * Creature.bodySize; // the visual scale.
    private static final double BodyRepulsionForce = 75.0; // body space.
    private static final double INVERSEMASS = 0.01; // large -> fast vel. changes.
    private static final double BIRTH_MUTATION_SIZE = 0.1;// changes from parent
    private static final double MAX_ABILITY = 1.0;
    private static final double MAX_RESOURCE = 1.0;
    private static final double INITIAL_RESOURCE = 0.6 * Creature.MAX_RESOURCE;
    private static final double HITPOINTS_PER_ATTACKER = 0.02;
    private static final double RECOVERY_RATE = Creature.HITPOINTS_PER_ATTACKER / 2.0;
    private static final double RandomWalkMagnitude = 0.0;
    
    // Creatures have visible traits.
    private int numTraits;
    private final Map<String, Integer> traitnamesIndices = new HashMap<>();
    private double[] traitVals, forceWgts, attackWgts, Wbias;
    private double[][] Wself, Wother;
    private final int r, a, isAtt; // indices relating to resource, ability, and
    // isAttacking.
    private final int index; // just an index to give each creature.
    private final Random rng;
    
    private double x, y; // holds position change as accumulates
    private double deltaX, deltaY; // holds position change as accumulates
    private double xVel = 0.0, yVel = 0.0;
    private Color naturalColour;
    private Creature victim;
    private final List<Creature> listOfAttackers = new LinkedList<>();
    
    /**
     * Construct a new Creature object. IF dad is not null, based the newbie on
     * his dad.
     */
    public Creature(int arenaWidth, int arenaHeight, DrawingCanvas c,
        Creature dad, int index, Random rng) {
        this.rng = rng;
        this.index = index % 1000;
        
        // Boring stuff to do with the canvas.
        lowlimit = Creature.bodySize / 2;
        highlimit = arenaHeight - (2 * Creature.bodySize);
        leftlimit = Creature.bodySize / 2;
        rightlimit = arenaWidth - (Creature.bodySize / 2);
        canvas = c;
        
        // The basic trait vals and parameters stuff.
        numTraits = 0;
        traitnamesIndices.put("ability", numTraits++);
        traitnamesIndices.put("resource", numTraits++);
        traitnamesIndices.put("attacking", numTraits++);
        traitnamesIndices.put("A", numTraits++);
        traitnamesIndices.put("B", numTraits++);
        traitnamesIndices.put("C", numTraits++);
        // useful shortcuts
        r = traitnamesIndices.get("resource");
        a = traitnamesIndices.get("ability");
        isAtt = traitnamesIndices.get("attacking");
        
        if (dad == null) {
            // Put new creature at a random position in the world.
            x = (rng.nextInt(arenaWidth - (2 * Creature.bodySize)));
            y = (rng.nextInt(arenaHeight - (2 * Creature.bodySize)));
            traitVals = new double[numTraits];
            for (int i = 0; i < traitVals.length; i++)
                traitVals[i] = rng.nextDouble();
            traitVals[r] = Creature.INITIAL_RESOURCE;
            traitVals[a] = 0.0; // MAX_ABILITY;
            // Then the parameters controlling behaviour.
            Wself = randomGaussian2DArray(numHiddenUnits, numTraits);
            Wother = randomGaussian2DArray(numHiddenUnits, numTraits);
            Wbias = randomGaussianArray(numHiddenUnits);
            forceWgts = randomGaussianArray(numHiddenUnits + 1);
            attackWgts = randomGaussianArray(numHiddenUnits + 1);
        } else {
            // Copy dad's position, but get out of his way a bit.
            double pertX = rng.nextGaussian();
            double pertY = rng.nextGaussian();
            double L = Math.sqrt((pertX * pertX) + (pertY * pertY));
            x = dad.x + ((0.1 * Creature.bodySize * pertX) / L);
            y = dad.y + ((0.1 * Creature.bodySize * pertY) / L);
            xVel = dad.xVel;
            yVel = dad.yVel;
            
            // Copy dad's Traits, and behavioural parameters.
            traitVals = dad.traitVals.clone();
            Wself = dad.Wself.clone();
            Wother = dad.Wother.clone();
            Wbias = dad.Wbias.clone();
            forceWgts = dad.forceWgts.clone();
            attackWgts = dad.attackWgts.clone();
            
            // Parent and child split the available resource.
            double res = dad.traitVals[r];
            traitVals[r] = res / 2; // resource is split.
            dad.traitVals[r] = res / 2;
            // ability copied, less a youth penalty.
            traitVals[a] = Math.max(0.1, dad.traitVals[a] - 0.1);
            
            // some mutation
            mutateSelf(Creature.BIRTH_MUTATION_SIZE);
        }
        
        // Combine traits other than resource and ability into a colour.
        int rd = (int) (traitVals[traitnamesIndices.get("A")] * 255.0);
        int bl = (int) (traitVals[traitnamesIndices.get("B")] * 255.0);
        int gn = (int) (traitVals[traitnamesIndices.get("C")] * 255.0);
        naturalColour = new Color(rd, gn, bl);
        // Nb. this is (should be...) the only place colours are determined.
    }
    
    /** Methods ---------------------------------------------------- */
    
    public void addAttacker(Creature attacker) {
        listOfAttackers.add(attacker);
    }
    
    public void attack() {
        if (victim != null)
            victim.addAttacker(this);
    }
    
    /** Find the net "force" attracting to/repelling from other. */
    @SuppressWarnings("unused")
    public double calcForce(Creature other, double[] h, double D) {
        double s = 0.0;
        for (int i = 0; i < h.length; i++)
            s += forceWgts[i] * h[i];
        double force = Math.tanh(s); // between -1 and 1.
        force *= (Creature.longScale - D) / Creature.longScale; // saliency
        if (D < Creature.bodySize)
            force = (-(Creature.bodySize - D) / Creature.bodySize)
                * Creature.BodyRepulsionForce;
        return force;
    }
    
    public double[] calcHiddenActivities(Creature other) {
        // first make sure those last two so-called "trait values" are right.
        traitVals[isAtt] = ((victim == other) ? 1.0 : 0.0);
        other.traitVals[isAtt] = ((other.victim == this) ? 1.0 : 0.0);
        
        double[] h = new double[numHiddenUnits + 1]; // last will be bias!
        for (int i = 0; i < numHiddenUnits; i++) {
            double s = Wbias[i];
            for (int j = 0; j < numTraits; j++)
                s += other.traitVals[j] * Wself[i][j];
            for (int j = 0; j < (numTraits / 2); j++)
                s += (traitVals[j] - other.traitVals[j]) * Wother[i][j];
            for (int j = numTraits / 2; j < numTraits; j++)
                s += (traitVals[j] - other.traitVals[j]) * Wother[i][j];
            // this last could plausibly be Math.abs(): "abs difference only".
            h[i] = Math.tanh(s);
        }
        h[numHiddenUnits] = 1.0;
        return h;
    }
    
    /** Find how much you want to attack this other creature */
    @SuppressWarnings("unused")
    public double calcPsi(Creature other, double[] h, double D) {
        double s = 0.0;
        for (int i = 0; i < h.length; i++)
            s += attackWgts[i] * h[i];
        double desire = Math.tanh(s); // between -1 and 1.
        return desire;
    }
    
    /**
     * Add up 'forces' to determine movements, and decide who, if anybody, to
     * attack. But don't DO
     * either of these things yet.
     */
    public void decideActions(List<Creature> creatures) {
        listOfAttackers.clear();
        victim = null;
        deltaX = 0.0;
        deltaY = 0.0;
        double psiMax = 0.0;
        
        // Go through every other creature in turn.
        for (Creature other : creatures)
            if (other != this) {
                // Calc. the vector to other (dx,dy), and its distance (D).
                double dx, dy, D2, D;
                dx = other.x - x;
                dy = other.y - y;
                D2 = (dx * dx) + (dy * dy);
                D = Math.sqrt(D2);
                // NB: (dx/D, dy/D) is normalised vec pointed at other creature.
                
                if (D < Creature.longScale) {
                    double[] h = calcHiddenActivities(other);
                    
                    double force = calcForce(other, h, D);
                    deltaX += (dx / D) * force; // resolve force into 2 components.
                    deltaY += (dy / D) * force;
                    
                    if (D < Creature.Dfightable) {
                        double psi = calcPsi(other, h, D);
                        if (psi > psiMax) {
                            psiMax = psi;
                            victim = other;
                        }
                    }
                }
            }
    }
    
    public String description() { // more verbose than toString...
        String str;
        str = "Ag " + index;
        if (victim != null)
            str = str + String.format("(-> %d)", victim.getIndex());
        str += String.format("\nTrait: \t");
        for (String trait : traitnamesIndices.keySet())
            str += String.format("%s\t",
                trait.substring(0, Math.min(8, trait.length())));
        str += String.format("\nValue: \t");
        for (String trait : traitnamesIndices.keySet()) {
            double v = traitVals[traitnamesIndices.get(trait)];
            str += String.format("%5.2f\t", v);
        }
        if (listOfAttackers.size() > 0) {
            str += "\nAttacked by: ";
            for (Creature ag : listOfAttackers)
                str += String.format(", %d", ag.index);
        }
        str += String.format("\nResult of pregnancy test: %b\n",
            pregnancyTest());
        return str;
    }
    
    public void display(DrawingCanvas canvas, int xpos, int ypos) {
        // draw creature as a coloured disk.
        canvas.setForeground(naturalColour);
        canvas.fillOval(xpos - (Creature.bodySize / 2), ypos
            - (Creature.bodySize / 2), Creature.bodySize, Creature.bodySize,
            false);
        
        // indicate resource as gray circle inside.
        double res = traitVals[r];
        int rSize = ((int) (Creature.bodySize * (Math.pow(res, 0.5))) * 9) / 10;
        canvas.setForeground(Color.LIGHT_GRAY);
        canvas.fillOval(xpos - (rSize / 2), ypos - (rSize / 2), rSize, rSize,
            false);
        
        canvas.setForeground(Color.BLACK);
        // circle size proportional to ability
        int aSize = (int) (Creature.bodySize * Math.sqrt(traitVals[a]
            / Creature.MAX_ABILITY));
        canvas.drawOval(xpos - (aSize / 2), ypos - (aSize / 2), aSize, aSize,
            false);
        
    }
    
    /**
     * Draw the creature, as a coloured circle, and indicate its victim with a
     * line.
     */
    public void draw() {
        // indicate the victim of an attack, if there is one.
        if (victim != null) {
            double halfwayX = (x + victim.getX()) / 2;
            double halfwayY = (y + victim.getY()) / 2;
            canvas.setForeground(Color.BLACK);
            canvas.drawLine((int) x, (int) y, (int) halfwayX, (int) halfwayY,
                false);
            canvas
                .fillOval((int) halfwayX - 3, (int) halfwayY - 3, 6, 6, false);
        }
        
        // indicate resource as gray circle inside.
        double res = traitVals[r];
        int rSize = ((int) (Creature.bodySize * (Math.pow(res, 0.5))) * 7) / 10;
        
        // draw the largest circle first (creature or its resource)
        if (rSize > Creature.bodySize) { // Draw resource first
            // draw resource as a grey disk
            canvas.setForeground(Color.LIGHT_GRAY);
            canvas.fillOval((int) x - (rSize / 2), (int) y - (rSize / 2),
                rSize, rSize, false);
            // draw creature as a coloured disk.
            canvas.setForeground(naturalColour);
            canvas.fillOval((int) x - (Creature.bodySize / 2), (int) y
                - (Creature.bodySize / 2), Creature.bodySize,
                Creature.bodySize, false);
        } else { // Draw creature first
            // draw creature as a coloured disk.
            canvas.setForeground(naturalColour);
            canvas.fillOval((int) x - (Creature.bodySize / 2), (int) y
                - (Creature.bodySize / 2), Creature.bodySize,
                Creature.bodySize, false);
            // draw resource as a grey disk
            canvas.setForeground(Color.LIGHT_GRAY);
            canvas.fillOval((int) x - (rSize / 2), (int) y - (rSize / 2),
                rSize, rSize, false);
        }
        
        // circle size proportional to ability
        int aSize = (int) (Creature.bodySize * Math.sqrt(traitVals[a]
            / Creature.MAX_ABILITY));
        canvas.setForeground(Color.BLACK);
        canvas.drawOval((int) x - (aSize / 2), (int) y - (aSize / 2), aSize,
            aSize, false);
    }
    
    public double getAbility() {
        return traitVals[a];
    }
    
    public Color getColor() {
        return naturalColour;
    }
    
    public int getIndex() {
        return index;
    }
    
    public double getResource() {
        return traitVals[r];
    }
    
    public Creature getVictim() {
        return victim;
    }
    
    /** Return the current position */
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void move() {
        /**
         * update velocity in response to Force impulse, and "INERTIA" (NB. this
         * isn't really INERTIA
         * though, it's "speed fading"). Assumes deltaX and deltaY have been
         * precomputed elsewhere.
         */
        
        // Basic random walk is superimposed on "purposeful" movement.
        deltaX += Creature.RandomWalkMagnitude * rng.nextGaussian();
        deltaY += Creature.RandomWalkMagnitude * rng.nextGaussian();
        
        xVel = (Creature.INERTIA * xVel) + (Creature.INVERSEMASS * deltaX);
        yVel = (Creature.INERTIA * yVel) + (Creature.INVERSEMASS * deltaY);
        
        x += xVel; // New default positions.
        y += yVel;
        
        // Tedious corrections to make if the preceding changes would
        // take it off canvas. nb: THERE IS A LOT OF REDUNDANCY IN
        // HERE!!!
        if (x < leftlimit) { // falling off the left
            xVel *= -1.0; // elastic collision
            x = (2 * leftlimit) - x;
        }
        if (x > rightlimit) { // falling off the right
            xVel *= -1.0;
            x = (2 * rightlimit) - x;
        }
        if (y < lowlimit) { // falling off the top
            yVel *= -1.0;
            y = (2 * lowlimit) - y;
        }
        if (y > highlimit) { // falling off the bottom
            yVel *= -1.0;
            y = (2 * highlimit) - y;
        }
        
    }
    
    public boolean moveToward(double toX, double toY) {
        double pullStrength = 0.1;
        x += pullStrength * (toX - x);
        y += pullStrength * (toY - y);
        return (Math.sqrt(Math.pow(toX - x, 2) + Math.pow(toY - y, 2)) < 25.0);
    }
    
    public void mutateSelf(double mutnSize) {
        for (int i = 0; i < traitVals.length; i++)
            if ((i != r) && (i != a))
                traitVals[i] = Math.min(
                    1,
                    Math.max(0,
                        traitVals[i]
                            + (mutnSize * ((2.0 * rng.nextDouble()) - 1.0))));
        
        // add Gaussian noise to all the weights
        for (int i = 0; i < forceWgts.length; i++)
            forceWgts[i] += mutnSize * rng.nextGaussian();
        for (int i = 0; i < attackWgts.length; i++)
            attackWgts[i] += mutnSize * rng.nextGaussian();
        for (int i = 0; i < Wself.length; i++)
            for (int j = 0; j < Wself[i].length; j++) {
                Wself[i][j] += mutnSize * rng.nextGaussian();
                Wother[i][j] += mutnSize * rng.nextGaussian();
            }
        for (int i = 0; i < Wbias.length; i++)
            Wbias[i] += mutnSize * rng.nextGaussian();
        
        // Combine traits other than resource and ability into a colour.
        int rd = (int) (traitVals[traitnamesIndices.get("A")] * 255.0);
        int bl = (int) (traitVals[traitnamesIndices.get("B")] * 255.0);
        int gn = (int) (traitVals[traitnamesIndices.get("C")] * 255.0);
        naturalColour = new Color(rd, gn, bl);
    }
    
    public boolean pregnancyTest() {
        if ((traitVals[r] >= Creature.MAX_RESOURCE)
            && (traitVals[a] >= (0.5 * Creature.MAX_ABILITY)))
            return (true);
        return (false);
    }
    
    public double[][] randomGaussian2DArray(int length1, int length2) {
        double[][] anArray = new double[length1][length2];
        for (int i = 0; i < length1; i++)
            for (int j = 0; j < length2; j++)
                anArray[i][j] = rng.nextGaussian();
        return anArray;
    }
    
    public double[] randomGaussianArray(int length) {
        double[] anArray = new double[length];
        for (int i = 0; i < length; i++)
            anArray[i] = rng.nextGaussian();
        return anArray;
    }
    
    public void removeAttacker(Creature attacker) {
        listOfAttackers.remove(attacker);
    }
    
    public void setAbility(double ab) {
        traitVals[a] = ab;
    }
    
    public void setResource(double res) {
        traitVals[r] = res;
    }
    
    @Override
    public String toString() {
        String str;
        str = "Creature " + index + "\n";
        return str;
    }
    
    /** Update resource and ability/willingness to go on attacking. */
    public String updateAbilitiesAndResources() {
        if (traitVals[a] < Creature.MAX_ABILITY)
            traitVals[a] += Creature.RECOVERY_RATE;
        if (!listOfAttackers.isEmpty()) { // Ack! I'm being assaulted!
            double hitpoints = Creature.HITPOINTS_PER_ATTACKER
                * Math.pow(listOfAttackers.size(), 1.0);
            traitVals[a] -= hitpoints;
            if (traitVals[a] <= 0) { // OUCH! That really hurt!
                // This creature is about to lose a fight, so it
                // takes itself OFF its victim's list of attackers.
                if (victim != null)
                    victim.removeAttacker(this);
                
                double res = traitVals[r];
                // resources are distributed amongst the attackers.
                double portion = res / listOfAttackers.size();
                for (Creature attackerDude : listOfAttackers)
                    if (attackerDude.traitVals[r] < Creature.MAX_RESOURCE)
                        attackerDude.traitVals[r] += portion;
                traitVals[r] = 0.0;
                traitVals[a] = 0.0;
                return ("die");
            }
        }
        return (null);
    }
}
