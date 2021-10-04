package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.commands.SubcommandBase;
import xyz.cronu.gangs.utils.Colorize;

import java.util.ArrayList;
import java.util.List;

public class GangInviteCMD extends SubcommandBase {
	@Override
	public String getName() {
		return "invite";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getSyntax() {
		return "/gang invite <player>";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 2){

				Player target = Bukkit.getPlayerExact(args[1]);

				if(target == null) {
					Colorize.message(player, "&cThe target player is invalid!");
					return; // TODO: Get Config Message
				}

				Gangs.getPlugin().getInviteManager().invitePlayer(player, target);


			} else {
				Colorize.message(player, getSyntax());
			}
		}
	}

	@Override
	public List<String> getParameters(CommandSender sender, String[] args) {
		return new ArrayList<>();
	}
}
