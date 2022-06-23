package de.warsteiner.jobs.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
 
public class WebManager {
	private Collection<String> updates = new ArrayList<String>();
	private HashMap<String, String> ulist = new HashMap<String, String>();
 
	public void loadVersionAndCheckUpdate(String url, String name, String ver) {
		try {

			URLConnection connection = new URL(url).openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			String version = sb.toString();

			if (!version.equalsIgnoreCase(ver)) {
				this.updates.add(name);
				this.ulist.put(name, version);
				Bukkit.getConsoleSender().sendMessage("§cThere is a new Update for §b"+name+" §cavailable!");
			}
  
		} catch (IOException ex) {
			ex.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§cFailed to send Web Request to Web-Server!");
		}
	}

	public HashMap<String, String> getVersionList() {
		return ulist;
	}
	
	public ArrayList<String> getUpdates() {
		return (ArrayList<String>)updates;
	}
 

 
}
