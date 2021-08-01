package whirss.minecraftparty.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import whirss.minecraftparty.Main;

public class OnBlockBreak implements Listener {
	
	private Main main;
	
	public OnBlockBreak(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(main.players.contains(event.getPlayer().getName())){
			//SPLEEF
			if(main.ingame_started){
				if(event.getBlock().getType() == Material.SNOW_BLOCK){
					event.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL, 2));
					event.getPlayer().updateInventory();
					event.getBlock().setType(Material.AIR);
					event.setCancelled(true);
				}else{
					event.setCancelled(true);
				}
			}else{
				event.setCancelled(true);
			}
		}
	}

}
