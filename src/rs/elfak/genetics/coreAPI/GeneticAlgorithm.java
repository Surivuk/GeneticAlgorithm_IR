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
		// boolean extinct = true;
		int t = (int) GA_population.get(0).getCost();
		double extinctionLimit = 2;
		/*if(t > 20)
			extinctionLimit = t - 5;*/

		int topCap = 5000;
		Random rand = new Random();
		if (GA_population.size() - topCap > 0)
			for(int i = GA_population.size() - 1; i > topCap - rand.nextInt(1000); i--){
				int index = rand.nextInt(GA_population.size());
				while(index == 0)
					index = rand.nextInt(GA_population.size());
				GA_population.remove(index);
			}
		 else {
			for (int i = GA_population.size() - 1; i >= 0; i--) {
				if (GA_population.get(i).getCost() < extinctionLimit)
					GA_population.remove(i);
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
		int lastSize = 0;
		while (/*counter < GA_numIterations &&*/ !done) {
			//System.out.format("==============================================================");

			FitnessPopulation(wordForGuessing);

			MatePopulation(GA_population.size() / 2, wordForGuessing.length());
			MutatePopulation();
			SortPopulation();
			if (!Alphas.isEmpty()) {
				if (GA_population.get(0).getCost() > Alphas.get(Alphas.size() - 1).cost) {

					// -------------alfa save
					Chromosome tmp = GA_population.get(0);
					CustomChromosome alfa = new CustomChromosome();
					alfa.cost = tmp.getCost();

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

					alfa.cost = tmp.getCost();

					alfa.genes = new ArrayList<Gene>();

					for (int i = 0; i < tmp.geneCount(); i++) {
						Gene gen = new CustomGene();
						gen.cost = tmp.getGene(i).cost;
						gen.value = tmp.getGene(i).value;
						alfa.addGene(gen);
					}

					alfa.logicName = tmp.logicName;

					CustomChromosome old_alfa = new CustomChromosome();
					tmp = GA_population.get(0);

					old_alfa.cost = tmp.getCost();

					old_alfa.genes = new ArrayList<Gene>();

					for (int i = 0; i < tmp.geneCount(); i++) {
						Gene gen = new CustomGene();
						gen.cost = tmp.getGene(i).cost;
						gen.value = tmp.getGene(i).value;
						old_alfa.addGene(gen);
					}

					old_alfa.logicName = tmp.logicName;
					GA_population.add(old_alfa);
					GA_population.set(0, alfa);
				}
			} else {
				Chromosome tmp = GA_population.get(0);
				CustomChromosome alfa = new CustomChromosome();
				alfa.cost = tmp.getCost();

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
			done = TestConvergence(256);
			RemoveDuplicates();
			counter++;
			//if(lastSize < Alphas.size()){
				lastSize = Alphas.size();
				System.out.println();
				Alphas.get(lastSize - 1).printContent();
				System.out.format("Iteration number %d finished.Population number %d", counter, GA_population.size());
			//}	
		}
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
