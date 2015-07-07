package es.luixal.remoteFileUpdater;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.luixal.remoteFileUpdater.Utils.DownloadListener;
import es.luixal.remoteFileUpdater.Utils.FileDownloader;
import es.luixal.remoteFileUpdater.Utils.Utils;


public class Main {

	public static Logger logger;
	public static LogFormatter logFormatter;
	
	public static DownloadListener downloadListener = new DownloadListener() {
		
		@Override
		public void onDownloadError(String filename, String url) {
			logger.log(Level.WARNING, "There was an error downloading file from: " + url);
		}
		
		@Override
		public void onDownloadCompleted(String filename, String url, String checksum) {
			logger.log(Level.INFO, "Download completed:" + System.lineSeparator() + "\t- Filename: " + filename + System.lineSeparator() + "\t- URL: " + url + System.lineSeparator() + "\t- Checksum: " + checksum);
		}
		
		@Override
		public void onDownloadCompleted(String filename, String url) {
			logger.log(Level.INFO, "Download completed:" + System.lineSeparator() + "\t- Filename: " + filename + System.lineSeparator() + "\t- URL: " + url);
		}
	};
	
	public static void main(String[] args) {
		logger = Logger.getLogger("LOG");
		logFormatter = new LogFormatter();
		try {
			FileHandler fh = new FileHandler("log.txt", true);
			fh.setFormatter(logFormatter);
			logger.addHandler(fh);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		logger.log(Level.INFO, System.lineSeparator() + "-------------------------------------------------------" + System.lineSeparator());
		for (int i = 0; i < args.length; i++) {
			System.out.println(i + ">> " + args[i] + "\n");
		}
		
		if (args.length > 0) {
			
			if (Utils.isValidURL(args[0])) {
				
				String filename = null;
				if (args.length > 1) filename = args[1];
				FileDownloader fd = new FileDownloader(
						filename,
						args[0],
						downloadListener);
				fd.downloadFileWithChecksum();
				
			} else {
				try {
					downloadFilesFromFile(args[0], null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} else {
			try {
				downloadFilesFromFile("urls.txt", null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void downloadFilesFromFile(String file, DownloadListener listener) throws FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	String[] lineParts = line.split(" ");
		    	if (lineParts.length > 1) {
		    		// we have a url and a name:
		    		FileDownloader fd = new FileDownloader(
							lineParts[1],
							lineParts[0],
							downloadListener);
					fd.downloadFileWithChecksum();
		    	} else {
		    		// we just have the url:
		    		FileDownloader fd = new FileDownloader(
							line,
							downloadListener);
					fd.downloadFileWithChecksum();
		    	}
		    }
		}
	}

}
