package rs.elfak.genetics.coreAPI;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public abstract class Chromosome {
	
	public String logicName;
	protected double cost;
	protected List<Gene> genes;
	
	public Chromosome(){
		genes = new ArrayList<>();
	}
	
	public double getCost(){
		double sum = 0;
		for(int i = 0 ; i < genes.size(); i++)
			sum += genes.get(i).getCost();
		return sum;
	}
	
	public void addGene(Gene value){
		genes.add(value);
	}
	
	public Gene getGene(int index){
		if(index < genes.size())
			return genes.get(index);
		else 
			return null;
	}
	public boolean setGene(int index,Gene g1)
	{
		if(index >= 0 && index < genes.size())
		{
			genes.set(index, g1);
			return true;
		}
		else{
			return false;
		}
	}
	
	public void replaceFisrt(Gene g){
		genes.set(0, g);
	}
	
	public int geneCount()
	{
		return genes.size();
	}
	public void MutateGenes(){
		Random rand = new Random();
		int howMany = rand.nextInt(genes.size() + 1);
		for(int i = 0; i < howMany; i++){
			int index = rand.nextInt(genes.size());
			genes.get(index).MutateGene();
		}
	}
	
	public void Fitness(Object obj){
		if(obj instanceof String)
		{
			int len = ((String) obj).length();
			String str = (String) obj;
			for(int i = 0 ; i < genes.size(); i++)
				genes.get(i).fitnesTest(str.subSequence(i, i+1));
		}
	}
	
	public boolean equals(Chromosome obj){
		boolean equal = true;
		int i = 0;
		
		while(i < genes.size() && equal){
			int j = 0;
			while(j < genes.size()){
				equal = equal && genes.get(i).isEqual(obj.getGene(j));
				j++;
			}
			if(!equal)
				return false;
			i++;
		}
		return equal;
	}
	
	public void printContent(){
		
		String msg = "";
		msg += "Cost: "+String.valueOf(getCost())+"\t";
		msg += logicName + "'s contents are : ";
		/*for(int i = 0 ; i < genes.size(); i++)
		{
			msg += genes.get(i).ShowResult() + "\t";
		}*/
		
		System.out.println(msg);
	}
}
