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
        //*******PARAMETERS*******//
        //Change this to alter the number of individuals.
        //Only works for even numbers.
        int pop_size = 30;
        //Change this to alter the number of genes in each individual.
        int genome_size = 3;
        //Select the domain here.
        double [] domain = {-1, 5};
        //Change this seed each time you'd like to produce a different
        //run. Record this seed for reproducibility of results.
        long seed = 835149854;
        //Change to alter the probability of crossover
        double p_c = 0.8;
        //Change this to alter mutation rate
        double p_m = 0.1;
        //The number of generations
        int generations = 50;
        //The Random used by the entire program.
        Random r = new Random(seed);

        //***MAIN EVOLUTIONARY ALGORITHM******************************************************************
        int t = 0;
        //Create the initial population.
        Population pop = new Population(pop_size, genome_size, domain[0], domain[1], r.nextLong());
        //Evaluate the initial population
        eval(pop);
        //Sample the initial population
        sample(pop);
        //While we still have generations to complete.
        while (t < generations) {
            //Create a new population to temporarily store selected individuals.
            Population pop_plus_one = new Population(pop_size);
            //Fill up this new population. Two individuals are selected at a time
            //and undergo crossover and mutation. These two individuals are placed
            //in the first half and second half of the new population.
            //Therefore only need to iterate half the size of the population.
            for (int i = 0; i < pop_size/2; i++) {
                //Create an array of individuals to store results of selection, crossover, and mutation.
                Individual [] individuals;
                individuals = mutate(crossover(select(pop, r), select(pop, r), r, p_c), r, p_m, domain[0], domain[1]);
                //Add the first individual to the first half of the population.
                pop_plus_one.setIndividual(individuals[0], i);
                //Add the second individual to the second half of the population.
                pop_plus_one.setIndividual(individuals[1], 15+i);
            }
            //The new population is complete, assign it as the previous generation
            //to move to the new gen.
            pop = pop_plus_one;
            //Evaluate the new generation.
            eval(pop);
            //Sample the new generation.
            //F is total pop fitness.
            double F = sample(pop);
            //At intervals of 10, print the total gen fitness.
            if (t % 10 == 0) {
                System.out.println("Total fitness for gen " + t + ": " + F);
            }
            //Increment the generation number.
            t += 1;
        }
        //When everything is done print the resulting solution.
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

    //The sample method generates the statistics for correct
    //population selection.
    private static double sample(Population population) {
        //Total population fitness
        double F = 0;
        //Rolling prob accumulates the probabilities
        //for insertion into pop_cumulative_prob in
        //the Population class.
        double rolling_prob = 0;
        //Calculate the total population fitness.
        for (int i = 0; i < population.getPop_size(); i++) {
            F = F + population.getIndividual(i).getFitness();
        }
        System.out.println("Pop Fit: " + F + "");
        //Calculate each individuals probability of selection.
        for (int i = 0; i < population.getPop_size(); i++) {
            Individual individual = population.getIndividual(i);
            double prob_selection = individual.getFitness() / F;
            individual.setProb_selection(prob_selection);
        }
        //Calculate the cumulative probability table directly
        //used for selecting individuals.
        for (int i = 0; i < population.getPop_size(); i++) {
            rolling_prob = rolling_prob + population.getIndividual(i).getProb_selection();
            population.setPop_cumulative_probs(rolling_prob, i);
        }
        //Ensure the last entry is always 1.0, that way something will
        //always be selected.
        population.setPop_cumulative_probs(1.0, population.getPop_size()-1);

        //Return the total population fitness so
        //it can be measured by caller to track
        //Population fitness over generations.
        return F;
    }

    //select takes in the population and the random to make a selection
    //of a new individual fro the population.
    private static Individual select(Population population, Random random) {
        double r = random.nextDouble();
        //Loop through the cumulative probability table and return
        //the first individual associated with the index of the table
        //that has a probability of selection greater than r.
        for (int i = 0; i < population.getPop_size(); i++) {
            double prob_select = population.getPop_cumulative_probs(i);
            if (r < prob_select) {
                return population.getIndividual(i);
            }
        }
        //Don't return anything if nothing was selected.
        //If we reach here something went wrong.
        System.out.println("Failed to select an Individual.");
        System.exit(-1);
        return new Individual(3);
    }

    //crossover takes in two individuals and using random and probability of crossover(p_c)
    //randomly crosses the genes between each other. There can either be a head swap or a
    //tail swap in the current configuration of 3 genes.
    //ex headswap:
    // i1: <1 2 3> i2: <4 5 6> --> i1: <4 2 3> i2: <1 5 6>
    private static Individual[] crossover(Individual i1, Individual i2, Random random, double p_c) {
        double r = random.nextDouble();
        Individual [] individuals = new Individual[2];
        if (r < p_c) {
            //Crossover occurs
            //Pick the point as 0 or 1.
            int cross_pt = random.nextInt(2);
            if (cross_pt == 0) {
                //Swap the head
                double temp0 = i1.getGene(0);
                i1.setGene(i2.getGene(0), 0);
                i2.setGene(temp0, 0);
            }
            if (cross_pt == 1) {
                //Swap the tail
                double temp2 = i1.getGene(2);
                i1.setGene(i2.getGene(2), 2);
                i2.setGene(temp2, 2);
            }
        }
        //Pack it up and send it home.
        individuals[0] = i1;
        individuals[1] = i2;
        return individuals;
    }

    //mutate takes two individuals at a time. It is designed to have the output of crossover
    //be its input. It also takes in the random. If it made it's own random then there wouldn't be
    //reproducible results. It takes in the probability of mutation and the bounds for the genes.
    //If a mutate is triggered, mutate will replace the gene with a random value over the domain.
    //It returns the same number of Individuals that it takes in.
    private static Individual[] mutate(Individual[] individuals, Random random, double p_m, double gene_lower_bound, double gene_upper_bound) {
        double r;
        //Loop through the individuals
        for (int i = 0; i < individuals.length; i++) {
            //Loop through an individual's genes
            for (int j = 0; j < individuals[i].getGenome_size(); j++) {
                r = random.nextDouble();
                if (r < p_m) {
                    //Mutation occurs
                    double gene_value = gene_lower_bound + (gene_upper_bound - gene_lower_bound) * random.nextDouble();
                    //Assign a random value from domain to gene.
                    individuals[i].setGene(gene_value, j);
                }
            }
        }
        //Pack it up and send it home.
        Individual[] ind = {individuals[0], individuals[1]};
        return ind;
    }
}
