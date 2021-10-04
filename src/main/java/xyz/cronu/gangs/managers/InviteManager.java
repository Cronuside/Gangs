package xyz.cronu.gangs.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.object.gang.Gang;
import xyz.cronu.gangs.object.gang.GangInvite;
import xyz.cronu.gangs.utils.Colorize;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Cronu
 * @project Gangs
 */

public class InviteManager {

	private Gangs plugin;
	private GangManager gangManager;

	public InviteManager(Gangs plugin){
		this.plugin = plugin;
		this.gangManager = plugin.getGangManager();
	}

	public void acceptInvite(Player sender, String gangName){
		Optional<Gang> gang = gangManager.getGangByName(gangName);
		if(!canAcceptInvite(sender, gangName, gang)) return;

		removeInvite(gang.get(), sender.getUniqueId());
		gang.get().addMember(sender);
		gang.get().messageMembers("&a" + sender.getName() + " has joined your gang!");

	}

	public void invitePlayer(Player sender, Player target){
		if(!gangManager.playerIsInGang(sender.getUniqueId())) return;
		if(gangManager.playerIsInGang(target.getUniqueId())) {
			Colorize.message(sender, "&cThis player is currently in a gang and can't be invited.");
			return; // Returns because the target is already in a gang
		}

		Optional<Gang> gang = gangManager.getPlayersGang(sender.getUniqueId());
		if(!gang.isPresent()) return;

		// Sending the player an invite.
		sendInvite(gang.get(), sender.getUniqueId(), target.getUniqueId());
		Colorize.message(target, "&aYou've been invited to join " + gang.get().getGangName() + ", use /gang join " + gang.get().getGangName() + ".");

		// Waiting 10 Seconds then checking if they still have an invite, if they do and haven't accepted then remove their invite.
		Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
			if(hasInvite(gang.get(), target.getUniqueId())){
				removeInvite(gang.get(), target.getUniqueId());
				Colorize.message(target, "&cYour invite to " + gang.get().getGangName() + " has expired!");
			}
		}, 20 * 10L);
	}

	public boolean canAcceptInvite(Player sender, String gangName, Optional<Gang> gang){
		if(gangManager.playerIsInGang(sender.getUniqueId())) {
			Colorize.message(sender, "&cYou're already in a gang, you can't accept this invite.");
			return false; // Returns because the sender is already in a gang
		}

		if(!gang.isPresent()) {
			Colorize.message(sender, "&cThis is an invalid gang.");
			return false; // Meaning invalid gang
		}

		if(!hasInvite(gang.get(), sender.getUniqueId())) {
			Colorize.message(sender, "&cYou don't have an invite to this gang.");
			return false; // If the player doesn't have an invite to the gang return.
		}
		return true;
	}

	public void removeInvite(Gang gang, UUID target){
		if(!hasInvite(gang, target)) return;
		Optional<GangInvite> invite = getInvite(gang, target);
		if(invite.isPresent())
			gang.getGangInvites().remove(invite.get());
	}

	public void sendInvite(Gang gang, UUID sender, UUID target){
		if(hasInvite(gang, target)) return;
		gang.getGangInvites().add(new GangInvite(target, sender));
	}

	public Optional<GangInvite> getInvite(Gang gang, UUID target){
		return gang.getGangInvites().stream().filter(invite -> invite.getGangInvitee().equals(target)).findAny();
	}

	public boolean hasInvite(Gang gang, UUID target){
		return gang.getGangInvites().stream().anyMatch(invite -> invite.getGangInvitee().equals(target));
	}

}
