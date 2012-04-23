package core.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Log implements Serializable {
	private static final long serialVersionUID = -7094153999679662171L;

	private List<LogMsg> fullLog = new ArrayList<LogMsg>();
	private final LogLevel consoleLevel = LogLevel.Debug;
	
	private void handleMsg(LogLevel level, String message) {
		LogMsg msg = new LogMsg(message, level);
		
		fullLog.add(msg);
		
		// print to console?
		if (this.consoleLevel.Number >= msg.Level.Number) {
			if (level == LogLevel.Error) {
				System.err.println(msg.toString());
			} else {
				System.out.println(msg.toString());
			}
		}
	}
	
	public void Debug(String msg) {
		handleMsg(LogLevel.Debug, msg);
	}
	
	public void Info(String msg) {
		handleMsg(LogLevel.Info, msg);
	}
	
	public void Warning(String msg) {
		handleMsg(LogLevel.Warning, msg);
	}
	
	public void Error(String msg) {
		handleMsg(LogLevel.Error, msg);
	}
	
	public void Error(Exception e) {
		String message = "Message: " + e.getMessage()
			+ "\r\n"  + "Exception: "
			+ "\r\n" + e;
		Error(message);
	}

	public List<String> getFullLog(LogLevel level) {
		List<String> result = new ArrayList<String>();
		
		for (LogMsg msg : this.fullLog) {
			if (level.Number >= msg.Level.Number) {
				result.add(msg.toString());
			}
		}
		
		return result;
	}
}
