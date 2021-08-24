package whirss.partydeluxe.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import whirss.partydeluxe.Main;

public class OnBlockPlace implements Listener  {
	
	private Main main;
	
	public OnBlockPlace(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if(main.players.contains(event.getPlayer().getName())){
			event.setCancelled(true);
		}
	}

}
