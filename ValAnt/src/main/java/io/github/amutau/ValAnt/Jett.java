package io.github.amutau.ValAnt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Jett extends AgentFunc implements Agent {
	
	Material[] mts = {Material.FEATHER, Material.RABBIT_FOOT, Material.GRAY_STAINED_GLASS, Material.ARROW};
	String[] nms = {"Jett Up Draft", "Jett Dash", "Jett Smoke", "Jett Ultimate"};
	
	boolean ult = false;
	int knives = 0;
	private ItemStack knife;
	
	public Jett(Player player) {
		super(player);
		initialize(mts,nms);
		initKnife();
	}

	public boolean util(int n, int m) {
		
		if(m == 0 && !this.usable(n)) {
			return false;
		}
		
		if(m == 1 && n != 3) {
			slowFall();
			return true;
		}
		
		if(n == 0) {
			upDraft();
		} else if(n == 1) {
			dash();
		} else if(n == 2) {
			smoke();
		} else if(n == 3) {
			ult(m);
		}
		
		
		
		return true;
	}
	
	public void update() {
		super.update();
		
		if(player.isOnGround()) {
			player.removePotionEffect(PotionEffectType.SLOW_FALLING);
		}
	}
	
	public void ult(int n) {
		if(n == 0) {
			activateUlt();
		} if(n == 1) {
			throwKnife();
		}
	}
	
	public void smoke() {
		
		Location trgt = this.player.getTargetBlock(null, 10).getLocation().add(0, 1, 0);
		double[] shape = {2,2,2};
		int[] color = {255, 255, 255};
		
		this.player.sendMessage("SMOKING");
		double density = 1;
		
		this.obs.add(new ParticleObject(trgt, shape, 1, 5*20, "sphere", density, color));
		
	}
	
	public void throwKnife() {
		
		if(this.ult) {
			player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), 17, 0);
			if(this.knives > 1) {
				removeKnife(this.knives);
				player.sendMessage("You have arrows remaining: " + this.knives);
			} else {
				player.sendMessage("Out of ult");
				this.ult = false;
			}
		}
		
		
	}
	
	public void removeKnife(int n) {
		ItemStack k = this.knife.clone();
		k.setAmount(n);
		player.getInventory().remove(k);
		this.giveKnife(n-1);
		this.knives--;
	}
	
	public void activateUlt() {
		this.ult = true;
		this.knives = 5;
		this.player.sendMessage("Ult Activated");
		
		this.delay[3] = 20*15;
		
		giveKnife(4);
	}
	
	public void initKnife() {
		ItemStack itm = new ItemStack(mts[3]);
		ItemMeta meta = itm.getItemMeta();
		meta.setDisplayName(nms[3]);
		meta.setCustomModelData(3);
		itm.setItemMeta(meta);
		this.knife = itm;
	}
	
	public void giveKnife(int n) {
		for(int i=0; i < n; i++) {
			player.getInventory().addItem(this.knife);
		}
	}
	
	public void upDraft() {
		
		Vector v = player.getVelocity();
		
		player.setVelocity(new Vector(v.getX(),1.618,v.getZ()));
		
		this.delay[0] = 20*10;
	}
	
	public void slowFall() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,60*20*60,0));
		
	}
	
	public void dash() {
		Vector v = player.getVelocity();
		
		Location loc = this.player.getLocation();
		double yaw = loc.getYaw() * Math.PI / 180;
		double[] dir = {-Math.sin(yaw),0.314,Math.cos(yaw)};
		
		loc.setY(loc.getY()+0.25);
		player.teleport(loc);
		
		double s = 1;
		player.setVelocity(new Vector(v.getX()+dir[0]*s,v.getY()+dir[1]*s,v.getZ()+dir[2]*s));
		
		this.delay[1] = 20*10;
	}

}
