package rs.elfak.genetics.implementation;

import java.util.Random;

import rs.elfak.genetics.coreAPI.Gene;

public class CustomGene extends Gene{
	
	public CustomGene() {
		Random r = new Random();

	    String alphabet = "qwertyuiopasdfghjklzxcvbnm";
	    
	    char value = alphabet.charAt(r.nextInt(alphabet.length()));
	    
	    this.setValue(value);
	}
	
	public CustomGene(char character) {
		this.setValue(character);
	}
	
	@Override
	public void MutateGene() {
		if(this.cost == 0){
			Random r = new Random();

		    String alphabet = "qwertyuiopasdfghjklzxcvbnm";
		    
		    char value = alphabet.charAt(r.nextInt(alphabet.length()));
		    
		    this.setValue(value);
		}
	}

	@Override
	public boolean isEqual(Gene g2) {
		// TODO Auto-generated method stub
		if(g2 instanceof CustomGene){
			CustomGene casted = (CustomGene) g2;
			char rightVal = (char) casted.getValue();
			char leftVal = (char) this.getValue();
			return leftVal == rightVal;
			
		}
		else{
			return false;
		}
	}

	@Override
	public void fitnesTest(Object test) {
		// TODO Auto-generated method stub
		if(test != null){
			if(test instanceof String){
				String goal = (String) test;
				
				char value = (char) this.getValue();
				String stringValueOf = String.valueOf(value);
				
				if(stringValueOf.equals(goal))
					this.cost = 1;
				else
					this.cost = 0;
			}
		}
		else{
			
		}
	}

	@Override
	public String ShowResult() {
		char val = (char) this.value;
		String returnVal = String.valueOf(val);
		
		return returnVal;
	}

}
