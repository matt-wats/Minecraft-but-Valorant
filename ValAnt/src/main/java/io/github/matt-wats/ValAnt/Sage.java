package io.github.amutau.ValAnt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Sage extends AgentFunc implements Agent {
	
	Material[] mts = {Material.IRON_BARS, Material.SLIME_BALL, Material.SLIME_BALL, Material.SLIME_BALL};
	String[] nms = {"Sage Wall", "1", "2", "3"};
	
	double wallSize = 12;
	double wallThick = 2;
	double wallHeight = 3;
	
	
	public Sage(Player player) {
		super(player);
		initialize(this.mts,this.nms);
	}

	public boolean util(int n, int m) {
		
		if(m == 0 && !this.usable(n)) {
			return false;
		}
		
		if(n == 0) {
			wall(m);
		}
		
		return true;
	}
	
	
	//Util 0 --> Sage Wall
	public void wall(int n) {
		if(n == 1) {
			seeWall();
		} else if(n == 0) {
			placeWall();
		}
	}
	public void seeWall() {
		
		Location loc = this.player.getLocation();
		
		Location trgt = this.player.getTargetBlock(null, 5).getLocation();
		trgt.setY(trgt.getY()+1.5);
		trgt.setYaw(loc.getYaw());
		
		double[] wallDim = {this.wallSize,this.wallHeight,this.wallThick};
		int[] color = {20,20,255};
		
		double density = 20;
		this.obs.add(new ParticleObject(trgt, wallDim, 20, 2*20, "cuboid", density, color));
		
		
	}
	public void placeWall() {
		
		Location loc = this.player.getLocation();
		double yaw = loc.getYaw() * Math.PI / 180;
		double[] dir = {-Math.sin(yaw),0,Math.cos(yaw)};
		double[] way = {Math.cos(yaw),1,Math.sin(yaw)};
		
		Location trgt = this.player.getTargetBlock(null, 5).getLocation();
		
		for(int i=0; i < this.wallSize; i++) {
			for(int j=0; j < this.wallHeight; j++) {
				for(int k=0; k < this.wallThick; k++) {
					
					double px = trgt.getBlockX()+way[0]*((this.wallSize-1)/2 - i) + dir[0]*(this.wallThick-k);
					double py = trgt.getBlockY()+way[1]*(j);
					double pz = trgt.getBlockZ()+way[2]*((this.wallSize-1)/2 - i) + dir[2]*(this.wallThick-k);
					
					Location place = trgt.clone();
					place.setX(px);
					place.setY(py+1);
					place.setZ(pz);

					Block block = place.getBlock();
					
					if(block.getType() == Material.AIR) {
						block.setType(Material.BLUE_ICE);
					}
					
				}
			}
		}
		
		this.delay[0] = 20*10;
		
	}

}
