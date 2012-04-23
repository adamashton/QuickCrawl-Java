package core.support;

import java.io.Serializable;

public enum LogLevel implements Serializable {
	NoLogging(0),
	Error(1),
	Warning(2),
	Info(3),
	Debug(4);
	
	public int Number;
	
	LogLevel(int level) {
		this.Number = level;
	}
}
