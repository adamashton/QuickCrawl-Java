package core.support;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogMsg implements Serializable {
	private static final long serialVersionUID = -7621012429252255578L;
	
	public String Msg;
	public LogLevel Level;
	public Date DateTime;
	
	private SimpleDateFormat defaultFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * 0=None, ..., 4=Debug
	 * @param msg
	 * @param level
	 */
	public LogMsg(String msg, LogLevel level) {
		Msg = msg;
		Level = level;
		DateTime = new Date();
	}
	
	@Override
	public String toString() {
		String result = defaultFormat.format(DateTime) + " | " + Msg;
		return result;
	}
}
