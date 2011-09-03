package com.LRFLEW.bukkit.eShop;

import org.bukkit.CoalType;
import org.bukkit.DyeColor;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.material.Coal;
import org.bukkit.material.Colorable;
import org.bukkit.material.Leaves;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Step;
import org.bukkit.material.Tree;

public class Materializer {
	
	public static boolean setMD (MaterialData md, String s) {
		try {
			byte b = Byte.parseByte(s);
			md.setData(b);
			return true;
		} catch (NumberFormatException e) {
			if (md instanceof Coal) {
				try {
					CoalType ct = CoalType.valueOf(s.toUpperCase());
					((Coal)md).setType(ct);
					return true;
				} catch (IllegalArgumentException e1) {}
			}
			if (md instanceof Colorable) {
				try {
					DyeColor dc = DyeColor.valueOf(s.toUpperCase());
					((Colorable)md).setColor(dc);
					return true;
				} catch (IllegalArgumentException e1) {}
			}
			if (md instanceof Leaves) {
				try {
					TreeSpecies ts = TreeSpecies.valueOf(s.toUpperCase());
					((Leaves)md).setSpecies(ts);
					return true;
				} catch (IllegalArgumentException e1) {}
			}
			if (md instanceof LongGrass) {
				try {
					GrassSpecies gs = GrassSpecies.valueOf(s.toUpperCase());
					((LongGrass)md).setSpecies(gs);
					return true;
				} catch (IllegalArgumentException e1) {}
			}
			if (md instanceof Step) {
				try {
					Material m = Material.valueOf(s.toUpperCase());
					if (isSteppible(m)) {
						((Step)md).setMaterial(m);
						return true;
					}
				} catch (IllegalArgumentException e1) {}
			}
			if (md instanceof Tree) {
				try {
					TreeSpecies ts = TreeSpecies.valueOf(s.toUpperCase());
					((Tree)md).setSpecies(ts);
					return true;
				} catch (IllegalArgumentException e1) {}
			}
		}
		
		return false;
	}
	
	public static boolean isSteppible (Material m) {
		if (m == Material.SANDSTONE) return true;
		if (m == Material.WOOD) return true;
		if (m == Material.COBBLESTONE) return true;
		if (m == Material.STONE) return true;
		return false;
	}
	
}
