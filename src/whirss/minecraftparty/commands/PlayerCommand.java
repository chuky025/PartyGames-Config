package whirss.minecraftparty.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import whirss.minecraftparty.Main;
import whirss.minecraftparty.Shop;

public class PlayerCommand implements CommandExecutor {

	private Main main;

	public PlayerCommand(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to run this command.");
				return true;
			}
			final Player p = (Player)sender;

			if(args.length > 0){
				if(args[0].equalsIgnoreCase("stats")){
					sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Statistics " + ChatColor.DARK_AQUA + "--");
					if(args.length > 1){
						String player = args[1];
						sender.sendMessage(ChatColor.GREEN + player + " has " + Integer.toString(main.getPlayerStats(player, "credits")) + " Credits.");
					}else{
						String player = p.getName();
						sender.sendMessage(ChatColor.GREEN + "You have " + Integer.toString(main.getPlayerStats(player, "credits")) + " Credits.");
					}
				}else if(args[0].equalsIgnoreCase("leaderboards")){
					sender.sendMessage(ChatColor.DARK_AQUA + "-- " + ChatColor.GOLD + "Leaderboards: " + ChatColor.DARK_AQUA + "--");
					if(args.length > 1){
						if(args[1].startsWith("credit")){
							main.outputLeaderboardsByCredits(p);
						}else if(args[1].startsWith("win")){
							main.outputLeaderboardsByWins(p);
						}else{
							sender.sendMessage(ChatColor.GREEN + "/mp leaderboards [credits/wins].");
						}
					}else{
						main.outputLeaderboardsByCredits(p);
					}
				}else if(args[0].equalsIgnoreCase("leave")){
					if(main.players.contains(p.getName())){
						p.teleport(main.getLobby());
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								p.teleport(main.getLobby());
							}
						}, 5);
						main.updateScoreboardOUTGAME(p.getName());
						p.getInventory().clear();
						p.updateInventory();
						Bukkit.getScheduler().runTaskLater(main, new Runnable(){
							public void run(){
								p.getInventory().setContents(main.pinv.get(p.getName()));
								p.updateInventory();
							}
						}, 10L);
						if(main.currentmg > -1){
							main.minigames.get(main.currentmg).leave(p);
						}
						main.players.remove(p.getName());
						p.sendMessage(ChatColor.RED + main.getConfig().getString("strings.you_left"));
						if(main.players.size() < main.min_players){
							Bukkit.getScheduler().runTaskLater(main, new Runnable(){
								public void run(){
									main.stopFull(p);
								}
							}, 15);
						}
					}
				}else if(args[0].equalsIgnoreCase("join")){
					if(main.players.contains(p.getName())){
						p.sendMessage(ChatColor.GOLD + "Use /mp leave to leave!");
					}else{
						if(main.players.size() > main.getConfig().getInt("config.max_players") - 1){
							p.sendMessage(ChatColor.RED + "You can't join because the minigames party is full!");
							return true;
						}
						main.players.add(p.getName());
						// if its the first player to join, start the whole minigame
						if(main.players.size() < main.min_players + 1){
							main.pinv.put(p.getName(), p.getInventory().getContents());
							main.startNew();
							if(main.min_players > 1){
								p.sendMessage(ChatColor.GREEN + "You joined the queue. There are " + ChatColor.GOLD + Integer.toString(main.min_players) + ChatColor.GREEN + " players needed to start.");
							}
						}else{ // else: just join the minigame
							try{
								main.pinv.put(p.getName(), p.getInventory().getContents());
								if(main.ingame_started){
									main.minigames.get(main.currentmg).lost.add(p);
									main.minigames.get(main.currentmg).spectate(p);
								}else{
									main.minigames.get(main.currentmg).join(p);
								}
							}catch(Exception e){}
							p.sendMessage(ChatColor.GREEN + "You joined the queue. There are " + ChatColor.GOLD + Integer.toString(main.min_players) + ChatColor.GREEN + " players needed to start.");
						}	
					}
				}else if(args[0].equalsIgnoreCase("shop")){
					Shop.openShop(main, p.getName());
				}else if(args[0].equalsIgnoreCase("skip")){
					if(!sender.hasPermission("minecraftparty.skip")){
						return true;
					}
					if(main.currentmg > -1){
						main.c_ += main.seconds-main.c;
						main.c = main.seconds;
					}
					if(args.length > 1){
						String count = args[1];
						main.currentmg += Integer.parseInt(count) - 1;
						main.minigames.get(main.currentmg).join(p);
					}
				}else{
					p.sendMessage(ChatColor.RED + "Unknown command");
				}
			}else{
				p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help");
		        p.sendMessage(ChatColor.GREEN + "/mp join " + ChatColor.WHITE + "Join a match");
		        p.sendMessage(ChatColor.GREEN + "/mp leave " + ChatColor.WHITE + "Leave match");
		        p.sendMessage(ChatColor.GREEN + "/mp stats [player] " + ChatColor.WHITE + "See a player statistics");
		        p.sendMessage(ChatColor.GREEN + "/mp leaderboards [wins|credits] " + ChatColor.WHITE + "See the Leaderboards");
		        p.sendMessage(ChatColor.GREEN + "/mp adminhelp " + ChatColor.WHITE + "Help for admins");
			}
			return true;
	}
}
