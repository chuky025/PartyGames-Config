package whirss.minecraftparty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import whirss.minecraftparty.commands.AdminCommand;
import whirss.minecraftparty.commands.PlayerCommand;
import whirss.minecraftparty.events.OnBlockBreak;
import whirss.minecraftparty.events.OnBlockPlace;
import whirss.minecraftparty.events.OnDrop;
import whirss.minecraftparty.events.OnEntityDamage;
import whirss.minecraftparty.events.OnFlightAttempt;
import whirss.minecraftparty.events.OnHunger;
import whirss.minecraftparty.events.OnInventoryClick;
import whirss.minecraftparty.events.OnMove;
import whirss.minecraftparty.events.OnPlayerCommand;
import whirss.minecraftparty.events.OnPlayerJoin;
import whirss.minecraftparty.events.OnPlayerLeave;
import whirss.minecraftparty.events.OnPlayerPortalEnter;
import whirss.minecraftparty.events.OnPlayerShearSheep;
import whirss.minecraftparty.events.OnPlayerTeleport;
import whirss.minecraftparty.events.OnProjectileLand;
import whirss.minecraftparty.events.OnSignChange;
import whirss.minecraftparty.events.OnSignUse;
import whirss.minecraftparty.minigames.ChickenTag;
import whirss.minecraftparty.minigames.ColorMatch;
import whirss.minecraftparty.minigames.DeadEnd;
import whirss.minecraftparty.minigames.DisIntegration;
import whirss.minecraftparty.minigames.JumpnRun;
import whirss.minecraftparty.minigames.LastArcherStanding;
import whirss.minecraftparty.minigames.MineField;
import whirss.minecraftparty.minigames.SheepFreenzy;
import whirss.minecraftparty.minigames.SlapFight;
import whirss.minecraftparty.minigames.SmokeMonster;
import whirss.minecraftparty.minigames.Spleef;
import whirss.minecraftparty.sql.MainSQL;

public class Main extends JavaPlugin implements Listener {

	/* setup pattern:
	 * 
	 * # - - -
	 * # - - -
	 * # - - -
	 * # # # #
	 * 
	 * #=minigame
	 * 
	 * IMPORTANT: LOBBY SPAWN MUST BE ABOVE SPAWNS
	 */

	/*
	 * SETUP
	 * 
	 * 1. build main lobby
	 * 2. /mp setlobby
	 * 3. go to location somewhere UNDER lobby
	 * 4. /mp setup
	 * 
	 */

	public static Economy econ = null;

	public ArrayList<Minigame> minigames = new ArrayList<Minigame>();
	public ArrayList<String> players = new ArrayList<String>();
	public ArrayList<String> players_doublejumped = new ArrayList<String>();
	public ArrayList<String> players_outgame = new ArrayList<String>();
	public ArrayList<String> players_left = new ArrayList<String>();
	public HashMap<String, ItemStack[]> pinv = new HashMap<String, ItemStack[]>();
	public static HashMap<String, Boolean> hasChicken = new HashMap<String, Boolean>();

	public int min_players = 1;
	public boolean running = false;

	public int minreward = 0;
	public int maxreward = 0;
	public int item_minreward = 0;
	public int item_maxreward = 0;
	
	public int item_id = 264;
	
	boolean economy = true;
	boolean item_rewards = true;
	
	public Location mainlobby = null;

	public int seconds = 60;
	
	Main m;
	public MainSQL msql;

	String currentversion = "1.6";
	

