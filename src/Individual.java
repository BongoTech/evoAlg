//*************************************************************************************************
//Author: Cory Mckiel
//Date Created: Feb 15, 2020
//Last Modified: Feb 15, 2020
//Associated Files: Population.java EvoAlg.java
//IDE: IntelliJ
//Java SDK: 12
//Program Description: This is an evolutionary algorithm designed to solve analytic functions.
//It is easily configurable for multiple population sizes, individual sizes, crossover and
//mutation rates, etc. With changes to specific functions different problems can be tackled.
//This program is meant be a modular and easily configurable platform for evolutionary
//algorithms.
//*************************************************************************************************

public class Individual {
    //The genome is the entire vector of genes.
    //Each genome represents a potential solution to the problem.
    private double [] genome;
    //The fitness is a measure of how well the genome solves
    //the problem. It is calculated by the eval() function.
    private double fitness;
    //The probability of selection is a ratio of this
    //individuals fitness compared to the total
    //population fitness. Higher values tend to be
    //selected more often because they are better
    //solutions.
    private double prob_selection;
    //The genome_size is the number of genes it
    //takes to form a solution to a function.
    //eg. f(x,y,z) needs 3 genes.
    private int genome_size;

    //Constructor
    Individual(int size) {
        genome_size = size;
        genome = new double[genome_size];
        fitness = 0;
        prob_selection = 0;
    }

    //*****GETTERS AND SETTERS*****//
    public double[] getGenome() {
        return genome;
    }

    public double getGene(int index) {
        //Bounds checking
        if (index < genome_size && index >= 0) {
            return genome[index];
        }
        else {
            return -1;
        }
    }

    public void setGenome(double[] genome) {
        this.genome = genome;
    }

    public void setGene(double value, int index) {
        if (index < genome_size && index >= 0) {
            genome[index] = value;
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getProb_selection() {
        return prob_selection;
    }

    public void setProb_selection(double prob_selection) {
        this.prob_selection = prob_selection;
    }

    public int getGenome_size() {
        return genome_size;
    }

    //This method prints out all information associated
    //with an individual.
    public void print() {
        System.out.print("Ind: <");
        for (int i = 0; i < genome_size; i++) {
            System.out.print(genome[i] + ", ");
        }
        System.out.println(">");
        System.out.println("Fitness: " + fitness);
        System.out.println("Probability of Selection: " + prob_selection);
    }
}
