/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ugurcem 1
 */
public class TSP {

    static int[][] distances = {
        /*C1 C2  C3  C4  C5  C6  C7  C8  C9  C10*/
        /*C1*/{0, 55, 34, 32, 54, 40, 36, 40, 37, 53},
        /*C2*/ {64, 0, 54, 55, 73, 45, 71, 50, 53, 52},
        /*C3*/ {51, 48, 0, 41, 40, 58, 55, 33, 35, 37},
        /*C4*/ {47, 46, 55, 0, 49, 45, 56, 52, 57, 55},
        /*C5*/ {50, 39, 43, 52, 0, 26, 40, 39, 38, 33},
        /*C6*/ {60, 49, 48, 57, 58, 0, 48, 47, 48, 48},
        /*C7*/ {51, 37, 44, 43, 38, 40, 0, 64, 48, 47},
        /*C8*/ {58, 41, 53, 45, 47, 43, 74, 0, 43, 42},
        /*C9*/ {53, 38, 40, 33, 36, 58, 35, 30, 0, 31},
        /*C10*/ {60, 39, 40, 56, 41, 41, 45, 59, 19, 0}
    };

    static List<Chromosome> population = new ArrayList<>();
    static List<Chromosome> matingPool = new ArrayList<>();
    static Chromosome bestIndividual = new Chromosome();
    final static int populationSize = 8;
    final static int chromosomeLength = 10;
    final static double crossoverRate = 0.6d;
    final static double mutationRate = 0.1d;
    final static int bestFitness = 319;
    
    public static void generateRandomPopulation() {
        for (int i = 0; i < populationSize; i++) {
            String path = generateRandomPath();
            population.add(new Chromosome(path));
        }
    }

    public static String generateRandomPath() {
        int[] cities;
        String path = "";

        cities = new Random().ints(0, 10).distinct().limit(10).toArray();
        for (int j = 0; j < chromosomeLength; j++) {
            path += (String.valueOf(cities[j]));
        }
        return path;
    }

    public static Chromosome[] tournamentSelection() {
        Chromosome[] parents = new Chromosome[2];
        parents[0] = getRandomParent();
        parents[1] = getRandomParent();
        return parents;
    }

    public static Chromosome getRandomParent() {
        Chromosome parent;
        Chromosome candidate1;
        Chromosome candidate2;
        int[] rand = new Random().ints(0, populationSize).distinct().limit(2).toArray();
        candidate1 = population.get(rand[0]);
        candidate2 = population.get(rand[1]);

        if (candidate1.fitness >= candidate2.fitness) {
            parent = candidate1;
        } else {
            parent = candidate2;
        }
        return parent;
    }

    public static Chromosome[] PMX(Chromosome p1, Chromosome p2) {
        Chromosome[] offsprings = new Chromosome[2];
        int[] Xpoints = getPoints(crossoverRate);
        String path;
        String route;
        int index1;
        int index2;
        char[] arr;
        char buff;

        route = p1.genotype;
        path = p2.genotype;
        for (int i = Xpoints[0]; i <= Xpoints[1]; i++) {
            index1 = i;
            index2 = route.indexOf(path.charAt(index1));
            arr = route.toCharArray();
            buff = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = buff;
            route = String.valueOf(arr);
        }
        offsprings[0] = new Chromosome(route);

        route = p2.genotype;
        path = p1.genotype;
        for (int i = Xpoints[0]; i <= Xpoints[1]; i++) {
            index1 = i;
            index2 = route.indexOf(path.charAt(index1));
            arr = route.toCharArray();
            buff = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = buff;
            route = String.valueOf(arr);
        }
        offsprings[1] = new Chromosome(route);

        return offsprings;
    }

    public static int[] getPoints(double rate) {
        int[] points = new int[2];
        boolean flag = false;
        double rand = 0;
        for (int i = 0; i < chromosomeLength; i++) {
            rand = Math.random();
            if (rand <= rate && flag == false) {
                points[0] = i;
                flag = true;
            }
            if (rand <= rate && points[0] != i && flag == true) {
                points[1] = i;
                break;
            }
            if (flag == true && i == chromosomeLength - 1) {
                i = 0;
            }
        }
        return points;
    }

    public static String swap(String route, int i, int j) {
        char[] arr;
        char buff;
        arr = route.toCharArray();
        buff = arr[i];
        arr[i] = arr[j];
        arr[j] = buff;
        route = String.valueOf(arr);
        return route;
    }

    public static void mutation(List<Chromosome> offsprings) {
        int size = offsprings.size();
        int[] Mpoint = new int[2];
        Chromosome current;
        boolean flag = false;
        for (int i = 0; i < size; i++) {
            current = offsprings.get(i);
            Mpoint = getPoints(mutationRate);
            if (Mpoint[0] != Mpoint[1]) {
                current.setGenotype(swap(current.genotype, Mpoint[0], Mpoint[1]));
            }
        }
    }

    public static List<Chromosome> applyElitism(List<Chromosome> list) {
        List<Chromosome> pop = new ArrayList<>();
        pop.addAll(list);
        Collections.sort(pop, new Chromosome());
        return pop.subList(0, 4);
    }

    public static List<Chromosome> fillMatingPool() {
        matingPool.clear();
        Chromosome[] parents = new Chromosome[2];
        Chromosome[] offsprings = new Chromosome[2];

        for (int i = 0; i < 2; i++) {
            parents = tournamentSelection();
            offsprings = PMX(parents[0], parents[1]);
            matingPool.addAll(Arrays.asList(offsprings));
        }
        mutation(matingPool);
        matingPool.addAll(applyElitism(population));
        return matingPool;
    }

    public static void main(String[] args) {
        generateRandomPopulation();
        bestIndividual = population.get(0);
        int i=0;
        while (bestIndividual.getFitness() > bestFitness) {
            i++;
            matingPool = fillMatingPool();
            population.clear();
            population.addAll(matingPool);
            Collections.sort(population, new Chromosome());
            bestIndividual = population.get(0);
            if (i == 80000) {
                System.out.println("The program got stuck on local optima "
                        + " by 80000 iteration");
                break;
            }
        }
        System.out.println("The genotype of the best individual is:"
                + bestIndividual.getGenotype() + " and its fitness is:" + bestIndividual.getFitness()
        +" ("+i+". iteration)");

    }
}
