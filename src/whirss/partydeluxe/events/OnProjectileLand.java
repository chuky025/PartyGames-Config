package whirss.partydeluxe.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import whirss.partydeluxe.Main;
import whirss.partydeluxe.Shop;

public class OnProjectileLand implements Listener {
	
	private Main main;
	
	public OnProjectileLand(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onProjectileLand(ProjectileHitEvent e) {   
		if (e.getEntity().getShooter() instanceof Player) {
			if (e.getEntity() instanceof Snowball) {
				Player player = (Player) e.getEntity().getShooter();
				if(main.players.contains(player.getName())){
					BlockIterator bi = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
					Block hit = null;
					while (bi.hasNext()) {
						hit = bi.next();
						if (hit.getTypeId() != 0) {
							break;
						}
					}
					try {
						if (hit.getLocation().getBlockY() < main.minigames.get(main.currentmg).spawn.getBlockY() && hit.getType() == Material.SNOW_BLOCK) {
							hit.setTypeId(0);

							player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1F);
							
						}
					} catch (Exception ex) { 
						
					}
				}
			} else if (e.getEntity() instanceof Egg){
				Player player = (Player) e.getEntity().getShooter();
				if(main.players.contains(player.getName())){
					BlockIterator bi = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
					Block hit = null;
					while (bi.hasNext()) {
						hit = bi.next();
						if (hit.getTypeId() != 0) {
							break;
						}
					}
					try {
						Location l = hit.getLocation();
						if (hit.getLocation().getBlockY() < main.minigames.get(main.currentmg).spawn.getBlockY() && hit.getType() == Material.SNOW_BLOCK) {
							if(e.getEntity().hasMetadata("mega")){
								for(int x = 1; x <= 5; x++){
									for(int z = 1; z <= 5; z++){
										Block b = l.getWorld().getBlockAt(new Location(l.getWorld(), l.getBlockX() + x - 3, l.getBlockY(), l.getBlockZ() + z - 3));
										b.setTypeId(0);
									}
								}
								Shop.removeFromPlayerShopComponent(main, player.getName(), "megagrenades", 1);
							}else{
								for(int x = 1; x <= 3; x++){
									for(int z = 1; z <= 3; z++){
										Block b = l.getWorld().getBlockAt(new Location(l.getWorld(), l.getBlockX() + x - 2, l.getBlockY(), l.getBlockZ() + z - 2));
										b.setTypeId(0);
									}
								}
								Shop.removeFromPlayerShopComponent(main, player.getName(), "grenades", 1);
							}
							hit.setTypeId(0);

							player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1F);
							/*for (Player sp : players) {
	                            sp.getPlayer().playEffect(new Location(hit.getWorld(), hit.getLocation().getBlockX(), hit.getLocation().getBlockY() + 1.0D, hit.getLocation().getBlockZ()), Effect.MOBSPAWNER_FLAMES, 25);
	                    	}*/
						}
					} catch (Exception ex) { 
						
					}
				}
			}
		}
	}

}
