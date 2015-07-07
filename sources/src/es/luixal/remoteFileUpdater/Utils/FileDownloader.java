package es.luixal.remoteFileUpdater.Utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {

	private String filename;
	private String url;
	private DownloadListener listener;
	
	public FileDownloader(String filename, String url, DownloadListener listener) {
		this.filename = filename;
		if (filename == null) this.filename = Utils.getFilenameFromUrl(url);
		this.url = url;
		this.listener = listener;
	}
	
	public FileDownloader(String url, DownloadListener listener) {
		this.filename = Utils.getFilenameFromUrl(url);
		this.url = url;
		this.listener = listener;
	}
	
	public void downloadFile() {
		try {
			this.saveUrl(this.filename, this.url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			this.listener.onDownloadError(this.filename, this.url);
		} catch (IOException e) {
			e.printStackTrace();
			this.listener.onDownloadError(this.filename, this.url);
		}
		this.listener.onDownloadCompleted(this.filename, this.url);
	}
	
	public void downloadFileWithChecksum() {
		try {
			this.saveUrl(this.filename, this.url);
			try {
				this.listener.onDownloadCompleted(
						this.filename,
						this.url,
						Utils.getMD5Checksum(this.filename));
			} catch (Exception e) {
				e.printStackTrace();
				this.listener.onDownloadCompleted(this.filename, this.url);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			this.listener.onDownloadError(this.filename, this.url);
		} catch (IOException e) {
			e.printStackTrace();
			this.listener.onDownloadError(this.filename, this.url);
		}
	}
	
	
	private void saveUrl(final String filename, final String urlString) throws MalformedURLException, IOException {
	    BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(urlString).openStream());
	        fout = new FileOutputStream(filename);

	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}
}
