package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.cronu.gangs.commands.CommandBase;
import xyz.cronu.gangs.commands.SubcommandBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GangCMD extends CommandBase {
	@Override
	public String getName() {
		return "gang";
	}

	@Override
	public List<SubcommandBase> getSubcommands() {
		return new ArrayList<>(Arrays.asList(
				new GangInviteCMD(),
				new GangJoinCMD(),
				new GangCreateCMD()
		));
	}

	@Override
	public void perform(CommandSender sender, Command command, String label, String[] args) {

	}
}
