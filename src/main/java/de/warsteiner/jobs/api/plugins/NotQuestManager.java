package de.warsteiner.jobs.api.plugins;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import de.warsteiner.jobs.api.Job;
import rocks.gravili.notquests.paper.NotQuests;
import rocks.gravili.notquests.paper.structs.QuestPlayer;
import rocks.gravili.notquests.paper.structs.conditions.Condition; 
 
public class NotQuestManager {
 
    private NotQuests notQuestsInstance;

    public void setClass() {
        notQuestsInstance = NotQuests.getInstance();
    }
    
	public boolean canHaveJob(Player player, Job job) {
		if(job.hasNotQuestCon()) {
			
			QuestPlayer questPlayer = notQuestsInstance.getQuestPlayerManager().getQuestPlayer(player.getUniqueId());
			 
			ArrayList<Condition> conditions = notQuestsInstance.getConversationManager().parseConditionsString(job.getNotQuestCon());
 
			for(Condition c : conditions){ 
				if(!c.check(questPlayer).isBlank()){
	                return false;
	              }
	       }
		}
		return true; 
	}

}
