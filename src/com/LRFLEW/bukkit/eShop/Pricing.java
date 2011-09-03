package com.LRFLEW.bukkit.eShop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.LRFLEW.register.payment.Methods;

public class Pricing {
	public final double buy, sell;
	
	public Pricing(double buy, double sell) {
		this.buy = buy;
		this.sell = sell;
	}
	
	public static void fillSettings ( Logger log, File pf, HashMap<MaterialData, Pricing> costs ) {
		costs.clear();
		String string;
		FileReader in = null;
		try {
			in = new FileReader(pf + File.separator + "item.price.txt");
		} catch (FileNotFoundException e) {
			pf.mkdirs();
			File file = new File(pf + File.separator + "item.price.txt");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "[eShop] unable to write a new plugins/eShop/item.price.txt " +
						"please create the file with each line reading the numbers for: " +
						"\"ID [DATA] BUY SELL\"");
				costs.clear();
				costs.clear();
				return;
			}
			log.log(Level.SEVERE, "[eShop] plugins/eShop/item.price.txt has been created " +
					"you now need to fill up the file with each line reading the numbers for: " +
					"\"ID [DATA] BUY SELL\"");
			costs.clear();
			return;
		}
		BufferedReader br = new BufferedReader(in);
		int line=0;
		byte i=0;
		boolean rounded = false;
		try {
			double dec;
			if (Methods.getMethod().fractionalDigits() == -1) dec = -1;
			else dec = Math.pow(10, Methods.getMethod().fractionalDigits());
			for (line=1; (string = br.readLine()) != null; line++) {
				String[] s = string.split(" ");
				int id;
				byte data;
				double buy, sell, temp;
				if (s.length == 3) {
					i=0; id = Integer.parseInt(s[0]); 
					if (id <= 0) throw new NumberFormatException();
					
					i=1; buy = Double.parseDouble(s[1]);
					if (buy < 0) throw new NumberFormatException();
					if (dec!=-1) {
						temp = Math.floor(buy*dec + 0.5D)/dec;
						if (temp != buy) rounded = true;
						buy = temp;
					}
					
					i=2; sell = Double.parseDouble(s[2]);
					if (sell < 0) throw new NumberFormatException();
					if (dec!=-1) {
						temp = Math.floor(sell*dec + 0.5D)/dec;
						if (temp != sell) rounded = true;
						sell = temp;
					}
					
					if (buy == 0 && sell == 0) continue;
					
					if (Material.getMaterial(id).getData() == null) data = 0;
					else data = -1;
					
					costs.put(new MaterialData(id, data), new Pricing( buy, sell ));
				} else if (s.length == 4) {
					i=0; id = Integer.parseInt(s[0]); 
					if (id <= 0) throw new NumberFormatException();
					
					i=1; buy = Double.parseDouble(s[2]);
					if (buy < 0) throw new NumberFormatException();
					if (dec!=-1) {
						temp = Math.round(buy*dec)/dec;
						if (temp != buy) rounded = true;
						buy = temp;
					}
					
					i=2; sell = Double.parseDouble(s[3]);
					if (sell < 0) throw new NumberFormatException();
					if (dec!=-1) {
						temp = Math.round(sell*dec)/dec;
						if (temp != sell) rounded = true;
						sell = temp;
					}
					
					i=3; data = Byte.parseByte(s[1]);
					if (data < 0) throw new NumberFormatException();
					
					if (buy == 0 && sell == 0) continue;
					
					if (Material.getMaterial(id).getMaxDurability() != -1 && data != 0) data = -1;
					else if (Material.getMaterial(id).getData() == null) data = 0;
					
					costs.put(new MaterialData(id, data), new Pricing( buy, sell ));
				} else {
					log.log(Level.SEVERE, "[eShop] there's an error in plugins/eShop/item.price.txt! " +
							"line " + line + " has an invalid number of parameters. " +
									"each line should have numbers for: " +
									"\"ID [DATA] BUY SELL\"");
					costs.clear();
					return;
				}
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "[eShop] unable to read plugins/eShop/item.price.txt! " +
					"make sure the system can read the file");
			
			costs.clear();
			return;
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "[eShop] Invalid entry in plugins/eShop/item.price.txt " +
					"on line " + line);
			if (i==0) log.log(Level.SEVERE, "[eShop] the item ID needs to be a positive integer");
			if (i==1) log.log(Level.SEVERE, "[eShop] the buying amount needs to be a positive floating point number");
			if (i==2) log.log(Level.SEVERE, "[eShop] the selling amount needs to be a positive floating point number");
			if (i==3) log.log(Level.SEVERE, "[eShop] the input for the data is invalid");
							
			costs.clear();
			return;
		}
		if (rounded) log.log(Level.WARNING, "[eShop] Prices have had to be rounded to " + 
		Methods.getMethod().fractionalDigits() + " decimal places. " + 
				"Current economies my become unballanced");
	}
	
	public static void fillILocal ( Logger log, File pf, HashMap<String, MaterialData> iLocal ) {
		iLocal.clear();
		String string;
		FileReader in = null;
		try {
			in = new FileReader(pf + File.separator + "item.local.txt");
		} catch (FileNotFoundException e) {
			pf.mkdirs();
			File file = new File(pf + File.separator + "item.local.txt");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "[eShop] unable to write a new plugins/eShop/item.local.txt " +
						"please create the file with each line reading: " +
						"\"NAME ID [DATA]\"");
				iLocal.clear();
				return;
			}
			log.log(Level.WARNING, "[eShop] plugins/eShop/item.local.txt has been created " +
					"you now should fill up the file with each line reading: " +
					"\"NAME ID [DATA]\"");
			iLocal.clear();
			return;
		}
		BufferedReader br = new BufferedReader(in);
		int line=0;
		byte i=0;
		try {
			for (line=1; (string = br.readLine()) != null; line++) {
				String[] s = string.split(" ");
				int id;
				byte data;
				if (s.length == 2) {
					if (s[0].contains(":")) throw new StringFormatException();
					
					i=0; id = Integer.parseInt(s[1]);
					if (id <= 0) throw new NumberFormatException();
					
					data = 0;
					
					iLocal.put(s[0].toLowerCase(), new MaterialData(id, data));
				} else if (s.length == 3) {
					if (s[0].contains(":")) throw new StringFormatException();
					
					i=0; id = Integer.parseInt(s[1]);
					if (id <= 0) throw new NumberFormatException();
					
					i=1; data = Byte.parseByte(s[2]);
					if (data < 0) throw new NumberFormatException();
					
					if (Material.getMaterial(id).getData() == null) data = 0;
					
					iLocal.put(s[0].toLowerCase(), new MaterialData(id, data));
				} else {
					log.log(Level.SEVERE, "[eShop] there's an error in plugins/eShop/item.local.txt! " +
							"line " + line + " has an invalid number of parameters. " +
							"each line should have: " +
							"\"NAME ID [DATA]\"");
					iLocal.clear();
					return;
				}
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "[eShop] unable to read plugins/eShop/item.local.txt! " +
					"make sure the system can read the file");
			
			iLocal.clear();
			return;
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "[eShop] Invalid entry in plugins/eShop/item.local.txt " +
					"on line " + line);
			if (i==0) log.log(Level.SEVERE, "[eShop] the item ID needs to be a positive integer");
			if (i==1) log.log(Level.SEVERE, "[eShop] the input for the data is invalid");
			
			iLocal.clear();
			return;
		} catch (StringFormatException e) {
			log.log(Level.SEVERE, "[eShop] Invalid entry in plugins/eShop/item.local.txt " +
					"on line " + line);
			log.log(Level.SEVERE, "[eShop] the name cannot contain ':'");
		}
	}
	
	public static class StringFormatException extends Exception {
		private static final long serialVersionUID = -3483180730246122036L;
	}
	
}
