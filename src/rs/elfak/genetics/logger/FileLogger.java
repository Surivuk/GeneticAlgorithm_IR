package rs.elfak.genetics.logger;

public class FileLogger extends Logger{

	public FileLogger(LogLevel mask) {
		super(mask);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void WriteMessage(String msg) {
		// TODO Auto-generated method stub
		System.out.println("Writing message: "+msg+" to a Log file.");
	}

}
