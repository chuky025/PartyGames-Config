package whirss.partydeluxe.nms;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import whirss.partydeluxe.Main;
import whirss.partydeluxe.PluginUtil;

public class NMSManager {

	public static void createParticles(Location l, int i, int j){
		try{
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
			if (version.contains("1_8_R1")) {
				NMSFunctions181 explosion = NMSFunctions181.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(version.contains("1_8_R2")){
				NMSFunctions182 explosion = NMSFunctions182.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else if(version.contains("1_8_R3")){
				NMSFunctions183 explosion = NMSFunctions183.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			} else if(version.contains("1_9_R1")){
				NMSFunctions191 explosion = NMSFunctions191.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			} else if(version.contains("1_9_R2")){
				NMSFunctions192 explosion = NMSFunctions192.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			} else if(version.contains("1_10_R1")){
				NMSFunctions1101 explosion = NMSFunctions1101.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			} else if(version.contains("1_11_R1")){
				NMSFunctions1111 explosion = NMSFunctions1111.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			} else if(version.contains("1_12_R1")){
				NMSFunctions1121 explosion = NMSFunctions1121.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}else{
				//fallback
				NMSFunctions1121 explosion = NMSFunctions1121.HUGE_EXPLOSION;
				explosion.animateAtLocation(l, i, j);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be unsupported! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
		}
	}
	
	public static ItemStack fakeGlow(ItemStack item){
		try{
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
			if (version.contains("1_8_R1")) {
				return NMSFunctions181.addGlow(item);
			}else if(version.contains("1_8_R2")){
				return NMSFunctions182.addGlow(item);
			}else if(version.contains("1_8_R3")){
				return NMSFunctions183.addGlow(item);
			} else if(version.contains("1_9_R1")){
				return NMSFunctions191.addGlow(item);
			} else if(version.contains("1_9_R2")){
				return NMSFunctions192.addGlow(item);
			} else if(version.contains("1_10_R1")){
				return NMSFunctions1101.addGlow(item);
			} else if(version.contains("1_11_R1")){
				return NMSFunctions1111.addGlow(item);
			} else if(version.contains("1_12_R1")){
				return NMSFunctions1121.addGlow(item);
			}else{
				//fallback
				return NMSFunctions1121.addGlow(item);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be unsupported! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
			return item;
		}
	}
	
	public static void createSheepFreenzyEffect(Location t){
		t.getWorld().playEffect(t, Effect.POTION_BREAK, 1);
	}
	
	public static void createFlameEffect(Location t){
		//t.getWorld().playEffect(t, Effect.FLAME, 1);
		t.getWorld().spawnParticle(Particle.FLAME, t, 1);
	}
	
	public static void createSmokeNormalEffect(Location t){
		//t.getWorld().playEffect(t, Effect.FLAME, 1);
		t.getWorld().spawnParticle(Particle.SMOKE_NORMAL, t, 1);
	}
	
	public static void createMinefieldEffect(Location t){
		try{
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
			if (version.contains("1_8_R1")) {
				NMSFunctions181 effect = NMSFunctions181.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			}else if(version.contains("1_8_R2")){
				NMSFunctions182 effect = NMSFunctions182.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			}else if(version.contains("1_8_R3")){
				NMSFunctions183 effect = NMSFunctions183.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			} else if(version.contains("1_9_R1")){
				NMSFunctions191 effect = NMSFunctions191.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			} else if(version.contains("1_9_R2")){
				NMSFunctions192 effect = NMSFunctions192.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			} else if(version.contains("1_10_R1")){
				NMSFunctions1101 effect = NMSFunctions1101.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			} else if(version.contains("1_11_R1")){
				NMSFunctions1111 effect = NMSFunctions1111.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			} else if(version.contains("1_12_R1")){
				NMSFunctions1121 effect = NMSFunctions1121.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			}else{
				//fallback
				NMSFunctions1121 effect = NMSFunctions1121.HUGE_EXPLOSION;
				effect.animateAtLocation(t, 1, 1);
			}
		}catch(Exception e){
			System.out.println("Your Bukkit build appears to be unsupported! Please post a comment with the following string on the project page: " + Bukkit.getVersion());
		}
	}
	
	public static void createBloodEffect(Main m, final Location t){
		Random r = new Random();
		for(int i = 0; i < r.nextInt(7) + 5; i++){
			t.getWorld().dropItemNaturally(t, new ItemStack(Material.INK_SACK, 1, (short)1));
		}
		Bukkit.getScheduler().runTaskLater(m, new Runnable(){
			public void run(){
				Entity[] entities = PluginUtil.getNearbyEntities(t, 5);
				for(Entity e : entities){
					if(e.getType() == EntityType.DROPPED_ITEM){
						e.remove();
					}
				}
			}
		}, 20);
	}
	
}
