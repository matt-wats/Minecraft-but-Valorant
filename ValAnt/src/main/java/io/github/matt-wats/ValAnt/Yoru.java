package io.github.amutau.ValAnt;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Yoru extends AgentFunc implements Agent {
	
	Material[] mts = {Material.BEACON, Material.NETHER_STAR, Material.SLIME_BALL, Material.BLUE_BANNER};
	String[] nms = {"Yoru TP", "Yoru Flash", "2", "Yoru Ult"};
	
	boolean ult = false;
	
	public Yoru(Player player, ValAnt plugin) {
		super(player, plugin);
		initialize(this.mts,this.nms);
	}
	
	public boolean util(int n, int m) {
		
		if(m == 0 && !this.usable(n)) {
			return false;
		}
		
		if(n == 0) {
			TP(m);
		} else if(n == 1 && m == 0) {
			flash();
		} else if(n == 3) {
			ult(m);
		}
		
		return true;
		
	}
	
	public void flash() {
		this.player.sendMessage("Flashing");
		this.obs.add(new Flash(this.player.getEyeLocation(),"bounce"));
	}
	
	public void ult(int n) {
		
		if(n == 0) {
			activateUlt();
		} else if(n == 1) {
			for(PotionEffect pe: this.player.getActivePotionEffects()) {
				this.player.removePotionEffect(pe.getType());
			}
			this.ult = false;
		}
		
	}
	
	public void activateUlt() {
		this.ult = true;
		this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*10,0));
		this.player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20*10,0));
		this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*10,0));
		
		int id = plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
		    public void run() {
		        ult(1);
		    }}, 20*10);
	}
	
	
	//yoru util 0 --> TP
	public void TP(int n) {
		if(n == 0) {
			sendTP();
		} else if(n == 1) {
			doTP();
		}
	}
	public void sendTP() {
		this.player.sendMessage("TP sent");
		//Get direction that TP travels
		Location loc = this.player.getLocation();
		//double yaw = loc.getYaw() * Math.PI / 180;
		//double[] dir = AgentFunc.getDir(yaw);
		loc.setY(loc.getY() + 0.25);
		
		//Create new Particle Object yoruTP
		double[] shape = {0.25,0.5,0.25};
		int[] color = {0,0,255};
		double density = 10;
		
		this.obs.add(new ParticleObject(loc, shape, 1, 10*20, "sphere", density, color, 0.2, "stop", "yoruTP"));
		this.delay[0] = 10*20;
	}
	public boolean doTP() {
		
		if(this.obs.size() < 1) {
			this.player.sendMessage("No TP to teleport to");
			return false;
		}
		
		this.player.sendMessage("Teleporting");
		//for each PlayerObject if it is the yoru tp then teleport to it and remove TP
		for(PlayerObject o: obs) {
			if(o instanceof ParticleObject && ((ParticleObject) o).what.equalsIgnoreCase("yoruTP")) {
				this.player.teleport(o.getLocation());
				((ParticleObject) o).setRemove();
			}
		}
		return true;
	}
	
	
	
}
