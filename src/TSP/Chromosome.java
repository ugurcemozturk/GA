/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TSP;

import java.util.Comparator;

/**
 *
 * @author ugurcem
 */
public class Chromosome implements Comparator<Chromosome>{

    int fitness;
    String genotype;

    public int getFitness() {
        return fitness;
    }

    public Chromosome() {
    }

    public void setFitness() {
        this.fitness = evaluateFitness();
    }

    public Chromosome(String route) {
        this.genotype = route;
        setFitness();
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String route) {
        this.genotype = route;
    }

    public int evaluateFitness() {
        int sum = 0;
        int source, destination;
        for (int i = 0; i < 9; i++) {
            source = genotype.codePointAt(i) - 48;                                      //UTF-8/16'YA GORE DEGER DONDURUR. 0 UTF-8/16'DA 48den baslar.
            destination = genotype.codePointAt(i + 1) - 48;
            sum+=TSP.distances[source][destination];
        }
        return sum;
    }
   
    @Override
    public int compare(Chromosome c1, Chromosome c2) {
        return c1.getFitness()-c2.getFitness();
    }

    
}
