package rs.elfak.genetics.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import rs.elfak.genetics.Pair;
import rs.elfak.genetics.coreAPI.Chromosome;
import rs.elfak.genetics.coreAPI.GeneticAlgorithm;
import rs.elfak.genetics.coreAPI.IMatingStrategy;
import rs.elfak.genetics.searcher.Search;

public class InformationRetrievelGA extends GeneticAlgorithm {

	private ArrayList<ParagraphGene> mutationPit = new ArrayList<ParagraphGene>();

	public InformationRetrievelGA(IMatingStrategy ms, int mutationFactor, String query) {
		super(ms, new ArrayList<Chromosome>(), mutationFactor);
		preProcess(query, 500);
	}

	public InformationRetrievelGA(IMatingStrategy ms, ArrayList<Chromosome> population, int mutationFactor) {
		super(ms, population, mutationFactor);
		// TODO Auto-generated constructor stub
	}

	public void MatePopulation(int numPairs, int numGenes) {

		SortPopulation();
		GA_population = MatingStrategy.Mate(GA_population, numGenes, numPairs);
		System.out.println("Sorted and mated population.");
	}

	public void MutatePopulation() {
		Random rand = new Random();
		int mutationChanceRoll = rand.nextInt(100);
		mutationChanceRoll++;
		if (mutationChanceRoll < this.GA_mutFactor) {
			System.out.println("Mutation started.");
			int documentIndex = rand.nextInt(GA_population.size());
			DocumentChromosome document = (DocumentChromosome) GA_population.get(documentIndex);
			System.out.println("Document selected for mutation: " + document.logicName);
			int paragraphFromDocumentIndex = rand.nextInt(document.geneCount());
			int mutationPitParticipantIndex = rand.nextInt(mutationPit.size());

			ParagraphGene gen = mutationPit.get(mutationPitParticipantIndex);
			ParagraphGene gen2 = (ParagraphGene) document.getGene(paragraphFromDocumentIndex);

			System.out.format("Paragraph for mutation is [\"%s\"] \n", gen2.Title);
			System.out.format("Paragraph from mutationPit is [\"%s\"] \n", gen.Title);

			mutationPit.remove(mutationPitParticipantIndex);
			mutationPit.add(gen2);

			document.setGene(paragraphFromDocumentIndex, gen);

		}

	}

	public void Darvin() {
		boolean extinct = true;
		int counter = GA_population.size() / 20;
		for (int i = GA_population.size() - 1; i >= 0; i--) {

			if (GA_population.get(i).getCost() == 0 && counter > 0) {
				System.out.format("Chromosome (%s) got extinct.\n", GA_population.get(i).logicName);
				GA_population.remove(i);
				counter--;
			}
		}
	}

	private void preProcess(String query, int numberOfParagraph) {
		Search searchEngine = new Search("", false);
		// GA_population = new ArrayList<Chromosome>();

		boolean flag = false;
		ArrayList<ParagraphGene> satisfactoryParagraphs = searchEngine.getTFIDF(query, numberOfParagraph);
		for (int i = 0; i < satisfactoryParagraphs.size(); i++) {

			String documentTitle = satisfactoryParagraphs.get(i).Title.split("#")[0];

			// only the best go in population

			if (i < satisfactoryParagraphs.size() / 2) {
				if (!flag) {
					System.out.println(
							"============================== ADDING POPULATION ========================================================================================\n");
					flag = true;
				}

				DocumentChromosome document = new DocumentChromosome(documentTitle);

				if (!this.GA_population.contains(document)) {

					document.addGene(satisfactoryParagraphs.get(i));
					GA_population.add(document);
					System.out.format("New document added to population.Document %s has %d genes.\n",
							document.logicName, document.geneCount());
				} else {
					int index = GA_population.indexOf(document);
					DocumentChromosome documentFromPopulation = (DocumentChromosome) GA_population.get(index);
					documentFromPopulation.addGene(satisfactoryParagraphs.get(i));
					GA_population.set(index, documentFromPopulation);
					System.out.format("Document exists in population,updating genes.Document %s has %d genes. \n",
							document.logicName, GA_population.get(index).geneCount());
				}

			}
			// rest goes in the mutation pool for possible mutations
			else {
				if (flag) {
					System.out.println(
							"============================== ADDING MUTANTS ============================================================================================\n");
					flag = false;
				}
				// split the paragraph name and get first part - this part is
				// the document name from which paragraph is originally taken
				// check if hashMap has it as a key
				ParagraphGene currentPara = satisfactoryParagraphs.get(i);

				boolean present = mutationPit.contains(currentPara);
				if (!present) {
					// DocumentChromosome document = new
					// DocumentChromosome(documentTitle);

					// document.addGene(satisfactoryParagraphs.get(i));
					mutationPit.add(currentPara);
					System.out.println("New paragraph added to look-up table.Paragraph name: " + currentPara.Title);
				} else {
					/*
					 * DocumentChromosome document =
					 * mutationPit.get(documentTitle);
					 * document.addGene(satisfactoryParagraphs.get(i));
					 * mutationPit.put(documentTitle, document);
					 * System.out.format(
					 * "Document %s exists in look-up table.Added gene and updated.Gene count for document %s is %d \n"
					 * ,documentTitle,documentTitle,document.geneCount());
					 */
				}
			}
		}

		// System.out.println(satisfactoryParagraphs.size());
		// System.out.println("Ende.HashMap size :
		// "+String.valueOf(mutationPit.size()));
		// System.out.println("Init population size :
		// "+String.valueOf(GA_population.size()));

		swapSmallDocs(1);
	}

