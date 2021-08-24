package whirss.partydeluxe.events;

import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import whirss.partydeluxe.Main;
import whirss.partydeluxe.Shop;
import whirss.partydeluxe.nms.NMSEffectManager;

public class OnPlayerShearSheep implements Listener {
	
	private Main main;
	
	public OnPlayerShearSheep(Main main) {
		this.main = main;
	}
	
	Random rand = new Random();
	@EventHandler
	public void onPlayerShearSheep(PlayerShearEntityEvent event){
		if(main.players.contains(event.getPlayer().getName())){
			int i = rand.nextInt(100);
			if(!main.currentscore.containsKey(event.getPlayer().getName())){
				main.currentscore.put(event.getPlayer().getName(), 0);
			}
			if(i < 5){
				main.currentscore.put(event.getPlayer().getName(), main.currentscore.get(event.getPlayer().getName()) + 3);
				int temp = Shop.getPlayerShopComponent(main, event.getPlayer().getName(), "sheepfreenzy_explosion_immunity");
				if(temp < 1){
					Shop.removeFromPlayerShopComponent(main, event.getPlayer().getName(), "sheepfreenzy_explosion_immunity", 1);
					if(main.getSheepFreenzy().getBoolean("minigame.enable_explosions")) {
						event.getPlayer().getLocation().getWorld().createExplosion(event.getEntity().getLocation(), 2F);	
					}
				}
			}else{
				main.currentscore.put(event.getPlayer().getName(), main.currentscore.get(event.getPlayer().getName()) + 1);
			}
			if(main.getSheepFreenzy().getBoolean("minigame.enable_particles")) {
				NMSEffectManager.createSheepFreenzyEffect(event.getEntity().getLocation());	
			}
			event.getEntity().remove();
		}
	}

}
