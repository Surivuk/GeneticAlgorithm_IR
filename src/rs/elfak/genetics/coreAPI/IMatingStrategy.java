package rs.elfak.genetics.coreAPI;

import java.util.ArrayList;

public interface IMatingStrategy {
	
	public ArrayList<Chromosome> Mate(ArrayList<Chromosome> population,int numGenes,int numPairs);
	
}
