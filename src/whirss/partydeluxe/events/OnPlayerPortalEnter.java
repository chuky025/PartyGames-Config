package whirss.partydeluxe.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.partydeluxe.Main;

public class OnPlayerPortalEnter implements Listener {
	
	private Main main;
	
	public OnPlayerPortalEnter(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerPortalEnter(EntityPortalEnterEvent event){
		if(event.getEntity() instanceof Player){
			final Player p = (Player) event.getEntity();
			if(main.players.contains(p.getName())){
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
				if(main.placeholderapi) {
					p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.you_left"))));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.you_left")));
				}
				if(main.players.size() < main.min_players){
					Bukkit.getScheduler().runTaskLater(main, new Runnable(){
						public void run(){
							main.stopFull(p);
						}
					}, 15);
				}
			}
		}
	}

}
