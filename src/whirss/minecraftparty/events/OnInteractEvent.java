package whirss.minecraftparty.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import whirss.minecraftparty.Main;
import whirss.minecraftparty.Shop;
import whirss.minecraftparty.nms.NMSEffectManager;

public class OnInteractEvent implements Listener {
	
	private Main main;
	
	public OnInteractEvent(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInteractEvent(PlayerInteractEvent event)
	{	
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if(event.hasBlock()){
				if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
				{
					final Sign s = (Sign) event.getClickedBlock().getState();
					if (s.getLine(1).equalsIgnoreCase(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Minecraft" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Party " + ChatColor.GRAY + "- " + ChatColor.WHITE + "Help")){
						if(main.players.contains(event.getPlayer().getName())){
							event.getPlayer().sendMessage(main.getMessages().getString("messages.game.on_join").replace("&", "§"));
						}else{
							if(main.players.size() > main.getConfig().getInt("config.max_players") - 1){
								event.getPlayer().sendMessage(main.getMessages().getString("messages.game.game_full").replace("&", "§"));
								return;
							}
							main.players.add(event.getPlayer().getName());
							// if its the first player to join, start the whole minigame
							if(main.players.size() < main.min_players + 1){
								main.pinv.put(event.getPlayer().getName(), event.getPlayer().getInventory().getContents());
								main.startNew();
								if(main.min_players > 1){
									event.getPlayer().sendMessage(main.getMessages().getString("messages.game.joined_queue").replace("%min_players%", Integer.toString(main.min_players)).replace("&", "§"));
								}
							}else{ // else: just join the minigame
								try{
									main.pinv.put(event.getPlayer().getName(), event.getPlayer().getInventory().getContents());
									if(main.currentmg > -1){
										if(main.ingame_started){
											main.minigames.get(main.currentmg).lost.add(event.getPlayer());
											main.minigames.get(main.currentmg).spectate(event.getPlayer());
										}else{
											main.minigames.get(main.currentmg).join(event.getPlayer());
										}
									}
								}catch(Exception e){
									event.getPlayer().sendMessage(ChatColor.RED + "An error occured.");
								}
							}
						}
					}
				}	
			}
		}else if(event.getAction().equals(Action.PHYSICAL)){
			if(event.getClickedBlock().getType() == Material.STONE_PLATE){
				if(main.players.contains(event.getPlayer().getName())){
					final Player p = event.getPlayer();
					p.getWorld().createExplosion(p.getLocation(), 0);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
						@Override
						public void run() {
							p.teleport(main.minigames.get(main.currentmg).spawn);
						}
					}, 5);
					event.getClickedBlock().setType(Material.AIR);
				}
			}
		}
		
		if(event.hasItem()){
			if(event.getItem() != null){
				if(event.getItem().getItemMeta().getDisplayName() != null){
					if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("megagrenade")){
						if(main.players.contains(event.getPlayer().getName())){
							Egg egg = event.getPlayer().launchProjectile(Egg.class);
							egg.setMetadata("mega", new FixedMetadataValue(main, "mega"));
							event.getPlayer().getInventory().remove(Material.EGG);
							event.getPlayer().updateInventory();
							event.setCancelled(true);
							event.getPlayer().getInventory().addItem(Shop.enchantedItemStack(new ItemStack(Material.EGG, Shop.getPlayerShopComponent(main, event.getPlayer().getName(), "megagrenades") - 1), "MegaGrenade"));
							event.getPlayer().updateInventory();
						}
					}
				}
			}
		}
	}

}
