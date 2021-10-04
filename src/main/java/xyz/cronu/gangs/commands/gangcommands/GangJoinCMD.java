package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.commands.SubcommandBase;
import xyz.cronu.gangs.utils.Colorize;

import java.util.ArrayList;
import java.util.List;

public class GangJoinCMD extends SubcommandBase {
	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getSyntax() {
		return "/gang join <gang>";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 2){

				String gangName = args[1];
				Gangs.getPlugin().getInviteManager().acceptInvite(player, gangName);

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
