package es.luixal.remoteFileUpdater.Utils;

public interface DownloadListener {
	
	void onDownloadCompleted(String filename, String url);
	void onDownloadCompleted(String filename, String url, String checksum);
	
	void onDownloadError(String filename, String url);

}
