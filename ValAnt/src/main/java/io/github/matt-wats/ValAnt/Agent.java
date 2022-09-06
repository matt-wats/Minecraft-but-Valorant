package io.github.amutau.ValAnt;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public interface Agent {
	
	public Player getPlayer();
	public ArrayList<PlayerObject> getObs();
	public boolean util(int n, int m);
	public void update();
	
	//public void initialize();
	
	
}
