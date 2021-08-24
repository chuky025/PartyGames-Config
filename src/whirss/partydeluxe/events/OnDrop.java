package whirss.partydeluxe.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import whirss.partydeluxe.Main;

public class OnDrop implements Listener {
	
	private Main main;
	
	public OnDrop(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(main.players.contains(event.getPlayer().getName())){
			event.getItemDrop().remove();
			event.setCancelled(true);
		}
	}

}
