package rs.elfak.genetics.coreAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import rs.elfak.genetics.Pair;
import rs.elfak.genetics.implementation.CustomChromosome;
import rs.elfak.genetics.implementation.CustomGene;

public class GeneticAlgorithm {

	protected int GA_numChromesInit;
	protected int GA_numChromes;
	protected int GA_numGenes;
	protected List<Chromosome> Alphas;
	//
	protected int GA_mutFactor;

	protected int GA_numIterations;

	protected ArrayList<Chromosome> GA_population;
	protected IMatingStrategy MatingStrategy;

	public GeneticAlgorithm(IMatingStrategy ms, ArrayList<Chromosome> population, int mutationFactor) {
		GA_mutFactor = mutationFactor;
		MatingStrategy = ms;
		GA_population = population;
		Alphas = new ArrayList<>();

		// def logical names
		for (int i = 0; i < population.size(); i++) {
			population.get(i).logicName = "Chrom" + new Integer(i).toString();
		}

		GA_numIterations = 1000;
		System.out.println("Genetics algorithm made.");
	}

	public void MatePopulation(int numPairs, int numGenes) {

		SortPopulation();
		GA_population = MatingStrategy.Mate(GA_population, numGenes, numPairs);
		// System.out.println("Sorted and mated population.");
	}

	public void MutatePopulation() {
		Random rand = new Random();
		for (int i = 0; i < GA_population.size(); i++) {
			int check = rand.nextInt(100);
			if (check < GA_mutFactor) {
				/*
				 * if(GA_population.get(i) == Alpha) {
				 * GA_population.get(i+1).MutateGenes(); }
				 */
				// else{
				GA_population.get(i).MutateGenes();
				// System.out.format("Mutating chromosome :
				// %s.\n",GA_population.get(i).logicName);
				// }

			}

		}
	}

	public void Darvin() {
		//boolean extinct = true;
		double extinctionLimit = GA_population.get(0).cost - 1;
		//int counter = GA_population.size() / 2 - 1;
		for (int i = GA_population.size() - 1; i > 0; i--) {

			if ( GA_population.get(i).getCost() <= extinctionLimit) {
				// System.out.format("Chromosome (%s) got
				// extinct.\n",GA_population.get(i).logicName);
				GA_population.remove(i);
				//counter--;
			}
		}
	}

	public void Start(int iterations) {

		GA_numIterations = iterations;
		System.out.println("Algorithm started.");
		boolean done = false;
		int counter = 0;
		Pair pairing = new Pair(GA_population);
		String wordForGuessing = "ththequickbrownfoxjumththequickbrownfoxjumpsoverthelazydogequickbrownfoxjumpsoverthelazydogpsoverthelazydogequickbrownfoxjumthequicthequickbrownfoxjumpsoverthelazydogkbrownfoxjumpsoverthelazydogpsoverthelththequickbrownfoxjumpsoverthelazydogequickbrownfoxs";
		while (counter < GA_numIterations && !done) {
			System.out.format("==============================================================");

			FitnessPopulation(wordForGuessing);

			MatePopulation(GA_population.size() / 2, wordForGuessing.length());
			MutatePopulation();
			SortPopulation();
			if (!Alphas.isEmpty()) {
				if (GA_population.get(0).cost > Alphas.get(Alphas.size() - 1).cost) {

					// -------------alfa save
					Chromosome tmp = GA_population.get(0);
					CustomChromosome alfa = new CustomChromosome();
					alfa.cost = tmp.cost;

					alfa.genes = new ArrayList<Gene>();

					for (int i = 0; i < tmp.geneCount(); i++) {
						Gene gen = new CustomGene();
						gen.cost = tmp.getGene(i).cost;
						gen.value = tmp.getGene(i).value;
						alfa.addGene(gen);

					}

					alfa.logicName = tmp.logicName;

					Alphas.add(alfa);

				} else {

					CustomChromosome alfa = new CustomChromosome();
					Chromosome tmp = Alphas.get(Alphas.size() - 1);

					alfa.cost = tmp.cost;

					alfa.genes = new ArrayList<Gene>();

					for (int i = 0; i < tmp.geneCount(); i++) {
						Gene gen = new CustomGene();
						gen.cost = tmp.getGene(i).cost;
						gen.value = tmp.getGene(i).value;
						alfa.addGene(gen);

					}

					alfa.logicName = tmp.logicName;

					GA_population.add(alfa);
				}
			} else {

				// -------------alfa save
				Chromosome tmp = GA_population.get(0);
				CustomChromosome alfa = new CustomChromosome();
				alfa.cost = tmp.cost;

				alfa.genes = new ArrayList<Gene>();

				for (int i = 0; i < tmp.geneCount(); i++) {
					Gene gen = new CustomGene();
					gen.cost = tmp.getGene(i).cost;
					gen.value = tmp.getGene(i).value;
					alfa.addGene(gen);

				}

				alfa.logicName = tmp.logicName;

				Alphas.add(alfa);

			}

			// -------------------------
			
			Darvin();

			done = TestConvergence(100);

			//RemoveDuplicates();
			
			// printResults();
			counter++;
			if (counter % 10 == 0) {
				//printResults();
				for (int i = 0; i < Alphas.size(); i++) {
					Alphas.get(i).printContent();
				}

				System.out.println("\nPress enter to continue...");
				Scanner keyboard = new Scanner(System.in);

				String myint = keyboard.nextLine();
			}

			System.out.format("Iteration number %d finished.Population number %d", counter, GA_population.size());
			System.out.println("\nAlpha : ");
			// Alpha.printContent();

		}
		System.out.format("==============================================================\n");
		System.out.format("Algorithm finished.Number of iterations %d, convergence achieved : %b", counter, done);
		// this.printResults();

	}

	public void SortPopulation() {

		Collections.sort(GA_population, new Comparator<Chromosome>() {

			public int compare(Chromosome c1, Chromosome c2) {
				Double d1 = new Double(c1.getCost());
				Double d2 = new Double(c2.getCost());
				return d2.compareTo(d1);
			}
		});

	}

	public void RemoveDuplicates() {
		GA_population = new ArrayList<Chromosome>(new LinkedHashSet<Chromosome>(GA_population));
	}

	public void FitnessPopulation(String goal) {
		// String goal = "Uspelo";
		for (int i = 0; i < GA_population.size(); i++) {
			GA_population.get(i).Fitness(goal);
		}
	}

	public boolean TestConvergence(double goal) {
		for (int i = 0; i < GA_population.size(); i++) {
			if (GA_population.get(i).getCost() == goal) {
				return true;
			}
		}
		return false;
		// test of finished
	}

	public void printResults() {
		for (int i = 0; i < GA_population.size(); i++) {
			GA_population.get(i).printContent();
		}
	}
}
