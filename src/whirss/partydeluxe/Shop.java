package whirss.partydeluxe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import whirss.partydeluxe.commands.AdminCommand;
import whirss.partydeluxe.commands.PlayerCommand;
import whirss.partydeluxe.nms.NMSManager;

public class Shop {

	public static int grenades_price = 0;
	public static int jumpboost_price = 0;
	public static int deadendboost_price = 0;
	
	public static int chickentagboost_price = 0;
	public static int slapfight_knockback_price = 0;
	public static int smokemonsterboost_price = 0;
	public static int jumpnrunboost_price = 0;
	public static int sheepfreenzyimmunity_price = 0;
	public static int megagrenades_price = 0;
	
	public static void loadPrices(Main m){
		grenades_price = m.getShop().getInt("shop.prices.grenades");
		jumpboost_price = m.getShop().getInt("shop.prices.jumpboost");
		deadendboost_price = m.getShop().getInt("shop.prices.deadendboost");
		
		chickentagboost_price = m.getShop().getInt("shop.prices.chickentagboost");
		slapfight_knockback_price = m.getShop().getInt("shop.prices.slapfight_knockback");
		smokemonsterboost_price = m.getShop().getInt("shop.prices.smokemonsterboost");
		jumpnrunboost_price = m.getShop().getInt("shop.prices.jumpnrunboost");
		sheepfreenzyimmunity_price = m.getShop().getInt("shop.prices.sheepfreenzyimmunity");
		megagrenades_price = m.getShop().getInt("shop.prices.megagrenades");
	}
	
	public static int getPlayerShopComponent(Main m, String p, String component) {
		int amount = 0;
		if(m.getShop().isSet("shop.data." + p + "." + component)){
			amount = m.getShop().getInt("shop.data." + p + "." + component);
		}
		return amount;
	}

	public static void addToPlayerShopComponent(Main m, String p, String component, int value) {
		if (m.getShop().contains("shop.data." + p + "." + component)) {
			m.getShop().set("shop.data." + p + "." + component, m.getShop().getInt("shop." + p + "." + component) + value);
		} else {
			m.getShop().set("shop.data." + p + "." + component, value);
		}
		m.saveConfig();
	}
	
	public static void removeFromPlayerShopComponent(Main m, String p, String component, int value) {
		if (m.getShop().contains("shop.data." + p + "." + component)) {
			m.getShop().set("shop.data." + p + "." + component, m.getShop().getInt("shop." + p + "." + component) - value);
		} else {
			m.getShop().set("shop.data." + p + "." + component, 0);
		}
		m.saveConfig();
	}


