package rs.elfak.genetics;

import java.util.ArrayList;
import java.util.Random;

import rs.elfak.genetics.coreAPI.Chromosome;

public class Mate {
	
	private Chromosome MT_father,
	MT_mother,
	MT_child1,
	MT_child2;
	
	private int MT_posChild1,
	MT_posChild2,
	MT_posLastChild,
	MT_posFather,
	MT_posMother,
	MT_numGenes,
	MT_numChromes;
	
	public Mate(ArrayList<Chromosome> population,int numGenes,int numChromes)
	{
		MT_posFather = 0;
		MT_posMother = 1;
		MT_numGenes = numGenes;
		MT_numChromes = numChromes;
		MT_posChild1 = population.size() / 2;
		MT_posChild2 = MT_posChild1 +1;
		MT_posLastChild = population.size() - 1;
		for(int i = MT_posLastChild; i >= MT_posChild1; i--)
		{
			population.remove(i);
		}
		MT_posFather = 0;
		MT_posMother = 1;
	}
	public ArrayList<Chromosome> Crossover(ArrayList<Chromosome> population,int numPairs)
	{
		for(int j = 0 ; j < numPairs; j++)
		{
			MT_father = population.get(MT_posFather);
			MT_mother = population.get(MT_posMother);
			MT_child1 = new Chromosome();
			MT_child2 = new Chromosome();
			Random rnum = new Random();
			int crossPoint = rnum.nextInt(MT_numGenes);
			
			//left side
			for(int i = 0 ; i < crossPoint;i++)
			{
				MT_child1.setGene(MT_father.getGene(i));
				MT_child2.setGene(MT_mother.getGene(i));
			}
			//right side
			for(int i = crossPoint; i < MT_numGenes;i++)
			{
				MT_child1.setGene(MT_mother.getGene(i));
				MT_child2.setGene(MT_father.getGene(i));
			}
			population.add(MT_posChild1,MT_child1);
			population.add(MT_posChild2, MT_child2);
			MT_posChild1 = MT_posChild1 + 2;
			MT_posChild2 = MT_posChild2 + 2;
			MT_posFather = MT_posFather + 2;
			MT_posMother = MT_posMother + 2;
		}
		return population;
	}
	
}
