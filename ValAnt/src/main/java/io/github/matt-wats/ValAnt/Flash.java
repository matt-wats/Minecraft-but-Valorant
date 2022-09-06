package io.github.amutau.ValAnt;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Flash implements PlayerObject {
	
	int time = 5*20;
	
	int airTime = 3*20;
	
	double curr;
	
	String onCollide = "null";
	
	int[] color = {255,255,255};
	
	Location loc;
	
	Vector vel = new Vector(0.5,0.5,0.5);
	
	Vector accel = new Vector(0.0,0.0,0.0);
	
	Vector dir;
	
	boolean bursted = false;

	private boolean remove;
	
	
	
	
	
	public Flash(Location loc, String onCollide) {
		this.loc = loc;
		this.onCollide = onCollide;
		this.curr = 0;
		Bukkit.broadcastMessage("Creating flash");
		this.dir = loc.getDirection();
	}
	
	public Flash(Location loc, String onCollide, Vector vel, int[] color, int time, int airTime) {
		this(loc,onCollide);
		
		this.vel = vel;
		this.color = color;
		this.time = time;
		this.airTime = airTime;
	}
	
	public Flash(Location loc, String onCollide, Vector vel, int[] color, int time, int airTime, Vector accel) {
		this(loc,onCollide,vel,color,time,airTime);
		
		this.accel = accel;
	}
	
	public void update() {
		move();
		show();
		age();
	}
	
	public void age() {
		this.curr++;
		Bukkit.broadcastMessage("CURR: " + this.curr);
		if(this.curr > this.airTime) {
			this.burst();
		}
	}
	
	public void burst() {
		this.bursted = true;
		loc.getWorld().spawnParticle(Particle.FLASH, loc, 3);
		this.blind();
		this.remove = true;
		Bukkit.broadcastMessage("BLINDING");
	}
	
	public void blind() {
		
		Player[] onPlayers = (Player[]) Bukkit.getOnlinePlayers().toArray();
		
		for(Player player: onPlayers) {
			
			Location pLoc = player.getEyeLocation().clone();
			
			double px = pLoc.getX();
			double py = pLoc.getY();
			double pz = pLoc.getZ();
			
			double fx = this.loc.getX();
			double fy = this.loc.getY();
			double fz = this.loc.getZ();
			
			double step = 1.0 / 50.0;
			for(int t=0; t < 1; t+=step) {
				
				double cx = (1-t)*px + t*fx;
				double cy = (1-t)*py + t*fy;
				double cz = (1-t)*pz + t*fz;
				
				pLoc.setX(cx);
				pLoc.setY(cy);
				pLoc.setZ(cz);
				
				if(!player.getWorld().getBlockAt(pLoc).getType().isOccluding()) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.time, 0));
				}
				
			}
			
			
		}
		
	}
	
	public void show() {
		Bukkit.broadcastMessage("SHowing flash");
		double[] shape = {0.125,0.125,0.125};
		new ParticleObject(this.loc, shape, 1, 1, "sphere",10,this.color);
		
	}
	
	public void onCollision() {
		if(this.onCollide.equalsIgnoreCase("null")) {
			
		} else if(this.onCollide.equalsIgnoreCase("bounce")) {
			this.vel = Collision.bounce(this.loc, this.vel);
		}
	}
	
	public void move() {
		
		loc.setX(loc.getX() + vel.getX()*dir.getX());
		loc.setY(loc.getY() + vel.getY()*dir.getY());
		loc.setZ(loc.getZ() + vel.getZ()*dir.getZ());
		
		accel();
	}
	
	public void accel() {
		
		vel.setX(vel.getX() + accel.getX());
		vel.setY(vel.getY() + accel.getY());
		vel.setZ(vel.getZ() + accel.getZ());
		
	}
	
	public boolean getRemove() {
		return this.remove;
	}

	public Location getLocation() {
		return this.loc;
	}
	
	//make clone of player location
	//if when clone looks at flash there are no blocks in between its eye sight path
	//then give blindess for time
	
	//and maybe if they are looking somewhere directly away from it (within pi radians)
	//half time for blindness
	
	
}
