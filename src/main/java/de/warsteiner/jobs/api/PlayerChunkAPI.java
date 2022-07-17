package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import de.warsteiner.jobs.UltimateJobs;

public class PlayerChunkAPI {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public HashMap<String, HashMap<Job, List<String>>> players = new HashMap<String, HashMap<Job, List<String>>>();

	public void loadChunks(String UUID, Job job) {
		FileConfiguration cfg = plugin.getChunkData().get();

		HashMap<Job, List<String>> tempMap = new HashMap<Job, List<String>>();

		if (cfg.getConfigurationSection("players.chunks." + job.getConfigID() + "." + UUID) == null) {
			List<String> list =  new ArrayList<String>();
			tempMap.put(job, list);
			players.put(UUID, tempMap);
			return;
		}
		cfg.getConfigurationSection("players.chunks." + job.getConfigID() + "." + UUID).getKeys(false).forEach(key -> {

			String chunks = cfg.getString("players.chunks." + job.getConfigID() + "." + key);
			String[] split = chunks.split(":");
			List<String> temp = new ArrayList<>();
			for (String s : split)
				temp.add(s);
			tempMap.put(job, temp);
		});

		players.put(UUID, tempMap);

	}

	public void addChunk(String UUID, Job job, String c) {
		List<String> old = players.get(UUID).get(job);

		old.add(c);

		players.get(UUID).put(job, old);
	}

	public void savePlayerChunks(String UUID, Job job) {
		File file = plugin.getChunkData().getfile();
		FileConfiguration cfg = plugin.getChunkData().get();

		for (Entry<String, HashMap<Job, List<String>>> e : players.entrySet()) {

			String uuid = e.getKey();

			HashMap<Job, List<String>> second = e.getValue();

			for (Entry<Job, List<String>> s : second.entrySet()) {

				Job j = s.getKey();

				String temp = "";
				for (String s2 : s.getValue()) {
					temp = temp + s2 + ":";
					cfg.set("players.chunks." + j.getConfigID() + "." + uuid, temp);
				}
			}

		}

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		players.remove(UUID);

	}

}