	private void swapSmallDocs(int swapLimit) {
		System.out.println(
				"================================ Now swapping population ==============================================");
		int sizeBeforeRemove = GA_population.size();
		for (int i = GA_population.size()-1; i >= 0 ; i--) {
			DocumentChromosome document = (DocumentChromosome) GA_population.get(i);
			if (document.geneCount() <= swapLimit) {
				System.out.format(
						"Found document (\" %s \") with less then or equal paragraphs to the limit %d.Now swaping...\n",
						document.logicName, swapLimit);
				mutationPit.add((ParagraphGene) document.getGene(i));
				GA_population.remove(i);
			}
		}
		System.out.println("Null cleanup started...");
		System.out.println("Population changed : "+String.valueOf(GA_population.removeAll(Collections.singleton(null))));
		System.out.println("Mutation pit changed : "+String.valueOf(mutationPit.removeAll(Collections.singleton(null))));
		
		System.out.println("Null cleanup ended.");
	}

	public void Start(int iterations) {

		GA_numIterations = iterations;
		System.out.println("Algorithm started.");
		boolean done = false;
		int counter = 0;
		// Pair pairing = new Pair(GA_population);
		// String wordForGuessing = "uspelo";
		while (counter < GA_numIterations && !done) {
			System.out.format("==============================================================");
			// do algorithm
			FitnessPopulation();
			SortPopulation();
			// MatePopulation(pairing.Top4(),wordForGuessing.length());
			MutatePopulation();
			done = TestConvergence(34.5);
			// Darvin();
			// printResults();
			counter++;
			System.out.format("Iteration number %d finished.Population number %d \n", counter, GA_population.size());

		}
		System.out.format("==============================================================\n");
		System.out.format("Algorithm finished.Number of iterations %d, convergence achieved : %b", counter, done);
		
		if(done)
		{
			System.out.println("----------------------------------------- First document that succeded -------------------------------------------");
			//Alpha.printContent();
		}
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
		//Alpha = GA_population.get(0);

	}

	public void RemoveDuplicates() {

	}

	public void FitnessPopulation() {

		for (int i = 0; i < GA_population.size(); i++) {
			GA_population.get(i).Fitness(null);
		}
	}

	public boolean TestConvergence(double goal) {

		for (int i = 0; i < GA_population.size(); i++) {
			DocumentChromosome document = (DocumentChromosome) GA_population.get(i);
			double normalizedCost = document.getCost();
			normalizedCost = normalizedCost / document.geneCount();
			if (normalizedCost >= goal)
			{
				//this.Alpha = document;
				return true;
			}
				
		}

		return false;
	}

	public void printResults() {
		System.out.format(
				"\n################################### Population size : %d #################################\n",
				GA_population.size());
		for (int i = 0; i < this.GA_population.size(); i++) {
			// System.out.print(String.valueOf(i));
			GA_population.get(i).printContent();
		}
	}

}
