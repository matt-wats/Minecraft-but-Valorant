package io.github.amutau.ValAnt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class ValAnt extends JavaPlugin {
	
	public static ArrayList<Agent> agents = new ArrayList<Agent>();
	
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MyListener(), this);
		repeat();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("yoru")) {
			clearAgent(player);
			agents.add(new Yoru(player,this));
		} else if(cmd.getName().equalsIgnoreCase("sage")) {
			clearAgent(player);
			agents.add(new Sage(player));
		} else if(cmd.getName().equalsIgnoreCase("clearAgent")) {
			clearAgent(player);
		} else if(cmd.getName().equalsIgnoreCase("phoenix")) {
			clearAgent(player);
			agents.add(new Phoenix(player,this));
		} else if(cmd.getName().equalsIgnoreCase("jett")) {
			clearAgent(player);
			agents.add(new Jett(player));
		}
		
		
		((Player) sender).sendMessage("You are now: " + cmd.getName().toUpperCase());
		
		return true;
	}
	
	public void clearAgent(Player player) {
		
		player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		
		for(int i=agents.size(); i > 0; i--) {
			if(agents.get(i-1).getPlayer().equals(player)) {
				agents.remove(i-1);
			}
		}
	}
	
	public void repeat() {
		
		int id = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		        updates();
		    }}, 0, 1);
	}
	
	public void updates() {
		for(Agent a: agents) {
			a.update();
			for(PlayerObject o: a.getObs()) {
				o.update();
				if(o.getRemove()) {
					a.getObs().remove(o);
				}
			}
		}
	}
	
	
	
}
