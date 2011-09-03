package com.LRFLEW.bukkit.eShop;

import java.util.HashMap;

import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.LRFLEW.register.payment.Methods;

public class EShop extends JavaPlugin {
	
	public final HashMap<MaterialData, Pricing> costs = new HashMap<MaterialData, Pricing>();
	public final HashMap<String, MaterialData> iLocal = new HashMap<String, MaterialData>();
	
	private final EssentialsHook eh = new EssentialsHook();
	private final Prefs prefs = new Prefs();
	private final Perms perms = new Perms();
	private final Com com = new Com(costs, iLocal, prefs, perms, eh);

	@Override
	public void onDisable() {
		costs.clear();
		iLocal.clear();
		prefs.clear();

        // NOTE: All registered events are automatically unregistered when a plugin is disabled
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " says Goodbye!" );
	}

	@Override
	public void onEnable() {
		if (!Methods.hasMethod()) this.getServer().getPluginManager().disablePlugin(this);
		
		else {
			Pricing.fillSettings(getServer().getLogger(), getDataFolder(), costs);
			Pricing.fillILocal(getServer().getLogger(), getDataFolder(), iLocal);
			prefs.load(getServer().getLogger(), getDataFolder());
			perms.setYeti(prefs);
			
			eh.loadEssentials();
			
			// set our CommandExecuter for our command
			this.getCommand("shop").setExecutor(com);
			
			// EXAMPLE: Custom code, here we just output some info so we can check all is well
			PluginDescriptionFile pdfFile = getDescription();
			System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		}
	}

}
