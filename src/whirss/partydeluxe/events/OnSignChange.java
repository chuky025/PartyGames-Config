package whirss.partydeluxe.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import whirss.partydeluxe.Main;

public class OnSignChange implements Listener {
	
	private Main main;
	
	public OnSignChange(Main main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(event.getLine(0).equalsIgnoreCase("[MCParty]")){
			if(event.getPlayer().hasPermission("minecraftparty.sign")){
				event.setLine(0, "");
				event.setLine(1, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help");
				event.setLine(2, ChatColor.BLACK + "Join");
				event.setLine(3, "");
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("messages.setup.sign_added")));
			}
		}
	}

}
