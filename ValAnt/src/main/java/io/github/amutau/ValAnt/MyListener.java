package io.github.amutau.ValAnt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MyListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(PlayerInteractEvent e) {
		if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() > -1) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		//String s = "snowball";
		String name = e.getEntity().getName();
		
		if(name.equalsIgnoreCase("Pitbull")) {
			Phoenix.fireBallImpact(e.getEntity().getLocation());
		}
		
	}
	
//	@EventHandler
//	public void res(EntityResurrectEvent e) {
//		
//		if(e instanceof Player) {
//			Player player = (Player) e.getEntity();
//			
//			PlayerInventory inv = player.getInventory();
//			
//			if(inv.getItemInMainHand().getItemMeta().getCustomModelData() > -1 || inv.getItemInOffHand().getItemMeta().getCustomModelData() > -1) {
//				e.setCancelled(true);
//			}
//			
//		}
//		
//	}
	
	@EventHandler
    public void fire(EntityDamageEvent e){
        Entity entity = e.getEntity();

        if(entity instanceof Player) {
        	
        	Player player = (Player) entity;
			ArrayList<Agent> agents = ValAnt.agents;
			
			for(Agent a: agents) {
				if(a.getPlayer().equals(player)) {
					
					if(a instanceof Yoru && ((Yoru) a).ult) { // yoru
						e.setCancelled(true);
						player.setFireTicks(0);
					} else if(a instanceof Phoenix) { // phoenix
						if(e.getCause()==DamageCause.FIRE||e.getCause()==DamageCause.FIRE_TICK||e.getCause()==DamageCause.HOT_FLOOR) {
							e.setCancelled(true);
						} else if(((Phoenix) a).ult) {
							if(player.getHealth() - e.getFinalDamage() <= 0) {
								((Phoenix) a).ultEnd();
								e.setCancelled(true);
							}
						}
						
					} // end of agents
					
				}
			}
        		
    		
        		
        	
        	
        }
        
        if(entity instanceof Player && e.getCause() == DamageCause.FIRE) {
        	Player player = (Player) entity;
			ArrayList<Agent> agents = ValAnt.agents;
			
			for(Agent a: agents) {
				if(a.getPlayer().equals(player) && a instanceof Phoenix) {
					player.setHealth(player.getHealth() + (1.0/20.0));
				}
			}
        }
    }
	
	
//	@EventHandler 
//	public void onDeath(PlayerDeathEvent e) {
//		
//		Player player = e.getEntity();
//		ArrayList<Agent> agents = ValAnt.agents;
//		
//		for(Agent a: agents) {
//			if(a.getPlayer().equals(player) && a instanceof Phoenix && ((Phoenix) a).ult) {
//				
//				e.
//				
//			}
//		}
//		
//	}
	
	
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		Action action = e.getAction();
		
		ItemStack itm = e.getItem();
		int util = itm.getItemMeta().getCustomModelData();
		ArrayList<Agent> agents = ValAnt.agents;
		
		if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
			
			for(Agent a: agents) {
				if(a.getPlayer().equals(player) && util > -1 && util < 4) {
					e.setCancelled(true);
					a.util(util,0);
				}
			}
			
		} else if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
			
			for(Agent a: agents) {
				if(a.getPlayer().equals(player) && util > -1 && util < 4) {
					e.setCancelled(true);
					a.util(util,1);
				}
			}
			
		}
		
		
	}
	
	

}
