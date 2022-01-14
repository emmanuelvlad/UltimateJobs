package de.warsteiner.jobs.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.utils.command.SubCommand;

public class SubHelp extends SubCommand {

	@Override
	public String getName() { 
		return "help";
	}

	@Override
	public String getDescription() {
		return "Command to get help";
	}

	@Override
	public String getSyntax() { 
		return "&7Usage &6/help";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
        if (args.length == 1) {
        	player.sendMessage("YES");
        }
	}

}
