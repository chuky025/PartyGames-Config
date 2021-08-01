package whirss.minecraftparty.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.minecraftparty.Main;

public class OnPlayerJoin implements Listener {
	
	private Main main;
	
	public OnPlayerJoin(Main main) {
		this.main = main;
	}
	
	@EventHandler 
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		
		// update credits from mysql
		try{
			if(main.msql.getCredits(p.getName()) > -1){
				main.updatePlayerStats(p.getName(), "wins", main.msql.getWins(p.getName()));
				main.updatePlayerStats(p.getName(), "credits", main.msql.getCredits(p.getName()));		
			}
		}catch(Exception e){
			main.getLogger().warning("An error occurred while syncing credits and wins for player " + p.getName());
		}

		if(main.players_left.contains(p.getName())){
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
				public void run(){
					p.teleport(main.getLobby());
					p.getInventory().setContents(main.pinv.get(p.getName()));
					p.updateInventory();
				}
			}, 4);
			main.players_left.remove(p.getName());
		}

		if (!main.getSettings().getBoolean("settings.game-on-join")) return;

		if(main.players.contains(event.getPlayer().getName())){
			if(main.getSettings().getBoolean("settings.enable_placeholderapi")) {
				p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.alredy_ingame"))));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.alredy_ingame")));
			}
			return;
		}
		main.players.add(p.getName());
		if(main.getSettings().getBoolean("settings.enable_placeholderapi")) {
			event.setJoinMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.player_joined"))));
		} else {
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.player_joined")));
		}


		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				
				// if its the first player to join, start the whole minigame
				if(main.players.size() < main.min_players + 1){
					main.pinv.put(p.getName(), p.getInventory().getContents());
					main.startNew();
					return;
				}
				
				try {
					main.pinv.put(p.getName(), p.getInventory().getContents());
					if (main.currentmg > -1) {
						main.minigames.get(main.currentmg).join(p);
						p.teleport(main.minigames.get(main.currentmg).spawn);
					}
				} catch (Exception ex) {
					if(main.getSettings().getBoolean("settings.enable_placeholderapi")) {
						p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.error"))));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.other.error")));
					}
				}
			}
		}, 6);
	}

}
