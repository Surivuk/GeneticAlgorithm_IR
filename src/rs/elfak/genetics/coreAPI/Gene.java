package rs.elfak.genetics.coreAPI;

public abstract class Gene {
	
	protected double _cost;
	protected Object _value;
	
	public void SetCost(double cost)
	{
		_cost = cost;
	}
	public double GetCost()
	{
		return _cost;
	}
	public void SetValue(Object obj)
	{
		_value = obj;
	}
	public Object GetValue()
	{
		return _value;
	}
	public abstract void MutateGene();
	public abstract boolean isEqual(Gene g2);
	public abstract void fitnesTest(Object test);
	public abstract String ShowResult();
}
