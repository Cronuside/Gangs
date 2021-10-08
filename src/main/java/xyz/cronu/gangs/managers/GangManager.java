package xyz.cronu.gangs.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.config.ConfigManager;
import xyz.cronu.gangs.object.gang.*;
import xyz.cronu.gangs.object.perk.Perk;
import xyz.cronu.gangs.object.perk.PerkType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Cronu
 * @project Gangs
 */

public class GangManager {

	public static HashMap<Gang, List<UUID>> gangMap = new HashMap<>();
	private ConfigManager configManager;
	private FileConfiguration gangsConfig;
	private Gangs plugin;

	public GangManager(Gangs plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.gangsConfig = plugin.getConfigManager().getConfig("gangs.yml");
	}

	public void createGang() {
		for (int i = 0; i < 100; i++) {
			List<GangMember> memberlist = new ArrayList<>();
			List<UUID> memberUUIDList = new ArrayList<>();

			for (int x = 0; x < 5; x++) {
				int random = ThreadLocalRandom.current().nextInt(1000000);
				UUID uuid = UUID.randomUUID();
				memberUUIDList.add(uuid);
				memberlist.add(new GangMember(
						uuid,
						GangRank.RECRUIT,
						UUID.randomUUID().toString(),
						new ArrayList<>(),
						new ArrayList<>(),
						random,
						random
				));
			}

			int random = ThreadLocalRandom.current().nextInt(1000000);

			Gang gang = new Gang(UUID.randomUUID().toString(),
					UUID.randomUUID(),
					memberlist,
					new GangStat(random, random, random));

			gangMap.put(gang, memberUUIDList);
		}

		saveGangs();

		LinkedHashMap<Gang, Long> top = getGangTop(getAllGangBlocks());

		for (Gang gang : top.keySet()) {
			System.out.println(gang.getGangName() + " " + top.get(gang));
		}

	}

	/*
		Returns a map of sorted values for gangs block top
	 */
	public LinkedHashMap<Gang, Long> getGangTop(HashMap<Gang, Long> topType) {
		return topType
				.entrySet()
				.stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	public HashMap<Gang, Long> getAllGangPrestiges() {
		HashMap<Gang, Long> gangPrestiges = new HashMap<>();
		gangMap.keySet().forEach(gang -> gangPrestiges.put(gang, gang.getGangStat().getGangLevel()));
		return gangPrestiges;
	}

	public HashMap<Gang, Long> getAllGangLevels() {
		HashMap<Gang, Long> gangLevels = new HashMap<>();
		gangMap.keySet().forEach(gang -> gangLevels.put(gang, gang.getGangStat().getGangLevel()));
		return gangLevels;
	}

	public HashMap<Gang, Long> getAllGangBlocks() {
		HashMap<Gang, Long> gangBlocks = new HashMap<>();
		gangMap.keySet().forEach(gang -> gangBlocks.put(gang, gang.getTotalBlocksBrokenMembers()));
		return gangBlocks;
	}

	public HashMap<Gang, GangStat> getAllGangStats() {
		HashMap<Gang, GangStat> gangStats = new HashMap<>();
		gangMap.keySet().forEach(gang -> gangStats.put(gang, gang.getGangStat()));
		return gangStats;
	}

	public Optional<Gang> getGangByName(String gangName) {
		return gangMap.keySet().stream().filter(gang -> gang.getGangName().equalsIgnoreCase(gangName)).findAny();
	}

	public Optional<Gang> getGangByMember(UUID uuid) {
		return gangMap.keySet().stream().filter(gang -> gangMap.get(gang).contains(uuid)).findAny();
	}

	public boolean doesGangExist(String gangName) {
		return gangMap.keySet().stream().anyMatch(gang -> gang.getGangName().equalsIgnoreCase(gangName));
	}

	public boolean doesPlayerHaveGang(UUID uuid) {
		return gangMap.keySet().stream().anyMatch(gang -> gangMap.get(gang).contains(uuid));
	}

	//<editor-fold desc="Saving & Loading Functionality">
	public void saveGang(Gang gang) {
		if (this.gangsConfig == null) return;
		String gangPath = "gangs." + gang.getGangName() + ".";

		configManager.setData(this.gangsConfig, gangPath + "owner", gang.getGangOwner().toString());
		configManager.setData(this.gangsConfig, gangPath + "blocks_mined", gang.getGangStat().getGangBlocksMined());
		configManager.setData(this.gangsConfig, gangPath + "level", gang.getGangStat().getGangLevel());
		configManager.setData(this.gangsConfig, gangPath + "prestige", gang.getGangStat().getGangPrestige());

		for (GangMember gangMember : gang.getGangMembers()) {

			String memberPath = gangPath + "members." + gangMember.getMember().toString() + ".";
			configManager.setData(this.gangsConfig, memberPath + "name", gangMember.getMemberName());
			configManager.setData(this.gangsConfig, memberPath + "blocks_mined", gangMember.getBlocksMined());
			configManager.setData(this.gangsConfig, memberPath + "points", gangMember.getPoints());
			configManager.setData(this.gangsConfig, memberPath + "rank", gangMember.getGangRank().name());

			for (Perk perk : gangMember.getMemberPerks()) {
				String memberPerkPath = memberPath + "perks." + perk.getPerkType().name() + ".";
				configManager.setData(this.gangsConfig, memberPerkPath + "multiplier", perk.getMultiplier());
			}


			List<String> permissions = new ArrayList<>();
			for (GangPermissions permission : gangMember.getMemberPermissions()) {
				permissions.add(permission.name().toUpperCase());
			}

			configManager.setData(this.gangsConfig, memberPath + "permissions", permissions);

		}

	}

	public void loadGangs() {
		if (this.gangsConfig == null) return;
		ConfigurationSection gangSection = gangsConfig.getConfigurationSection("gangs");
		if (gangSection == null) return;

		for (String gangName : gangSection.getKeys(false)) {

			ConfigurationSection memberSection = gangSection.getConfigurationSection(gangName + ".members"); // Gets the members section within the gang.
			if (memberSection == null) return;
			List<GangMember> members = new ArrayList<>();
			List<UUID> memberUUIDList = new ArrayList<>();

			for (String memberUUID : memberSection.getKeys(false)) {

				memberUUIDList.add(UUID.fromString(memberUUID));
				ConfigurationSection memberPerkSection = memberSection.getConfigurationSection(memberUUID + ".perks"); // Gets the perk section from the member.
				if (memberPerkSection == null) return;
				List<Perk> memberPerks = new ArrayList<>();

				// Loops through all perks and creates an instance of Perk and adds it to the memberPerks array list.
				memberPerkSection.getKeys(false).forEach(perk -> memberPerks.add(new Perk(PerkType.valueOf(perk), memberPerkSection.getDouble(perk + ".multiplier"))));

				List<GangPermissions> memberPermissions = new ArrayList<>();
				for (String permission : memberSection.getStringList(memberUUID + ".permissions")) {
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

			gangMap.put(gang, memberUUIDList);

		}
	}

	public void removeGang(Gang gang) {
		gangMap.remove(gang);
		configManager.setData(gangsConfig, gang.getGangName(), null);
	}

	public void saveGangs() {
		gangMap.keySet().forEach(this::saveGang);
	}
	//</editor-fold>

}
