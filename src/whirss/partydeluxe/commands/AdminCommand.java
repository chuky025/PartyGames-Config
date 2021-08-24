package whirss.partydeluxe.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.partydeluxe.Main;
import whirss.partydeluxe.Minigame;

public class AdminCommand implements CommandExecutor {
	
	private Main main;
	
	public AdminCommand(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to run this command.");
				return true;
			}
			final Player p = (Player)sender;

			if(args.length > 0){
				if(args[0].equalsIgnoreCase("setup")){
					if(p.hasPermission("partydeluxe.admin.setup") || sender.hasPermission("partydeluxe.admin.*")){
						if (args.length > 1) {
							if (args[1].equalsIgnoreCase("colormatch")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "ColorMatch"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "ColorMatch")));
										}
										main.setupColorMatch(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "ColorMatch"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "ColorMatch")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("spleef")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "Spleef"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "Spleef")));
										}
										main.setupSpleef(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "Spleef"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "Spleef")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("minefield")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "MineField"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "MineField")));
										}
										main.setupMineField(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "MineField"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "MineField")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("jumpnrun")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "JumpnRun"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "JumpnRun")));
										}
										main.setupJumpnRun(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "JumpnRun"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "JumpnRun")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("deadend")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "DeadEnd"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "DeadEnd")));
										}
										main.setupDeadEnd(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "DeadEnd"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "DeadEnd")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("redalert")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "RedAlert"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "RedAlert")));
										}
										main.setupRedAlert(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "RedAlert"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "RedAlert")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("lastarcherstanding")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "LastArcherStanding"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "LastArcherStanding")));
										}
										main.setupLastArcherStanding(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "LastArcherStanding"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "LastArcherStanding")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("sheepfreenzy")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SheepFreenzy"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SheepFreenzy")));
										}
										main.setupSheepFreenzy(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SheepFreenzy"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SheepFreenzy")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("smokemonster")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SmokeMonster"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SmokeMonster")));
										}
										main.setupSmokeMonster(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SmokeMonster"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SmokeMonster")));
										}
									}
								});
							}
							if (args[1].equalsIgnoreCase("slapfight")) {
								Bukkit.getServer().getScheduler().runTask(main, new Runnable(){
									public void run(){
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SlapFight"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_start").replace("%minigame%", "SlapFight")));
										}
										main.setupSlapFight(p.getLocation());
										if(main.placeholderapi) {
											sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SlapFight"))));
										} else {
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.setup_finish").replace("%minigame%", "SlapFight")));
										}
									}
								});
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Use: /pda setup [colormatch, spleef, minefield, jumpnrun, deadend, redalert, lastarcherstanding, sheepfreenzy, smokemonster, slapfight]");
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
					}
				}else if(args[0].equalsIgnoreCase("setuppoint")){
					if(p.hasPermission("partydeluxe.admin.setup") || sender.hasPermission("partydeluxe.admin.*")){
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
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				
				}else if(args[0].equalsIgnoreCase("setlobby")){
					if(sender.hasPermission("partydeluxe.admin.setlobby") || sender.hasPermission("partydeluxe.admin.*")){
						main.getConfig().set("lobby.world", p.getLocation().getWorld().getName());
						main.getConfig().set("lobby.location.x", p.getLocation().getBlockX());
						main.getConfig().set("lobby.location.y", p.getLocation().getBlockY());
						main.getConfig().set("lobby.location.z", p.getLocation().getBlockZ());
						main.saveConfig();
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.saved_lobby"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.saved_lobby")));
						}
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else if(args[0].equalsIgnoreCase("setcomponent")){
					// /mp setcomponent [minigame] [component]
					if(sender.hasPermission("partydeluxe.admin.setup") || sender.hasPermission("partydeluxe.admin.*")){
						if(args.length > 2){
							main.saveComponentForMinigame(args[1], args[2], p.getLocation());
							sender.sendMessage(main.getMessages().getString("messages.setup.saved_component"));
						} else {
							sender.sendMessage(ChatColor.RED + "Use: /pda setcomponent [game] [component]");
						}
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else if(args[0].equalsIgnoreCase("list")){
					if(sender.hasPermission("partydeluxe.admin.list") || sender.hasPermission("partydeluxe.admin.*")) {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.minigames_title"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.minigames_title")));
						}
						for(Minigame m : main.minigames) {
							if(m.isEnabled()) {
								sender.sendMessage(ChatColor.GREEN + m.name);
							} else {
								sender.sendMessage(ChatColor.RED + m.name);
							}
						}
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					if(sender.hasPermission("partydeluxe.admin.reload") || sender.hasPermission("partydeluxe.admin.*")) {
						
						//config files
						main.reloadConfig();
						main.reloadMessages();
						main.reloadMessages();
						main.reloadMysql();
						main.reloadScoreboard();
						main.reloadSettings();
						main.reloadSettings();
						main.reloadShop();
						main.reloadTitles();
						
						//minigames folder
						main.reloadColorMatch();
						main.reloadDeadEnd();
						main.reloadJumpnRun();
						main.reloadLastArcherStanding();
						main.reloadMineField();
						main.reloadRedAlert();
						main.reloadSheepFreenzy();
						main.reloadSlapFight();
						main.reloadSmokeMonster();
						main.reloadSpleef();
						
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.reload"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.reload")));
						}
						
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else if(args[0].equalsIgnoreCase("enable")){
					if(sender.hasPermission("partydeluxe.admin.enable")) {
						if(args.length > 1) {
							main.enableMinigame(sender, args[1]);
						} else {
							sender.sendMessage(ChatColor.RED + "Use: /pda enable [colormatch, spleef, minefield, jumpnrun, deadend, redalert, lastarcherstanding, sheepfreenzy, smokemonster, slapfight]");
						}
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else if(args[0].equalsIgnoreCase("disable")){
					if(sender.hasPermission("partydeluxe.admin.disable") || sender.hasPermission("partydeluxe.admin.*")) {
						if(args.length > 1) {
							main.disableMinigame(sender, args[1]);
						} else {
							sender.sendMessage(ChatColor.RED + "Use: /pda disable <colormatch/spleef/minefield/jumpnrun/deadend/redalert/lastarcherstanding/sheepfreenzy/smokemonster/slapfight>");
						}
					} else {
						if(main.placeholderapi) {
							sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
						}
					}
				}else{
					if(main.placeholderapi){
						sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.unknown_command")));
					}
				}
			}else{
				if(sender.hasPermission("partydeluxe.admin.help") || sender.hasPermission("partydeluxe.admin.*")) {
					sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help");
					sender.sendMessage(ChatColor.GREEN + "/pda setlobby " + ChatColor.WHITE + "Sets the server lobby.");
					sender.sendMessage(ChatColor.GREEN + "/pda setup [game] " + ChatColor.WHITE + "Sets the mini-games ");
					sender.sendMessage(ChatColor.GREEN + "/pda enable/disable [game] " + ChatColor.WHITE + "Activate or deactivate a mini-game");
					sender.sendMessage(ChatColor.GREEN + "/pda list " + ChatColor.WHITE + "See the list of minigames");
					sender.sendMessage(ChatColor.GREEN + "/pda setcomponent [game] [component] " + ChatColor.WHITE + "See a player statistics");
					sender.sendMessage(ChatColor.GREEN + "/pda reload " + ChatColor.WHITE + "Reload the plugin configuration");
				} else {
					if(main.placeholderapi) {
						sender.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.no_perms")));
					}
				}
			}
			return true;
	}

}