	public static void openShop(final Main m, String p) {
		IconMenu iconm = new IconMenu(m.getShop().getString("shop.gui.title").replace("%credits%", Integer.toString(m.getPlayerStats(p, "credits"))), 18, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				String d = event.getName();
				Player p = event.getPlayer();
				int currentcredits = m.getPlayerStats(p.getName(), "credits");
				if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.grenades"))) {
					if (currentcredits >= grenades_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - grenades_price);
						m.msql.updateShopperStats(p.getName(), grenades_price);
						addToPlayerShopComponent(m, p.getName(), "grenades", 1);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.grenades"))));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.no_money")));
					}
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.jumpboost"))) {
					if (currentcredits >= jumpboost_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - jumpboost_price);
						m.msql.updateShopperStats(p.getName(), jumpboost_price);
						addToPlayerShopComponent(m, p.getName(), "jump_boost", 1);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.jumpboost"))));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.no_money")));
					}
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.deadendboost"))) {
					if (currentcredits >= deadendboost_price) {
						m.updatePlayerStats(p.getName(), "credits", currentcredits - deadendboost_price);
						m.msql.updateShopperStats(p.getName(), deadendboost_price);
						addToPlayerShopComponent(m, p.getName(), "speed_boost", 1);
						p.sendMessage(ChatColor.GREEN + "You bought a Speed Boost!");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.deadendboost"))));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.no_money")));
					}
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.slapfight_knockback"))) {
					Shop.t(m, p, slapfight_knockback_price, "slapfight_knockback", ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.slapfight_knockback"))));
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.smokemonsterboost"))) {
					Shop.t(m, p, smokemonsterboost_price, "smokemonster_boost", ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.smokemonsterboost"))));
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.jumpnrun"))) {
					Shop.t(m, p, jumpnrunboost_price, "jumpnrun_boost", ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.jumpnrun"))));
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.sheepfreenzyimmunity"))) {
					Shop.t(m, p, sheepfreenzyimmunity_price, "sheepfreenzy_explosion_immunity", ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.sheepfreenzyimmunity"))));
				}else if (d.equalsIgnoreCase(m.getShop().getString("shop.displayname.megagranades"))) {
					Shop.t(m, p, megagrenades_price, "megagrenades", ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.purchased_product").replace("%product%", m.getShop().getString("shop.displayname.megagranades"))));
				}
				event.setWillClose(true);
			}
		}, m)
		.setOption(0, new ItemStack(Material.EGG, 1), m.getShop().getString("shop.displayname.grenades"), false, m.getShop().getString("shop.gui.descriptions.grenades"), "Cost: " + Integer.toString(grenades_price))
		.setOption(1, new ItemStack(Material.POTION, 1), m.getShop().getString("shop.displayname.jumpboost"), false, m.getMessages().getString("shop.gui.descriptions.jumpboost"), "Cost: " + Integer.toString(jumpboost_price))
		.setOption(2, new ItemStack(Material.POTION, 1), m.getShop().getString("shop.displayname.deadendboost"), false, m.getMessages().getString("shop.gui.descriptions.deadendboost"), "Cost: " + Integer.toString(deadendboost_price))
		.setOption(10, new ItemStack(Material.STICK, 1), m.getShop().getString("shop.displayname.slapfight_knockback"), true, m.getMessages().getString("shop.gui.descriptions.slapfight_knockback"), "Cost: " + Integer.toString(slapfight_knockback_price))
		.setOption(11, new ItemStack(Material.POTION, 1), m.getShop().getString("shop.displayname.smokemonsterboost"), false, m.getMessages().getString("shop.gui.descriptions.smokemonsterboost"), "Cost: " + Integer.toString(smokemonsterboost_price))
		.setOption(12, new ItemStack(Material.POTION, 1), m.getShop().getString("shop.displayname.jumpnrun"), false, m.getMessages().getString("shop.gui.descriptions.jumpnrun"), "Cost: " + Integer.toString(jumpnrunboost_price))
		.setOption(13, getCustomHead("MHF_Sheep"), m.getShop().getString("shop.displayname.sheepfreenzyimmunity"), false, m.getMessages().getString("shop.gui.descriptions.sheepfreenzyimmunity"), "Cost: " + Integer.toString(sheepfreenzyimmunity_price))
		.setOption(14, new ItemStack(Material.EGG, 1), m.getShop().getString("shop.displayname.megagranades"), true, m.getMessages().getString("shop.gui.descriptions.megagranades"), "Cost: " + Integer.toString(megagrenades_price));

		iconm.open(Bukkit.getPlayerExact(p));
	}
	
	public static ItemStack getCustomHead(String name){
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		skullmeta.setOwner(name);
		item.setItemMeta(skullmeta);
		return item;
	}
	
	public static ItemStack enchantedItemStack(ItemStack item, String name){
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
        item.setItemMeta(im);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        return NMSManager.fakeGlow(item);
	}
	
	
	public static void t(Main m, Player p, int price, String comp, String message){
		int currentcredits = m.getPlayerStats(p.getName(), "credits");
		if (currentcredits >= price) {
			m.updatePlayerStats(p.getName(), "credits", currentcredits - price);
			m.msql.updateShopperStats(p.getName(), price);
			addToPlayerShopComponent(m, p.getName(), comp, 1);
			p.sendMessage(ChatColor.GREEN + message);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getMessages().getString("messages.game.no_money")));
		}
	}
	
}
