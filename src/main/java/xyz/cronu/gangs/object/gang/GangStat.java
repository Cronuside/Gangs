package xyz.cronu.gangs.object.gang;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Cronu
 * @project Gangs
 */

public class GangStat {

	private long gangBlocksMined;
	private long gangLevel;
	private long gangPrestige;

	public GangStat(long gangBlocksMined, long gangLevel, long gangPrestige) {
		this.gangBlocksMined = gangBlocksMined;
		this.gangLevel = gangLevel;
		this.gangPrestige = gangPrestige;
	}

	public GangStat(ConfigurationSection configurationSection, String gangName) {
		this.gangBlocksMined = configurationSection.getLong(gangName + ".blocks_mined");
		this.gangLevel = configurationSection.getLong(gangName + ".level");
		this.gangPrestige = configurationSection.getLong(gangName + ".prestige");
	}

	public void setGangBlocksMined(long gangBlocksMined) {
		this.gangBlocksMined = gangBlocksMined;
	}

	public void setGangLevel(long gangLevel) {
		this.gangLevel = gangLevel;
	}

	public void setGangPrestige(long gangPrestige) {
		this.gangPrestige = gangPrestige;
	}

	public long getGangBlocksMined() {
		return gangBlocksMined;
	}

	public long getGangLevel() {
		return gangLevel;
	}

	public long getGangPrestige() {
		return gangPrestige;
	}
}
