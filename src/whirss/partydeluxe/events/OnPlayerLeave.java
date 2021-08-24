package whirss.partydeluxe.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import whirss.partydeluxe.Main;

public class OnPlayerLeave implements Listener {
	
	private Main main;
	
	public OnPlayerLeave(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		if(main.players.contains(event.getPlayer().getName())){
			main.players.remove(event.getPlayer().getName());
			main.players_left.add(event.getPlayer().getName());
			
			if(main.players.size() < main.min_players){
				main.stopFull();
			}
		}

		if(main.players.size() < main.min_players){
			main.stopFull();
		}
	}

}
