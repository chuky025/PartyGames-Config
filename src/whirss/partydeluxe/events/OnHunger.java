package whirss.partydeluxe.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import whirss.partydeluxe.Main;

public class OnHunger implements Listener {
	
	private Main main;
	
	public OnHunger(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player)event.getEntity();
			if(main.players.contains(p.getName())){
				event.setCancelled(true);
			}
		}
	}

}
