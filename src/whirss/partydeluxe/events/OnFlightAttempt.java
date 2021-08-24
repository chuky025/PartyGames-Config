package whirss.partydeluxe.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.partydeluxe.Main;

public class OnFlightAttempt implements Listener {

	private Main main;

	public OnFlightAttempt(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event) {
		final Player p = event.getPlayer();
	    if(p.getGameMode() != GameMode.CREATIVE) {
	    	if(main.currentmg < 0 || main.currentmg > main.minigames.size() - 1 || !main.players.contains(p.getName())){
	    		return;
	    	}
	    	if(main.players.contains(p.getName()) && main.minigames.get(main.currentmg).name.equalsIgnoreCase("slapfight")){
	    		if(!main.players_doublejumped.contains(p.getName())){
	    			if(main.getSlapFight().getBoolean("minigame.enable_double_jump")) {
	    				event.getPlayer().setAllowFlight(false);
				        event.getPlayer().setFlying(false);
				        event.setCancelled(true);
			    		event.getPlayer().setVelocity(p.getVelocity().setY(1.4F)); // add(new Vector(0,0.7,0)
			    		main.players_doublejumped.add(p.getName());
				        Bukkit.getScheduler().runTaskLater(main, new Runnable(){
				        	public void run(){
				        		main.players_doublejumped.remove(p.getName());
				        	}
				        }, 20 * 10);
		    		}else{
		    			if(main.placeholderapi) {
		    				p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.double_jump"))));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.game.double_jump")));
						}
		    			
		    			p.setAllowFlight(false);
				        p.setFlying(false);
				        event.setCancelled(true);
		    		}
	    		}
	    	}
	    }
	}

}