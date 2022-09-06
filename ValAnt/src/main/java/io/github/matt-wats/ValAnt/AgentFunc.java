package io.github.amutau.ValAnt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AgentFunc {
	
	Player player;
	ArrayList<PlayerObject> obs;
	ValAnt plugin;
	
	int[] delay = {0,0,0,0};
	
	public AgentFunc(Player player) {
		this.player = player;
		this.obs = new ArrayList<PlayerObject>();
	}
	
	public AgentFunc(Player player, ValAnt plugin) {
		this(player);
		this.plugin = plugin;
	}
	
	public static double[] getWay(double yaw) {
		yaw = Math.PI * yaw / 180;
		double[] way = {Math.cos(yaw),1,Math.sin(yaw)};
		return way;
	}
	
	public static double[] getDir(double yaw) {
		yaw = Math.PI * yaw / 180;
		double[] dir = {-Math.sin(yaw),0,Math.cos(yaw)};
		return dir;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public ArrayList<PlayerObject> getObs() {
		return this.obs;
	}
	
	public void initialize(Material[] mts,String[] nms) {
		
		for(int i=0; i < mts.length; i++) {
			ItemStack itm = new ItemStack(mts[i]);
			ItemMeta meta = itm.getItemMeta();
			meta.setDisplayName(nms[i]);
			meta.setCustomModelData(i);
			itm.setItemMeta(meta);
			player.getInventory().addItem(itm);
		}
		
	}
	
	public static double dist(double x0, double y0, double z0, double x1, double y1, double z1) {
		
		double x = Math.pow(x0-x1, 2);
		double y = Math.pow(y0-y1, 2);
		double z = Math.pow(z0-z1, 2);
		
		return Math.sqrt(x+y+z);
		
	}
	
	public boolean usable(int n) {
		
		if(delay[n] == 0) {
			return true;
		}
		
		this.player.sendMessage("Wait " + delay[n]/20 + " seconds until recharge");
		return false;
	}
	
	public void update() {
		
		for(int i=0; i < delay.length; i++) {
			if(delay[i] > 0) {
				delay[i] = delay[i] - 1;
			}
		}
		
	}

}
