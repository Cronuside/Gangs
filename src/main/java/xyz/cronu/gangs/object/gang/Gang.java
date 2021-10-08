package xyz.cronu.gangs.object.gang;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.managers.GangManager;
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
	private GangManager gangManager;

	public Gang(String gangName, UUID gangOwner, List<GangMember> gangMembers, GangStat gangStat) {
		this.gangName = gangName;
		this.gangOwner = gangOwner;
		this.gangMembers = gangMembers;
		this.gangStat = gangStat;
		this.gangInvites = new ArrayList<>();
		this.gangManager = Gangs.getPlugin().getGangManager();
	}


	/*
		The sender is a player who has received an invite to the gang,
		this method will run some checks to ensure that they can actually
		join the gang, if so, it'll add them to the gang and send relevant messages.
	 */

	public void acceptInvite(Player sender){
		if(isGangMember(sender.getUniqueId()) || gangManager.doesPlayerHaveGang(sender.getUniqueId())) {
			Colorize.message(sender, "&cYou're already in a gang!");
			return;
		}

		if(!hasInvite(sender.getUniqueId())){
			Colorize.message(sender, "&cYou do not have an invite to join this gang!");
			return;
		}

		removeGangInvite(sender.getUniqueId());
		addPlayerToGang(sender.getUniqueId());
		sendMessageToAllMembers("&a" + sender.getName() + " has successfully joined " + getGangName() + "!");
	}

	/*
		The sender and the target must be online to be invited
		due to expiration feature of invites.

		Also Doesn't make sense to invite an offline player...
	 */
	public void sendInvite(Player target, Player sender){
		if(isGangMember(target.getUniqueId()) || gangManager.doesPlayerHaveGang(target.getUniqueId())) {
			Colorize.message(sender, "&cThis player is already in a gang!");
			return;
		}

		if(hasInvite(target.getUniqueId())){
			Colorize.message(sender, "&cThis player already has an invite please wait for them to respond!");
			return;
		}

		Optional<GangMember> gangMember = getGangMember(sender.getUniqueId());
		if(!gangMember.isPresent()) return;

		if(!gangMember.get().hasAllPermissions() || !gangMember.get().hasInvitePermission()){
			Colorize.message(sender, "&cYou do not have permission to invite others!");
			return;
		}


		Colorize.message(target, "&aYou've been invited to join " + getGangName() + ", use /gang join " + getGangName() + "!");
		Colorize.message(sender, "&aYou've successfully invited " + target.getName() + " to join your gang!");
		addGangInvite(sender.getUniqueId(), target.getUniqueId());

		Bukkit.getScheduler().runTaskLaterAsynchronously(Gangs.getPlugin(), () -> {
			if(hasInvite(target.getUniqueId())){
				removeGangInvite(target.getUniqueId());
				Colorize.message(target, "&cYour invite to " + getGangName() + " has expired because you took too long!");
			}
		}, 20L * 10L);


	}

	/*
		Creates a new instance of a GangMember and adds them into the gang.
		After doing this, it saves the gang data to config so that everything
		is updated as it should be.
	 */
	public void addPlayerToGang(UUID uuid){
		gangMembers.add(new GangMember(
				uuid,
				GangRank.RECRUIT,
				Bukkit.getOfflinePlayer(uuid).getName(),
				new ArrayList<>(Arrays.asList(
						new Perk(PerkType.POINT, 1),
						new Perk(PerkType.BLOCK, 1),
						new Perk(PerkType.MONEY, 1)
				)),
				new ArrayList<>(),
				0L,
				0L
		));
		gangManager.saveGang(this);
	}

	/*
		Finds the instance of target and sender Gang Members
		and attempts to kick the target if their member instance
		is located.
	 */
	public void kickMemberFromGang(UUID target, UUID sender){
		if(!isGangMember(target)) {
			Colorize.message(sender, "&cThis player is not in your gang!");
			return;
		}

		Optional<GangMember> targetMember = getGangMember(target);
		Optional<GangMember> senderMember = getGangMember(sender);
		if(!targetMember.isPresent() || !senderMember.isPresent()) return;

		if(!senderMember.get().hasAllPermissions() || !senderMember.get().hasKickPermission()){
			Colorize.message(sender, "&cYou do not have permission to do this!");
			return;
		}


		Colorize.message(sender, "&aYou've successfully kicked this player from your gang!");
		sendMessageToAllMembers("&a" + targetMember.get().getMemberName() + " has been kicked from your gang!");
		gangMembers.remove(targetMember.get());

		gangManager.saveGang(this);

	}

	/*
		Attempts to remove the sender from the gang.
	 */
	public void leaveGang(UUID sender){
		Optional<GangMember> senderMember = getGangMember(sender);
		if(!senderMember.isPresent()) return;

		if(senderMember.get().getGangRank() == GangRank.LEADER && gangMembers.size() > 1){
			Colorize.message(sender, "&cYou cannot leave the gang as you're leader.");
		} else if(gangMembers.size() == 1){
			disbandGang();
		} else {
			sendMessageToAllMembers("&a" + senderMember.get().getMemberName() + " has left the gang!");
			gangMembers.remove(senderMember.get());
			gangManager.saveGang(this);
		}
	}

	/*
		Deletes the gang entirely from the server,
		this can never be undone.
	 */
	public void disbandGang(){
		Colorize.broadcast("&a" + getGangName() + " has been disbanded!");
		gangManager.removeGang(this);
	}

	public void sendMessageToAllMembers(String message){
		gangMembers.stream().filter(gangMember -> Bukkit.getPlayer(gangMember.getMember()) != null).forEach(gangMember -> Colorize.message(gangMember.getMember(), message));
	}

	public Optional<GangInvite> getGangInvite(UUID uuid){
		return gangInvites.stream().filter(invite -> invite.getGangInvitee().equals(uuid)).findAny();
	}

	public Optional<GangMember> getGangMember(UUID uuid){
		return gangMembers.stream().filter(gangMember -> gangMember.getMember().equals(uuid)).findAny();
	}

	public boolean isGangMember(UUID uuid){
		return getGangMember(uuid).isPresent();
	}

	public boolean hasInvite(UUID uuid){
		return getGangInvite(uuid).isPresent();
	}

	public void addGangInvite(UUID sender, UUID target){
		gangInvites.add(new GangInvite(target, sender));
	}

	public void removeGangInvite(UUID target){
		if(!hasInvite(target) || !getGangInvite(target).isPresent()) return;
		gangInvites.remove(getGangInvite(target).get());
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
