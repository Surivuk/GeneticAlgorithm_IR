package rs.elfak.genetics;

import java.util.ArrayList;

import rs.elfak.genetics.coreAPI.Chromosome;

public class Pair {

	private ArrayList<Chromosome> PR_population;
	public Pair(ArrayList<Chromosome> population)
	{
		PR_population = population;
	}
	public int TopDown()
	{
		return (PR_population.size() / 4);
	}
	public int Top4()
	{
		return 4;
	}
}
