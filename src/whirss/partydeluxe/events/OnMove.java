package whirss.partydeluxe.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import whirss.partydeluxe.Main;
import whirss.partydeluxe.Minigame;
import whirss.partydeluxe.minigames.SmokeMonster;
import whirss.partydeluxe.nms.NMSManager;

public class OnMove implements Listener {
	
	private Main main;
	
	public OnMove(Main main) {
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event){
		try{
			if(main.players.contains(event.getPlayer().getName())){
				if(event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
					if(main.currentmg > -1 && main.currentmg < main.minigames.size()){
						if(main.minigames.get(main.currentmg).name.equalsIgnoreCase("slapfight")){
							event.getPlayer().setAllowFlight(true);
						}	
					}
				}
				if(main.currentmg > -1){
					final Minigame current = main.minigames.get(main.currentmg);
					if(!current.lost.contains(event.getPlayer())){
						if(main.started && !main.ingame_started){
							if(current.name.equalsIgnoreCase("JumpnRun") || current.name.equalsIgnoreCase("MineField")){
								final Player p = event.getPlayer();
								if(p.getLocation().getBlockZ() > current.spawn.getBlockZ() + 1){
									Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run(){
											p.teleport(current.spawn);
										}
									}, 5);
								}else if(p.getLocation().getBlockY() + 2 < current.spawn.getBlockY()){
									Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run(){
											p.teleport(current.spawn);
										}
									}, 5);
								}
							}
						}
						if(main.started && main.ingame_started){
							if(current.name.equalsIgnoreCase("DeadEnd")){
								World w = event.getPlayer().getWorld();
								Location under = new Location(w, event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockY() - 1, event.getPlayer().getLocation().getBlockZ());
								Location under2 = new Location(w, event.getPlayer().getLocation().getBlockX() - 1, event.getPlayer().getLocation().getBlockY() - 1, event.getPlayer().getLocation().getBlockZ() -1);
								Location under3 = new Location(w, event.getPlayer().getLocation().getBlockX() + 1, event.getPlayer().getLocation().getBlockY() - 1, event.getPlayer().getLocation().getBlockZ() +1);
								if(w.getBlockAt(under).getType() == Material.valueOf(main.getDeadEnd().getString("minigame.materials.game_floor"))){
									
									Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run(){
											w.getBlockAt(under).setType(Material.AIR);
											if(main.getDeadEnd().getBoolean("minigame.enable_particles")) {
												NMSManager.createSmokeNormalEffect(under);
											}
											if(main.getDeadEnd().getBoolean("minigame.remove_several_blocks")) {
												w.getBlockAt(under2).setType(Material.AIR);
												w.getBlockAt(under3).setType(Material.AIR);
												if(main.getDeadEnd().getBoolean("minigame.enable_particles")) {
													NMSManager.createSmokeNormalEffect(under2);
													NMSManager.createSmokeNormalEffect(under3);
												}
											}
										}
									}, 10);
								}
							}
							if(current.name.equalsIgnoreCase("SmokeMonster")){
								for(Location l : SmokeMonster.locs){
									if(event.getPlayer().getLocation().distance(l) < 3 || event.getPlayer().getLocation().distance(l.add(0D, -1.5D, 0D))  < 3){
										current.lost.add(event.getPlayer());
										int count = 0;
										for(String pl : main.players){
											Player p = Bukkit.getPlayerExact(pl);
											if(p.isOnline()){
												if(!current.lost.contains(p)){
													count++;
												}
											}
										}
										main.sendPlace(count, event.getPlayer());
										current.spectate(event.getPlayer());
										if(count < 2){
											main.c_ += main.seconds-main.c;
											main.c = main.seconds; 
										}
									}
								}
							}
							if(current.name.equalsIgnoreCase("JumpnRun") || current.name.equalsIgnoreCase("MineField")){
								final Player p = event.getPlayer();
								if(p.getLocation().getBlockZ() > current.finish.getBlockZ()){
									main.c_ += main.seconds-main.c;
									main.c = main.seconds; // just skips all the remaining seconds and sets to 60, current timer will do the rest
									current.winners.add(p);
									current.spectate(event.getPlayer());
									return;
								}
							}
							if(event.getPlayer().getLocation().getBlockY() + 2 < current.spawn.getBlockY()){
								if(current.name.equalsIgnoreCase("JumpnRun") || current.name.equalsIgnoreCase("MineField")){
									final Player p = event.getPlayer();
									Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run(){
											p.teleport(current.spawn);
										}
									}, 5);
									return;
								}
								if(current.name.equalsIgnoreCase("slapfight")){
									if(event.getPlayer().getLocation().getBlockY() > current.spawn.getBlockY() - 5){
										return;
									}
								}
								current.lost.add(event.getPlayer());
								int count = 0;
								for(String pl : main.players){
									Player p = Bukkit.getPlayerExact(pl);
									if(p.isOnline()){
										if(!current.lost.contains(p)){
											count++;
										}
									}
								}
								main.sendPlace(count, event.getPlayer());
								current.spectate(event.getPlayer());
								// there's only one man standing
								if(count < 2){
									main.c_ += main.seconds-main.c;
									main.c = main.seconds; // just skips all the remaining seconds and sets to 60, current timer will do the rest
								}
							}
						}
					}else if(current.lost.contains(event.getPlayer())){
						if(main.started && main.ingame_started){
							if(event.getPlayer().getLocation().getBlockY() < current.spectatorlobby.getBlockY() || event.getPlayer().getLocation().getBlockY() > current.spectatorlobby.getBlockY()){
								//current.spectate(event.getPlayer());
								final Player p = event.getPlayer();
								final Minigame mg = current;
								final float b = p.getLocation().getYaw();
								final float c = p.getLocation().getPitch();
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
									@Override
									public void run() {
										p.setAllowFlight(true);
										p.setFlying(true);
										//p.teleport(new Location(p.getWorld(), p.getLocation().getBlockX(), mg.spectatorlobby.getBlockY(), p.getLocation().getBlockZ(), b, c));
										//p.getLocation().setYaw(b);
										//p.getLocation().setPitch(c);
									}
								}, 5);
							}
						}
					}	
				}

			}	
		}catch(Exception e){
			for(StackTraceElement et : e.getStackTrace()){
				System.out.println(et);
			}
		}

	}

}
