package rs.elfak.genetics.implementation;

import rs.elfak.genetics.coreAPI.Gene;

public class ParagraphGene extends Gene {
	
	public String Title;
	public String Content;
	//public double rating;
	
	public ParagraphGene()
	{
		super(0,null);
		
		Title = "Not set";
		Content = "Empty";
		
		
	}
	public ParagraphGene(String title,String content)
	{
		super(0, content);
		Title = title;
		
	}
	public ParagraphGene(String title,String content,float value)
	{
		super(value,content);
		Title = title;
		Content = content;
		
	}
	@Override
	public void MutateGene() {
		// TODO Auto-generated method stub
		
		
		
	}
	@Override
	public boolean equals(Object g2) {
		if(g2 instanceof ParagraphGene)
		{
			/*String mineTitle = this.Title.split("#")[0];
			String otherTitle = ((ParagraphGene) g2).Title.split("#")[0];
			
			return (mineTitle.equals(otherTitle));*/
			
			if(g2 instanceof ParagraphGene)
			{
				return (this.Title.equals(((ParagraphGene) g2).Title) && this.Content.equals(((ParagraphGene) g2).Content));
			}
		}
		return false;
	}
	@Override
	public void fitnesTest(Object test) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String ShowResult() {
		// TODO Auto-generated method stub
		String serialized = "\tParagraph title : "+this.Title+" Rating : "+String.valueOf(this.cost) +"\n";
		serialized += "\tContent:\n"+"\t------------------------------------------------------------------\n\t"+this.Content+"\t------------------------------------------------------------------\n";
		return serialized;
	}
	@Override
	public boolean isEqual(Gene g2) {
		
		//genes are the same if title and content are the same
				if(g2 instanceof ParagraphGene)
				{
					return (this.Title.equals(((ParagraphGene) g2).Title) && this.Content.equals(((ParagraphGene) g2).Content));
				}
				return false;
		
	}
	
}
