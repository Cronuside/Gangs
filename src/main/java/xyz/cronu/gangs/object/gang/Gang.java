package xyz.cronu.gangs.object.gang;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.object.perk.Perk;
import xyz.cronu.gangs.object.perk.PerkType;
import xyz.cronu.gangs.utils.Colorize;

import java.util.*;

/**
 * @author Cronu
 * @project Gangs
 */

public class Gang {

	private String gangName;
	private UUID gangOwner;
	private List<GangMember> gangMembers;
	private List<GangInvite> gangInvites;
	private GangStat gangStat;

	public Gang(String gangName, UUID gangOwner, List<GangMember> gangMembers, GangStat gangStat) {
		this.gangName = gangName;
		this.gangOwner = gangOwner;
		this.gangMembers = gangMembers;
		this.gangStat = gangStat;
		this.gangInvites = new ArrayList<>();
	}

	public void messageMembers(String message) {
		gangMembers.stream().filter(member -> Bukkit.getPlayer(member.getMember()) != null).forEach(member -> Colorize.message(Bukkit.getPlayer(member.getMember()), message));
	}


	public void addMember(Player target) {
		List<Perk> perkList = new ArrayList<>(Arrays.asList(new Perk(PerkType.BLOCK, 1), new Perk(PerkType.MONEY, 1), new Perk(PerkType.POINT, 1)));

		GangMember gangMember = new GangMember(
				target.getUniqueId(), GangRank.RECRUIT, target.getName(),
				perkList,
				new ArrayList<>(),
				1,
				1
		);

		gangMembers.add(gangMember);
	}

	public void removeMember(UUID target) {
		if (!isInGang(target)) return;
		if (getGangMember(target).isPresent()) {
			if(getGangMembers().size() == 1) disband();
			else getGangMembers().remove(getGangMember(target).get());
		}
	}

	public Optional<GangMember> getGangMember(UUID target) {
		return getGangMembers().stream().filter(gangMember -> gangMember.getMember().equals(target)).findAny();
	}

	public boolean isInGang(UUID target) {
		for (GangMember gangMember : getGangMembers()) {
			if (gangMember.getMember().equals(target)) return true;
		}
		return false;
	}

	public void disband(){
		Gangs.getPlugin().getGangManager().disbandGang(this);
		Gangs.getPlugin().getGangManager().removeGang(this);

		Colorize.broadcast("&a" + getGangName() + " has been disbanded!");
	}

	public void setGangInvites(List<GangInvite> gangInvites) {
		this.gangInvites = gangInvites;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public void setGangOwner(UUID gangOwner) {
		this.gangOwner = gangOwner;
	}

	public void setGangMembers(List<GangMember> gangMembers) {
		this.gangMembers = gangMembers;
	}

	public void setGangStat(GangStat gangStat) {
		this.gangStat = gangStat;
	}

	public List<GangInvite> getGangInvites() {
		return gangInvites;
	}

	public String getGangName() {
		return gangName;
	}

	public UUID getGangOwner() {
		return gangOwner;
	}

	public List<GangMember> getGangMembers() {
		return gangMembers;
	}

	public GangStat getGangStat() {
		return gangStat;
	}
}
