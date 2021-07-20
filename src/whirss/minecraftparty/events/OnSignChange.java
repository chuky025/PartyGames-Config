package whirss.minecraftparty.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChange implements Listener {
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(event.getLine(0).toLowerCase().contains("MinecraftParty") || event.getLine(1).toLowerCase().contains("MinecraftParty")){
			if(event.getPlayer().hasPermission("minecraftparty.sign")){
				event.setLine(0, "");
				event.setLine(1, ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "[PARTY]");
			}
		}
	}

}
