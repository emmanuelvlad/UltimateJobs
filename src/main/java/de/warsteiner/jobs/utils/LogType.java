package de.warsteiner.jobs.utils;

public enum LogType {
	
	GET("§f"),
	CHECK("§5"),
	CREATED("§b"),
	SET("§a"),
	REMOVED("§c"),
	UPDATED("§e"),
	SAVED("§6"),
	LOADED("§9"),
	FAILED("§4§l");
	
    private final String text;

    LogType(final String text) {
        this.text = text;
    }

    public String getColor() {
        return text;
    }

}
