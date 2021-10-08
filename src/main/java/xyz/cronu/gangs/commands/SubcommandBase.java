package xyz.cronu.gangs.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/*
 * I DID NOT CREATE THIS
 */

public abstract class SubcommandBase {
	public abstract String getName();
	public abstract String[] getAliases();
	public abstract String getSyntax();
	public abstract int requiredArgs();
	public abstract void perform(CommandSender sender, String[] args);
	public abstract List<String> getParameters(CommandSender sender, String[] args);
}
