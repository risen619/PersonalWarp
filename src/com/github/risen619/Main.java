package com.github.risen619;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.risen619.Executors.PSetWarp;
import com.github.risen619.Executors.PWarp;
import com.github.risen619.Executors.PWarps;
import com.github.risen619.Executors.PMyWarps;

public class Main extends JavaPlugin
{
	public PMyWarps pwpExec = new PMyWarps();
	public PWarps pwarpsExec = new PWarps();
	public PWarp pwarpExec = new PWarp();
	public PSetWarp psetWarpExec = new PSetWarp();
	
	public WarpsManager wm;
	
	@Override
	public void onEnable()
	{	
		WarpsManager.setServer(getServer());
		getCommand("pmywarps").setExecutor(pwpExec);
		getCommand("pwarps").setExecutor(pwarpsExec);
		getCommand("pwarp").setExecutor(pwarpExec);
		getCommand("psetwarp").setExecutor(psetWarpExec);
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