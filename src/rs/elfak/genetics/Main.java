package rs.elfak.genetics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import rs.elfak.genetics.coreAPI.Chromosome;
import rs.elfak.genetics.coreAPI.GeneticAlgorithm;
import rs.elfak.genetics.implementation.Crossover;
import rs.elfak.genetics.implementation.CustomChromosome;
import rs.elfak.genetics.implementation.CustomCrossover;
import rs.elfak.genetics.implementation.CustomGene;
import rs.elfak.genetics.implementation.DocumentChromosome;
import rs.elfak.genetics.implementation.InformationRetrievelGA;
import rs.elfak.genetics.implementation.ParagraphGene;
import rs.elfak.genetics.searcher.Search;
import sun.awt.CharsetString;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Search searchEngine = new Search("", false);

		// for indexing
		// int sum = searchEngine.indexCorpus("D:\\Crawler Data 2\\",
		// "D:\\Index2\\");

		// HashMap<String,DocumentChromosome> mutationPit = new
		// HashMap<String,DocumentChromosome>();
		// ArrayList<DocumentChromosome> population = new
		// ArrayList<DocumentChromosome>();
		// Crossover operator = new Crossover();

		// InformationRetrievelGA GA = new InformationRetrievelGA(operator,50,
		// "computer electronics mathematics");

		// GA.printResults();

		// GA.Start(200);

		int geneCount = 256;

		ArrayList<Chromosome> population = new ArrayList<Chromosome>();

		for (int i = 0; i < 50; i++) {
			CustomChromosome chromo = new CustomChromosome();
			for (int j = 0; j < geneCount; j++) {
				CustomGene gen = new CustomGene();
				chromo.addGene(gen);
			}
			population.add(chromo);
		}
		
		CustomCrossover operator = new CustomCrossover();

		GeneticAlgorithm GA = new GeneticAlgorithm(operator, population, 50);
		
		GA.Start(4000);
	}

}
