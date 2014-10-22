package com.hjwylde.uni.comp422.project02.question03_1;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

import org.jgap.InvalidConfigurationException;
import org.jgap.audit.FitnessImprovementMonitor;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Exp;
import org.jgap.gp.function.GreaterThan;
import org.jgap.gp.function.IfElse;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Sine;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;
import org.jgap.gp.terminal.Constant;
import org.jgap.gp.terminal.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Main {

    private static final List<double[]> TRAINING_DATA;

    private Variable x;

    static {
        TRAINING_DATA = generateData();
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        GPConfiguration config = new GPConfiguration();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setMinInitDepth(2);
        config.setMaxInitDepth(10);
        config.setPopulationSize(200);
        config.setFitnessFunction(main.new FormulaFitnessFunction());
        config.setStrictProgramCreation(true);
        config.setMaxCrossoverDepth(8);
        config.setMonitor(new FitnessImprovementMonitor(1, 3, 10.0d));

        GPGenotype gp = main.create(config);

        gp.setVerboseOutput(true);

        for (int i = 0; i < 200; i++) {
            if (i % 25 == 0) {
                System.out.println("Evolution: " + i);
            }

            gp.evolve();

            if (gp.getFittestProgram().getFitnessValue() <= 20) {
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

        CommandGene[][] nodeSets = {{x, new Add(conf, CommandGene.DoubleClass), new Multiply(conf,
                CommandGene.DoubleClass), new Divide(conf, CommandGene.DoubleClass), new Sine(conf,
                CommandGene.DoubleClass), new Exp(conf, CommandGene.DoubleClass), new IfElse(conf,
                CommandGene.BooleanClass), new GreaterThan(conf, CommandGene.DoubleClass),
                new Constant(conf, CommandGene.DoubleClass, 0.0), new Constant(conf,
                CommandGene.DoubleClass, 1.0), new Constant(conf, CommandGene.DoubleClass, 2.0),
                new Constant(conf, CommandGene.DoubleClass, 3.0)}};

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets, 50, true);
    }

    private static List<double[]> generateData() {
        List<double[]> data = new ArrayList<>();

        for (double x = -5.0; x <= 5.0; x += 0.25) {
            double result;
            if (x > 0) {
                result = 1.0 / x + sin(x);
            } else {
                result = 2 * x + pow(x, 2) + 3.0;
            }

            data.add(new double[] {x, result});
        }

        return data;
    }

    private class FormulaFitnessFunction extends GPFitnessFunction {

        public double computeRawFitness(final IGPProgram ind) {
            double error = 0;

            for (double[] data : TRAINING_DATA) {
                Main.this.x.set(data[0]);

                double result = ind.execute_double(0, new Object[0]);

                error += Math.abs(result - data[1]);
            }

            return error;
        }

        protected double evaluate(final IGPProgram a_subject) {
            return computeRawFitness(a_subject);
        }
    }
}

