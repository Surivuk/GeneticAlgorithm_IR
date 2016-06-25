package rs.elfak.genetics.implementation;

import rs.elfak.genetics.coreAPI.Chromosome;

public class DocumentChromosome extends Chromosome{
	
	//public String documentName;
	
	public DocumentChromosome(String name)
	{
		this.logicName = name;
	}
	
	public void MutateGenes(){
		
	}
	public void Fitness(Object obj){
		this.cost = 0;
		for(int i = 0 ; i < this.geneCount(); i++)
		{
			this.cost += this.genes.get(i).getCost();
		}
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof DocumentChromosome)
		{
			return logicName.equals(((DocumentChromosome) obj).logicName);
		}
		return false;
		
	}
	public void printContent()
	{
		System.out.format("==================================== Document \"%s\" has %d paragraphs.=============================================\n",this.logicName,this.geneCount() );
		for(int i = 0; i < this.geneCount(); i++)
		{
			
			System.out.println(this.genes.get(i).ShowResult());
		}
	}
}
