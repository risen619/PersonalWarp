package com.github.risen619;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.risen619.Executors.PSetWarp;
import com.github.risen619.Executors.PWarp;
import com.github.risen619.Executors.PWarps;
import com.github.risen619.Executors.PAddMember;
import com.github.risen619.Executors.PMyWarps;

public class Main extends JavaPlugin
{
	private PMyWarps pwpExec = new PMyWarps();
	private PWarps pwarpsExec = new PWarps();
	private PWarp pwarpExec = new PWarp();
	private PSetWarp psetWarpExec = new PSetWarp();
	private PAddMember paddMemberExec = new PAddMember();
	
	private WarpsManager wm;
	
	@Override
	public void onEnable()
	{	
		WarpsManager.setServer(getServer());
		getCommand("pmywarps").setExecutor(pwpExec);
		getCommand("pwarps").setExecutor(pwarpsExec);
		getCommand("pwarp").setExecutor(pwarpExec);
		getCommand("psetwarp").setExecutor(psetWarpExec);
		getCommand("paddmember").setExecutor(paddMemberExec);
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