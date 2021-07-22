package whirss.minecraftparty.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import whirss.minecraftparty.Main;

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
				p.sendMessage(main.getMessages().getString("messages.game.you_left").replace("&", "§"));
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
