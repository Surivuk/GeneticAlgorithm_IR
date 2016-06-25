package rs.elfak.genetics.coreAPI;

public abstract class Gene {
	
	protected double cost;
	protected Object value;
	
	public Gene(double cost,Object value)
	{
		this.cost = cost;
		this.value = value;
	}
	
	public void setCost(double cost){
		this.cost = cost;
	}
	
	public double getCost(){
		return cost;
	}
	
	public void setValue(Object obj){
		value = obj;
	}
	
	public Object getValue(){
		return value;
	}
	
	public abstract void MutateGene();
	public abstract boolean isEqual(Gene g2);
	public abstract void fitnesTest(Object test);
	public abstract String ShowResult();
}
