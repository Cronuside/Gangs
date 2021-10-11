package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cronu.gangs.commands.SubcommandBase;
import xyz.cronu.gangs.menus.GangTopMenu;
import xyz.cronu.gangs.object.gang.GangTopType;

import java.util.ArrayList;
import java.util.List;

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
		return "/gang top <type>";
	}

	@Override
	public int requiredArgs() {
		return 2;
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("Only players can execute commands.");
			return;
		}

		Player player = (Player) sender;
		String type = args[1];

		new GangTopMenu(player, GangTopType.valueOf(type.toUpperCase())).open();

	}

	@Override
	public List<String> getParameters(CommandSender sender, String[] args) {
		return new ArrayList<>();
	}
}
