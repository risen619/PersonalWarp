package com.github.risen619;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.risen619.Executors.PSetWarp;
import com.github.risen619.Executors.PSign;
import com.github.risen619.Executors.PWarp;
import com.github.risen619.Executors.PWarps;
import com.github.risen619.Listeners.BlockPlaceListener;
import com.github.risen619.Listeners.SignClickListener;
import com.github.risen619.TabCompleters.PAddMemberCompleter;
import com.github.risen619.TabCompleters.PDelWarpCompleter;
import com.github.risen619.TabCompleters.PSetWarpCompleter;
import com.github.risen619.TabCompleters.PSignCompleter;
import com.github.risen619.TabCompleters.PWarpCompeleter;
import com.github.risen619.Executors.PAddMember;
import com.github.risen619.Executors.PDelWarp;
import com.github.risen619.Executors.PMyWarps;

public class Main extends JavaPlugin
{	
	private WarpsManager wm;
	
	@Override
	public void onEnable()
	{	
		WarpsManager.setServer(getServer());
		
		getCommand("pmywarps").setExecutor(new PMyWarps());
		
		getCommand("pwarps").setExecutor(new PWarps());
		
		getCommand("pwarp").setExecutor(new PWarp());
		getCommand("pwarp").setTabCompleter(new PWarpCompeleter());
		
		getCommand("psetwarp").setExecutor(new PSetWarp());
		getCommand("psetwarp").setTabCompleter(new PSetWarpCompleter());
		
		getCommand("paddmember").setExecutor(new PAddMember());
		getCommand("paddmember").setTabCompleter(new PAddMemberCompleter());
		
		getCommand("pdelwarp").setExecutor(new PDelWarp());
		getCommand("pdelwarp").setTabCompleter(new PDelWarpCompleter());
		
		getCommand("psign").setExecutor(new PSign());
		getCommand("psign").setTabCompleter(new PSignCompleter());
		
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
		getServer().getPluginManager().registerEvents(new SignClickListener(), this);
		
		wm = WarpsManager.getInstance();
		getLogger().info("PersonalWarp enabled.");
	}
	
	@Override
	public void onDisable()
	{
		wm.closeDb();
		getLogger().info("PersonalWarp disabled.");
	}
}