package xyz.cronu.gangs;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.cronu.gangs.commands.CommandBase;
import xyz.cronu.gangs.commands.gangcommands.GangCMD;
import xyz.cronu.gangs.config.ConfigManager;
import xyz.cronu.gangs.managers.GangManager;
import xyz.cronu.gangs.managers.InviteManager;

import java.util.Arrays;

public final class Gangs extends JavaPlugin {

	private static Gangs plugin;
	private static GangManager gangManager;
	private static ConfigManager configManager;
	private static InviteManager inviteManager;

	@Override
	public void onEnable() {
		plugin = this;

		ConfigManager.getInstance().setPlugin(this);
		configManager = ConfigManager.getInstance();
		gangManager = new GangManager(this);
		inviteManager = new InviteManager(this);

		initializeCommands(new GangCMD());
		gangManager.loadGangs();

		loadedMessage();
	}

	@Override
	public void onDisable() {
		gangManager.saveGangs();
	}

	public void loadedMessage(){
		System.out.println("--------- GANGS 1.0 ---------");
		System.out.println("");
		System.out.println(plugin.getName() + " loaded using " + plugin.getServer().getVersion());
		System.out.println("Developed by: Cronu#0646");
		System.out.println("");
		System.out.println("--------- GANGS 1.0 ---------");
	}

	public void initializeCommands(CommandBase... commands){
		Arrays.stream(commands).forEach(command -> getCommand(command.getName()).setExecutor(command));
	}

	public static Gangs getPlugin() {
		return plugin;
	}

	public InviteManager getInviteManager() {
		return inviteManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public GangManager getGangManager() {
		return gangManager;
	}
}
