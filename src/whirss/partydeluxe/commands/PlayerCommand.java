package whirss.partydeluxe.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.partydeluxe.Main;
import whirss.partydeluxe.Shop;

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
					if(main.placeholderapi){
						sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.stats_title"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.stats_title")));
					}
					
					if(args.length > 1){
						String player = args[1];
						if(main.placeholderapi){
							sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.player_credits").replace("%player%", player).replace("%credits%", Integer.toString(main.getPlayerStats(player, "credits"))))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.player_credits").replace("%player%", player).replace("%credits%", Integer.toString(main.getPlayerStats(player, "credits")))));
						}
					}else{
						String player = p.getName();
						if(main.placeholderapi){
							sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.your_credits").replace("%credits%", Integer.toString(main.getPlayerStats(player, "credits"))))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.your_credits").replace("%credits%", Integer.toString(main.getPlayerStats(player, "credits")))));
						}
					}
				}else if(args[0].equalsIgnoreCase("leaderboards")){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.leaderboard_title")));
					if(args.length > 1){
						if(args[1].startsWith("credit")){
							main.outputLeaderboardsByCredits(p);
						}else if(args[1].startsWith("win")){
							main.outputLeaderboardsByWins(p);
						}else{
							sender.sendMessage(ChatColor.RED + "Use: /pd leaderboards [credits, wins]");
						}
					}else{
						main.outputLeaderboardsByCredits(p);
					}
				}else if(args[0].equalsIgnoreCase("leave")){
					if(main.players.contains(p.getName())){
						p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
						p.teleport(main.getLobby());
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								p.teleport(main.getLobby());
								p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
							}
						}, 20);
						p.getInventory().clear();
						p.updateInventory();
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								p.getInventory().setContents(main.pinv.get(p.getName()));
								p.updateInventory();
							}
						}, 10L);
						if(main.currentmg > -1){
							main.minigames.get(main.currentmg).leave(p);
						}
						main.players.remove(p.getName());
						if(main.placeholderapi){
							sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.you_left"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.you_left")));
						}
						
						if(main.players.size() < main.min_players){
							Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
								public void run(){
									main.stopFull(p);
									p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
								}
							}, 20);
						}
					}
				}else if(args[0].equalsIgnoreCase("join")){
					if(main.bungee) {
						if(main.placeholderapi){
							sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command")));
						}
					} else {
						if(main.players.contains(p.getName())){
							if(main.placeholderapi){
								sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.on_join"))));
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.on_join")));
							}
						}else{
							if(main.players.size() > main.max_players - 1){
								if(main.placeholderapi){
									sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.game_full"))));
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.game_full")));
								}
								return true;
							}
							main.players.add(p.getName());
							// if its the first player to join, start the whole minigame
							if(main.players.size() < main.min_players + 1){
								main.pinv.put(p.getName(), p.getInventory().getContents());
								main.startNew();
								if(main.min_players > 1){
									if(main.placeholderapi){
										sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.joined_queue").replace("%min_players%", Integer.toString(main.min_players)))));
									} else {
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.joined_queue").replace("%min_players%", Integer.toString(main.min_players))));
									}
									
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
								if(main.placeholderapi){
									sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.joined_queue").replace("%min_players%", Integer.toString(main.min_players)))));
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.joined_queue").replace("%min_players%", Integer.toString(main.min_players))));
								}
							}	
						}
					}
				}else if(args[0].equalsIgnoreCase("shop")){
					Shop.openShop(main, p.getName());
				}else if(args[0].equalsIgnoreCase("skip")){
					if(!sender.hasPermission("partydeluxe.skip")){
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
					if(main.placeholderapi){
						sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command")));
					}
				}
			}else{
				p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help");
		        p.sendMessage(ChatColor.GREEN + "/pd join " + ChatColor.WHITE + "Join a match");
		        p.sendMessage(ChatColor.GREEN + "/pd leave " + ChatColor.WHITE + "Leave match");
		        p.sendMessage(ChatColor.GREEN + "/pd stats [player] " + ChatColor.WHITE + "See a player statistics");
		        p.sendMessage(ChatColor.GREEN + "/pd leaderboards [wins|credits] " + ChatColor.WHITE + "See the Leaderboards");
		        if(sender.hasPermission("partydeluxe.admin.help") || sender.hasPermission("partydeluxe.admin.*")) {
		        	p.sendMessage(ChatColor.GREEN + "/pda help " + ChatColor.WHITE + "Help for admins");	
		        }
			}
			return true;
	}
}