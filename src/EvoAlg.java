//*************************************************************************************************
//Author: Cory Mckiel
//Date Created: Feb 15, 2020
//Last Modified: Feb 15, 2020
//Associated Files: Population.java Individual.java
//IDE: IntelliJ
//Java SDK: 12
//Program Description: This is an evolutionary algorithm designed to solve analytic functions.
//It is easily configurable for multiple population sizes, individual sizes, crossover and
//mutation rates, etc. With changes to specific functions different problems can be tackled.
//This program is meant be a modular and easily configurable platform for evolutionary
//algorithms.
//Current Function: f(x,y,z) = x^2 + y^2 + z^2
//Current Objective: Minimize
//*************************************************************************************************

import java.util.Random;

public class EvoAlg {
    public static void main(String [] args) {
        //Change this to alter the number of individuals.
        int pop_size = 30;
        //Change this to alter the number of genes in each individual.
        int genome_size = 3;
        //Select the domain here.
        double [] domain = {-1, 5};
        //Change this seed each time you'd like to produce a different
        //run. Record this seed for reproducibility of results.
        long seed = 835149854;
        Random r = new Random(seed);

        //Create the initial population.
        Population pop = new Population(pop_size, genome_size, domain[0], domain[1], r.nextLong());

        //Evaluate the initial population
        eval(pop);

        //Printing to test population generation and reproducibility of results.
        pop.print();
    }

    //The eval function assigns a fitness value to each individual in the
    //Population. This value is based off the function f(x,y,z)= x^2 + y^2 + z^2.
    //In our case the Domain of the function is [-1,5] making the range [0,75].
    //Because we are minimizing we would like to reward the smallest values.
    //During sampling the highest fitness number is rewarded. To fix this
    //contradiction we remap small values to high ones and vice versa
    //by subtracting the calculated function value from the ranges max
    //value. Small function values result in very little being subtracted
    //from the max range, 75, which means that the fitness value is very
    //high for small function values.
    //Ex:
    //  function value = 0.05 (This is a good value we would like to reward)
    //  fitness value = 75 - 0.05 = 74.95 (This is a very high value that will
    //                                      be rewarded in sampling)
    private static void eval(Population population) {
        for (int i = 0; i < population.getPop_size(); i++) {
            Individual individual = population.getIndividual(i);
            double x_sq = Math.pow(individual.getGene(0), 2);
            double y_sq = Math.pow(individual.getGene(1), 2);
            double z_sq = Math.pow(individual.getGene(2), 2);
            double function_value = x_sq + y_sq + z_sq;
            double fitness_value = 75 - function_value;
            individual.setFitness(fitness_value);
        }
    }
}
