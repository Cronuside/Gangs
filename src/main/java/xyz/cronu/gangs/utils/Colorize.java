package xyz.cronu.gangs.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Cronu
 * @project Gangs
 */

public class Colorize {

	public static void broadcast(String message){
		Bukkit.getServer().broadcastMessage(text(message));
	}

	public static String text(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void message(UUID target, String message){
		Objects.requireNonNull(Bukkit.getPlayer(target)).sendMessage(text(message));
	}

	public static void message(Player target, String message){
		target.sendMessage(text(message));
	}

}
