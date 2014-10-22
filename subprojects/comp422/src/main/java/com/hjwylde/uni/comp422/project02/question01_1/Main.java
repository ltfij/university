package com.hjwylde.uni.comp422.project02.question01_1;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.Abs;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Modulo;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;
import org.jgap.gp.terminal.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Main {

    private static final String TEST_DATA_PATH = "/project02/question01_1/xor_bigtest.pat";
    private static final String TRAINING_DATA_PATH = "/project02/question01_1/xor_bigtrain.pat";

    private static final List<double[]> TEST_DATA;
    private static final List<double[]> TRAINING_DATA;

    static {
        try {
            InputStream in = checkNotNull(Main.class.getResourceAsStream(TEST_DATA_PATH),
                    "in cannot be null");
            TEST_DATA = loadData(in);

            in = checkNotNull(Main.class.getResourceAsStream(TRAINING_DATA_PATH),
                    "in cannot be null");
            TRAINING_DATA = loadData(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Variable x;
    private Variable y;

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        GPConfiguration config = new GPConfiguration();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setMinInitDepth(1);
        config.setMaxInitDepth(3);
        config.setPopulationSize(200);
        config.setFitnessFunction(main.new FormulaFitnessFunction());
        config.setStrictProgramCreation(true);
        config.setMaxCrossoverDepth(5);

        GPGenotype gp = main.create(config);

        gp.setVerboseOutput(true);

        for (int i = 0; i < 200; i++) {
            if (i % 25 == 0) {
                System.out.println("Evolution: " + i);
            }

            gp.evolve();

            if (gp.getFittestProgram().getFitnessValue() <= 0.05) {
                break;
            }
        }

        GPPopulation pop = gp.getGPPopulation();

        for (Object obj : pop.determineFittestChromosomes(3)) {
            IGPProgram program = (IGPProgram) obj;

            System.out.println();
            System.out.println("==========");
            System.out.println("Program:");
            System.out.println("==========");
            gp.outputSolution(program);
        }
    }

    private GPGenotype create(GPConfiguration conf) throws InvalidConfigurationException {
        Class[] types = {CommandGene.DoubleClass,};
        Class[][] argTypes = {{}};

        x = Variable.create(conf, "x", CommandGene.DoubleClass);
        y = Variable.create(conf, "y", CommandGene.DoubleClass);

        CommandGene[][] nodeSets = {{x, y, new Add(conf, CommandGene.DoubleClass), new Subtract(
                conf, CommandGene.DoubleClass), new Multiply(conf, CommandGene.DoubleClass),
                new Divide(conf, CommandGene.DoubleClass), new Abs(conf, CommandGene.DoubleClass),
                new Modulo(conf, CommandGene.DoubleClass)}};

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets, 30, true);
    }

    private static List<double[]> loadData(InputStream in) throws IOException {
        List<double[]> data = new ArrayList<>();

        Scanner scanner = new Scanner(in);
        scanner.skip("[ \n]*");

        double[] line;
        while (scanner.hasNext()) {
            line = new double[3];

            line[0] = scanner.nextDouble();
            scanner.skip("[ \n]*");
            line[1] = scanner.nextDouble();
            scanner.skip("[ \n]*");
            line[2] = scanner.nextDouble();
            scanner.skip("[ \n]*");

            data.add(line);
        }

        return data;
    }

    private class FormulaFitnessFunction extends GPFitnessFunction {

        public double computeRawFitness(final IGPProgram ind) {
            double error = 0;

            for (double[] data : TRAINING_DATA) {
                Main.this.x.set(data[0]);
                Main.this.y.set(data[1]);

                double result = ind.execute_double(0, new Object[0]);

                error += Math.abs(result - data[2]);
            }

            return error;
        }

        protected double evaluate(final IGPProgram a_subject) {
            return computeRawFitness(a_subject);
        }
    }
}

