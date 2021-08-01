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
			if(p.isOnline()){
				if(!lost.contains(p)){
					m.win(p);
				}
			}
		}
		
		lost.clear();
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
						m.getConfig().set("ag", name);
						m.saveConfig();
						m.reloadConfig();
						//p.sendMessage(ChatColor.GREEN + "Starting in " + ChatColor.GOLD + Integer.toString(count));
						
						if(m.placeholderapi) {
							p.sendTitle(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title").replace("%count%", Integer.toString(count)).replace("%minigame%", name))),PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle").replace("%minigame%", name))), 0, 30, 0);
						} else {
							p.sendTitle(ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.countdown.title").replace("%count%", Integer.toString(count)).replace("%minigame%", name)),ChatColor.translateAlternateColorCodes('&', m.getTitles().getString("titles.after_countdown.subtitle").replace("%minigame%", name)), 0, 30, 0);
						}
					}
				}
				count--;
				if(count < 0){
					for(String p_ : m.players){
						Player p = Bukkit.getPlayerExact(p_);
						if(p.isOnline()){
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
				
				List<String> description = m.getMessages().getStringList("messages.minigames." + name.toLowerCase());
				for(int i=0;i<description.size();i++) {
					String message = description.get(i);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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
		m.getConfig().set("minigames." + name + ".enabled", f);
		m.saveConfig();
	}
	
	public boolean isEnabled(){
		if(!m.getConfig().isSet("minigames." + name + ".enabled")){
			setEnabled(true);
			return true;
		}else{
			return m.getConfig().getBoolean("minigames." + name + ".enabled");
		}
	}

	
}
