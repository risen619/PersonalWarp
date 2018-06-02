package com.github.risen619;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.risen619.Executors.PSetWarp;
import com.github.risen619.Executors.PWarp;
import com.github.risen619.Executors.PWarps;
import com.github.risen619.TabCompleters.PAddMemberCompleter;
import com.github.risen619.TabCompleters.PDelWarpCompleter;
import com.github.risen619.TabCompleters.PSetWarpCompleter;
import com.github.risen619.TabCompleters.PWarpCompeleter;
import com.github.risen619.Executors.PAddMember;
import com.github.risen619.Executors.PDelWarp;
import com.github.risen619.Executors.PMyWarps;

public class Main extends JavaPlugin
{
	private PMyWarps pwpExec = new PMyWarps();
	private PWarps pwarpsExec = new PWarps();
	private PWarp pwarpExec = new PWarp();
	private PSetWarp psetWarpExec = new PSetWarp();
	private PAddMember paddMemberExec = new PAddMember();
	private PDelWarp pdelWarpExec = new PDelWarp();
	
	private WarpsManager wm;
	
	@Override
	public void onEnable()
	{	
		WarpsManager.setServer(getServer());
		
		getCommand("pmywarps").setExecutor(pwpExec);
		
		getCommand("pwarps").setExecutor(pwarpsExec);
		
		getCommand("pwarp").setExecutor(pwarpExec);
		getCommand("pwarp").setTabCompleter(new PWarpCompeleter());
		
		getCommand("psetwarp").setExecutor(psetWarpExec);
		getCommand("psetwarp").setTabCompleter(new PSetWarpCompleter());
		
		getCommand("paddmember").setExecutor(paddMemberExec);
		getCommand("paddmember").setTabCompleter(new PAddMemberCompleter());
		
		getCommand("pdelwarp").setExecutor(pdelWarpExec);
		getCommand("pdelwarp").setTabCompleter(new PDelWarpCompleter());
		
		
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