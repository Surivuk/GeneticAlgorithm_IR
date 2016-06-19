package rs.elfak.genetics.logger;




public class ConsoleLogger extends Logger{

	public ConsoleLogger(LogLevel mask) {
		super(mask);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void WriteMessage(String msg) {
		// TODO Auto-generated method stub
		System.out.println("Writing to console: "+msg);
	}

}
