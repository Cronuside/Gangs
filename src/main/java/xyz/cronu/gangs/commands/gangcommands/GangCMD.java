package xyz.cronu.gangs.commands.gangcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.cronu.gangs.commands.CommandBase;
import xyz.cronu.gangs.commands.SubcommandBase;

import java.util.Arrays;
import java.util.List;

public class GangCMD extends CommandBase {
	@Override
	public String getName() {
		return "gang";
	}

	@Override
	public List<SubcommandBase> getSubcommands() {
		return Arrays.asList(
						new GangKickCMD(),
						new GangPromoteCMD(),
						new GangDemoteCMD(),
						new GangTopCMD(),
						new GangLeaveCMD(),
						new GangJoinCMD(),
						new GangTransferCMD(),
						new GangInfoCMD(),
						new GangFlexCMD(),
						new GangDisbandCMD(),
						new GangInviteCMD()
				);
	}

	@Override
	public void perform(CommandSender sender, Command command, String label, String[] args) {

	}
}
