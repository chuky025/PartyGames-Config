package whirss.partydeluxe.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import whirss.partydeluxe.Main;

public class OnInventoryClick implements Listener {
	
	private Main main;
	
	public OnInventoryClick(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if(main.players.contains(((Player)event.getWhoClicked()).getName())){
			event.setCancelled(true);
		}
	}

}
