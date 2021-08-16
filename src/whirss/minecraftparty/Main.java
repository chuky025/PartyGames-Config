package whirss.minecraftparty;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.clip.placeholderapi.PlaceholderAPI;
import whirss.minecraftparty.commands.AdminCommand;
import whirss.minecraftparty.commands.PlayerCommand;
import whirss.minecraftparty.events.OnBlockBreak;
import whirss.minecraftparty.events.OnBlockPlace;
import whirss.minecraftparty.events.OnDrop;
import whirss.minecraftparty.events.OnEntityDamage;
import whirss.minecraftparty.events.OnFlightAttempt;
import whirss.minecraftparty.events.OnHunger;
import whirss.minecraftparty.events.OnInteractEvent;
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
import whirss.minecraftparty.minigames.ColorMatch;
import whirss.minecraftparty.minigames.DeadEnd;
import whirss.minecraftparty.minigames.JumpnRun;
import whirss.minecraftparty.minigames.LastArcherStanding;
import whirss.minecraftparty.minigames.MineField;
import whirss.minecraftparty.minigames.RedAlert;
import whirss.minecraftparty.minigames.SheepFreenzy;
import whirss.minecraftparty.minigames.SlapFight;
import whirss.minecraftparty.minigames.SmokeMonster;
import whirss.minecraftparty.minigames.Spleef;
import whirss.minecraftparty.sql.MainSQL;

public class Main extends JavaPlugin implements Listener {

	public static Economy econ = null;

	public ArrayList<Minigame> minigames = new ArrayList<Minigame>();
	public ArrayList<String> players = new ArrayList<String>();
	public ArrayList<String> players_doublejumped = new ArrayList<String>();
	public ArrayList<String> players_outgame = new ArrayList<String>();
	public ArrayList<String> players_left = new ArrayList<String>();
	public HashMap<String, ItemStack[]> pinv = new HashMap<String, ItemStack[]>();
	
	public boolean running = false;
	
	//plugin
	public boolean update = true;
	public boolean placeholderapi = false;
	
	public boolean bungee = false;
	public boolean connect_hub = true;
	public boolean shutdown = true;
	public String hub = null;
	
	//game
	public int min_players = 1;
	public int max_players = 50;
	public boolean announcements = true;
	public int seconds = 60;
	
	//rewards

	public boolean credits_enable = true;
	public boolean economy = true;
	public int credits_minreward = 0;
	public int credits_maxreward = 0;
	public int item_minreward = 0;
	public int item_maxreward = 0;
	public int item_id = 264;
	boolean item_rewards = true;
	
	
	//in game
	public String your_place = null;
	public String place1 = null;
	public String place2 = null;
	public String place3 = null;
	public int reward = 0;
	
	
	public Location mainlobby = null;
	
	Main m;
	public MainSQL msql;

	String currentversion = "1.8";
	
	//config files
	private FileConfiguration messages = null;
	private File messagesFile = null;
	private FileConfiguration settings = null;
	private File settingsFile = null;
	private FileConfiguration scoreboard = null;
	private File scoreboardFile = null;
	private FileConfiguration shop = null;
	private File shopFile = null;
	private FileConfiguration titles = null;
	private File titlesFile = null;
	private FileConfiguration mysql = null;
	private File mysqlFile = null;
	
	//minigames folder
	private FileConfiguration colormatch = null;
	private File colormatchFile = null;
	private FileConfiguration deadend = null;
	private File deadendFile = null;
	private FileConfiguration redalert = null;
	private File redalertFile = null;
	private FileConfiguration jumpnrun = null;
	private File jumpnrunFile = null;
	private FileConfiguration lastarcherstanding = null;
	private File lastarcherstandingFile = null;
	private FileConfiguration minefield = null;
	private File minefieldFile = null;
	private FileConfiguration sheepfreenzy = null;
	private File sheepfreenzyFile = null;
	private FileConfiguration slapfight = null;
	private File slapfightFile = null;
	private FileConfiguration smokemonster = null;
	private File smokemonsterFile = null;
	private FileConfiguration spleef = null;
	private File spleefFile = null;

