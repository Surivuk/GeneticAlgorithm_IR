package rs.elfak.genetics.coreAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.Scanner;

import rs.elfak.genetics.Pair;

public class GeneticAlgorithm extends Object {
	
	
	protected int GA_numChromesInit;
	protected int GA_numChromes;
	protected int GA_numGenes;
	protected Chromosome Alpha;
	//
	protected int GA_mutFactor;
	
	
	protected int GA_numIterations;
	
	protected ArrayList<Chromosome> GA_population;
	protected IMatingStrategy MatingStrategy;
	
	
	
	public GeneticAlgorithm(IMatingStrategy ms,ArrayList<Chromosome> population,int mutationFactor)
	{
		GA_mutFactor = mutationFactor;
		MatingStrategy = ms;
		GA_population = population;
		
		
		//def logical names
		for(int i = 0 ; i < population.size() ; i++)
		{
			population.get(i).logicName = "Chrom"+ new Integer(i).toString();
		}
		
		GA_numIterations = 1000;
		System.out.println("Genetics algorithm made.");
	}
	
	public void MatePopulation(int numPairs,int numGenes){
		
		SortPopulation();
		GA_population = MatingStrategy.Mate(GA_population,numGenes, numPairs);
		System.out.println("Sorted and mated population.");
	}
	public void MutatePopulation()
	{
		for(int i = 0 ; i < GA_population.size(); i++)
		{
			int check = (int)(Math.random() * 100);
			if(check < GA_mutFactor)
			{
				if(GA_population.get(i) == Alpha)
				{
					
				}
				else{
					GA_population.get(i).MutateGenes();
					System.out.format("Mutating chromosome : %s.\n",GA_population.get(i).logicName);
				}
				
			}
				
		}
	}
	public void Darvin()
	{
		boolean extinct = true;
		int counter = GA_population.size() / 20;
		for(int i = GA_population.size()-1 ; i >= 0 ;i--)
		{
			
			if(GA_population.get(i).GetCost() == 0 && counter > 0)
			{
				System.out.format("Chromosome (%s) got extinct.\n",GA_population.get(i).logicName);
				GA_population.remove(i);
				counter--;
			}
		}
	}
	public void Start(int iterations){
		
		GA_numIterations = iterations;
		System.out.println("Algorithm started.");
		boolean done = false;
		int counter = 0;
		Pair pairing = new Pair(GA_population);
		String wordForGuessing = "uspelo";
		while(/*counter < GA_numIterations &&*/ !done)
		{
			System.out.format("==============================================================");
			//do algorithm
			FitnessPopulation(wordForGuessing);
			//SortPopulation();
			MatePopulation(pairing.Top4(),wordForGuessing.length());
			MutatePopulation();
			done = TestConvergence(wordForGuessing.length());
			Darvin();
			printResults();
			counter++;
			System.out.format("Iteration number %d finished.Population number %d",counter,GA_population.size());
			System.out.println("\nAlpha : ");
			Alpha.printContent();
			Scanner keyboard = new Scanner(System.in);
			
			String myint = keyboard.nextLine();
			
		}
		System.out.format("==============================================================\n");
		System.out.format("Algorithm finished.Number of iterations %d, convergence achieved : %b", counter,done);
		//this.printResults();
		
	}
	
	public void SortPopulation(){

		Collections.sort(GA_population, new Comparator<Chromosome>(){

		  public int compare(Chromosome c1, Chromosome c2)
		  {
			 Double d1 = new Double(c1.GetCost());
			 Double d2 = new Double(c2.GetCost());
		     return d2.compareTo(d1);
		  }
		});
		Alpha = GA_population.get(0);
		
	}
	public void RemoveDuplicates()
	{
		ArrayList<Chromosome> test = new ArrayList<Chromosome>();
		
		for(int i = 0 ; i < GA_population.size() ; i++)
		{
			
		}
	}
	public void FitnessPopulation(String goal){
		//String goal = "Uspelo";
		for(int i = 0 ; i < GA_population.size(); i++)
		{
			GA_population.get(i).Fitness(goal);
		}
	}
	
	public boolean TestConvergence(double goal)
	{
		for(int i = 0 ; i < GA_population.size(); i++)
		{
			if(GA_population.get(i).GetCost() == goal)
			{
				return true;
			}
		}
		return false;
		//test of finished
	}
	public void printResults()
	{
		for(int i=0 ; i < GA_population.size(); i++)
		{
			GA_population.get(i).printContent();
		}
	}
}
