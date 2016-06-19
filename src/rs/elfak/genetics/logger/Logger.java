package rs.elfak.genetics.logger;



public abstract class Logger {
	
	public enum LogLevel
	{
	    NONE (0),                 //        0
	    Info (1),                 //        1
	    Debug (2),                //       10
	    Warning (4),              //      100
	    Error (8),                //     1000
	    FunctionalMessage (16),   //    10000
	    FunctionalError (32),     //   100000
	    All (63)    ;              //   111111
		
		private int level;
		
		LogLevel(int level){
			this.level = level;
		}
		public int level(){
			
			return this.level;
			
		}
		public void operatorLogicalOr(LogLevel right)
		{
			this.level = level | right.level();
		}
	}
	
    
	protected LogLevel logMask;
	
	protected Logger next;
	
	public Logger(LogLevel mask)
	{
		this.logMask = mask;
	}
	public Logger SetNext(Logger nextlogger)
	{
		next = nextlogger;
		return nextlogger;
	}
	
	public void Message(String msg, LogLevel severity)
    {
        if ((severity.level() & logMask.level()) != 0) //True only if all logMask bits are set in severity
        {
            WriteMessage(msg);
        }
        if (next != null) 
        {
            next.Message(msg, severity); 
        }
    }

    abstract protected void WriteMessage(String msg);
}
