package whirss.partydeluxe.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import whirss.partydeluxe.Main;


public class MainSQL {
	
	private Main main;
	
	public MainSQL(Main main) {
		this.main = main;
	}

	public void updateWinnerStats(String p_, int reward){
		if(!main.getMysql().getBoolean("mysql.enabled")){
			return;
		}
		MySQL MySQL = new MySQL(main.getMysql().getString("mysql.host"), main.getMysql().getString("mysql.port"), main.getMysql().getString("mysql.database"), main.getMysql().getString("mysql.user"), main.getMysql().getString("mysql.password"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mcparty WHERE player='" + p_ + "'");
			if(!res3.isBeforeFirst()){
				// there's no such user
				c.createStatement().executeUpdate("INSERT INTO mcparty VALUES('0', '" + p_ + "', '" + Integer.toString(reward) + "', '1')");
				return;
			}
			res3.next();
			int credits = res3.getInt("credits") + reward;
			int wins = res3.getInt("wins") + 1;
			
			c.createStatement().executeUpdate("UPDATE mcparty SET credits='" + Integer.toString(credits) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int getCredits(String p_){
		if(!main.getMysql().getBoolean("mysql.enabled")){
			return -1;
		}
		MySQL MySQL = new MySQL(main.getMysql().getString("mysql.host"), main.getMysql().getString("mysql.port"), main.getMysql().getString("mysql.database"), main.getMysql().getString("mysql.user"), main.getMysql().getString("mysql.password"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mcparty WHERE player='" + p_ + "'");

			/*if(res3.next()){
				int credits = res3.getInt("credits");
				return credits;
			}
			return -1;*/
			
			res3.next();
			int credits = res3.getInt("credits");
			return credits;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("New User detected.");
		}
		return -1;
	}
	
	public int getWins(String p_){
		if(!main.getMysql().getBoolean("mysql.enabled")){
			return -1;
		}
		MySQL MySQL = new MySQL(main.getMysql().getString("mysql.host"), main.getMysql().getString("mysql.port"), main.getMysql().getString("mysql.database"), main.getMysql().getString("mysql.user"), main.getMysql().getString("mysql.password"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mcparty WHERE player='" + p_ + "'");

			res3.next();
			int wins = res3.getInt("wins");
			return wins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void updateShopperStats(String p_, int amount){
		if(!main.getMysql().getBoolean("mysql.enabled")){
			return;
		}
		MySQL MySQL = new MySQL(main.getMysql().getString("mysql.host"), main.getMysql().getString("mysql.port"), main.getMysql().getString("mysql.database"), main.getMysql().getString("mysql.user"), main.getMysql().getString("mysql.password"));
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM mcparty WHERE player='" + p_ + "'");
			if(!res3.isBeforeFirst()){
				// there's no such user
				//c.createStatement().executeUpdate("INSERT INTO mcparty VALUES('0', '" + p_ + "', '" + Integer.toString(amount) + "', '1')");
				return;
			}
			res3.next();
			int credits = res3.getInt("credits") - amount;
			int wins = res3.getInt("wins") + 1;
			
			c.createStatement().executeUpdate("UPDATE mcparty SET credits='" + Integer.toString(credits) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
