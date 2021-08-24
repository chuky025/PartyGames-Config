package whirss.partydeluxe.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import whirss.partydeluxe.Main;

public class OnPlayerTeleport implements Listener {
	
	private Main main;
	
	public OnPlayerTeleport(Main main) {
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		final int visibleDistance = main.getServer().getViewDistance() * 16;
		// Fix the visibility issue one tick later
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			@Override
			public void run() {
				// Refresh nearby clients
				final List<Player> nearby = main.getPlayersWithin(player, visibleDistance);
				// Hide every player
				main.updateEntities(player, nearby, false);
				// Then show them again
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					@Override
					public void run() {
						main.updateEntities(player, nearby, true);
					}
				}, 1);
			}
		}, 15);
	}

}
