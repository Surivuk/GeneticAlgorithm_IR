package rs.elfak.genetics.coreAPI;

public class Chromosome {
	
	public String logicName;
	protected int _nGenes;
	protected double _cost;
	protected Gene[] _genes;
	
	public Chromosome(int numGenes)
	{
		_nGenes = numGenes;
		_genes = new Gene[_nGenes];
	}
	public int GetGenes()
	{
		return _nGenes;
	}
	
	public double GetCost()
	{
		double sum = 0;
		for(int i = 0 ; i < _nGenes; i++)
		{
			sum += _genes[i].GetCost();
		}
		return sum;
	}
	public void SetGene(int index,Gene value)
	{
		if(index < _nGenes && index >= 0 )
		{
			_genes[index] = value;
		}
	}
	public Gene GetGene(int index)
	{
		if(index < _nGenes)
		{
			return _genes[index];
		}
		else return null;
	}
	

	public void MutateGenes()
	{
		int index = (int)(Math.random() * 10);
		index = index  % _genes.length;
		
		_genes[index].MutateGene();
		
		
	}
	public void Fitness(Object obj)
	{
		if(obj instanceof String)
		{
			int len = ((String) obj).length();
			String str = (String) obj;
			for(int i = 0 ; i < _genes.length; i++)
			{
				
				_genes[i].fitnesTest(str.subSequence(i, i+1));
			}
		}
	}
	public boolean equals(Chromosome obj)
	{
		boolean equal = true;
		int i = 0;
		
		while(i < _genes.length && equal)
		{
			int j = 0;
			while(j < obj.GetGenes())
			{
				equal = equal && _genes[i].isEqual(obj.GetGene(j));
				j++;
			}
			//ako nakon uporedjivanja prvog svog gena sa svim ostalim genima u hromozomu obj ne nadjemo nijedan takav
			//to znaci da se hormozomi razlikuju
			if(!equal)
				return false;
			i++;
		}
		return equal;
	}
	public void printContent()
	{
		String msg = logicName + "'s contents are : ";
		for(int i = 0 ; i < _genes.length; i++)
		{
			msg += _genes[i].ShowResult() + "\t";
		}
		msg += "\tCost: "+String.valueOf(GetCost());
		System.out.println(msg);
	}
}
