package com.hjwylde.uni.comp422.project02.question05_2;

import static com.google.common.base.Preconditions.checkNotNull;

import com.hjwylde.uni.comp422.project02.question05_1.Datum;

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

    private static final String DATA_PATH = "/project02/question05_2/balance.data";

    private static final List<Datum> DATA;

    static {
        try {
            InputStream in = checkNotNull(Main.class.getResourceAsStream(DATA_PATH),
                    "in cannot be null");
            DATA = loadData(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Variable[] variables;

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

        IGPProgram program = pop.determineFittestProgram();

        System.out.println();
        System.out.println("==========");
        System.out.println("Program:");
        System.out.println("==========");
        gp.outputSolution(program);

        for (int i = 0; i < DATA.size(); i++) {
            System.out.println(main.execute(program, DATA.get(i)));
        }
    }

    private GPGenotype create(GPConfiguration conf) throws InvalidConfigurationException {
        Class[] types = {CommandGene.DoubleClass,};
        Class[][] argTypes = {{}};

        variables = new Variable[4];
        for (int i = 0; i < 4; i++) {
            variables[i] = Variable.create(conf, "f" + i, CommandGene.DoubleClass);
        }

        CommandGene[][] nodeSets =
                {{variables[0], variables[1], variables[2], variables[3], new Add(conf,
                        CommandGene.DoubleClass), new Subtract(conf, CommandGene.DoubleClass),
                        new Multiply(conf, CommandGene.DoubleClass), new Divide(conf,
                        CommandGene.DoubleClass), new Abs(conf, CommandGene.DoubleClass),
                        new Constant(conf, CommandGene.DoubleClass, 1.0), new Constant(conf,
                        CommandGene.DoubleClass, 2.0), new Constant(conf, CommandGene.DoubleClass,
                        3.0), new Constant(conf, CommandGene.DoubleClass, 5.0), new Modulo(conf,
                        CommandGene.DoubleClass)}};

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets, 100, true);
    }

    private double execute(IGPProgram ind, Datum datum) {
        for (int i = 0; i < variables.length; i++) {
            variables[i].set(datum.getFeatures().get(i));
        }

        return ind.execute_double(0, new Object[0]);
    }

    private double fitness(IGPProgram ind, Datum datum) {
        double result = Math.abs(execute(ind, datum));
        if (result == Double.NaN) {
            return 500.0;
        }

        return Math.abs(result - ((double) datum.getClazz()));
    }

    private double fitness(IGPProgram ind, List<Datum> data) {
        double error = 0;

        for (Datum datum : data) {
            error += fitness(ind, datum);
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

            data.add(new Datum(Integer.parseInt(values[values.length - 1]), features));
        }

        return data;
    }

    private class FormulaFitnessFunction extends GPFitnessFunction {

        public double computeRawFitness(final IGPProgram ind) {
            return fitness(ind, DATA);
        }

        protected double evaluate(final IGPProgram a_subject) {
            return computeRawFitness(a_subject);
        }
    }
}

