package io.github.amutau.ValAnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleObject implements PlayerObject {
	
	public Location loc;
	double vel = 0;
	public String type;
	int life = 0;
	int lifespan = 200;
	Particle prtcl;
	public boolean remove = false;
	int amt = 1;
	private double[] way;
	private double[] dir;
	private double[] shape;
	private int[] color;
	private double yaw;
	private double density;
	private String onCollide = "null";
	public String what;
	
	public ParticleObject(Location loc, double[] shape, String type) {
		this.loc = loc;
		this.shape = shape;
		this.type = type;
		this.yaw = loc.getYaw();
		
		way = AgentFunc.getWay(this.yaw);
		dir = AgentFunc.getDir(this.yaw);
		
		this.life = 0;
	}
	
	public ParticleObject(Location loc, double[] shape, int amt, int lifespan, String type, double density, int[] color) {
		this(loc,shape,type);
		
		this.lifespan = lifespan;
		this.amt = amt;
		this.color = color;	
		this.density = density;
	}
	
	public ParticleObject(Location loc, double[] shape, int amt, int lifespan, String type, double density, int[] color, double vel, 
			String onCollide, String what) {
		this(loc,shape,amt,lifespan,type,density,color);
		this.vel = vel;
		this.onCollide = onCollide;
		this.what = what;
	}
	
	
	
	public void update() {
		age();
		show();
		move();
	}
	
	public void age() {
		this.life++;
		Bukkit.broadcastMessage("LIFE: " + this.life + ".. SPAN: " + this.lifespan + ".. BOOL: " + (this.life>this.lifespan));
		if(this.life > this.lifespan) {
			setRemove();
		}
	}
	
	public boolean show() {
		
		if(this.type.equalsIgnoreCase("sphere")) {
			this.sphere();
		} else if(this.type.equalsIgnoreCase("cuboid")) {
			this.cuboid();
		} else {
			this.simple();
		}
		
		return true;
	}
	
	public void simple() {
		
		double red = this.color[0]/255D;
		double green = this.color[1]/255D;
		double blue = this.color[2]/255D;
		this.loc.getWorld().spawnParticle(Particle.SPELL_MOB, this.loc, 0, red, green, blue, this.amt);
	}
	
	public void cuboid() {
		
		double step = this.density / 20;
		
		for(double i=0; i < this.shape[0]; i+=step) {
			for(double j=0; j < this.shape[1]; j+=step) {
				for(double k=0; k < this.shape[2]; k+=step) {
					
					double px = loc.getX()+way[0]*((this.shape[0]-1)/2 - i) + dir[0]*(this.shape[2]-k);
					double py = loc.getY()+way[1]*(j);
					double pz = loc.getZ()+way[2]*((this.shape[0]-1)/2 - i) + dir[2]*(this.shape[2]-k);
					
					Location temp = loc.clone();
					temp.setX(px);
					temp.setY(py);
					temp.setZ(pz);
					
					double red = this.color[0]/255D;
					double green = this.color[1]/255D;
					double blue = this.color[2]/255D;
					this.loc.getWorld().spawnParticle(Particle.SPELL_MOB, temp, 0, red, green, blue, 1);
					
					
				
				}
			}
		}
	}
	
	public void sphere() {
		
		double step = this.density / 10;
		
		for(double theta=0; theta < 2*Math.PI; theta+=step) {
			for(double phi=0; phi < Math.PI; phi+=step) {
			
			double sinTheta = Math.sin(theta);
			double cosTheta = Math.cos(theta);
			
			double sinPhi = Math.sin(phi);
			double cosPhi = Math.cos(phi);
			
			double px = loc.getX()+ (this.shape[0])*sinPhi*cosTheta;
			double py = loc.getY()+ (this.shape[1])*sinPhi*sinTheta+1;
			double pz = loc.getZ()+ (this.shape[2])*cosPhi;
			
			
			
			Location temp = loc.clone();
			temp.setX(px);
			temp.setY(py);
			temp.setZ(pz);
			
			double red = this.color[0]/255D;
			double green = this.color[1]/255D;
			double blue = this.color[2]/255D;
			this.loc.getWorld().spawnParticle(Particle.SPELL_MOB, temp, 0, red, green, blue, 1);
								
							
			}
		}
		
	}
	
	public void move() {
		
		checkMovement();
		
		loc.setX(loc.getX() + vel*dir[0]);
		loc.setY(loc.getY() + vel*dir[1]);
		loc.setZ(loc.getZ() + vel*dir[2]);
	}
	
	public void checkMovement() {
		
		if(this.onCollide.equalsIgnoreCase("stop")) {
			if(Collision.collide(this.loc, this.dir, this.vel)) {
				this.vel = 0;
			}
		} else if(this.onCollide.equals("bounce")) {
			
		} else {
			
		}
		
	}
	
	public void setRemove() {
		this.remove = true;
	}
	
	public boolean getRemove() {
		return this.remove;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
}
