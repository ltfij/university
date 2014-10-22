package com.hjwylde.uni.comp422.project02.question04_1;

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
import org.jgap.gp.terminal.Constant;
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

    private static final String ROOT_PATH = "/project02/question04_1/";
    private static final String LIVER_TRAIN_PATH = ROOT_PATH + "liver-train.data";
    private static final String LIVER_TEST_PATH = ROOT_PATH + "liver-test.data";

    private static final List<Datum> LIVER_TRAIN_DATA;
    private static final List<Datum> LIVER_TEST_DATA;

    static {
        try {
            InputStream in = checkNotNull(Main.class.getResourceAsStream(LIVER_TRAIN_PATH),
                    "in cannot be null");
            LIVER_TRAIN_DATA = loadData(in);
            in = checkNotNull(Main.class.getResourceAsStream(LIVER_TEST_PATH), "in cannot be null");
            LIVER_TEST_DATA = loadData(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Variable[] variables;

    public static void main(String[] args) throws Exception {
        double[] data = new double[30];
        double mean = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = new Main().run();
            mean += data[i];
        }

        mean /= data.length;

        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance += Math.pow(data[i] - mean, 2);
        }

        variance /= data.length;
        double stddev = Math.sqrt(variance);

        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }

        System.out.println("mean: " + mean);
        System.out.println("variance: " + variance);
        System.out.println("std: " + stddev);
    }

    private GPGenotype create(GPConfiguration conf) throws InvalidConfigurationException {
        Class[] types = {CommandGene.DoubleClass,};
        Class[][] argTypes = {{}};

        variables = new Variable[6];
        for (int i = 0; i < 6; i++) {
            variables[i] = Variable.create(conf, "f" + i, CommandGene.DoubleClass);
        }

        CommandGene[][] nodeSets =
                {{variables[0], variables[1], variables[2], variables[3], variables[4],
                        variables[5], new Add(conf, CommandGene.DoubleClass), new Subtract(conf,
                        CommandGene.DoubleClass), new Multiply(conf, CommandGene.DoubleClass),
                        new Divide(conf, CommandGene.DoubleClass), new Abs(conf,
                        CommandGene.DoubleClass), new Constant(conf, CommandGene.DoubleClass, 0.0),
                        new Constant(conf, CommandGene.DoubleClass, 1.0), new Constant(conf,
                        CommandGene.DoubleClass, 2.0), new Constant(conf, CommandGene.DoubleClass,
                        3.0), new Constant(conf, CommandGene.DoubleClass, 5.0), new Modulo(conf,
                        CommandGene.DoubleClass)}};

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets, 100, true);
    }

    private double fitness(IGPProgram ind, List<Datum> data) {
        double error = 0;

        for (Datum datum : data) {
            for (int i = 0; i < variables.length; i++) {
                Main.this.variables[i].set(datum.getFeatures().get(i));
            }

            double result = ind.execute_double(0, new Object[0]);

            error += Math.abs(result - (datum.getClazz() ? 1 : 0));
        }

        return error;
    }

    private static List<Datum> loadData(InputStream in) throws IOException {
        List<Datum> data = new ArrayList<>();

        Scanner scanner = new Scanner(in);
        scanner.skip("[ \n]*");

        List<Double> features;
        while (scanner.hasNext()) {
            features = new ArrayList<>();

            String[] values = scanner.nextLine().split(",");
            for (int i = 0; i < values.length - 1; i++) {
                features.add(Double.parseDouble(values[i]));
            }

            data.add(new Datum(values[values.length - 1].equals("1"), features));
        }

        return data;
    }

    private double run() throws InvalidConfigurationException {
        GPConfiguration config = new GPConfiguration();
        GPConfiguration.reset();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setMinInitDepth(1);
        config.setMaxInitDepth(3);
        config.setPopulationSize(200);
        config.setFitnessFunction(new FormulaFitnessFunction());
        config.setStrictProgramCreation(true);
        config.setMaxCrossoverDepth(5);

        GPGenotype gp = create(config);

        gp.setVerboseOutput(true);

        for (int i = 0; i < 300; i++) {
            //            if (i % 25 == 0) {
            //                System.out.println("Evolution: " + i);
            //            }

            gp.evolve();

            if (gp.getFittestProgram().getFitnessValue() <= 10) {
                break;
            }
        }

        GPPopulation pop = gp.getGPPopulation();

        //        for (Object obj : pop.determineFittestChromosomes(3)) {
        //            IGPProgram program = (IGPProgram) obj;
        //
        //            System.out.println();
        //            System.out.println("==========");
        //            System.out.println("Program:");
        //            System.out.println("==========");
        //            gp.outputSolution(program);
        //
        //            System.out.println(program.getFitnessValue());
        //            System.out.println(program.getFitnessValue() / LIVER_DATA.size());
        //        }

        System.out.println(pop.determineFittestProgram().getFitnessValue());
        System.out.println(fitness(pop.determineFittestProgram(), LIVER_TRAIN_DATA));
        System.out.println(fitness(pop.determineFittestProgram(), LIVER_TEST_DATA));

        double fitness = fitness(pop.determineFittestProgram(), LIVER_TEST_DATA);
        double size = LIVER_TEST_DATA.size();
        return fitness / size;

        //        return fitness(pop.determineFittestProgram(), LIVER_TEST_DATA) / ((double) LIVER_TEST_DATA.size());
    }

    private class FormulaFitnessFunction extends GPFitnessFunction {

        public double computeRawFitness(final IGPProgram ind) {
            return fitness(ind, LIVER_TRAIN_DATA);
        }

        protected double evaluate(final IGPProgram a_subject) {
            return computeRawFitness(a_subject);
        }
    }
}
