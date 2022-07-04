package de.warsteiner.jobs.manager;
 
import java.io.BufferedReader;
import java.io.File; 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.net.URL;
import java.net.URLConnection; 
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList; 

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
 
public class WebManager {
	
	public boolean canUpdate = false;
	public String newVersion = null;
	
	public ArrayList<String> added = new ArrayList<String>();
	public ArrayList<String> updated = new ArrayList<String>();
	public ArrayList<String> removed = new ArrayList<String>();
	
	public void checkVersion() {
		try {

			URLConnection connection = new URL("https://apiv3.war-projects.com/ultimatejobs/version.txt").openConnection();
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

			String ver = UltimateJobs.getPlugin().getDescription().getVersion();
			
			if (!version.equalsIgnoreCase(ver)) {
				canUpdate = true;
				newVersion = version;
				
				getChangeLogOfVersion(version);
				Bukkit.getConsoleSender().sendMessage("§c§lThere was a Update found for UltimateJobs!");
			} else {
				Bukkit.getConsoleSender().sendMessage("§a§lNo Update for UltimateJobs found!");
			}
			r.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§cFailed to send Web Request to Web-Server!");
		}
	}
	
	  public void downloadUpdate(Player player) {
		  
			File folder_1 = new File("plugins/UltimateJobs/", "updates");

			if (!folder_1.exists()) {
				folder_1.mkdir();
			}
		  
			String ver = UltimateJobs.getPlugin().getWebManager().newVersion;
			
			String url = "https://apiv3.war-projects.com/ultimatejobs/changelogs/UltimateJobs-"+ver+".jar";
			
		  
		 
			try {
				InputStream inputStream = new URL(url).openStream();
				Files.copy(inputStream, Paths.get("plugins/UltimateJobs/updates/UltimateJobs-"+ver+".jar"), StandardCopyOption.REPLACE_EXISTING);
				player.sendMessage(AdminCommand.prefix+"§aUpdate can be found in plugins/UltimateJobs/updates/");
			} catch (IOException e) {
				player.sendMessage(AdminCommand.prefix+"§cFailed to download Update, please check the logs");
				e.printStackTrace();
			}
        
             
	  }
	 
 
	  
 
	public void checkVersionWithPlayer(Player player) {
		try {
			 
			URLConnection connection = new URL("https://apiv3.war-projects.com/ultimatejobs/version.txt").openConnection();
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

			String ver = UltimateJobs.getPlugin().getDescription().getVersion();
			
			if (!version.equalsIgnoreCase(ver)) {
				canUpdate = true;
				newVersion = version;
				
				getChangeLogOfVersion(version);
				player.sendMessage(AdminCommand.prefix+"§4There was a new Update Found!");
				UltimateJobs.getPlugin().getGUIAddonManager().createUpdateMenu(player);
			} else {
				player.sendMessage(AdminCommand.prefix+"§aNo Update Found!");
			}
  
		} catch (IOException ex) {
			ex.printStackTrace();
			player.sendMessage(AdminCommand.prefix+"§4Failed to contact Webserver...");
		}
	}
 
 
	public void getChangeLogOfVersion(String v) {
		try {
			
			added.clear();
			updated.clear();
			removed.clear();
 
			URLConnection connection = new URL("https://apiv3.war-projects.com/ultimatejobs/changelogs/"+v+".txt").openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			String line;
			String m = null;
			 while ((line = r.readLine()) != null)  {
				sb.append(line);  
			}
			 
			 m = sb.toString();
			 
			String[] split = m.split(":");
			
			for(String s : split) {
		 
				String[] l = s.split(",");
			 
				if(l[0].toUpperCase().equals("ADDED")) {
					added.add(l[1]);
				} else if(l[0].toUpperCase().equals("UPDATED")) {
					updated.add(l[1]);
				} else if(l[0].toUpperCase().equals("REMOVED")) {
					removed.add(l[1]);
				}
				
			}
			r.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§cFailed to send Web Request to Web-Server!");
		}
	}
 
}
