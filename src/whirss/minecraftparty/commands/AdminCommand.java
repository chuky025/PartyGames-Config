package whirss.minecraftparty.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import whirss.minecraftparty.Main;
import whirss.minecraftparty.Minigame;

public class AdminCommand implements CommandExecutor {
	
	private Main main;
	
	public AdminCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to run this command.");
				return true;
			}
			final Player p = (Player)sender;

			if(args.length > 0){
				if(args[0].equalsIgnoreCase("setup")){
					// setup all arenas and spawns and lobbies and spectatorlobbies and what not
					if(p.hasPermission("minecraftparty.setup")){
						Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
							public void run(){
								main.setupAll(p.getLocation());
							}
						});
					}
				}else if(args[0].equalsIgnoreCase("setuppoint")){
					if(p.hasPermission("minecraftparty.setup")){
						final Location l = main.getComponentForMinigame("ColorMatch", "spawn");
						if(l != null){
							l.add(0.5, -2, 0.5);
							Bukkit.getScheduler().runTaskLater(main, new Runnable(){
								public void run(){
									p.setAllowFlight(true);
									p.setFlying(true);
									p.teleport(l);
								}
							}, 5L);
						}
					}
				
				}else if(args[0].equalsIgnoreCase("setlobby")){
					if(sender.hasPermission("minecraftparty.setlobby")){
						main.getConfig().set("lobby.world", p.getLocation().getWorld().getName());
						main.getConfig().set("lobby.location.x", p.getLocation().getBlockX());
						main.getConfig().set("lobby.location.y", p.getLocation().getBlockY());
						main.getConfig().set("lobby.location.z", p.getLocation().getBlockZ());
						main.saveConfig();
						p.sendMessage(ChatColor.GREEN + "Saved Main lobby.");	
					}
				}else if(args[0].equalsIgnoreCase("setcomponent")){
					// /mp setcomponent [minigame] [component]
					if(sender.hasPermission("minecraftparty.setup")){
						if(args.length > 2){
							main.saveComponentForMinigame(args[1], args[2], p.getLocation());
							p.sendMessage(ChatColor.GREEN + "Saved component");
						}
					}
				}else if(args[0].equalsIgnoreCase("stats")){
					sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Statistics " + ChatColor.DARK_AQUA + "--");
					if(args.length > 1){
						String player = args[1];
						sender.sendMessage(ChatColor.GREEN + player + " has " + Integer.toString(main.getPlayerStats(player, "credits")) + " Credits.");
					}else{
						String player = p.getName();
						sender.sendMessage(ChatColor.GREEN + "You have " + Integer.toString(main.getPlayerStats(player, "credits")) + " Credits.");
					}
				}else if(args[0].equalsIgnoreCase("list")){
					sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Minigames: " + ChatColor.DARK_AQUA + "--");
					for(Minigame m : main.minigames){
						if(m.isEnabled()){
							sender.sendMessage(ChatColor.GREEN + m.name);
						}else{
							sender.sendMessage(ChatColor.RED + m.name);
						}
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					main.reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "Successfully reloaded config.");
				}else if(args[0].equalsIgnoreCase("enable")){
					if(args.length > 1){
						if(sender.hasPermission("minecraftparty.enable")){
							main.enableMinigame(sender, args[1]);
						}
					}
				}else if(args[0].equalsIgnoreCase("disable")){
					if(args.length > 1){
						if(sender.hasPermission("minecraftparty.disable")){
							main.disableMinigame(sender, args[1]);
						}
					}
				}else{
					p.sendMessage(ChatColor.RED + "Unknown command");
				}
			}else{
				p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help");
		        p.sendMessage(ChatColor.GREEN + "/mpa setlobby " + ChatColor.WHITE + "Sets the server lobby.");
		        p.sendMessage(ChatColor.GREEN + "/mpa setup " + ChatColor.WHITE + "Sets the mini-games ");
		        p.sendMessage(ChatColor.GREEN + "/mpa enable/disable [game]" + ChatColor.WHITE + "Activate or deactivate a mini-game");
		        p.sendMessage(ChatColor.GREEN + "/mpa list" + ChatColor.WHITE + "See the list of minigames");
		        p.sendMessage(ChatColor.GREEN + "/mpa setcomponent [game] [component] " + ChatColor.WHITE + "See a player statistics");
		        p.sendMessage(ChatColor.GREEN + "/mpa reload" + ChatColor.WHITE + "Reload the plugin configuration");
			}
			return true;
	}

}
