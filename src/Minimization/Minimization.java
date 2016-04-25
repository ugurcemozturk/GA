/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ugurcem
 */
public class Minimization {

    final static int chromosomeLength = 8;
    final static int populationSize = 6;
    final static double crossoverRate = 0.7d;
    final static double mutationRate = 0.125d;
    final static int bestFitness = -64;
    static List<Chromosome> population = new ArrayList<>();
    static List<Chromosome> matingPool = new ArrayList<>();
    static Chromosome bestIndividual = new Chromosome();

    public static void main(String[] args) {
        int iterations = 0;
        for (int i = 0; i < 100; i++) {
            iterations += run();
        }
        System.out.println("The average of iterations is:" + iterations / 100);
     
    }

    public static void clearAllData() {
        population.clear();
        matingPool.clear();
        bestIndividual = new Chromosome();
    }

    public static int run() {
        clearAllData();
        generateRandomPopulation();
        bestIndividual = population.get(0);
        int i = 0;
        while (bestIndividual.getFitness() != bestFitness) {

            i++;
            matingPool = fillMatingPool();
            population.clear();
            population.addAll(matingPool);
            Collections.sort(population, new Chromosome());
            bestIndividual = population.get(0);

            if (i == 400) {
                System.out.println("The program got stuck on local optima "
                        + " by 400 iteration.\n Running again...");
                run();
            }
        }
        System.out.println("The genotype of the best individual is:"
                + bestIndividual.getGenotype() + " and its fitness is:"
                + bestIndividual.getFitness() + " (" + i + ". iteration)");
        return i;
    }

    public static List<Chromosome> fillMatingPool() {
        matingPool.clear();
        Chromosome[] parents = new Chromosome[2];
        Chromosome[] offsprings = new Chromosome[2];

        for (int i = 0; i < 2; i++) {
            parents = tournamentSelection();
            offsprings = crossover(parents[0], parents[1]);
            matingPool.addAll(Arrays.asList(offsprings));
        }
        mutation(matingPool);
        matingPool.addAll(applyElitism(population));
        return matingPool;
    }

    public static List<Chromosome> applyElitism(List<Chromosome> list) {
        List<Chromosome> pop = new ArrayList<>();
        pop.addAll(list);
        Collections.sort(pop, new Chromosome());
        return pop.subList(0, 2);
    }

    public static Chromosome[] tournamentSelection() {
        Chromosome[] parents = new Chromosome[2];
        parents[0] = getRandomParent();
        parents[1] = getRandomParent();
        return parents;
    }

    public static Chromosome getRandomParent() {
        Chromosome parent = new Chromosome();
        Chromosome candidate1 = new Chromosome();
        Chromosome candidate2 = new Chromosome();
        int[] rand = new Random().ints(0, populationSize).distinct().limit(2).toArray();
        candidate1 = population.get(rand[0]);
        candidate2 = population.get(rand[1]);

        if (candidate1.getFitness() >= candidate2.getFitness()) {
            parent = candidate1;
        } else {
            parent = candidate2;
        }
        return parent;
    }

    public static void generateRandomPopulation() {
        for (int i = 0; i < populationSize; i++) {
            String path = generateRandomBinary();
            population.add(new Chromosome(path));
        }
    }

    public static String generateRandomBinary() {
        int[] buff = new int[chromosomeLength];
        String binary = "";
        buff = new Random().ints(0, 2).limit(chromosomeLength).toArray();
        for (int i = 0; i < chromosomeLength; i++) {
            binary += String.valueOf(buff[i]);
        }
        return binary;
    }

    public static Chromosome[] crossover(Chromosome p1, Chromosome p2) {
        Chromosome[] offsprings = new Chromosome[2];
        int point = getPoints(crossoverRate);
        String binary = "";
        if (point != 0) {
            binary = p1.getGenotype().substring(0, point) + p2.getGenotype().substring(point, 8);
            offsprings[0] = new Chromosome(binary);

            binary = p2.getGenotype().substring(0, point) + p1.getGenotype().substring(point, 8);
            offsprings[1] = new Chromosome(binary);
        } else {
            offsprings[0] = new Chromosome(p1.getGenotype());
            offsprings[1] = new Chromosome(p2.getGenotype());
        }
        return offsprings;
    }

    public static void mutation(List<Chromosome> offsprings) {
        int size = offsprings.size();
        int Mpoint = 0;
        Chromosome current = new Chromosome();
        for (int i = 0; i < size; i++) {
            current = offsprings.get(i);
            for (int j = 0; j < chromosomeLength; j++) {
                Mpoint = getPoints(mutationRate);
                if (Mpoint != 0) {
                    reverse(current, Mpoint);
                }
            }
        }
    }

    public static void reverse(Chromosome ch, int p) {
        char[] arr = ch.getGenotype().toCharArray();

        if (arr[p] == '1') {
            arr[p] = '0';
        } else {
            arr[p] = '1';
        }
        ch.setGenotype(String.valueOf(arr));
    }

    public static int getPoints(double rate) {
        int point = 0;
        double rand = 0;
        for (int i = 1; i < chromosomeLength; i++) {
            rand = Math.random();
            if (rand <= rate) {
                point = i;
                break;
            }
        }
        return point;
    }
}
