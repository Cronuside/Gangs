package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.Gangs;
import xyz.cronu.gangs.commands.SubcommandBase;
import xyz.cronu.gangs.object.gang.Gang;
import xyz.cronu.gangs.utils.Colorize;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GangTopCMD extends SubcommandBase {
	@Override
	public String getName() {
		return "top";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getSyntax() {
		return "/gang top";
	}

	@Override
	public int requiredArgs() {
		return 1;
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("Only players can execute commands.");
			return;
		}

		Player player = (Player) sender;
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

		Optional<Gang> gang = Gangs.getPlugin().getGangManager().getGangByMember(player.getUniqueId());
		if(!gang.isPresent()){
			Colorize.message(player, "&cYou're not in a gang!");
			return;
		}

		// TODO: Top stats menu or something...

	}

	@Override
	public List<String> getParameters(CommandSender sender, String[] args) {
		return new ArrayList<>();
	}
}
