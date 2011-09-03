package com.LRFLEW.bukkit.eShop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

public class Prefs extends Properties {
	private static final long serialVersionUID = 4574320160373783541L;
	
	public void load (Logger log, File pf) {
		clear();
		
		try {
			Properties d = new Properties();
			d.load(this.getClass().getClassLoader().getResourceAsStream(
					"plugin.local.txt"));
			putAll(d);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File(pf + File.separator + "plugin.local.txt");
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			pf.mkdirs();
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				InputStream dflt = this.getClass().getClassLoader().getResourceAsStream(
						"plugin.local.txt");
				byte[] buffer = new byte[dflt.available()];
				for (int i = 0; i != -1; i = dflt.read(buffer)) {
					out.write(buffer, 0, i);
				}
				log.log(Level.WARNING, "[eShop] plugins/eShop/plguin.local.txt has been created. " +
						"you can change the messages the player sees " +
						"by editing that file");
				in = new FileInputStream(file);
			} catch (IOException e1) {
				log.log(Level.SEVERE, "[eShop] unable to write plugins/eShop/plugin.local.txt! " +
						"using the default messages");
				
				in = this.getClass().getClassLoader().getResourceAsStream(
						"plugin.local.txt");
			}
		}
		try {
			load(in);
		} catch (IOException e) {
			log.log(Level.SEVERE, "[eShop] unable to read plugins/eShop/item.price.txt! " +
					"make sure the system can read the file");
		}
	}
	
	
	public void sendMessage ( CommandSender sender, String toSend, Object... replacements ) {
		String t = getProperty(toSend);
		if (t == null) new Error("Missing Property " + toSend).printStackTrace();
		t = formatString(t);
		
		if (t.endsWith("\u00A7") || t.contains("\u00A7\n")) new Exception("Property " + toSend + 
				" ends with excape char '&'").printStackTrace();
		
		String[] msgs = t.split("\n");
		char lc = 'f';
		int count = 0;
		for (String s : msgs) {
			while (s.contains("\u0000") && count < replacements.length) {
				String r;
				if (replacements[count] instanceof MaterialData) {
					r = replacements[count++].toString().replaceAll("\\(\\d*\\)", "");
				} else {
					r = replacements[count++].toString();
				}
				s = s.replaceFirst("\u0000", r);
			}
			sender.sendMessage("\u00A7" + lc + s);
			int test = s.lastIndexOf("\u00A7");
			if (test != -1) lc = s.charAt(test+1);
		}
	}
	
	public static String formatString ( String toFormat ) {
		String s = toFormat;
		s = s.replace("&&", "\u0000");
		s = s.replace("&r", "\n");
		s = s.replace("&R", "\n");
		s = s.replace("&n", "\n");
		s = s.replace("&N", "\n");
		s = s.replace("&", "\u00A7");
		s = s.replace("\u0000", "&");
		s = s.replace("\u00A7g", "\u0000");
		s = s.replace("\u00A7G", "\u0000");
		
		return s;
	}
	
}
