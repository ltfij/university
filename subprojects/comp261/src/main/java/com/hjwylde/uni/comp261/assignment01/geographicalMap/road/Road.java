package com.hjwylde.uni.comp261.assignment01.geographicalMap.road;

/*
 * Code for Assignment 1, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Road {

    public static final double[] SPEED_ZONES = {5.0, 20.0, 40.0, 60.0, 80.0, 100.0, 110.0, 0.0};

    public static final int TRANSPORT_CAR = 0;
    public static final int TRANSPORT_PEDESTRIAN = 1;
    public static final int TRANSPORT_BICYCLE = 2;

    private final int rID;

    private String label = "UNKNOWN LABEL";
    private String city = "UNKNOWN CITY";

    private int speedZone = 7;
    private int roadClass = 0;
    private boolean oneWay = false;
    private boolean useableByCar = true;
    private boolean useableByPedestrian = true;
    private boolean useableByBicycle = true;

    public Road(int rID) {
        this.rID = rID;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || !(obj instanceof Road))
            return false;

        Road other = (Road) obj;
        if (rID != other.rID)
            return false;

        return true;
    }

    /**
     * @return the <code>city</code>.
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the <code>label</code>.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the <code>rID</code>
     */
    public int getRID() {
        return rID;
    }

    /**
     * @return the <code>roadClass</code>
     */
    public int getRoadClass() {
        return roadClass;
    }

    /**
     * @return the <code>speedZone</code>
     */
    public int getSpeedZone() {
        return speedZone;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = 1;
        result = (prime * result) + rID;

        return result;
    }

    /**
     * @return the <code>oneWay</code>
     */
    public boolean isOneWay() {
        return oneWay;
    }

    /**
     * @return the <code>useableByBicycle</code>
     */
    public boolean isUseableByBicycle() {
        return useableByBicycle;
    }

    /**
     * @return the <code>useableByCar</code>
     */
    public boolean isUseableByCar() {
        return useableByCar;
    }

    /**
     * @return the <code>useableByPedestrian</code>
     */
    public boolean isUseableByPedestrian() {
        return useableByPedestrian;
    }

    /**
     * @param city the <code>city</code> to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param label the <code>label</code> to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param oneWay the <code>oneWay</code> to set
     */
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    /**
     * @param roadClass the <code>roadClass</code> to set
     */
    public void setRoadClass(int roadClass) {
        this.roadClass = roadClass;
    }

    /**
     * @param speedZone the <code>speedZone</code> to set
     */
    public void setSpeedZone(int speedZone) {
        this.speedZone = speedZone;
    }

    /**
     * @param useableByBicycle the <code>useableByBicycle</code> to set
     */
    public void setUseableByBicycle(boolean useableByBicycle) {
        this.useableByBicycle = useableByBicycle;
    }

    /**
     * @param useableByCar the <code>useableByCar</code> to set
     */
    public void setUseableByCar(boolean useableByCar) {
        this.useableByCar = useableByCar;
    }

    /**
     * @param useableByPedestrian the <code>useableByPedestrian</code> to set
     */
    public void setUseableByPedestrian(boolean useableByPedestrian) {
        this.useableByPedestrian = useableByPedestrian;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Road [rID=" + rID + ", label=" + label + ", city=" + city + ", speedZone="
                + speedZone + ", roadClass=" + roadClass + ", oneWay=" + oneWay + ", useableByCar="
                + useableByCar + ", useableByPedestrian=" + useableByPedestrian
                + ", useableByBicycle=" + useableByBicycle + "]";
    }
}
