//*************************************************************************************************
//Author: Cory Mckiel
//Date Created: Feb 15, 2020
//Last Modified: Feb 15, 2020
//Associated Files: EvoAlg.java Individual.java
//IDE: IntelliJ
//Java SDK: 12
//Program Description: This is an evolutionary algorithm designed to solve analytic functions.
//It is easily configurable for multiple population sizes, individual sizes, crossover and
//mutation rates, etc. With changes to specific functions different problems can be tackled.
//This program is meant be a modular and easily configurable platform for evolutionary
//algorithms.
//*************************************************************************************************

import java.util.Random;

public class Population {
    //The population is represented by an array of Individuals.
    private Individual [] population;
    //The cumulative probability array will be set during
    //Sampling the population. It is used to select individuals
    //for the next population.
    private double [] pop_cumulative_probs;
    //The number of Individuals in the population.
    private int pop_size;
    //This seed is used for the reproduction of results,
    //Using the same seed results in the exact same initial population.
    private long pop_seed;

    //Use this Constructor to make a blank population.
    Population(int pop_size) {
        this.pop_size = pop_size;
        population = new Individual[this.pop_size];
        pop_cumulative_probs = new double[this.pop_size];

        pop_seed = 0;
    }


    //Constructor. Takes in the population size, Genome size, the bounds for the allowable gene values,
    //and the seed used for 'random' generation of the population.
    Population(int pop_size, int genome_size, double gene_lower_bound, double gene_upper_bound, long pop_seed) {
        this.pop_size = pop_size;
        this.pop_seed = pop_seed;
        population = new Individual[this.pop_size];
        pop_cumulative_probs = new double[this.pop_size];

        //Initialize the Individuals in the population.
        for (int i = 0; i < this.pop_size; i++) {
            population[i] = new Individual(genome_size);
        }
        //Call this init function to 'randomly' generate values
        //for each gene and each individual within the population.
        init(genome_size, gene_lower_bound, gene_upper_bound);
    }

    //***GETTERS AND SETTERS***//
    public  void setIndividual(Individual individual, int i) {
        if (i >= pop_size || i < 0) {
            System.out.println("Invalid Population index in 'setIndividual' from caller");
            System.exit(-1);
        }
        population[i] = individual;
    }

    public Individual getIndividual(int i) {
        if (i >= pop_size || i < 0) {
            System.out.println("Invalid Population index from caller.");
            System.exit(-1);
        }
        return population[i];
    }

    public void setPop_cumulative_probs(double prob, int i) {
        if (i >= pop_size || i < 0) {
            System.out.println("Invalid cumulative_prob index from caller.");
            System.exit(-1);
        }
        pop_cumulative_probs[i] = prob;
    }

    public int getPop_size() {
        return pop_size;
    }

    //init takes the gene size and the allowable gene value bounds. It then 'randomly'
    //selects allowable values for each gene in each individual to serve as the initial
    //population.
    private void init(int genome_size, double gene_lower_bound, double gene_upper_bound) {
        Random r = new Random(pop_seed);
        for (int i = 0; i < pop_size; i++) {
            for (int j = 0; j < genome_size; j++) {
                //Generate double within the bounds.
                double gene_value = gene_lower_bound + (gene_upper_bound - gene_lower_bound) * r.nextDouble();
                population[i].setGene(gene_value, j);
            }
        }
    }

    //This method prints out an entire population of individuals.
    public void print() {
        for (int i = 0; i < pop_size; i++) {
            System.out.println("\nPopulation member: " + i);
            population[i].print();
        }
        System.out.println("\n\nCumulative Probs:");
        for (int i = 0; i < pop_size; i++) {
            System.out.println(pop_cumulative_probs[i]);
        }
    }
}
