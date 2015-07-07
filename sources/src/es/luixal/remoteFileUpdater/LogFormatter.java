package es.luixal.remoteFileUpdater;

import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogFormatter extends SimpleFormatter {

	@Override
	public synchronized String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		sb.append( "[" + (new Date()).toString() + "] " );
		sb.append("[" + record.getLevel().toString() + "]");
		sb.append(System.lineSeparator());
		sb.append(record.getMessage());
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
}
