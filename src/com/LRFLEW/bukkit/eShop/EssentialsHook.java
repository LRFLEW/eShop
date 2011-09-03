package com.LRFLEW.bukkit.eShop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

public class EssentialsHook {
	private Essentials essentials;
	
	public Essentials getEssentials () { return essentials; }
	public void setEssentials (Essentials essentials) { this.essentials = essentials; }
	public boolean hasEssentials() {return essentials != null; }
	
	public void loadEssentials () {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if (p != null && p.getClass().getName().equals("com.earth2me.essentials.Essentials")) {
			essentials = (Essentials)p;
		}
	}
	
	public Material getMaterial (String s) {
		if (essentials != null) try {
				return essentials.getItemDb().get(s).getType();
		} catch (Exception e) {}
		return null;
	}
	
}
