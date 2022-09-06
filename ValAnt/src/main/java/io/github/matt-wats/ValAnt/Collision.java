package io.github.amutau.ValAnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Collision {
	
	Location loc;
	
	double vel;
	double[] dir;
	
	public static boolean collide(Location loc, double[] dir, double vel) {
		
		World w = loc.getWorld();
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		double vx = dir[0];
		double vy = dir[1];
		double vz = dir[2];
		
		double nx = x + vel*vx;
		double ny = y + vel*vy;
		double nz = z + vel*vz;
		
		Location temp = loc.clone();
		temp.setX(nx);
		temp.setY(ny);
		temp.setZ(nz);
		
		if(!w.getBlockAt(temp).getType().isAir()) {
			return true;
		}
		
		return false;
	}
	
	public static Vector bounce(Location loc, Vector vel) {
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		World w = loc.getWorld();
		
		while(w.getBlockAt(loc).isPassable()) {
			double inc = 0.05;
			
			loc.setX(loc.getX() + inc*vel.getX());
			loc.setY(loc.getY() + inc*vel.getY());
			loc.setZ(loc.getZ() + inc*vel.getZ());
			
		}
		
		int xr = (int) (x - Math.abs(Math.floor(x)));
		int yr = (int) (y - Math.abs(Math.floor(y)));
		int zr = (int) (z - Math.abs(Math.floor(z)));
		
		double xdif = xr / vel.getX();
		double ydif = yr / vel.getY();
		double zdif = zr / vel.getZ();
		
		if(Math.min(xdif, ydif) == xdif) {
			if(Math.min(xdif, zdif) == xdif) {
				vel.setX(-vel.getX());
			} else {
				vel.setZ(-vel.getZ());
			}
		} else {
			if(Math.min(ydif, zdif) == zdif) {
				vel.setZ(-vel.getZ());
			} else {
				vel.setY(-vel.getY());
			}
		}
		
		return vel;
	}
	
}
