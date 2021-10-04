package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.commands.SubcommandBase;

import java.util.ArrayList;
import java.util.List;

public class GangCreateCMD extends SubcommandBase {
	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getSyntax() {
		return "/gang create <gang>";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 2){

				String gangName = args[1];
				Gangs.getPlugin().getGangManager().createNewGang(player, gangName);

			} else {

			}
		}
	}

	@Override
	public List<String> getParameters(CommandSender sender, String[] args) {
		return new ArrayList<>();
	}
}
