package whirss.minecraftparty;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import me.clip.placeholderapi.PlaceholderAPI;

public class Minigame {
	
	public ArrayList<Player> lost = new ArrayList<Player>();
	public ArrayList<Player> winners = new ArrayList<Player>();
	
	public String name = "";
	public static Main m;
	public Location spawn;
	public Location lobby;
	public Location spectatorlobby;
	public Location finish;
	public String description;
	public boolean enabled;
	
	public Minigame(String arg1, String arg2, Main arg3, Location arg4, Location arg5, Location arg6, Location arg7){
		name = arg1;
		description = arg2;
		m = arg3;
		spawn = arg4;
		lobby = arg5;
		spectatorlobby = arg6;
		finish = arg7;
	}
	
	public void getWinner(){
		for(String pl : m.players){
			Player p = Bukkit.getPlayerExact(pl);
			if(p.isOnline() && !lost.contains(p)){
				m.win(p);
			}
		}
		
		lost.clear();
		winners.clear();
	}
	
	int count = 5;
	BukkitTask cooldown = null;
	
	public void startCooldown(){
		//scoreboard.cancelScoreboardWaiting();
				
		//final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {

		final BukkitTask id__ = Bukkit.getServer().getScheduler().runTaskTimer(m, new Runnable() {
			public void run(){
				
				for(String p_ : m.players){
					Player p = Bukkit.getPlayerExact(p_);
					if(p.isOnline()){
						m.removeScoreboard(p);
						//p.sendMessage(ChatColor.GREEN + "Starting in " + ChatColor.GOLD + Integer.toString(count));
						
						if(name.equals("ColorMatch")) {
							m.getConfig().set("data.ag", m.getColorMatch().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getColorMatch().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getColorMatch().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("DeadEnd")) {
							m.getConfig().set("data.ag", m.getDeadEnd().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getDeadEnd().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getDeadEnd().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("JumpnRun")) {
							m.getConfig().set("data.ag", m.getJumpnRun().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getJumpnRun().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getJumpnRun().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("LastArcherStanding")) {
							m.getConfig().set("data.ag", m.getLastArcherStanding().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getLastArcherStanding().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getLastArcherStanding().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("MineField")) {
							m.getConfig().set("data.ag", m.getMineField().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getMineField().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getMineField().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("RedAlert")) {
							m.getConfig().set("data.ag", m.getRedAlert().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getRedAlert().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getRedAlert().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("SheepFreenzy")) {
							m.getConfig().set("data.ag", m.getSheepFreenzy().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSheepFreenzy().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSheepFreenzy().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("SlapFight")) {
							m.getConfig().set("data.ag", m.getSlapFight().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSlapFight().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSlapFight().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("SmokeMonster")) {
							m.getConfig().set("data.ag", m.getSmokeMonster().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSmokeMonster().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSmokeMonster().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						if(name.equals("Spleef")) {
							m.getConfig().set("data.ag", m.getSpleef().getString("minigame.displayname"));
							m.saveConfig();
							m.reloadConfig();
							if(m.placeholderapi) {
								p.sendTitle
								(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSpleef().getString("minigame.displayname")))),
								PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle
								(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title")
										.replace("%count%", Integer.toString(count)).replace("%minigame%", m.getSpleef().getString("minigame.displayname"))),
								ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle")
										.replace("%minigame%", name)), 0, 30, 0);
							}
						}
						
					}
				}
				count--;
				if(count < 0){
					for(String p_ : m.players){
						Player p = Bukkit.getPlayerExact(p_);
						if(p.isOnline()){
							m.your_place = null;
							m.place1 = null;
							m.place2 = null;
							m.place3 = null;
							m.reward = 0;
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 30.0F, 50.0F);
							if(m.placeholderapi) {
								p.sendTitle(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.title").replace("%count%", Integer.toString(count)).replace("%minigame%", name))),PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle").replace("%minigame%", name))), 0, 30, 0);
							} else {
								p.sendTitle(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.title").replace("%count%", Integer.toString(count)).replace("%minigame%", name)),ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle").replace("%minigame%", name)), 0, 30, 0);
							}
						}
					}
					m.registerMinigameStart(m.minigames.get(m.currentmg).start());
					m.ingame_started = true;
					count = 5;
					cooldown.cancel();
					cooldown = null;
				}
			}
		}, 20, 20);
		cooldown = id__;
	}
	
	public BukkitTask start(){
		return null;
	}
	
