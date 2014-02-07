package com.hjwylde.uni.swen221.lab02.shapes.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.hjwylde.uni.swen221.lab02.shapes.core.*;
import com.hjwylde.uni.swen221.lab02.shapes.math.Vec2D;

/*
 * Code for Laboratory 2, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class BoardFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanel bottomPanel;
    private JPanel centerPanel;
    private BoardCanvas boardCanvas;
    private ClockThread clock;
    private Random random = new Random();
    private ArrayList<Shape> shapes = new ArrayList<>();

    public BoardFrame() {
        super("Shape Viewer");

        boardCanvas = new BoardCanvas(shapes);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        Border cb =
                BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
                        BorderFactory.createLineBorder(Color.gray));
        centerPanel.setBorder(cb);
        centerPanel.add(boardCanvas, BorderLayout.CENTER);

        JButton resetbk = new JButton("Reset");
        JButton sqbk = new JButton("Add Square");
        JButton rectbk = new JButton("Add Rectangle");
        JButton circbk = new JButton("Add Circle");
        JButton starbk = new JButton("Add Star");

        resetbk.addActionListener(this);
        sqbk.addActionListener(this);
        rectbk.addActionListener(this);
        circbk.addActionListener(this);
        starbk.addActionListener(this);

        bottomPanel = new JPanel();
        bottomPanel.add(resetbk);
        bottomPanel.add(sqbk);
        bottomPanel.add(rectbk);
        bottomPanel.add(circbk);
        bottomPanel.add(starbk);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setFocusable(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        // finally, start the clock ticking

        clock = new ClockThread(10, this);
        clock.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset"))
            shapes.clear();
        else if (cmd.equals("Add Square"))
            shapes.add(new Square(randomWidth(), randomPosition(), randomVelocity(), Color.BLUE));
        else if (cmd.equals("Add Rectangle"))
            shapes.add(new Rectangle(randomPosition(), randomWidth(), randomWidth(),
                    randomVelocity(), Color.RED));
        else if (cmd.equals("Add Circle"))
            shapes.add(new Circle(randomPosition(), randomWidth(), randomVelocity(),
                    Color.DARK_GRAY));
        else if (cmd.equals("Add Star")) {
            /*
             * Ugly star implementation! But it shows that the Polygon shape works.
             */

            List<Vec2D> points = new LinkedList<>();

            points.add(new Vec2D(15, 0));
            points.add(new Vec2D(20, 10));
            points.add(new Vec2D(30, 15));
            points.add(new Vec2D(20, 20));
            points.add(new Vec2D(25, 30));
            points.add(new Vec2D(15, 20));
            points.add(new Vec2D(5, 30));
            points.add(new Vec2D(10, 20));
            points.add(new Vec2D(0, 15));
            points.add(new Vec2D(10, 10));
            points.add(new Vec2D(15, 0));

            shapes.add(new Polytope(randomPosition(), points, randomVelocity(), Color.MAGENTA));
        }
    }

    public synchronized void add(Shape s) {
        shapes.add(s);
        repaint();
    }

    public synchronized void clockTick() {
        // First, check for shape collisions
        for (int i = 0; i < shapes.size(); ++i)
            for (int j = i + 1; j < shapes.size(); ++j) {
                Shape is = shapes.get(i);
                Shape js = shapes.get(j);
                is.checkShapeCollision(js);
                js.checkShapeCollision(is);
            }

        // Second, detect for wall collisions
        int width = boardCanvas.getWidth();
        int height = boardCanvas.getHeight();

        for (Shape s : shapes)
            s.checkWallCollision(width, height);

        // Third, update positions.
        for (Shape s : shapes)
            s.clockTick();

        // Finally, repaint the entire display
        boardCanvas.repaint();
    }

    public Vec2D randomPosition() {
        int x = random.nextInt(boardCanvas.getWidth());
        int y = random.nextInt(boardCanvas.getHeight());
        return new Vec2D(x, y);
    }

    public Vec2D randomVelocity() {
        double x = 2 * random.nextDouble();
        double y = 2 * random.nextDouble();
        return new Vec2D(x, y);
    }

    public int randomWidth() {
        return 10 + random.nextInt(20); // min size 10, max size 30
    }
}
