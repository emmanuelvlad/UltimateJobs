package de.warsteiner.jobs.utils.command;

import java.util.ArrayList;
import java.util.List;

public class  SubCommandRegistry  {
	  
    private final List<SubCommand> subCommandList = new ArrayList<SubCommand>();
 
    public List<SubCommand> getSubCommandList() {
        return subCommandList;
    }

}
