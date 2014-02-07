package com.hjwylde.uni.swen221.assignment03.chessview.pieces;

/*
 * Code for Assignment 3, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public abstract class PieceImpl {

    protected boolean isWhite;

    public PieceImpl(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof PieceImpl) {
            PieceImpl p = (PieceImpl) o;
            return (o.getClass() == getClass()) && (isWhite == p.isWhite);
        }

        return false;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = 1;
        result = (prime * result) + (isWhite ? 1231 : 1237);
        result += (prime * result) + (getClass().hashCode());

        return result;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
