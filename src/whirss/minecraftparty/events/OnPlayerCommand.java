package whirss.minecraftparty.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import whirss.minecraftparty.Main;
import whirss.minecraftparty.Shop;

public class OnPlayerCommand implements Listener {
	
	private Main main;
	
	public OnPlayerCommand(Main main) {
		this.main = main;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		if(main.players.contains(event.getPlayer().getName())){
			if(event.getMessage().startsWith("/leave") || event.getMessage().equalsIgnoreCase("/quit")){
				final Player p = event.getPlayer();
				p.teleport(main.getLobby());
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
					public void run(){
						p.teleport(main.getLobby());
					}
				}, 5);
				main.updateScoreboardOUTGAME(p.getName());
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
				event.setCancelled(true);
				return;
			}else if(event.getMessage().equalsIgnoreCase("/shop")){
				Shop.openShop(main, event.getPlayer().getName());
				event.setCancelled(true);
			}
			
			if(!event.getPlayer().isOp()){
				if(event.getMessage().startsWith("/mp") || event.getMessage().equalsIgnoreCase("/mcparty") || event.getMessage().equalsIgnoreCase("/mcp") 
						|| event.getMessage().equalsIgnoreCase("/minecraftparty") || event.getMessage().equalsIgnoreCase("/mpadmin") || event.getMessage().equalsIgnoreCase("/mpa")  
						|| event.getMessage().equalsIgnoreCase("/mcpartyadmin") || event.getMessage().equalsIgnoreCase("/minecraftpartyadmin")){
					// nothing
				}else{
					event.setCancelled(true);
					event.getPlayer().sendMessage(main.getMessages().getString("messages.game.ingame_commands").replace("&", "§"));
				}
			}
		}
	}

}