	@Override
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		m = this;
		msql = new MainSQL(this);
		int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run(){
				if(isValidMinigame("ColorMatch") && isValidMinigame("Spleef") && isValidMinigame("MineField") && isValidMinigame("JumpnRun") && isValidMinigame("DeadEnd")){
					ColorMatch cm = new ColorMatch(m, m.getComponentForMinigame("ColorMatch", "spawn"), m.getLobby(), m.getComponentForMinigame("ColorMatch", "spectatorlobby"));
					minigames.add(cm);
					getServer().getPluginManager().registerEvents(cm, m);
					Spleef sp = new Spleef(m, m.getComponentForMinigame("Spleef", "spawn"), m.getLobby(), m.getComponentForMinigame("Spleef", "spectatorlobby"));
					minigames.add(sp);
					getServer().getPluginManager().registerEvents(sp, m);
					MineField mf = new MineField(m, m.getComponentForMinigame("MineField", "spawn"), m.getLobby(), m.getComponentForMinigame("MineField", "spectatorlobby"), m.getComponentForMinigame("MineField", "finishline"));
					minigames.add(mf);
					getServer().getPluginManager().registerEvents(mf, m);
					JumpnRun jr = new JumpnRun(m, m.getComponentForMinigame("JumpnRun", "spawn"), m.getLobby(), m.getComponentForMinigame("JumpnRun", "spectatorlobby"), m.getComponentForMinigame("JumpnRun", "finishline"));
					minigames.add(jr);
					getServer().getPluginManager().registerEvents(jr, m);
					DeadEnd de = new DeadEnd(m, m.getComponentForMinigame("DeadEnd", "spawn"), m.getLobby(), m.getComponentForMinigame("DeadEnd", "spectatorlobby"));
					minigames.add(de);
					getServer().getPluginManager().registerEvents(de, m);
					DisIntegration di = new DisIntegration(m, m.getComponentForMinigame("DisIntegration", "spawn"), m.getLobby(), m.getComponentForMinigame("DisIntegration", "spectatorlobby"));
					minigames.add(di);
					getServer().getPluginManager().registerEvents(di, m);
					LastArcherStanding las = new LastArcherStanding(m, m.getComponentForMinigame("LastArcherStanding", "spawn"), m.getLobby(), m.getComponentForMinigame("LastArcherStanding", "spectatorlobby"));
					minigames.add(las);
					getServer().getPluginManager().registerEvents(las, m);
					SheepFreenzy sf = new SheepFreenzy(m, m.getComponentForMinigame("SheepFreenzy", "spawn"), m.getLobby(), m.getComponentForMinigame("SheepFreenzy", "spectatorlobby"));
					minigames.add(sf);
					getServer().getPluginManager().registerEvents(sf, m);
					SmokeMonster sm = new SmokeMonster(m, m.getComponentForMinigame("SmokeMonster", "spawn"), m.getLobby(), m.getComponentForMinigame("SmokeMonster", "spectatorlobby"));
					minigames.add(sm);
					getServer().getPluginManager().registerEvents(sm, m);
					SlapFight slf = new SlapFight(m, m.getComponentForMinigame("SlapFight", "spawn"), m.getLobby(), m.getComponentForMinigame("SlapFight", "spectatorlobby"));
					minigames.add(slf);
					getServer().getPluginManager().registerEvents(slf, m);
					ChickenTag ct = new ChickenTag(m, m.getComponentForMinigame("ChickenTag", "spawn"), m.getLobby(), m.getComponentForMinigame("ChickenTag", "spectatorlobby"));
					minigames.add(ct);
					getServer().getPluginManager().registerEvents(ct, m);
				}
			}
		}, 20);

		getConfig().options().header("I recommend you to set auto_updating to true for possible future bugfixes.");
		
		// I'm running on windows, just making sure for Linux users:
		getConfig().addDefault("mysql.enabled", false);
		getConfig().addDefault("mysql.host", "127.0.0.1");
		getConfig().addDefault("mysql.database", "bukkit");
		getConfig().addDefault("mysql.user", "root");
		getConfig().addDefault("mysql.pw", "toor");
		getConfig().addDefault("config.auto_updating", true);
		getConfig().addDefault("config.min_players", 1);
		getConfig().addDefault("config.max_players", 50);
		getConfig().addDefault("config.game-on-join", false);
		getConfig().addDefault("config.max_reward", 30);
		getConfig().addDefault("config.min_reward", 10);
		getConfig().addDefault("config.use_economy", false);
		getConfig().addDefault("config.use_item_rewards", false);
		getConfig().addDefault("config.item_reward_maxamount", 10);
		getConfig().addDefault("config.item_reward_minamount", 3);
		getConfig().addDefault("config.item_reward_id", 264);
		getConfig().addDefault("config.scoreboardoutgame", true);
		getConfig().addDefault("config.announcements", true);
		getConfig().addDefault("config.seconds_for_each_minigame", 60); // ô.ô
		
		getConfig().addDefault("strings.you_left", "You left the game.");
		getConfig().addDefault("strings.next_round_30_seconds", "Next round in 30 seconds! You can leave with /mp leave.");
		
		getConfig().addDefault("strings.description.colormatch", "Jump to the color corresponding to the wool color in your inventory!");
		getConfig().addDefault("strings.description.deadend", "Don't fall while the blocks are disappearing behind you!");
		getConfig().addDefault("strings.description.disintegration", "Don't fall while the floor is disappearing!");
		getConfig().addDefault("strings.description.jumpnrun", "Jump to the finish!");
		getConfig().addDefault("strings.description.lastarcherstanding", "Shoot the others with the bow!");
		getConfig().addDefault("strings.description.minefield", "Run to the finish without touching the mines!");
		getConfig().addDefault("strings.description.sheepfreenzy", "Shear as many Sheeps as possible! Attention: Some of them explode.");
		getConfig().addDefault("strings.description.smokemonster", "Avoid the smoke monster!");
		getConfig().addDefault("strings.description.spleef", "Destroy the floor under your opponents to make them fall and lose!");
		getConfig().addDefault("strings.description.slapfight", "Slap the other players to fall! You can use Double Jump in case you fall, too.");
		getConfig().addDefault("strings.description.chickentag", "Pass the chicken to others or you'll lose!");

		getConfig().addDefault("strings.your_place", "You are <place> place.");

		Shop.initShop(this);
		
		getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		Shop.loadPrices(this);

		min_players = getConfig().getInt("config.min_players");

		minreward = getConfig().getInt("config.min_reward");
		maxreward = getConfig().getInt("config.max_reward");
		item_minreward = getConfig().getInt("config.item_reward_minamount");
		item_maxreward = getConfig().getInt("config.item_reward_maxamount");

		item_id = getConfig().getInt("config.item_reward_id"); 
		seconds = getConfig().getInt("config.seconds_for_each_minigame");
		
		if(minreward > maxreward){
			int temp = maxreward;
			maxreward = minreward;
			minreward = temp;
		}
		
		if(item_minreward > item_maxreward){
			int temp = item_maxreward;
			item_maxreward = item_minreward;
			item_minreward = temp;
		}

		economy = getConfig().getBoolean("config.use_economy");
		item_rewards = getConfig().getBoolean("config.use_item_rewards");
		
		int pluginId = 9703;
        Metrics metrics = new Metrics(this, pluginId);

		/*if(getConfig().getBoolean("config.auto_updating")){
			Updater updater = new Updater(this, 71596, this.getFile(), Updater.UpdateType.DEFAULT, false);
		}*/

		if(economy){
			if (!setupEconomy()) {
	            getLogger().severe(String.format("[%s] - No iConomy dependency found! Disabling Economy.", getDescription().getName()));
	            economy = false;
	        }
		}
		
		RegisterCommands();
		RegisterEvents();

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MinecraftParty by Whirss");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Version " + currentversion + " (Spigot " + Bukkit.getBukkitVersion() + " )");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------");
		
		//Update Checker
		new UpdateChecker(this, 86837).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
            	//getLogger().info("There is not a new update available.");
            } else {
            	getLogger().info("An update to MinecraftParty is available: " + version + ". You are on " + currentversion);
            	getLogger().info("Download it here: www.spigotmc.org/resources/86837/");
            }
        });
		
		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public void RegisterCommands() {
		this.getCommand("minecraftpartyadmin").setExecutor(new AdminCommand(this));
		this.getCommand("minecraftparty").setExecutor(new PlayerCommand(this));
	}
	
	public void RegisterEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new OnBlockBreak(this), this);
		pm.registerEvents(new OnBlockPlace(this), this);
		pm.registerEvents(new OnDrop(this), this);
		pm.registerEvents(new OnEntityDamage(this), this);
		pm.registerEvents(new OnFlightAttempt(this), this);
		pm.registerEvents(new OnHunger(this), this);
		pm.registerEvents(new OnInventoryClick(this), this);
		pm.registerEvents(new OnMove(this), this);
		pm.registerEvents(new OnPlayerCommand(this), this);
		pm.registerEvents(new OnPlayerLeave(this), this);
		pm.registerEvents(new OnPlayerJoin(this), this);
		pm.registerEvents(new OnPlayerPortalEnter(this), this);
		pm.registerEvents(new OnPlayerShearSheep(this), this);
		pm.registerEvents(new OnPlayerTeleport(this), this);
		pm.registerEvents(new OnProjectileLand(this), this);
		pm.registerEvents(new OnSignUse(this), this);
		pm.registerEvents(new OnSignChange(), this);
		
	}
	

	/*public void nextMinigame(Player p){
		// get current minigame and make winners
		// get new minigame and tp all to the new one
		p.setAllowFlight(false);
		p.setFlying(false);
		minigames.get(currentmg).getWinner();
		if(currentmg < minigames.size() - 1){
			currentmg += 1;
		}else{
			stop(currentid);
		}
		for(Minigame mg : minigames){
			mg.lost.clear();
		}
		minigames.get(currentmg).join(p);
	}*/

	public void win(Player p){
		if(p == null){
			getLogger().severe("Could not resolve winner: " + Integer.toString(currentmg));
			return;
		}
		//p.sendMessage(ChatColor.GOLD + "You won this round!");
		this.updatePlayerStats(p.getName(), "wins", getPlayerStats(p.getName(), "wins") + 1);
		Random r = new Random();
		int reward = r.nextInt((maxreward - minreward) + 1) + minreward;
		if(p.hasPermission("minecraftparty.double_coins")){
			reward = reward * 2;
		}else if(p.hasPermission("minecraftparty.triple_coins")){
			reward = reward * 3;
		}
		this.updatePlayerStats(p.getName(), "credits", getPlayerStats(p.getName(), "credits") + reward);		

		if(getConfig().getBoolean("config.announcements")){
			getServer().broadcastMessage(ChatColor.GOLD	+ p.getName() + " won this round and earned " + ChatColor.BLUE + Integer.toString(reward) + ChatColor.GOLD + " Credits!");
		}

		p.sendMessage("§aYou earned " + Integer.toString(reward) + " Credits this round.");

		msql.updateWinnerStats(p.getName(), reward);
		
		if(economy){
			EconomyResponse r_ = econ.depositPlayer(p.getName(), reward);
			if(!r_.transactionSuccess()) {
				getServer().getPlayer(p.getName()).sendMessage(ChatColor.RED + String.format("An error occured: %s", r_.errorMessage));
            }
		}
		
		if(item_rewards){
			int reward_ = r.nextInt((item_maxreward - item_minreward) + 1) + item_minreward;
			if(p.hasPermission("minecraftparty.double_coins")){
				reward_ = reward_ * 2;
			}else if(p.hasPermission("minecraftparty.triple_coins")){
				reward_ = reward_ * 3;
			}
			p.sendMessage("§aYou earned " + Integer.toString(reward_) + " " + Material.getMaterial(item_id).name() + " this round. You'll get them at the end.");
			if(rewardcount.containsKey(p.getName())){
				reward_ += rewardcount.get(p.getName());
			}
			rewardcount.put(p.getName(), reward_);
		}
		
		updateScoreboardOUTGAME(p.getName());
	}
	
	public final HashMap<String, Integer> rewardcount = new HashMap<String, Integer>();
	
	public void giveItemRewards(final Player p, boolean task){
		if(task){
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run(){
					if(!rewardcount.containsKey(p.getName())){
						return;
					}
					p.getInventory().addItem(new ItemStack(item_id, rewardcount.get(p.getName())));
					p.updateInventory();
					rewardcount.remove(p.getName());
				}
			}, 10L);
		}else{
			if(!rewardcount.containsKey(p.getName())){
				return;
			}
			p.getInventory().addItem(new ItemStack(item_id, rewardcount.get(p.getName())));
			p.updateInventory();
			rewardcount.remove(p.getName());
		}
	}


	/**
	 * NEW TIMER PART
	 */
	public int c = 0; // count
	public int c_ = 0; 
	public boolean ingame_started = false;
	public boolean started = false;
	BukkitTask t = null;
	public int currentmg = 0;
	BukkitTask currentid = null;
	public void secondsTick(int disabledMinigamesC){
		
		if(!ingame_started){
			return;
		}
		
		// update scoreboard
		updateScoreboard(seconds - c);

		// stop the whole party after some rounds
		if(c_ > (minigames.size() - disabledMinigamesC) * seconds - 3){
			//Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable(){
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				public void run(){
					startNew();
				}
			}, 30 * 20); // 30 secs
			t.cancel();
			started = false;
			ingame_started = false;

			if(currentid != null){
				currentid.cancel();
			}
			
			
			if(!minigames.get(minigames.size() - 1).name.toLowerCase().equalsIgnoreCase("minefield")){
				minigames.get(minigames.size() - 1).reset(this.getComponentForMinigame(minigames.get(minigames.size() - 1).name, "spawn"));
			}else{
				Location t = this.getComponentForMinigame("MineField", "spawn");
				minigames.get(minigames.size() - 1).reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
			}
			
			ArrayList<String> remove = new ArrayList<String>();
			for(String pl : players){
				Player p = Bukkit.getPlayerExact(pl);
				if(p.isOnline()){
					minigames.get(minigames.size() - 1).leave(p);
					p.sendMessage(ChatColor.GOLD + getConfig().getString("strings.next_round_30_seconds"));
					p.getInventory().clear();
					p.updateInventory();
					updateScoreboardOUTGAME(pl);
				}else{
					remove.add(p.getName());
				}
			}

			// removes players that aren't online anymore
			for(String p : remove){
				players.remove(p);
			}

			remove.clear();

			currentmg = -1;
			currentid = null;

			// reset all:
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				public void run(){
					resetAll(false);
				}
			}, 20L);
			
			c = 0;
			c_ = 0;
			if(currentid != null){
				currentid.cancel();
			}
		}

		// start the next minigame after 60 seconds
		if(c == seconds || c > seconds){
			c = 0;
			if(currentid != null){
				currentid.cancel();
			}
			//currentid = nextMinigame();
			nextMinigame();
		}


		c += 1;
		c_ += 1;
	}

	
	//public BukkitTask nextMinigame(){
	public void nextMinigame(){
		
		ingame_started = false;
		Minigame cmg = null;
		
		if(currentmg > -1){
			cmg = minigames.get(currentmg);
			//System.out.println(currentmg + " " + cmg.isEnabled());
		}
		
		// check disabled minigames at the end
		int count = 0;
		for(int i = minigames.size() - 1; i > -1; i--){
			if(!minigames.get(i).isEnabled()){
				count++;
			}else{
				break;
			}
		}
		
		if(currentmg < minigames.size() - 1 - count){
			currentmg += 1;
		}else{
			if(currentid != null){
				stop(currentid);
				if(minigames.get(minigames.size() -1).name.equalsIgnoreCase("minefield")){
					Location t = this.getComponentForMinigame("MineField", "spawn");
					minigames.get(minigames.size() - 1).reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
				}
				minigames.get(minigames.size() - 1).reset(this.getComponentForMinigame(minigames.get(minigames.size() - 1).name, "spawn"));
				return;
			}
		}
		if(cmg != null){
			
			// reset current minigame
			final Minigame cmg_ = cmg;
			if(!cmg.name.toLowerCase().equalsIgnoreCase("minefield")){
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					public void run(){
						cmg_.reset(m.getComponentForMinigame(cmg_.name, "spawn"));
					}
				}, 20L);
			}else{
				Location t = this.getComponentForMinigame("MineField", "spawn");
				cmg.reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
			}
			
			if(cmg.isEnabled()){
				cmg.getWinner();
			}
			
			if(currentmg > -1){
				if(!minigames.get(currentmg).isEnabled()){
					currentscore.clear();
					for(Minigame mg : minigames){
						mg.lost.clear();
					}
					//System.out.println(c_ + " " + (minigames.size() - disabledMinigamesCount) * seconds);
					//c_ += seconds;
					//System.out.println("Turned to " + c_ + " " + (minigames.size() - disabledMinigamesCount) * seconds);
					nextMinigame();
					return;
				}
			}
		}
		currentscore.clear();
		for(Minigame mg : minigames){
			mg.lost.clear();
		}

		//System.out.println(currentmg);
		
		if(currentmg > -1){
			if(!minigames.get(currentmg).isEnabled()){
				currentscore.clear();
				for(Minigame mg : minigames){
					mg.lost.clear();
				}
				//System.out.println(c_ + " " + (minigames.size() - disabledMinigamesCount) * seconds);
				//c_ += seconds;
				//System.out.println("Turned to " + c_ + " " + (minigames.size() - disabledMinigamesCount) * seconds);
				nextMinigame();
				return;
			}
		}
		
		for(String pl : players){
			final Player p = Bukkit.getPlayerExact(pl);
			if(p.isOnline()){
				p.setAllowFlight(false);
				p.setFlying(false);
				p.getInventory().clear();

				if(minigames.size() < 1){ // that looks like bs, I should fix that
					if(currentmg < minigames.size() - 1){
						currentmg += 1;
					}
					minigames.get(currentmg).join(p);
				}else{
					minigames.get(currentmg).join(p);
				}
			}
		}


		
		if(currentmg > -1 && currentmg < minigames.size()){
			//return minigames.get(currentmg).start();
			minigames.get(currentmg).startCooldown();
		}else{
			//return null;
		}
	}
	
	public void registerMinigameStart(BukkitTask minigame){
		currentid = minigame;
	}

	int disabledMinigamesCount = 0;
	public void startNew(){
		if(!started && !ingame_started){
			if(players.size() > min_players - 1){
				// reset all
				for(Minigame m : minigames){
					m.lost.clear();
				}
				currentmg = -1;
				currentid = null;

				// randomize minigames order
				this.shuffleMinigames();
				
				// start first minigame
				//currentid = nextMinigame();
				nextMinigame();
				
				// calculate the amount of disabled minigames
				disabledMinigamesCount = 0;
				for(Minigame m : minigames){
					if(!m.isEnabled()){
						disabledMinigamesCount++;
					}
				}
				
				// start main timer
				//t = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
				t = Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable(){
					public void run(){
						secondsTick(disabledMinigamesCount);
					}
				}, 120, 20);

				started = true;
			}	
		}
	}
	/**
	 * NEW TIMER PART
	 */

	public HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	public Main main;

	public void updateScoreboard(int c){

		ScoreboardManager manager = Bukkit.getScoreboardManager();

		boolean isNeeded = false;
		boolean isSheep = false;
		
		if(minigames.get(currentmg).name.equalsIgnoreCase("minefield") || minigames.get(currentmg).name.equalsIgnoreCase("jumpnrun")){
			isNeeded = true;
		}

		if(minigames.get(currentmg).name.equalsIgnoreCase("sheepfreenzy")){
			isNeeded = true;
			isSheep = true;
		}
		
		for(String pl : players){
			Player p = Bukkit.getPlayerExact(pl);
			Scoreboard board = manager.getNewScoreboard();

			Objective objective = board.registerNewObjective("test", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			objective.setDisplayName("[" + Integer.toString(currentmg + 1) + "/" + Integer.toString(minigames.size()) + "] [" + Integer.toString(c) + "]");

			for(String pl_ : players){
				Player p_ = Bukkit.getPlayerExact(pl_);
				if(isNeeded){
					int score = 0;
					if(!isSheep){
						score = p_.getLocation().getBlockZ() - minigames.get(currentmg).finish.getBlockZ();
						if(currentscore.containsKey(pl_)){
							int oldscore = currentscore.get(pl_);
							if(score > oldscore){
								currentscore.put(pl_, score);
							}else{
								score = oldscore;
							}
						}else{
							currentscore.put(pl_, score);
						}
						if(currentmg > -1 && currentmg < minigames.size()){
							if(minigames.get(currentmg).lost.contains(p_)){
								String tempn = ChatColor.RED + pl_;
								if(p_.getName().length() > 14){
									tempn = ChatColor.RED + pl_.substring(0, pl_.length() - 3);
								}
								objective.getScore(Bukkit.getOfflinePlayer(tempn)).setScore(score);
							}else{
								String tempn = ChatColor.GREEN + pl_;
								if(p_.getName().length() > 14){
									tempn = ChatColor.GREEN + pl_.substring(0, pl_.length() - 3);
								}
								objective.getScore(Bukkit.getOfflinePlayer(tempn)).setScore(score);
							}
						}else{
							objective.getScore(p_).setScore(score);
						}
					}else{
						if(!currentscore.containsKey(pl_)){
							currentscore.put(pl_, 0);
						}
						objective.getScore(p_).setScore(currentscore.get(pl_));
					}
				}else{
					if(currentmg > -1 && currentmg < minigames.size()){
						if(minigames.get(currentmg).lost.contains(p_)){
							String tempn = ChatColor.RED + pl_;
							if(p_.getName().length() > 14){
								tempn = ChatColor.RED + pl_.substring(0, pl_.length() - 3);
							}
							objective.getScore(Bukkit.getOfflinePlayer(tempn)).setScore(0);
						}else{
							String tempn = ChatColor.GREEN + pl_;
							if(p_.getName().length() > 14){
								tempn = ChatColor.GREEN + pl_.substring(0, pl_.length() - 3);
							}
							objective.getScore(Bukkit.getOfflinePlayer(tempn)).setScore(0);
						}
					}else{
						objective.getScore(p_).setScore(0);
					}
				}
			}

			p.setScoreboard(board);
		}
	}


	public void updateScoreboardOUTGAME(final String player){
		
		if(!getConfig().getBoolean("config.scoreboardoutgame")){
			return;
		}
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();

		final Player p = Bukkit.getPlayer(player);

		Scoreboard board = manager.getNewScoreboard();

		Objective objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		objective.setDisplayName(ChatColor.GOLD + "MinecraftParty!");

		objective.getScore(Bukkit.getOfflinePlayer("Credits")).setScore(this.getPlayerStats(player, "credits"));

		p.setScoreboard(board);

		Runnable r = new Runnable() {
	        public void run() {
	        	m.removeScoreboard(p);
	        }
	    };
	    
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, r, 20 * 10);

	}

	public void removeScoreboard(Player p) {
		try {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			
			sc.clearSlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void start(){
		// if not running -> start
		// else just join current game
		//    if no current game, join into waiting lobby

		if(players.size() > min_players - 1){
			// reset all
			for(Minigame m : minigames){
				m.lost.clear();
			}
			currentmg = 0;
			currentid = 0;

			// every player joins again (or maybe first time)
			for(Player p : players){
				minigames.get(0).join(p);
			}
			final int stopid = minigames.get(0).start();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run(){
					Bukkit.getServer().getScheduler().cancelTask(stopid);
				}
			}, 1200);

			// main running timer
			if(!running){
				final int id__ = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run(){
						int count = 0;
						ArrayList<Player> remove = new ArrayList<Player>();
						for(Player p : players){
							if(p.isOnline()){
								nextMinigame(p);
								count += 1;
							}else{
								remove.add(p);
							}
						}

						for(Player p : remove){
							players.remove(p);
						}

						remove.clear();

						if(count < min_players){ // one player left
							stopFull();
						}
					}
				}, 1200, 1200); // each 60 seconds -> change minigame	

				currentid = id__;

				int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run(){
						start();
					}
				}, minigames.size() * 1200 + 20 * 30); // 20 * 30: wait 30 seconds after all games	

				int id_ = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run(){
						stop(id__);
					}
				}, minigames.size() * 1200 - 40);

				running = true;
			}
		}
	}*/

	public void stop(BukkitTask id){
		id.cancel();
		running = false;

		for(Minigame mg : minigames){
			mg.lost.clear();
		}
		players_doublejumped.clear();
		
		ArrayList<Player> remove = new ArrayList<Player>();
		for(String pl : players){
			Player p = Bukkit.getPlayerExact(pl);
			if(p.isOnline()){
				/*for (PotionEffect effect : p.getActivePotionEffects()) {
					if(p.hasPotionEffect(effect.getType())){
						try {
							p.removePotionEffect(effect.getType());
						} catch (Exception e) {
							
						}
					}
				}*/
				if(p.hasPotionEffect(PotionEffectType.JUMP)){
					p.removePotionEffect(PotionEffectType.JUMP);
				}
				try{
					minigames.get(minigames.size() - 1).leave(p);
				}catch(Exception e){
					if(currentmg > -1 && currentmg < minigames.size()){
						minigames.get(currentmg).leave(p);
					}
				}
				
				p.sendMessage(ChatColor.GOLD + getConfig().getString("strings.next_round_30_seconds"));
				p.getInventory().clear();
				p.updateInventory();
			}else{
				remove.add(p);
			}
		}

		// removes players that aren't online anymore
		for(Player p : remove){
			players.remove(p.getName());
		}

		remove.clear();

		currentmg = -1;

		Bukkit.getScheduler().runTask(this, new Runnable(){
			public void run(){
				resetAll(false);
			}
		});

	}

	public void stopFull(){
		Bukkit.getServer().getScheduler().cancelTasks(this);
		
		for(String pl : players){
			final Player p = Bukkit.getPlayerExact(pl);
			if(p.isOnline()){
				p.getInventory().clear();
				p.updateInventory();
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					public void run(){
						p.getInventory().setContents(pinv.get(p.getName()));
						p.updateInventory();
					}
				}, 10L);
				
				minigames.get(minigames.size() - 1).leave(p);
				p.sendMessage(ChatColor.RED + "Stopping minigame, because there are less players than required! (" + Integer.toString(min_players) + ")");
			}
		}

		running = false;
		started = false;
		ingame_started = false;
		players.clear();
		players_doublejumped.clear();
		currentmg = 0;

		Bukkit.getScheduler().runTaskLater(this, new Runnable(){
			public void run(){
				resetAll(true);
			}
		}, 20L);
	}
	
	public void stopFull(Player p_){
		stopFull();
		updateScoreboardOUTGAME(p_.getName());
	}

	public Location getLobby(){
		if(!getConfig().isSet("lobby.location")){
			getLogger().severe(ChatColor.BLUE + "A LOBBY COULD NOT BE FOUND. PLEASE FIX THIS WITH /mp setlobby.");
			for(Player p : Bukkit.getOnlinePlayers()){
				if(p.isOp()){
					p.sendMessage(ChatColor.BLUE + "[MinecraftParty] " + ChatColor.RED + "A lobby could NOT be found, which leads to errors in the console. Please fix this with " + ChatColor.GOLD + "/mp setlobby.");
				}
			}
		}
		return new Location(getServer().getWorld(getConfig().getString("lobby.world")), getConfig().getInt("lobby.location.x"), getConfig().getInt("lobby.location.y"), getConfig().getInt("lobby.location.z"));
	}

	public Location getComponentForMinigame(String minigame, String component, String count){
		if(isValidMinigame(minigame)){
			String base = "minigames." + minigame + "." + component + count;
			return new Location(Bukkit.getWorld(getConfig().getString(base + ".world")), getConfig().getInt(base + ".location.x"), getConfig().getInt(base + ".location.y"), getConfig().getInt(base + ".location.z"));
		}
		return null;
	}

	public Location getComponentForMinigame(String minigame, String component){
		if(isValidMinigame(minigame)){
			String base = "minigames." + minigame + "." + component;
			return new Location(Bukkit.getWorld(getConfig().getString(base + ".world")), getConfig().getInt(base + ".location.x"), getConfig().getInt(base + ".location.y"), getConfig().getInt(base + ".location.z"));
		}
		return null;
	}

	public void saveComponentForMinigame(String minigame, String component, Location comploc){
		String base = "minigames." + minigame + "." + component;
		getConfig().set(base + ".world", comploc.getWorld().getName());
		getConfig().set(base + ".location.x", comploc.getBlockX());
		getConfig().set(base + ".location.y", comploc.getBlockY());
		getConfig().set(base + ".location.z", comploc.getBlockZ());
		this.saveConfig();
	}

	public boolean isValidMinigame(String minigame){
		if(getConfig().isSet("minigames." + minigame) && getConfig().isSet("minigames." + minigame + ".lobby") && getConfig().isSet("minigames." + minigame + ".spawn") && getConfig().isSet("minigames." + minigame + ".spectatorlobby")){
			return true;
		}
		return false;
	}

	public void setupAll(Location start){
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();

		ColorMatch.setup(start, this, "ColorMatch");
		Spleef.setup(new Location(start.getWorld(), x, y, z + 64 + 20), this, "Spleef");
		MineField.setup(new Location(start.getWorld(), x, y, z + 64 * 2 + 20 * 2), this, "MineField");
		JumpnRun.setup(new Location(start.getWorld(), x, y, z + 64 * 3 + 20 * 3), this, "JumpnRun");
		DeadEnd.setup(new Location(start.getWorld(), x + 64 + 20, y, z), this, "DeadEnd");
		DisIntegration.setup(new Location(start.getWorld(), x + 64 * 2 + 20 * 2, y, z), this, "DisIntegration");
		LastArcherStanding.setup(new Location(start.getWorld(), x + 64 * 3 + 20 * 3, y, z), this, "LastArcherStanding");
		SheepFreenzy.setup(new Location(start.getWorld(), x + 64 + 20, y, z + 64 + 20), this, "SheepFreenzy");
		SmokeMonster.setup(new Location(start.getWorld(), x + 64 * 2 + 20 * 2, y, z + 64 + 20), this, "SmokeMonster");
		SlapFight.setup(new Location(start.getWorld(), x + 64 * 3 + 20 * 3, y, z + 64 + 20), this, "SlapFight");
		ChickenTag.setup(new Location(start.getWorld(), x + 64 + 20, y, z + 64 * 2 + 20 * 2), this, "ChickenTag");

		
		/*
		 * next minigame locations: (TODO FOR LATER USE)
		 * 
		 * new Location(start.getWorld(), x, y, z + 64 * 2 + 20 * 2) [MINEFIELD]
		 * new Location(start.getWorld(), x, y, z + 64 * 3 + 20 * 3) [JUMPNRUN]
		 * new Location(start.getWorld(), x + 64 + 20, y, z) [DEADEND]
		 * new Location(start.getWorld(), x + 64 * 2 + 20 * 2, y, z) [DISINTEGRATION]
		 * new Location(start.getWorld(), x + 64 * 3 + 20 * 3, y, z) [LASTARCHERSTANDING]
		 * 
		 * would create the following pattern:
		 * 
		 * # - - -
		 * # - - -
		 * # - - -
		 * # # # #
		 * 
		 * #=minigame
		 * 
		 * IMPORTANT: LOBBY SPAWN MUST BE ABOVE SPAWNS!
		 */ 

		minigames.clear();
		minigames.add(new ColorMatch(this, this.getComponentForMinigame("ColorMatch", "spawn"), this.getComponentForMinigame("ColorMatch", "lobby"), this.getComponentForMinigame("ColorMatch", "spectatorlobby")));
		minigames.add(new Spleef(this, this.getComponentForMinigame("Spleef", "spawn"), this.getComponentForMinigame("Spleef", "lobby"), this.getComponentForMinigame("Spleef", "spectatorlobby")));
		minigames.add(new MineField(this, this.getComponentForMinigame("MineField", "spawn"), this.getComponentForMinigame("MineField", "lobby"), this.getComponentForMinigame("MineField", "spectatorlobby"), m.getComponentForMinigame("MineField", "finishline")));
		minigames.add(new JumpnRun(this, this.getComponentForMinigame("JumpnRun", "spawn"), this.getComponentForMinigame("JumpnRun", "lobby"), this.getComponentForMinigame("JumpnRun", "spectatorlobby"), m.getComponentForMinigame("JumpnRun", "finishline")));
		minigames.add(new DeadEnd(this, this.getComponentForMinigame("DeadEnd", "spawn"), this.getComponentForMinigame("DeadEnd", "lobby"), this.getComponentForMinigame("DeadEnd", "spectatorlobby")));
		minigames.add(new DisIntegration(this, this.getComponentForMinigame("DisIntegration", "spawn"), this.getComponentForMinigame("DisIntegration", "lobby"), this.getComponentForMinigame("DisIntegration", "spectatorlobby")));
		minigames.add(new LastArcherStanding(this, this.getComponentForMinigame("LastArcherStanding", "spawn"), this.getComponentForMinigame("LastArcherStanding", "lobby"), this.getComponentForMinigame("LastArcherStanding", "spectatorlobby")));
		minigames.add(new SheepFreenzy(this, this.getComponentForMinigame("SheepFreenzy", "spawn"), this.getComponentForMinigame("SheepFreenzy", "lobby"), this.getComponentForMinigame("SheepFreenzy", "spectatorlobby")));
		minigames.add(new SmokeMonster(this, this.getComponentForMinigame("SmokeMonster", "spawn"), this.getComponentForMinigame("SmokeMonster", "lobby"), this.getComponentForMinigame("SmokeMonster", "spectatorlobby")));
		minigames.add(new SlapFight(this, this.getComponentForMinigame("SlapFight", "spawn"), this.getComponentForMinigame("SlapFight", "lobby"), this.getComponentForMinigame("SlapFight", "spectatorlobby")));
		minigames.add(new ChickenTag(this, this.getComponentForMinigame("ChickenTag", "spawn"), this.getComponentForMinigame("ChickenTag", "lobby"), this.getComponentForMinigame("ChickenTag", "spectatorlobby")));

		getLogger().info("[MinecraftParty] Finished Setup.");
	}

	public void resetAll(boolean flag){
		getLogger().info("Resetting in ALL mode: " +  Boolean.toString(flag));
		
		if(flag){
			for(Minigame m : minigames){
				if(m.name.toLowerCase().equalsIgnoreCase("minefield")){
					Location t = this.getComponentForMinigame("MineField", "spawn");
					m.reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
				}else{
					m.reset(this.getComponentForMinigame(m.name, "spawn"));
				}
				
			}
			/*ColorMatch.reset(this.getComponentForMinigame("ColorMatch", "spawn"));
			Spleef.reset(this.getComponentForMinigame("Spleef", "spawn"));
			Location t = this.getComponentForMinigame("MineField", "spawn");
			MineField.reset(new Location(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ() + 30));
			DeadEnd.reset(this.getComponentForMinigame("DeadEnd", "spawn"));
			DisIntegration.reset(this.getComponentForMinigame("DisIntegration", "spawn"));*/
		}
	}


	/***
	 * saves player statistics
	 * @param player
	 * @param component component to be updated; can be "wins" or "credits"
	 * @param value value to be saved
	 */
	public void updatePlayerStats(String player, String component, int value){
		getConfig().set(player + "." + component, value);
		this.saveConfig();
	}

	public int getPlayerStats(String player, String component){
		int ret = 0;
		if(getConfig().isSet(player + "." + component)){
			ret = getConfig().getInt(player + "." + component);
		}
		return ret;
	}
	
	public void outputLeaderboardsByCredits(Player p){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        
		Set<String> keys = getConfig().getKeys(false);
		keys.remove("mysql");
		keys.remove("config");
		keys.remove("shop");
		keys.remove("minigames");
		keys.remove("lobby");
		keys.remove("strings");
		for(String key : keys){
			map.put(key, getConfig().getInt(key + ".credits"));
		}
		
		sorted_map.putAll(map);
		
		int i = 0;
		
		for(String player : sorted_map.keySet()){
			if(i > 10){
				return;
			}
			i++;
			p.sendMessage(ChatColor.DARK_PURPLE + player + ChatColor.GOLD + " : " + ChatColor.DARK_PURPLE + getConfig().getInt(player + ".credits"));
		}
	}
	
	public void outputLeaderboardsByWins(Player p){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        
		Set<String> keys = getConfig().getKeys(false);
		keys.remove("mysql");
		keys.remove("config");
		keys.remove("shop");
		keys.remove("minigames");
		keys.remove("lobby");
		keys.remove("strings");
		for(String key : keys){
			map.put(key, getConfig().getInt(key + ".wins"));
		}
		
		sorted_map.putAll(map);
		
		int i = 0;
		
		for(String player : sorted_map.keySet()){
			if(i > 10){
				return;
			}
			i++;
			p.sendMessage(ChatColor.DARK_PURPLE + player + ChatColor.GOLD + " : " + ChatColor.DARK_PURPLE + getConfig().getInt(player + ".wins"));
		}
	}
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
	}

	
	
	public void sendPlace(int count, Player p){
		String place = Integer.toString(count + 1) + "th";
		if(count == 0){
			place = Integer.toString(count + 1) + "st";
		}else if(count == 1){
			place = Integer.toString(count + 1) + "nd";
		}else if(count == 2){
			place = Integer.toString(count + 1) + "rd";
		}
		p.sendMessage(ChatColor.BLUE + getConfig().getString("strings.your_place").replaceAll("<place>", place));
	}
	
	
	public void disableMinigame(CommandSender sender, String minigame){
		if(!running){
			for(Minigame mg : minigames){
				if(mg.name.toLowerCase().equalsIgnoreCase(minigame)){
					mg.setEnabled(false);
					sender.sendMessage(ChatColor.RED + "Disabled " + mg.name + ".");
					return;
				}
			}
			sender.sendMessage(ChatColor.RED + "Could not find given Minigame.");
		}else{
			sender.sendMessage(ChatColor.RED + "You cannot change the state of a minigame while a game is running.");
		}
	}
	
	public void enableMinigame(CommandSender sender, String minigame){
		if(!running){
			for(Minigame mg : minigames){
				if(mg.name.toLowerCase().equalsIgnoreCase(minigame)){
					mg.setEnabled(true);
					sender.sendMessage(ChatColor.GREEN + "Enabled " + mg.name + ".");
					return;
				}
			}
			sender.sendMessage(ChatColor.RED + "Could not find given Minigame.");
		}else{
			sender.sendMessage(ChatColor.RED + "You cannot change the state of a minigame while a game is running.");
		}
	}
	
	public void shuffleMinigames(){
		Collections.shuffle(minigames);
	}
	
	
	
	// Teleportation fix
	// I really think this is nothing important, but whatever, people need it so much
	
	// Thanks to Comphenix and mbaxter
	// Ref: https://forums.bukkit.org/threads/invisible-teleport-bug-really-need-it-fixed.102135/
	
	// Nope nope nope nope sometimes simply crashes
	// will look into that later
	

	public void updateEntities(Player tpedPlayer, List<Player> players_, boolean visible) {
		for (Player player : players_) {
			if (visible) {
				tpedPlayer.showPlayer(player);
				player.showPlayer(tpedPlayer);
			} else {
				tpedPlayer.hidePlayer(player);
				player.hidePlayer(tpedPlayer);
			}
		}
	}

	public List<Player> getPlayersWithin(Player player, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : getServer().getOnlinePlayers()) {
			if (p != player && p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				res.add(p);
			}
		}
		return res;
	}
}
