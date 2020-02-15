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

        //Printing to test population generation and reproducibility of results.
        pop.print();
    }
}
