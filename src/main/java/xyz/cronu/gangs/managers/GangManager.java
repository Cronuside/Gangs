package xyz.cronu.gangs.managers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.config.ConfigManager;
import xyz.cronu.gangs.object.gang.*;
import xyz.cronu.gangs.object.perk.Perk;
import xyz.cronu.gangs.object.perk.PerkType;
import xyz.cronu.gangs.utils.Colorize;

import java.util.*;

/**
 * @author Cronu
 * @project Gangs
 */

public class GangManager {

	public static List<Gang> gangList = new ArrayList<>();
	private ConfigManager configManager;
	private FileConfiguration gangsConfig;
	private InviteManager inviteManager;
	private Gangs plugin;

	public GangManager(Gangs plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.inviteManager = plugin.getInviteManager();
		this.gangsConfig = plugin.getConfigManager().getConfig("gangs.yml");
	}

	public void createNewGang(Player initiator, String gangName){
		if(!canCreateGang(initiator, gangName)) return;

		List<Perk> perkList = new ArrayList<>(Arrays.asList(new Perk(PerkType.BLOCK, 1), new Perk(PerkType.MONEY, 1), new Perk(PerkType.POINT, 1)));

		GangMember gangMember = new GangMember(
				initiator.getUniqueId(), GangRank.LEADER, initiator.getName(),
				perkList,
				new ArrayList<>(Collections.singletonList(GangPermissions.ALL)),
				1,
				1
		);


		Gang gang = new Gang(gangName, initiator.getUniqueId(), new ArrayList<>(Collections.singletonList(gangMember)), new GangStat(0,1, 0));
		saveGang(gang);
		gangList.add(gang);

		Colorize.message(initiator, "&aYou've successfully created a gang called " + gangName + ".");
		Colorize.broadcast("&a" + initiator.getName() + " has created the gang " + gangName + "!");

	}

	public Optional<Gang> getGangByName(String gangName){
		return gangList.stream().filter(gang -> gang.getGangName().equalsIgnoreCase(gangName)).findAny();
	}

	public Optional<Gang> getPlayersGang(UUID player){
		return gangList.stream().filter(gang -> gang.getGangMembers().stream().anyMatch(member -> member.getMember().equals(player))).findAny();
	}

	public void disbandGang(Gang gang){
		configManager.setData(this.gangsConfig, "gangs." + gang.getGangName(), null);
	}

	public void removeGang(Gang gang){
		if(gangList == null || gangList.isEmpty()) return;
		if(!gangList.contains(gang)) return;
		gangList.remove(gang);
	}

	//<editor-fold desc="Boolean Functionality">
	public boolean canCreateGang(Player initiator, String gangName){
		if(playerIsInGang(initiator.getUniqueId())) {
			Colorize.message(initiator, "&cYou're currently in a gang.");
			return false;
		}
		if(gangAlreadyExists(gangName)) {
			Colorize.message(initiator, "&cThis gang already exists.");
			return false;
		}

		if(!StringUtils.isAlpha(gangName)) {
			Colorize.message(initiator, "&cThe Gang Name must only contain letters.");
			return false;
		}

		if(gangName.length() < 4 || gangName.length() > 12) {
			Colorize.message(initiator, "&cThe gang name must be between 4 - 12 characters.");
			return false;
		}

		return true;
	}

	public boolean gangAlreadyExists(String gangName){
		return gangList.stream().anyMatch(gang -> gang.getGangName().equalsIgnoreCase(gangName));
	}

	public boolean playerIsInGang(UUID uuid){
		for(Gang gang : gangList){
			for(GangMember gangMember : gang.getGangMembers()){
				if(gangMember.getMember().equals(uuid)) return true;
			}
		}
		return false;
	}
	//</editor-fold>
	//<editor-fold desc="Saving & Loading Functionality">
	public void saveGang(Gang gang) {
		if (this.gangsConfig == null) return;
		String gangPath = "gangs." + gang.getGangName() + ".";

		configManager.setData(this.gangsConfig, gangPath + "owner", gang.getGangOwner().toString());
		configManager.setData(this.gangsConfig, gangPath + "blocks_mined", gang.getGangStat().getGangBlocksMined());
		configManager.setData(this.gangsConfig, gangPath + "level", gang.getGangStat().getGangLevel());
		configManager.setData(this.gangsConfig, gangPath + "prestige", gang.getGangStat().getGangPrestige());

		for(GangMember gangMember : gang.getGangMembers()){

			String memberPath = gangPath + "members." + gangMember.getMember().toString() + ".";
			configManager.setData(this.gangsConfig, memberPath + "name", gangMember.getMemberName());
			configManager.setData(this.gangsConfig, memberPath + "blocks_mined", gangMember.getBlocksMined());
			configManager.setData(this.gangsConfig, memberPath + "points", gangMember.getPoints());
			configManager.setData(this.gangsConfig, memberPath + "rank", gangMember.getGangRank().name());

			for(Perk perk : gangMember.getMemberPerks()){
				String memberPerkPath = memberPath + "perks." + perk.getPerkType().name() + ".";
				configManager.setData(this.gangsConfig, memberPerkPath + "multiplier", perk.getMultiplier());
			}


			List<String> permissions = new ArrayList<>();
			for(GangPermissions permission : gangMember.getMemberPermissions()){
				permissions.add(permission.name().toUpperCase());
			}

			configManager.setData(this.gangsConfig, memberPath + "permissions", permissions);

		}

	}

	public void loadGangs() {
		if(this.gangsConfig == null) return;
		ConfigurationSection gangSection = gangsConfig.getConfigurationSection("gangs");
		if(gangSection == null) return;

		for (String gangName : gangSection.getKeys(false)) {

			System.out.println(gangName);

			ConfigurationSection memberSection = gangSection.getConfigurationSection(gangName + ".members"); // Gets the members section within the gang.
			if(memberSection == null) return;
			List<GangMember> members = new ArrayList<>();

			for (String memberUUID : memberSection.getKeys(false)) {

				System.out.println(memberUUID);

				ConfigurationSection memberPerkSection = memberSection.getConfigurationSection(memberUUID + ".perks"); // Gets the perk section from the member.
				if(memberPerkSection == null) return;
				List<Perk> memberPerks = new ArrayList<>();

				// Loops through all perks and creates an instance of Perk and adds it to the memberPerks array list.
				memberPerkSection.getKeys(false).forEach(perk -> memberPerks.add(new Perk(PerkType.valueOf(perk), memberPerkSection.getDouble(perk + ".multiplier"))));

				List<GangPermissions> memberPermissions = new ArrayList<>();
				for(String permission : memberSection.getStringList(memberUUID + ".permissions")){
					System.out.println(permission);
					memberPermissions.add(GangPermissions.valueOf(permission.toUpperCase()));
				}

				// Creating an instance of the gang member
				GangMember gangMember = new GangMember(
						UUID.fromString(memberUUID),
						GangRank.valueOf(memberSection.getString(memberUUID + ".rank").toUpperCase()),
						Bukkit.getOfflinePlayer(UUID.fromString(memberUUID)).getName(),
						memberPerks,
						memberPermissions,
						memberSection.getLong(memberUUID + ".blocks_mined"),
						memberSection.getLong(memberUUID + ".points")
				);

				members.add(gangMember);

			}

			Gang gang = new Gang(
					gangName,
					UUID.fromString(gangSection.getString(gangName + ".owner")),
					members,
					new GangStat(gangSection, gangName)
			);

			gangList.add(gang);

		}
	}

	public void saveGangs() {
		gangList.forEach(this::saveGang);
	}
	//</editor-fold>

}
