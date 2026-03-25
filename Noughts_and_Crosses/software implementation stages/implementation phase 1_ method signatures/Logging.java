package data;
import java.io.IOException;
import java.nio.file.Path;

abstract public class Logging {
	
	abstract static class LoggerService {
		
		abstract public void appendRoundEntry(RoundEntry e);
		
		abstract public void append(String csvLine);
		
		abstract public synchronized void flushToDisk(String filename);
		
		abstract public String samplePath(String filename);
	}
}

