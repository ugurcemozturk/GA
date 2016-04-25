/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minimization;

import java.util.Comparator;

/**
 *
 * @author ugurcem
 */
public class Chromosome implements Comparator<Chromosome> {

    int fitness;
    String genotype;

    public Chromosome(String genotype) {
        this.genotype = genotype;
        setFitness();
    }

    Chromosome() {
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness() {
        this.fitness = evaluateFitness();
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
        setFitness();
    }

    public int evaluateFitness() {
        short x = Short.valueOf(this.genotype, 2).byteValue();
        return (int) (Math.pow(x, 2)+(16*x));
    }
    @Override
    public int compare(Chromosome c1, Chromosome c2) {
        return c1.getFitness()-c2.getFitness();
    }
}
