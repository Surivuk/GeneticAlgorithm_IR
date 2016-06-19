package rs.elfak.genetics.logger;

public class EmailLogger extends Logger{

	public EmailLogger(LogLevel mask) {
		super(mask);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void WriteMessage(String msg) {
		// TODO Auto-generated method stub
		System.out.print("Sending email , message: "+msg); 
		
	}

}