	public void join(final Player p){
		if(p.hasPotionEffect(PotionEffectType.JUMP)){
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		if(p.hasPotionEffect(PotionEffectType.SPEED)){
			p.removePotionEffect(PotionEffectType.SPEED);
		}
		if(p.getPassenger()  != null){
			Entity t = p.getPassenger();
			p.eject();
			t.remove();
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.teleport(spawn);
				p.setGameMode(GameMode.SURVIVAL);
				p.setAllowFlight(false);
				p.setFlying(false);
				
				List<String> description = m.getMessages().getStringList("messages.game.description");
				for(int i=0;i<description.size();i++) {
					String message = description.get(i);
					if(name.equals("ColorMatch")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getColorMatch().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getColorMatch().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("DeadEnd")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getDeadEnd().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getDeadEnd().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("JumpnRun")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getJumpnRun().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getJumpnRun().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("LastArcherStanding")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getLastArcherStanding().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getLastArcherStanding().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("MineField")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getMineField().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getMineField().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("RedAlert")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getRedAlert().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getRedAlert().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("SheepFreenzy")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSheepFreenzy().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSheepFreenzy().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("SlapFight")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSlapFight().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSlapFight().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("SmokeMonster")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSmokeMonster().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSmokeMonster().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
					if(name.equals("Spleef")) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSpleef().getString("minigame.displayname"))
									.replace("%nl%", "\n")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
									.replace("%displayname%", m.getSpleef().getString("minigame.displayname"))
									.replace("%nl%", "\n"));
						}
					}
				}
			}
		}, 5);
	}
	
	public void leave(final Player p){
		/*for (PotionEffect effect : p.getActivePotionEffects()) {
			if(p.hasPotionEffect(effect.getType())){
				try {
					p.removePotionEffect(effect.getType());
				} catch (Exception e) {
					
				}
			}
		}*/
		if(p.hasPotionEffect(PotionEffectType.JUMP)){
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		if(p.hasPotionEffect(PotionEffectType.SPEED)){
			p.removePotionEffect(PotionEffectType.SPEED);
		}
		if(p.getGameMode().equals(GameMode.SPECTATOR)) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				p.teleport(lobby);
				p.setAllowFlight(false);
				p.setFlying(false);
				if(p.hasPotionEffect(PotionEffectType.JUMP)){
					p.removePotionEffect(PotionEffectType.JUMP);
				}
				if(p.hasPotionEffect(PotionEffectType.SPEED)){
					p.removePotionEffect(PotionEffectType.SPEED);
				}
				if(p.getGameMode().equals(GameMode.SPECTATOR)) {
					p.setGameMode(GameMode.SURVIVAL);
				}
				if(m.bungee) {
					if(m.connect_hub) {
						if(m.placeholderapi) {
							p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.you_left"))));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.you_left")));
						}
						m.sendToServer(p, m.getSettings().getString("settings.plugin.bungee.hub"));
					} else {
						if(m.shutdown) {
							if(m.placeholderapi) {
								p.kickPlayer(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.you_left"))));
							} else {
								p.kickPlayer(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.you_left")));
							}
						}
					}
					
					if(m.shutdown) {
						Bukkit.getServer().shutdown();
					}
				}

				
			}
		}, 20);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable(){
			public void run(){
				m.giveItemRewards(p, true);
			}
		}, 20L);
		
	}
	
	public void spectate(final Player p){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
			@Override
			public void run() {
				if(p.getPassenger() != null){
					Entity t = p.getPassenger();
					p.eject();
					t.remove();
				}
				p.setAllowFlight(true);
				p.setFlying(true);
				p.teleport(spectatorlobby);
				p.getInventory().clear();
				p.setGameMode(GameMode.SPECTATOR);
			}
		}, 5);
	}
	
	
	public void reset(final Location location){
	}
	
	public void setEnabled(boolean f){
		enabled = f;
		if(name.equals("ColorMatch")) {
			m.getColorMatch().set("minigame.enabled", f);
			m.saveColorMatch();
		}
		if(name.equals("DeadEnd")) {
			m.getDeadEnd().set("minigame.enabled", f);
			m.saveDeadEnd();
		}
		if(name.equals("JumpnRun")) {
			m.getJumpnRun().set("minigame.enabled", f);
			m.saveJumpnRun();
		}
		if(name.equals("LastArcherStanding")) {
			m.getLastArcherStanding().set("minigame.enabled", f);
			m.saveLastArcherStanding();
		}
		if(name.equals("MineField")) {
			m.getMineField().set("minigame.enabled", f);
			m.saveMineField();
		}
		if(name.equals("RedAlert")) {
			m.getRedAlert().set("minigame.enabled", f);
			m.saveRedAlert();
		}
		if(name.equals("SheepFreenzy")) {
			m.getSheepFreenzy().set("minigame.enabled", f);
			m.saveSheepFreenzy();
		}
		if(name.equals("SlapFight")) {
			m.getSlapFight().set("minigame.enabled", f);
			m.saveSlapFight();
		}
		if(name.equals("SmokeMonster")) {
			m.getSmokeMonster().set("minigame.enabled", f);
			m.saveSmokeMonster();
		}
		if(name.equals("Spleef")) {
			m.getSpleef().set("minigame.enabled", f);
			m.saveSpleef();
		}
	}
	
	public boolean isEnabled(){
		if(name.equals("ColorMatch")) {
			if(!m.getColorMatch().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getColorMatch().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("DeadEnd")) {
			if(!m.getDeadEnd().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getDeadEnd().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("JumpnRun")) {
			if(!m.getJumpnRun().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getJumpnRun().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("LastArcherStanding")) {
			if(!m.getLastArcherStanding().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getLastArcherStanding().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("MineField")) {
			if(!m.getMineField().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getMineField().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("RedAlert")) {
			if(!m.getRedAlert().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getRedAlert().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("SheepFreenzy")) {
			if(!m.getSheepFreenzy().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getSheepFreenzy().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("SlapFight")) {
			if(!m.getSlapFight().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getSlapFight().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("SmokeMonster")) {
			if(!m.getSmokeMonster().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getSmokeMonster().getBoolean("minigame.enabled");
			}
		}
		if(name.equals("Spleef")) {
			if(!m.getSpleef().isSet("minigame.enabled")){
				setEnabled(true);
				return true;
			}else{
				return m.getSpleef().getBoolean("minigame.enabled");
			}
		}
		return false;
	}

	
}
