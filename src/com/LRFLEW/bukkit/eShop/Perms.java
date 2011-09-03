package com.LRFLEW.bukkit.eShop;

import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

public class Perms {
	private boolean useYeti = false;
	
	@SuppressWarnings("deprecation")
	public boolean hasPermission (Player p, String s) {
		if (useYeti) return Permissions.Security.has(p, s);
		else return p.hasPermission(s);
	}
	
	public void setYeti (Prefs prefs) {
		if (prefs.getProperty("Permissions") != null) useYeti = true;
		else useYeti = false;
	}
	
}
