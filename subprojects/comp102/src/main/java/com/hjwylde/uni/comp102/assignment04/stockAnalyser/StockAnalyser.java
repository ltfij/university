package com.hjwylde.uni.comp102.assignment04.stockAnalyser;

/*
 * Code for Assignment 4, COMP 102 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import com.hjwylde.uni.comp102.util.DrawingCanvas;

/**
 * The class contains two methods for analysing the prices of a stock over the course of a day on
 * the stock market. There are several things that an invester may be interested in: The opening and
 * closing prices (the first and the last prices for the day) and whether the stock rose or fell in
 * price, and by how much The maximum and the minimum prices at any point during the day The average
 * price during the day.
 * 
 * The program has two methods. They both read a sequence of prices from the user (through the
 * terminal window). One method computes and prints out the six numbers of interest: maximum,
 * minimum, and average price. open and close price, amount it rose (or fell) The other method plots
 * the prices through the day as a simple bar graph.
 */
public class StockAnalyser {

    Scanner scan = new Scanner(System.in);

    ArrayList<Integer> stockPrices = new ArrayList<>();
    int maximumPrice = Integer.MIN_VALUE;
    int minimumPrice = Integer.MAX_VALUE;
    double averagePrice = 0;

    public StockAnalyser() {
        plotPrices();
    }

    /**
     * printStats reads a sequence of prices from the user (using Scanner and the terminal window)
     * and prints out the maximum, minimum, and average price over the day the opening and closing
     * prices, the amount the price rose or fell, The sequence is terminated by any word
     * (non-number) such as "close" or "end". All the prices are in cents (as positive integers) The
     * method must keep track of the maximum and minimum prices, the count and sum of the prices,
     * and the opening price. It will need variables for each of these quantities, all of which need
     * to be initialised to an appropriate value. It will need a loop to keep reading the prices
     * until there isn't an integer next. [Core] There is guaranteed to be at least one price, you
     * only need to print the maximum, minimum, and average price Hint, keep track of the sum of the
     * prices using a variable of type double [Extension] Print the opening and closing prices as
     * well, along with the rise/fall. The method should work even if there were no prices for the
     * day [Challenge] The method should also compute and printout the median price (a value such
     * that at least half the values are less than or equal to it and at least half the values are
     * greater than or equal to it). You may find some of the static methods in the Math class and
     * the Arrays class to be helpful.
     */
    public void analysePrices() {
        averagePrice = 0;

        if (stockPrices.size() != 0) {
            maximumPrice = Integer.MIN_VALUE;
            minimumPrice = Integer.MAX_VALUE;

            for (int element : stockPrices) {
                maximumPrice = Math.max(element, maximumPrice);
                minimumPrice = Math.min(element, minimumPrice);

                averagePrice += element;
            }

            averagePrice /= stockPrices.size();
        } else {
            maximumPrice = 0;
            minimumPrice = 0;
        }
    }

    /**
     * Reads a sequence of prices (integers) from the user (using Scanner and the terminal window)
     * and plots a bar graph of them, using narrow rectangles whose heights are equal to the price.
     * The sequence is terminated by any word (non-number) such as "close" or "end". The method may
     * assume that there are at most 24 numbers. The method will need a loop to keep reading the
     * prices until there isn't a number next. Each time round the loop, it needs to read the next
     * price and work out where to draw the rectangle for the bar. It will need a JFrame and a
     * DrawingCanvas to do the drawing on. [Core] assume that all the numbers are between 0 and 500
     * [Extension] 1. Any price greater than 500 should be plotted as if it were just 500. 2. The
     * graph should also have a horizontal green line at the height of the opening price and a red
     * line at the height of the closing price. [Challenge only:] The graph should also have labels
     * on the axes, roughly every 50 pixels Make the method read all the numbers, then scale the
     * graph to fit them, so that the largest numbers fit on the graph. The numbers on the y axis
     * should reflect the scaling. Hint; you may want to use an array or the ArrayList class to
     * store the numbers as you read them.
     */
    public void plotPrices() {
        // Set up the window for drawing
        JFrame frame = new JFrame("Core: Plot of numbers");
        frame.setSize(716, 638); // Makes canvas: 700 pixels wide, 600 pixels high.
        DrawingCanvas canvas = new DrawingCanvas();
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
        drawGraph(canvas, 500, 50);

        String userInput;

        System.out.println("Enter prices, end with 'close':\n");
        while (true) {
            userInput = scan.next();
            if (userInput.equalsIgnoreCase("close"))
                break;

            stockPrices.add(Integer.parseInt(userInput));
            analysePrices();
            drawGraph(canvas, maximumPrice, stockPrices.size());
        }

        System.out.println("Opening Price: " + stockPrices.get(0));
        System.out.println("Closing Price: " + stockPrices.get(stockPrices.size() - 1));
        System.out.println("Change in price: "
                + String.valueOf(stockPrices.get(stockPrices.size() - 1) - stockPrices.get(0)));
        System.out.println("Maximum Price: " + maximumPrice);
        System.out.println("Minimum Price: " + minimumPrice);
        System.out.println("Average Price: " + averagePrice);
    }

    private void drawGraph(DrawingCanvas canvas, int maxPrice, int maxIndex) {
        int graphWidth = canvas.getWidth() - 100;
        int graphHeight = canvas.getHeight() - 100;

        canvas.clear();

        canvas.drawString("Price", 5, (graphHeight / 2) + 45);
        canvas.drawLine(70, 50, 70, graphHeight + 50);
        canvas.drawString("Input Index", (graphWidth / 2) + 45, graphHeight + 80);
        canvas.drawLine(70, graphHeight + 50, graphWidth + 70, graphHeight + 50);

        for (int y = 0; y <= 20; y++) {
            canvas.drawLine(66, ((graphHeight * (20 - y)) / 20) + 50, 70,
                    ((graphHeight * (20 - y)) / 20) + 50);
            canvas.drawString(String.valueOf((maxPrice * y) / 20), 45,
                    ((graphHeight * (20 - y)) / 20) + 54);
        }

        for (int x = 0; x <= maxIndex; x++) {
            canvas.drawLine(((graphWidth * x) / maxIndex) + 70, graphHeight + 50,
                    ((graphWidth * x) / maxIndex) + 70, graphHeight + 54);
            canvas.drawString(String.valueOf(x), ((graphWidth * x) / maxIndex) + 67,
                    graphHeight + 65);
        }

        for (int i = 0; i < stockPrices.size(); i++)
            canvas.fillRect(((i * graphWidth) / maxIndex) + 70,
                    (graphHeight + 50) - ((stockPrices.get(i) * graphHeight) / maxPrice),
                    graphWidth / maxIndex, (stockPrices.get(i) * graphHeight) / maxPrice);
    }

    public static void main(String[] args) {
        new StockAnalyser();
    }
}
