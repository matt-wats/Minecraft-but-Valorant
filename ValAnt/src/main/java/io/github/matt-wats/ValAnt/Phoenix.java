package io.github.amutau.ValAnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Phoenix extends AgentFunc implements Agent {
	
	Material[] mts = {Material.IRON_BARS, Material.SNOWBALL, Material.SLIME_BALL, Material.RED_BANNER};
	String[] nms = {"Phoenix Wall", "Phoenix Moli?", "2", "Phoenix Ult"};
	
	double wallSize = 16;
	double wallHeight = 3;
	
	static double ringSize = 5;
	
	Location ultLoc;
	
	boolean ult = false;

	public Phoenix(Player player) {
		super(player);
		initialize(mts,nms);
		
		//player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*60*60*24, 0));
	}
	
	public Phoenix(Player player, ValAnt plugin) {
		this(player);
		
		this.plugin = plugin;
	}

	public boolean util(int n, int m) {

		if(m == 0 && !this.usable(n)) {
			return false;
		}
		
		if(n == 0 && m == 0) {
			fireWall();
		} if(n == 1 && m == 0) {
			fireBall();
		} if(n == 3 && m == 0) {
			ult();
		}
		
		return true;
	}
	
	public void ult() {
		this.ult = true;
		this.player.setFireTicks(20);
		
		this.player.sendMessage("Ult Started");
		
		this.ultLoc = this.player.getLocation();
		int id = plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
		    public void run() {
		        ultEnd();
		    }}, 20*10);
	}
	public void ultEnd() {
		
		if(this.ult) {
			player.setHealth(20);
			player.sendMessage("Ult Ended");
			
			int[] color = {255,0,0};
			double[] shape = {0.25, 0.5, 0.25};
			double density = 15;
			
			this.obs.add(new ParticleObject(this.player.getLocation().add(0,1,0), shape, 1, 1*20, "sphere", density, color));
			
			this.player.teleport(ultLoc);
			this.ult = false;
			
			
		}
		
	}
	
	
	public static void fireBallImpact(Location loc) {
		
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		Bukkit.broadcastMessage("Y LEVEL :" + loc.getY() + ".... BLOCK: " + y);
		
		for(int i=(int) -ringSize; i <= ringSize; i++) {
			for(int j=(int) -ringSize; j <= ringSize; j++) {
				
				int bx = x+i;
				int bz = z+j;
				
				if(dist(x, y, z, bx, y, bz) < ringSize) {
					
					Location place = loc.clone();
					place.setX(bx);
					place.setZ(bz);
					place.setY(y-2);
					
					for(int k=0 ; k < 3; k++) {
						Block block = place.getBlock();
						
						if(block.getType().isSolid()) {
							block.setType(Material.MAGMA_BLOCK);
						} else if(block.isPassable()) {
							block.setType(Material.FIRE);
						}
						place.setY(place.getY()+1);
					}
					
				}
				
			}
		}
		
	}
	
	public void fireBall() {
		
		//player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), 17, 0);
		
		Snowball ball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
		
		
		ball.setVelocity(player.getLocation().getDirection().normalize().multiply(2.0));
		
		ball.setCustomName("Pitbull");
		//ItemMeta meta = ball.getItem().getItemMeta();
		//meta.setCustomModelData(1);
		//ball.getItem().setItemMeta(meta);
		
		
	}
	
	public void fireWall() {
		
		Location loc = this.player.getLocation();
		
		double yaw = loc.getYaw() * Math.PI / 180;
		double[] dir = {-Math.sin(yaw),0,Math.cos(yaw)};
		
		for(int i=0; i < this.wallSize; i++) {
			
			double px = loc.getX()+dir[0]*(i+2);
			double py = loc.getY()+dir[1]*(i+2);
			double pz = loc.getZ()+dir[2]*(i+2);
			
			Location place = loc.clone();
			place.setX(px);
			place.setY(py+1);
			place.setZ(pz);

			Block block = place.getBlock();
			
			while(block.isPassable()) {
				if(place.getY() > 0) {
					place.setY(place.getY()-1);
				}
				block = place.getBlock();
			}
			
			for(int j=0; j < this.wallHeight; j++) {
				
				place.setY(place.getY()+1);
				block = place.getBlock();
				
				if(block.isPassable()) {
					if(j < this.wallHeight-1) {
						block.setType(Material.MAGMA_BLOCK);
					} else if(j == this.wallHeight-1) {
						block.setType(Material.FIRE);
					}
				}
				
			}
			
			
		}
		
		this.delay[0] = 10*20;
	}

}
