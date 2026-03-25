package full_method_and_class_implementations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class File_logger {
	
	static class Logger {
        private final List<String> entries = Collections.synchronizedList(new ArrayList<>());
        private final Path dir;
        
        public Logger() {
            String home = System.getProperty("user.home");
            dir = Paths.get(home, "Documents", "swiftbot_logs");
            try { Files.createDirectories(dir);} catch (IOException ignored) {}
            		
        }
            
            public void append(String csv) { 
            	entries.add(csv); 
            	}
           
            public synchronized void flushToDisk(String filename) 
            		throws IOException 
            {
            	
                if (entries.isEmpty()) 
                	return;
                
                Path path = dir.resolve(filename);
                try (BufferedWriter w = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {        
                	for (String s : entries) 
                    	w.write(s + System.lineSeparator());
                    
                    w.flush();
                    entries.clear();
                }
            	}
            public String samplePath(String filename) { 
            	return dir.resolve(filename).toAbsolutePath().toString(); 
            	
                 }
            }

	}
