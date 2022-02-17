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

	public final NotQuests getNotQuestsInstance() {
		return notQuestsInstance;
	}
    
	public boolean canHaveJob(final Player player, final Job job) {
		if(job.hasNotQuestCon()) {

			final QuestPlayer questPlayer = notQuestsInstance.getQuestPlayerManager().getQuestPlayer(player.getUniqueId());

			final ArrayList<Condition> conditions = notQuestsInstance.getConversationManager().parseConditionsString(job.getNotQuestCon());

			if(conditions == null || conditions.isEmpty()){
				return true;
			}
 
			for(final Condition c : conditions){
				if(!c.check(questPlayer).isBlank()){
	                return false;
	            }
	       }
		}
		return true; 
	}
	
	public boolean canBypassJob(final Player player, final Job job) {
		if(job.hasByPassNotQuestCon()) {

			final QuestPlayer questPlayer = notQuestsInstance.getQuestPlayerManager().getQuestPlayer(player.getUniqueId());
			 
			final ArrayList<Condition> conditions = notQuestsInstance.getConversationManager().parseConditionsString(job.getByPassNotQuestCon());

			if(conditions == null || conditions.isEmpty()){
				return true;
			}
 
			for(final Condition c : conditions){
				if(!c.check(questPlayer).isBlank()){
	                return false;
				}
	       }
		}
		return true; 
	}

}
