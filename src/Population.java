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
    int pop_size;
    //This seed is used for the reproduction of results,
    //Using the same seed results in the exact same initial population.
    long pop_seed;

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
    }
}