	@Override
	public void onEnable(){
		m = this;
		msql = new MainSQL(this);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
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
					RedAlert di = new RedAlert(m, m.getComponentForMinigame("RedAlert", "spawn"), m.getLobby(), m.getComponentForMinigame("RedAlert", "spectatorlobby"));
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
				}
			}
		}, 20);
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------------------------------------------------------");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + " __  __ _                            __ _   ____            _");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "|  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "| |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "| |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "|_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "                                                                |___/");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Version: " + currentversion + "  /  Source Code: https://github.com/Whirss/MinecraftParty");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------------------------------------------------------");
		if(getConfig().getInt("no_modify") == 17) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[MinecraftParty] Your configuration is outdated and will probably not work in this version of the plugin.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[MinecraftParty] Update your configuration or use an older version of the plugin.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[MinecraftParty] Tutorial to update the configuration: youtu.be/soon");
		}
		
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
        //config
		registerConfig();
		registerSettings();
		registerMessages();
		registerScoreboard();
		registerShop();
		registerTitles();
		registerMysql();
		
		//minigames folder
        registerColorMatch();
        registerDeadEnd();
        registerJumpnRun();
        registerLastArcherStanding();
        registerMineField();
        registerRedAlert();
        registerSheepFreenzy();
        registerSlapFight();
        registerSmokeMonster();
        registerSpleef();
        
		//Other
		RegisterCommands();
		RegisterEvents();
		Shop.loadPrices(this);
		
		
		
		
		
		//plugin
		update = getSettings().getBoolean("settings.plugin.update_check");
		placeholderapi = getSettings().getBoolean("settings.plugin.enable_placeholderapi");
		bungee = getSettings().getBoolean("settings.plugin.bungee.enable");
		connect_hub = getSettings().getBoolean("settings.plugin.bungee.connect_to_hub");
		shutdown = getSettings().getBoolean("settings.plugin.bungee.shutdown_when_game_ends");
		
		//game
		min_players = getSettings().getInt("settings.game.min_players");
		max_players = getSettings().getInt("settings.game.max_players");
		announcements = getSettings().getBoolean("settings.game.announcements");
		seconds = getSettings().getInt("settings.game.seconds_for_each_minigame");
		
		//rewards
		credits_enable = getSettings().getBoolean("settings.rewards.credits.enable");
		economy = getSettings().getBoolean("settings.rewards.credits.use_economy");
		credits_minreward = getSettings().getInt("settings.rewards.credits.min_amount");
		credits_maxreward = getSettings().getInt("settings.rewards.credits.max_amount");
		
		item_rewards = getSettings().getBoolean("config.use_item_rewards");
		item_minreward = getSettings().getInt("settings.rewards.items.min_amount");
		item_maxreward = getSettings().getInt("settings.rewards.items.max_amount");
		item_id = getSettings().getInt("settings.rewards.items.item_id"); 
		
		if(credits_minreward > credits_maxreward){
			int temp = credits_maxreward;
			credits_maxreward = credits_minreward;
			credits_minreward = temp;
		}
		
		if(item_minreward > item_maxreward){
			int temp = item_maxreward;
			item_maxreward = item_minreward;
			item_minreward = temp;
		}
		
		int pluginId = 9703;
        new Metrics(this, pluginId);

		if(economy && !setupEconomy()){
			getLogger().severe(String.format("[%s] - No iConomy dependency found! Disabling Economy.", getDescription().getName()));
            economy = false;
		}
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PAPIVariables(this).register();
        } else {
            if(placeholderapi){
            	Bukkit.getConsoleSender().sendMessage("[MinecraftParty] PlaceholderAPI plugin has not been detected on this server. Deactivating...");
            	getSettings().set("settings.enable_placeholderapi", false);
            	saveSettings();
            	reloadSettings();
            }
        }
		
		//Update Checker
        if(update){
		new UpdateChecker(this, 86837).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
            	//Bukkit.getConsoleSender().sendMessage("There is not a new update available.");
            } else {
            	Bukkit.getConsoleSender().sendMessage("[MinecraftParty] An update to MinecraftParty is available: " + version + ". You are on " + currentversion);
            	Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Download it here: www.spigotmc.org/resources/86837/");
            }
        });
       }
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
		pm.registerEvents(new OnInteractEvent(this), this);
		pm.registerEvents(new OnSignChange(this), this);
		
	}
	
	//Config files
	//config.yml
	public void registerConfig(){
		File config = new File(this.getDataFolder(),"config.yml");
		if(!config.exists()){
			this.getConfig().options().copyDefaults(true);
			getConfig().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n"
		                      + ""
		                      + "We highly recommend not to modify this file.");
			saveConfig();
		}
	}
	
	//settings.yml:
	public FileConfiguration getSettings() {
		if(settings == null) {
			reloadSettings();
		}
		return settings;
	}
	
	public void reloadSettings(){
		if(settings == null){
			settingsFile = new File(getDataFolder(),"settings.yml");
		}
		settings = YamlConfiguration.loadConfiguration(settingsFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/settings.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				settings.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveSettings(){
		try{
			settings.save(settingsFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerSettings(){
		settingsFile = new File(this.getDataFolder(),"settings.yml");
		if(!settingsFile.exists()){
			this.getSettings().options().copyDefaults(true);
			getSettings().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveSettings();
		}
	}
	
	
	
	//messages.yml
	public FileConfiguration getMessages() {
		if(messages == null) {
			reloadMessages();
		}
		return messages;
	}
	
	public void reloadMessages(){
		if(messages == null){
			messagesFile = new File(getDataFolder(),"messages.yml");
		}
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/messages.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				messages.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveMessages(){
		try{
			messages.save(messagesFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerMessages(){
		messagesFile = new File(this.getDataFolder(),"messages.yml");
		if(!messagesFile.exists()){
			this.getMessages().options().copyDefaults(true);
			getMessages().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveMessages();
		}
	}
	
	
	
	//scoreboard.yml
	public FileConfiguration getScoreboard() {
		if(scoreboard == null) {
			reloadScoreboard();
		}
		return scoreboard;
	}
	
	public void reloadScoreboard(){
		if(scoreboard == null){
			scoreboardFile = new File(getDataFolder(),"scoreboard.yml");
		}
		scoreboard = YamlConfiguration.loadConfiguration(scoreboardFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/scoreboard.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				scoreboard.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveScoreboard(){
		try{
			scoreboard.save(scoreboardFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerScoreboard(){
		scoreboardFile = new File(this.getDataFolder(),"scoreboard.yml");
		if(!scoreboardFile.exists()){
			this.getScoreboard().options().copyDefaults(true);
			getScoreboard().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveScoreboard();
		}
	}
	
	
	
	//shop.yml
	public FileConfiguration getShop() {
		if(shop == null) {
			reloadShop();
		}
		return shop;
	}
	
	public void reloadShop(){
		if(shop == null){
			shopFile = new File(getDataFolder(),"shop.yml");
		}
		shop = YamlConfiguration.loadConfiguration(shopFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/shop.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				shop.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveShop(){
		try{
			shop.save(shopFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerShop(){
		shopFile = new File(this.getDataFolder(),"shop.yml");
		if(!shopFile.exists()){
			this.getShop().options().copyDefaults(true);
			getShop().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveShop();
		}
	}
	
	
	
	//titles.yml
	public FileConfiguration getTitles() {
		if(titles == null) {
			reloadTitles();
		}
		return titles;
	}
	
	public void reloadTitles(){
		if(titles == null){
			titlesFile = new File(getDataFolder(),"titles.yml");
		}
		titles = YamlConfiguration.loadConfiguration(titlesFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/titles.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				titles.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveTitles(){
		try{
			titles.save(titlesFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerTitles(){
		titlesFile = new File(this.getDataFolder(),"titles.yml");
		if(!titlesFile.exists()){
			this.getTitles().options().copyDefaults(true);
			getTitles().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveTitles();
		}
	}
	
	
	
	//mysql.yml
	public FileConfiguration getMysql() {
		if(mysql == null) {
			reloadMysql();
		}
		return mysql;
	}
	
	public void reloadMysql(){
		if(mysql == null){
			mysqlFile = new File(getDataFolder(),"mysql.yml");
		}
		mysql = YamlConfiguration.loadConfiguration(mysqlFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/mysql.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				mysql.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveMysql(){
		try{
			mysql.save(mysqlFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerMysql(){
		mysqlFile = new File(this.getDataFolder(),"mysql.yml");
		if(!mysqlFile.exists()){
			this.getMysql().options().copyDefaults(true);
			getMysql().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveMysql();
		}
	}
	
	
	
	
	//minigames folder
	//colormatch
	public FileConfiguration getColorMatch() {
		if(colormatch == null) {
			reloadColorMatch();
		}
		return colormatch;
	}
	
	public void reloadColorMatch(){
		if(colormatch == null){
			colormatchFile = new File(getDataFolder()+"/minigames","colormatch.yml");
		}
		colormatch = YamlConfiguration.loadConfiguration(colormatchFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/colormatch.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				colormatch.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveColorMatch(){
		try{
			colormatch.save(colormatchFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerColorMatch(){
		colormatchFile = new File(getDataFolder()+"/minigames","colormatch.yml");
		if(!colormatchFile.exists()){
			this.getColorMatch().options().copyDefaults(true);
			getColorMatch().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveColorMatch();
		}
	}
	
	//deadend
	public FileConfiguration getDeadEnd() {
		if(deadend == null) {
			reloadDeadEnd();
		}
		return deadend;
	}
	
	public void reloadDeadEnd(){
		if(deadend == null){
			deadendFile = new File(getDataFolder()+"/minigames","deadend.yml");
		}
		deadend = YamlConfiguration.loadConfiguration(deadendFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/deadend.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				deadend.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveDeadEnd(){
		try{
			deadend.save(deadendFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerDeadEnd(){
		deadendFile = new File(getDataFolder()+"/minigames","deadend.yml");
		if(!deadendFile.exists()){
			this.getDeadEnd().options().copyDefaults(true);
			getDeadEnd().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveDeadEnd();
		}
	}
	
	//redalert
	public FileConfiguration getRedAlert() {
		if(redalert == null) {
			reloadRedAlert();
		}
		return redalert;
	}
	
	public void reloadRedAlert(){
		if(redalert == null){
			redalertFile = new File(getDataFolder()+"/minigames","redalert.yml");
		}
		redalert = YamlConfiguration.loadConfiguration(redalertFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/redalert.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				redalert.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveRedAlert(){
		try{
			redalert.save(redalertFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerRedAlert(){
		redalertFile = new File(getDataFolder()+"/minigames","redalert.yml");
		if(!redalertFile.exists()){
			this.getRedAlert().options().copyDefaults(true);
			getRedAlert().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveRedAlert();
		}
	}
	
	//jumpnrun
	public FileConfiguration getJumpnRun() {
		if(jumpnrun == null) {
			reloadJumpnRun();
		}
		return jumpnrun;
	}
	
	public void reloadJumpnRun(){
		if(jumpnrun == null){
			jumpnrunFile = new File(getDataFolder()+"/minigames","jumpnrun.yml");
		}
		jumpnrun = YamlConfiguration.loadConfiguration(jumpnrunFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/jumpnrun.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				jumpnrun.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveJumpnRun(){
		try{
			jumpnrun.save(jumpnrunFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerJumpnRun(){
		jumpnrunFile = new File(getDataFolder()+"/minigames","jumpnrun.yml");
		if(!jumpnrunFile.exists()){
			this.getJumpnRun().options().copyDefaults(true);
			getJumpnRun().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveJumpnRun();
		}
	}
	
	//lastarcherstanding
	public FileConfiguration getLastArcherStanding() {
		if(lastarcherstanding == null) {
			reloadLastArcherStanding();
		}
		return lastarcherstanding;
	}
	
	public void reloadLastArcherStanding(){
		if(lastarcherstanding == null){
			lastarcherstandingFile = new File(getDataFolder()+"/minigames","lastarcherstanding.yml");
		}
		lastarcherstanding = YamlConfiguration.loadConfiguration(lastarcherstandingFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/lastarcherstanding.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				lastarcherstanding.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveLastArcherStanding(){
		try{
			lastarcherstanding.save(lastarcherstandingFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerLastArcherStanding(){
		lastarcherstandingFile = new File(getDataFolder()+"/minigames","lastarcherstanding.yml");
		if(!lastarcherstandingFile.exists()){
			this.getLastArcherStanding().options().copyDefaults(true);
			getLastArcherStanding().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveLastArcherStanding();
		}
	}
	
	//minefield
	public FileConfiguration getMineField() {
		if(minefield == null) {
			reloadMineField();
		}
		return minefield;
	}
	
	public void reloadMineField(){
		if(minefield == null){
			minefieldFile = new File(getDataFolder()+"/minigames","minefield.yml");
		}
		minefield = YamlConfiguration.loadConfiguration(minefieldFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/minefield.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				minefield.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveMineField(){
		try{
			minefield.save(minefieldFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerMineField(){
		minefieldFile = new File(getDataFolder()+"/minigames","minefield.yml");
		if(!minefieldFile.exists()){
			this.getMineField().options().copyDefaults(true);
			getMineField().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveMineField();
		}
	}
	
	//sheepfreenzy
	public FileConfiguration getSheepFreenzy() {
		if(sheepfreenzy == null) {
			reloadSheepFreenzy();
		}
		return sheepfreenzy;
	}
	
	public void reloadSheepFreenzy(){
		if(sheepfreenzy == null){
			sheepfreenzyFile = new File(getDataFolder()+"/minigames","sheepfreenzy.yml");
		}
		sheepfreenzy = YamlConfiguration.loadConfiguration(sheepfreenzyFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/sheepfreenzy.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				sheepfreenzy.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveSheepFreenzy(){
		try{
			sheepfreenzy.save(sheepfreenzyFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerSheepFreenzy(){
		sheepfreenzyFile = new File(getDataFolder()+"/minigames","sheepfreenzy.yml");
		if(!sheepfreenzyFile.exists()){
			this.getSheepFreenzy().options().copyDefaults(true);
			getSheepFreenzy().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveSheepFreenzy();
		}
	}
	
	//slapfight
	public FileConfiguration getSlapFight() {
		if(slapfight == null) {
			reloadSlapFight();
		}
		return slapfight;
	}
	
	public void reloadSlapFight(){
		if(slapfight == null){
			slapfightFile = new File(getDataFolder()+"/minigames","slapfight.yml");
		}
		slapfight = YamlConfiguration.loadConfiguration(slapfightFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/slapfight.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				slapfight.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveSlapFight(){
		try{
			slapfight.save(slapfightFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerSlapFight(){
		slapfightFile = new File(getDataFolder()+"/minigames","slapfight.yml");
		if(!slapfightFile.exists()){
			this.getSlapFight().options().copyDefaults(true);
			getSlapFight().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveSlapFight();
		}
	}
	
	//smokemonster
	public FileConfiguration getSmokeMonster() {
		if(smokemonster == null) {
			reloadSmokeMonster();
		}
		return smokemonster;
	}
	
	public void reloadSmokeMonster(){
		if(smokemonster == null){
			smokemonsterFile = new File(getDataFolder()+"/minigames","smokemonster.yml");
		}
		smokemonster = YamlConfiguration.loadConfiguration(smokemonsterFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/smokemonster.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				smokemonster.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveSmokeMonster(){
		try{
			smokemonster.save(smokemonsterFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerSmokeMonster(){
		smokemonsterFile = new File(getDataFolder()+"/minigames","smokemonster.yml");
		if(!smokemonsterFile.exists()){
			this.getSmokeMonster().options().copyDefaults(true);
			getSmokeMonster().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveSmokeMonster();
		}
	}
	
	//spleef
	public FileConfiguration getSpleef() {
		if(spleef == null) {
			reloadSpleef();
		}
		return spleef;
	}
	
	public void reloadSpleef(){
		if(spleef == null){
			spleefFile = new File(getDataFolder()+"/minigames","spleef.yml");
		}
		spleef = YamlConfiguration.loadConfiguration(spleefFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("files/spleef.yml"),"UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				spleef.setDefaults(defConfig);
			}			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void saveSpleef(){
		try{
			spleef.save(spleefFile);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	public void registerSpleef(){
		spleefFile = new File(getDataFolder()+"/minigames","spleef.yml");
		if(!spleefFile.exists()){
			this.getSpleef().options().copyDefaults(true);
			getSpleef().options().header(
		              "  __  __ _                            __ _   ____            _\n"
		                      + " |  \\/  (_)_ __   ___  ___ _ __ __ _ / _| |_|  _ \\ __ _ _ __| |_ _   _\n"
		                      + " | |\\/| | | '_ \\ / _ \\/ __| '__/ _` | |_| __| |_) / _` | '__| __| | | |\n"
		                      + " | |  | | | | | |  __| (__| | | (_| |  _| |_|  __| (_| | |  | |_| |_| |\n"
		                      + " |_|  |_|_|_| |_|\\___|\\___|_|  \\__,_|_|  \\__|_|   \\__,_|_|   \\__|\\__, |\n"
		                      + "                                                                  |___/\n");
			saveSpleef();
		}
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
		
		if(credits_enable) {
			reward = r.nextInt((credits_maxreward - credits_minreward) + 1) + credits_minreward;
			if(p.hasPermission("minecraftparty.double_coins")){
				reward = reward * 2;
			}else if(p.hasPermission("minecraftparty.triple_coins")){
				reward = reward * 3;
			}
			this.updatePlayerStats(p.getName(), "credits", getPlayerStats(p.getName(), "credits") + reward);		

			if(announcements){
				if(getSettings().getBoolean("settings.plugin.enable_placeholderapi")){
					getServer().broadcastMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.winner_broadcast").replace("%player%", p.getName()).replace("%credits%", Integer.toString(reward)))));
				} else {
					getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.winner_broadcast").replace("%player%", p.getName()).replace("%credits%", Integer.toString(reward))));
				}
			}
			
			if(getSettings().getBoolean("settings.plugin.enable_placeholderapi")){
				p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.credits_earned").replace("%player%", p.getName()).replace("%credits%", Integer.toString(reward)))));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.credits_earned").replace("%player%", p.getName()).replace("%credits%", Integer.toString(reward))));
			}

			msql.updateWinnerStats(p.getName(), reward);
			
			if(economy){
				EconomyResponse r_ = econ.depositPlayer(p.getName(), reward);
				if(!r_.transactionSuccess()) {
					getServer().getPlayer(p.getName()).sendMessage(ChatColor.RED + String.format("An error occured: %s", r_.errorMessage));
	            }
			}
		} else {
			if(announcements){
				if(getSettings().getBoolean("settings.plugin.enable_placeholderapi")){
					getServer().broadcastMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.winner_broadcast").replace("%player%", p.getName()))));
				} else {
					getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.winner_broadcast").replace("%player%", p.getName())));
				}
			}
			
			if(getSettings().getBoolean("settings.plugin.enable_placeholderapi")){
				p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.credits_earned").replace("%player%", p.getName()))));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.credits_earned").replace("%player%", p.getName())));
			}
		}
		if(item_rewards){
			int reward_ = r.nextInt((item_maxreward - item_minreward) + 1) + item_minreward;
			if(p.hasPermission("minecraftparty.double_coins")){
				reward_ = reward_ * 2;
			}else if(p.hasPermission("minecraftparty.triple_coins")){
				reward_ = reward_ * 3;
			}
			if(placeholderapi){
				p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.reward_earned").replace("%number%", Integer.toString(reward_)).replace("%material%", Material.getMaterial(item_id).name()))));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.reward_earned").replace("%number%", Integer.toString(reward_)).replace("%material%", Material.getMaterial(item_id).name())));
			}
			
			if(rewardcount.containsKey(p.getName())){
				reward_ += rewardcount.get(p.getName());
			}
			rewardcount.put(p.getName(), reward_);
		}
		
		//updateScoreboardOUTGAME(p.getName());
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
	private int t;
	public int currentmg = 0;
	BukkitTask currentid = null;
	public void secondsTick(int disabledMinigamesC){
		
		if(!ingame_started){
			return;
		}
		
		// update scoreboard
		if(getScoreboard().getBoolean("scoreboard.toggle")) {
			updateScoreboard();
		}
		

		// stop the whole party after some rounds
		if(c_ > (minigames.size() - disabledMinigamesC) * seconds - 3){
			//Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable(){
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				public void run(){
					startNew();
				}
			}, 30 * 20); // 30 secs
			Bukkit.getScheduler().cancelTask(t);
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
					if(placeholderapi){
						p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.next_round"))));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.next_round")));
					}
					p.getInventory().clear();
					p.updateInventory();
					//updateScoreboardOUTGAME(pl);
					/*if(bungee) {
						if(connect_hub) {
							sendServer(p, m.getSettings().getString("settings.plugin.bungee.hub"));
						}
						if(shutdown) {
							Bukkit.getServer().shutdown();
							Bukkit.broadcastMessage("hola");
						}
					}*/
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
			resetAll(false);
			
			c = 0;
			c_ = 0;
			if(currentid != null){
				currentid.cancel();
			}
		}

		// start the next minigame after 60 seconds
		if(c == seconds || c > seconds){
			c = -6;
			if(currentid != null){
				currentid.cancel();
			}
			//currentid = nextMinigame();
			for(String pl : players) {
				Player p = Bukkit.getPlayerExact(pl);
				sendSummary(p);	
			}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
				@Override
				public void run() {
					nextMinigame();
				}
			}, 100);
			
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
						mg.winners.clear();
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
			mg.winners.clear();
		}

		//System.out.println(currentmg);
		
		if(currentmg > -1){
			if(!minigames.get(currentmg).isEnabled()){
				currentscore.clear();
				for(Minigame mg : minigames){
					mg.lost.clear();
					mg.winners.clear();
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
					m.winners.clear();
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
				t = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
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

	private void updateScoreboard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = manager.getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("score", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', getScoreboard().getString("scoreboard.title")));
		List<String> lines = getScoreboard().getStringList("scoreboard.lines");
		for(String pl : players) {
		Player p = Bukkit.getPlayerExact(pl);
		if(placeholderapi) {
			for(int i=0;i<lines.size();i++) {
				Score score = objective.getScore(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', lines.get(i)
						.replace("%players%", Integer.toString(players.size()) )
						.replace("%min_players%", Integer.toString(min_players))
						.replace("%minigame%", m.getConfig().getString("data.ag"))
						.replace("%time%", Integer.toString(seconds - c))
						.replace("%round%", Integer.toString(currentmg + 1))
						.replace("%max_round%", Integer.toString(minigames.size()))
						)));
				
				score.setScore(lines.size()-(i));
					
					p.setScoreboard(scoreboard);
			}
		} else {
			for(int i=0;i<lines.size();i++) {
				Score score = objective.getScore(ChatColor.translateAlternateColorCodes('&', lines.get(i)
						.replace("%players%", Integer.toString(players.size()) )
						.replace("%min_players%", Integer.toString(min_players))
						.replace("%minigame%", m.getConfig().getString("data.ag"))
						.replace("%time%", Integer.toString(seconds - c))
						.replace("%round%", Integer.toString(currentmg + 1))
						.replace("%max_round%", Integer.toString(minigames.size()))
						));
				
				score.setScore(lines.size()-(i));
					p.setScoreboard(scoreboard);
				}
			}
		}
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
			mg.winners.clear();
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
				
				if(placeholderapi){
					p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.next_round"))));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.next_round")));
				}
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

		resetAll(false);

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
				if(placeholderapi){
					p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.stopped_game").replace("%min_players%", Integer.toString(min_players)))));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.stopped_game").replace("%min_players%", Integer.toString(min_players))));
				}
			}
		}

		running = false;
		started = false;
		ingame_started = false;
		players.clear();
		players_doublejumped.clear();
		currentmg = 0;

		resetAll(true);
	}
	
	public void stopFull(Player p_){
		stopFull();
		//updateScoreboardOUTGAME(p_.getName());
	}

	public Location getLobby(){
		if(!getConfig().isSet("lobby.location")){
			getLogger().severe(ChatColor.BLUE + "A LOBBY COULD NOT BE FOUND. PLEASE FIX THIS WITH /mpa setlobby.");
			for(Player p : Bukkit.getOnlinePlayers()){
				if(p.isOp()){
					p.sendMessage(ChatColor.BLUE + "[MinecraftParty] " + ChatColor.RED + "A lobby could NOT be found, which leads to errors in the console. Please fix this with " + ChatColor.GOLD + "/mpa setlobby.");
				}
			}
		}
		return new Location(getServer().getWorld(getConfig().getString("lobby.world")), getConfig().getInt("lobby.location.x"), getConfig().getInt("lobby.location.y"), getConfig().getInt("lobby.location.z"));
	}

	public Location getComponentForMinigame(String minigame, String component, String count){
		if(isValidMinigame(minigame)){
			String base = "minigame." + "data." + component + count;
			if(minigame.equals("ColorMatch")) {
				return new Location(Bukkit.getWorld(getColorMatch().getString(base + ".world")), getColorMatch().getInt(base + ".location.x"), getColorMatch().getInt(base + ".location.y"), getColorMatch().getInt(base + ".location.z"));
			}
			if(minigame.equals("DeadEnd")) {
				return new Location(Bukkit.getWorld(getDeadEnd().getString(base + ".world")), getDeadEnd().getInt(base + ".location.x"), getDeadEnd().getInt(base + ".location.y"), getDeadEnd().getInt(base + ".location.z"));
			}
			if(minigame.equals("JumpnRun")) {
				return new Location(Bukkit.getWorld(getJumpnRun().getString(base + ".world")), getJumpnRun().getInt(base + ".location.x"), getJumpnRun().getInt(base + ".location.y"), getJumpnRun().getInt(base + ".location.z"));
			}
			if(minigame.equals("LastArcherStanding")) {
				return new Location(Bukkit.getWorld(getLastArcherStanding().getString(base + ".world")), getLastArcherStanding().getInt(base + ".location.x"), getLastArcherStanding().getInt(base + ".location.y"), getLastArcherStanding().getInt(base + ".location.z"));
			}
			if(minigame.equals("MineField")) {
				return new Location(Bukkit.getWorld(getMineField().getString(base + ".world")), getMineField().getInt(base + ".location.x"), getMineField().getInt(base + ".location.y"), getMineField().getInt(base + ".location.z"));
			}
			if(minigame.equals("RedAlert")) {
				return new Location(Bukkit.getWorld(getRedAlert().getString(base + ".world")), getRedAlert().getInt(base + ".location.x"), getRedAlert().getInt(base + ".location.y"), getRedAlert().getInt(base + ".location.z"));
			}
			if(minigame.equals("SheepFreenzy")) {
				return new Location(Bukkit.getWorld(getSheepFreenzy().getString(base + ".world")), getSheepFreenzy().getInt(base + ".location.x"), getSheepFreenzy().getInt(base + ".location.y"), getSheepFreenzy().getInt(base + ".location.z"));
			}
			if(minigame.equals("SlapFight")) {
				return new Location(Bukkit.getWorld(getSlapFight().getString(base + ".world")), getSlapFight().getInt(base + ".location.x"), getSlapFight().getInt(base + ".location.y"), getSlapFight().getInt(base + ".location.z"));
			}
			if(minigame.equals("SmokeMonster")) {
				return new Location(Bukkit.getWorld(getSmokeMonster().getString(base + ".world")), getSmokeMonster().getInt(base + ".location.x"), getSmokeMonster().getInt(base + ".location.y"), getSmokeMonster().getInt(base + ".location.z"));
			}
			if(minigame.equals("Spleef")) {
				return new Location(Bukkit.getWorld(getSpleef().getString(base + ".world")), getSpleef().getInt(base + ".location.x"), getSpleef().getInt(base + ".location.y"), getSpleef().getInt(base + ".location.z"));
			}
		}
		return null;
	}

	public Location getComponentForMinigame(String minigame, String component){
		if(isValidMinigame(minigame)){
			String base = "minigame." + "data." + component;
			if(minigame.equals("ColorMatch")) {
				return new Location(Bukkit.getWorld(getColorMatch().getString(base + ".world")), getColorMatch().getInt(base + ".location.x"), getColorMatch().getInt(base + ".location.y"), getColorMatch().getInt(base + ".location.z"));
			}
			if(minigame.equals("DeadEnd")) {
				return new Location(Bukkit.getWorld(getDeadEnd().getString(base + ".world")), getDeadEnd().getInt(base + ".location.x"), getDeadEnd().getInt(base + ".location.y"), getDeadEnd().getInt(base + ".location.z"));
			}
			if(minigame.equals("JumpnRun")) {
				return new Location(Bukkit.getWorld(getJumpnRun().getString(base + ".world")), getJumpnRun().getInt(base + ".location.x"), getJumpnRun().getInt(base + ".location.y"), getJumpnRun().getInt(base + ".location.z"));
			}
			if(minigame.equals("LastArcherStanding")) {
				return new Location(Bukkit.getWorld(getLastArcherStanding().getString(base + ".world")), getLastArcherStanding().getInt(base + ".location.x"), getLastArcherStanding().getInt(base + ".location.y"), getLastArcherStanding().getInt(base + ".location.z"));
			}
			if(minigame.equals("MineField")) {
				return new Location(Bukkit.getWorld(getMineField().getString(base + ".world")), getMineField().getInt(base + ".location.x"), getMineField().getInt(base + ".location.y"), getMineField().getInt(base + ".location.z"));
			}
			if(minigame.equals("RedAlert")) {
				return new Location(Bukkit.getWorld(getRedAlert().getString(base + ".world")), getRedAlert().getInt(base + ".location.x"), getRedAlert().getInt(base + ".location.y"), getRedAlert().getInt(base + ".location.z"));
			}
			if(minigame.equals("SheepFreenzy")) {
				return new Location(Bukkit.getWorld(getSheepFreenzy().getString(base + ".world")), getSheepFreenzy().getInt(base + ".location.x"), getSheepFreenzy().getInt(base + ".location.y"), getSheepFreenzy().getInt(base + ".location.z"));
			}
			if(minigame.equals("SlapFight")) {
				return new Location(Bukkit.getWorld(getSlapFight().getString(base + ".world")), getSlapFight().getInt(base + ".location.x"), getSlapFight().getInt(base + ".location.y"), getSlapFight().getInt(base + ".location.z"));
			}
			if(minigame.equals("SmokeMonster")) {
				return new Location(Bukkit.getWorld(getSmokeMonster().getString(base + ".world")), getSmokeMonster().getInt(base + ".location.x"), getSmokeMonster().getInt(base + ".location.y"), getSmokeMonster().getInt(base + ".location.z"));
			}
			if(minigame.equals("Spleef")) {
				return new Location(Bukkit.getWorld(getSpleef().getString(base + ".world")), getSpleef().getInt(base + ".location.x"), getSpleef().getInt(base + ".location.y"), getSpleef().getInt(base + ".location.z"));
			}
		}
		return null;
	}

	public void saveComponentForMinigame(String minigame, String component, Location comploc){
		String base = "minigame." + "data." + component;
		if(minigame.equals("ColorMatch")) {
			getColorMatch().set(base + ".world", comploc.getWorld().getName());
			getColorMatch().set(base + ".location.x", comploc.getBlockX());
			getColorMatch().set(base + ".location.y", comploc.getBlockY());
			getColorMatch().set(base + ".location.z", comploc.getBlockZ());
			saveColorMatch();
		}
		if(minigame.equals("DeadEnd")) {
			getDeadEnd().set(base + ".world", comploc.getWorld().getName());
			getDeadEnd().set(base + ".location.x", comploc.getBlockX());
			getDeadEnd().set(base + ".location.y", comploc.getBlockY());
			getDeadEnd().set(base + ".location.z", comploc.getBlockZ());
			saveDeadEnd();
		}
		if(minigame.equals("JumpnRun")) {
			getJumpnRun().set(base + ".world", comploc.getWorld().getName());
			getJumpnRun().set(base + ".location.x", comploc.getBlockX());
			getJumpnRun().set(base + ".location.y", comploc.getBlockY());
			getJumpnRun().set(base + ".location.z", comploc.getBlockZ());
			saveJumpnRun();
		}
		if(minigame.equals("LastArcherStanding")) {
			getLastArcherStanding().set(base + ".world", comploc.getWorld().getName());
			getLastArcherStanding().set(base + ".location.x", comploc.getBlockX());
			getLastArcherStanding().set(base + ".location.y", comploc.getBlockY());
			getLastArcherStanding().set(base + ".location.z", comploc.getBlockZ());
			saveLastArcherStanding();
		}
		if(minigame.equals("MineField")) {
			getMineField().set(base + ".world", comploc.getWorld().getName());
			getMineField().set(base + ".location.x", comploc.getBlockX());
			getMineField().set(base + ".location.y", comploc.getBlockY());
			getMineField().set(base + ".location.z", comploc.getBlockZ());
			saveMineField();
		}
		if(minigame.equals("RedAlert")) {
			getRedAlert().set(base + ".world", comploc.getWorld().getName());
			getRedAlert().set(base + ".location.x", comploc.getBlockX());
			getRedAlert().set(base + ".location.y", comploc.getBlockY());
			getRedAlert().set(base + ".location.z", comploc.getBlockZ());
			saveRedAlert();
		}
		if(minigame.equals("SheepFreenzy")) {
			getSheepFreenzy().set(base + ".world", comploc.getWorld().getName());
			getSheepFreenzy().set(base + ".location.x", comploc.getBlockX());
			getSheepFreenzy().set(base + ".location.y", comploc.getBlockY());
			getSheepFreenzy().set(base + ".location.z", comploc.getBlockZ());
			saveSheepFreenzy();
		}
		if(minigame.equals("SlapFight")) {
			getSlapFight().set(base + ".world", comploc.getWorld().getName());
			getSlapFight().set(base + ".location.x", comploc.getBlockX());
			getSlapFight().set(base + ".location.y", comploc.getBlockY());
			getSlapFight().set(base + ".location.z", comploc.getBlockZ());
			saveSlapFight();
		}
		if(minigame.equals("SmokeMonster")) {
			getSmokeMonster().set(base + ".world", comploc.getWorld().getName());
			getSmokeMonster().set(base + ".location.x", comploc.getBlockX());
			getSmokeMonster().set(base + ".location.y", comploc.getBlockY());
			getSmokeMonster().set(base + ".location.z", comploc.getBlockZ());
			saveSmokeMonster();
		}
		if(minigame.equals("Spleef")) {
			getSpleef().set(base + ".world", comploc.getWorld().getName());
			getSpleef().set(base + ".location.x", comploc.getBlockX());
			getSpleef().set(base + ".location.y", comploc.getBlockY());
			getSpleef().set(base + ".location.z", comploc.getBlockZ());
			saveSpleef();
		}
	}

	public boolean isValidMinigame(String minigame){
		if(minigame.equals("ColorMatch") && getColorMatch().isSet("minigame." + "data") && getColorMatch().isSet("minigame." + "data" + ".lobby") && getColorMatch().isSet("minigame." + "data" + ".spawn") && getColorMatch().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("DeadEnd") && getDeadEnd().isSet("minigame." + "data") && getDeadEnd().isSet("minigame." + "data" + ".lobby") && getDeadEnd().isSet("minigame." + "data" + ".spawn") && getDeadEnd().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("JumpnRun") && getJumpnRun().isSet("minigame." + "data") && getJumpnRun().isSet("minigame." + "data" + ".lobby") && getJumpnRun().isSet("minigame." + "data" + ".spawn") && getJumpnRun().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("LastArcherStanding") && getLastArcherStanding().isSet("minigame." + "data") && getLastArcherStanding().isSet("minigame." + "data" + ".lobby") && getLastArcherStanding().isSet("minigame." + "data" + ".spawn") && getLastArcherStanding().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("MineField") && getMineField().isSet("minigame." + "data") && getMineField().isSet("minigame." + "data" + ".lobby") && getMineField().isSet("minigame." + "data" + ".spawn") && getMineField().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("RedAlert") && getRedAlert().isSet("minigame." + "data") && getRedAlert().isSet("minigame." + "data" + ".lobby") && getRedAlert().isSet("minigame." + "data" + ".spawn") && getRedAlert().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("SheepFreenzy") && getSheepFreenzy().isSet("minigame." + "data") && getSheepFreenzy().isSet("minigame." + "data" + ".lobby") && getSheepFreenzy().isSet("minigame." + "data" + ".spawn") && getSheepFreenzy().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("SlapFight") && getSlapFight().isSet("minigame." + "data") && getSlapFight().isSet("minigame." + "data" + ".lobby") && getSlapFight().isSet("minigame." + "data" + ".spawn") && getSlapFight().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("SmokeMonster") && getSmokeMonster().isSet("minigame." + "data") && getSmokeMonster().isSet("minigame." + "data" + ".lobby") && getSmokeMonster().isSet("minigame." + "data" + ".spawn") && getSmokeMonster().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		if(minigame.equals("Spleef") && getSpleef().isSet("minigame." + "data") && getSpleef().isSet("minigame." + "data" + ".lobby") && getSpleef().isSet("minigame." + "data" + ".spawn") && getSpleef().isSet("minigame." + "data" + ".spectatorlobby")) {
			return true;
		}
		return false;
	}
	
	public void setupColorMatch(Location start) {
		ColorMatch.setup(start, this, "ColorMatch");
		minigames.add(new ColorMatch(this, this.getComponentForMinigame("ColorMatch", "spawn"), this.getComponentForMinigame("ColorMatch", "lobby"), this.getComponentForMinigame("ColorMatch", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished ColorMatch Setup.");
	}
	
	public void setupSpleef(Location start) {
		Spleef.setup(start, this, "Spleef");
		minigames.add(new Spleef(this, this.getComponentForMinigame("Spleef", "spawn"), this.getComponentForMinigame("Spleef", "lobby"), this.getComponentForMinigame("Spleef", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished Spleef Setup.");
	}
	
	public void setupMineField(Location start) {
		MineField.setup(start, this, "MineField");
		minigames.add(new MineField(this, this.getComponentForMinigame("MineField", "spawn"), this.getComponentForMinigame("MineField", "lobby"), this.getComponentForMinigame("MineField", "spectatorlobby"), m.getComponentForMinigame("MineField", "finishline")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished MineField Setup.");
	}
	
	public void setupJumpnRun(Location start) {
		JumpnRun.setup(start, this, "JumpnRun");
		minigames.add(new JumpnRun(this, this.getComponentForMinigame("JumpnRun", "spawn"), this.getComponentForMinigame("JumpnRun", "lobby"), this.getComponentForMinigame("JumpnRun", "spectatorlobby"), m.getComponentForMinigame("JumpnRun", "finishline")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished JumpnRun Setup.");
	}
	
	public void setupDeadEnd(Location start) {
		DeadEnd.setup(start, this, "DeadEnd");
		minigames.add(new DeadEnd(this, this.getComponentForMinigame("DeadEnd", "spawn"), this.getComponentForMinigame("DeadEnd", "lobby"), this.getComponentForMinigame("DeadEnd", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished DeadEnd Setup.");
	}
	
	public void setupRedAlert(Location start) {
		RedAlert.setup(start, this, "RedAlert");
		minigames.add(new RedAlert(this, this.getComponentForMinigame("RedAlert", "spawn"), this.getComponentForMinigame("RedAlert", "lobby"), this.getComponentForMinigame("RedAlert", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished RedAlert Setup.");
	}
	
	public void setupLastArcherStanding(Location start) {
		LastArcherStanding.setup(start, this, "LastArcherStanding");
		minigames.add(new LastArcherStanding(this, this.getComponentForMinigame("LastArcherStanding", "spawn"), this.getComponentForMinigame("LastArcherStanding", "lobby"), this.getComponentForMinigame("LastArcherStanding", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished LastArcherStanding Setup.");
	}
	
	public void setupSheepFreenzy(Location start) {
		SheepFreenzy.setup(start, this, "SheepFreenzy");
		minigames.add(new SheepFreenzy(this, this.getComponentForMinigame("SheepFreenzy", "spawn"), this.getComponentForMinigame("SheepFreenzy", "lobby"), this.getComponentForMinigame("SheepFreenzy", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished SheepFreenzy Setup.");
	}
	
	public void setupSmokeMonster(Location start) {
		SmokeMonster.setup(start, this, "SmokeMonster");
		minigames.add(new SmokeMonster(this, this.getComponentForMinigame("SmokeMonster", "spawn"), this.getComponentForMinigame("SmokeMonster", "lobby"), this.getComponentForMinigame("SmokeMonster", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished SmokeMonster Setup.");
		Bukkit.getConsoleSender().sendMessage("");
	}
	
	public void setupSlapFight(Location start) {
		SlapFight.setup(start, this, "SlapFight");
		minigames.add(new SlapFight(this, this.getComponentForMinigame("SlapFight", "spawn"), this.getComponentForMinigame("SlapFight", "lobby"), this.getComponentForMinigame("SlapFight", "spectatorlobby")));
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Finished SlapFight Setup.");
	}
	
	public void clearMinigames() {
		minigames.clear();
	}

	public void resetAll(boolean flag){
		Bukkit.getConsoleSender().sendMessage("[MinecraftParty] Resetting in ALL mode: " +  Boolean.toString(flag));
		
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
			RedAlert.reset(this.getComponentForMinigame("RedAlert", "spawn"));*/
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
		String place = Integer.toString(count + 1);
		your_place = place;
		
		if(count == 0){
			place1 = p.getName();
		}else if(count == 1){
			place2 = p.getName();
		}else if(count == 2){
			place3 = p.getName();
		}

		if(placeholderapi){
			p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.your_place").replace("%place%", place))));
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.game.your_place").replace("%place%", place)));
		}
	}
	
	public void sendSummary(Player p) {
		List<String> description = m.getMessages().getStringList("messages.game.summary.minigame");
		for(int i=0;i<description.size();i++) {
			String message = description.get(i);
			if(getSettings().getBoolean("settings.game.send_summary")) {
				if(placeholderapi) {
					p.sendMessage(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', message)
							.replace("%place_1%", String.valueOf(place1))
							.replace("%place_2%", String.valueOf(place2))
							.replace("%place_3%", String.valueOf(place3))
							.replace("%credits_earned%", Integer.valueOf(reward)+"")
							.replace("%stars_earned%", "0")
							.replace("%place_player%", Integer.valueOf(your_place)+"")));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
							.replace("%place_1%", String.valueOf(place1))
							.replace("%place_2%", String.valueOf(place2))
							.replace("%place_3%", String.valueOf(place3))
							.replace("%credits_earned%", Integer.valueOf(reward)+"")
							.replace("%stars_earned%", "0")
							.replace("%place_player%", your_place));
				}
			}
		}
	}
	
	
	public void disableMinigame(CommandSender sender, String minigame){
		if(!running){
			for(Minigame mg : minigames){
				if(mg.name.toLowerCase().equalsIgnoreCase(minigame)){
					mg.setEnabled(false);
					if(placeholderapi){
						sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_minigame").replace("%minigame%", mg.name))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_minigame").replace("%minigame%", mg.name)));
					}
					return;
				}
			}
			if(placeholderapi){
				sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_error1").replace("%minigame%", minigame))));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_error1").replace("%minigame%", minigame)));
			}
			
		}else{
			if(placeholderapi){
				sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_error2").replace("%minigame%", minigame))));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.disable_error2").replace("%minigame%", minigame)));
			}
		}
	}
	
	public void enableMinigame(CommandSender sender, String minigame){
		if(!running){
			for(Minigame mg : minigames){
				if(mg.name.toLowerCase().equalsIgnoreCase(minigame)){
					mg.setEnabled(true);
					if(placeholderapi){
						sender.sendMessage(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.enable_minigame").replace("%minigame%", mg.name))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.enable_minigame").replace("%minigame%", mg.name)));
					}
					return;
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.enable_error1").replace("%minigame%", minigame)));
		}else{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessages().getString("messages.setup.enable_error2").replace("%minigame%", minigame)));
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
	
	public void sendToServer(Player p, String server) {
	    ByteArrayOutputStream b = new ByteArrayOutputStream();
	    DataOutputStream out = new DataOutputStream(b);
	    try {
	      out.writeUTF("Connect");
	      out.writeUTF(server);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
	  }
}
