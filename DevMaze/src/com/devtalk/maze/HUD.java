package com.devtalk.maze;

import java.util.ArrayList;
import java.util.List;

public class HUD {
	
	List<HUDModule> modules;
	
	public HUD(DevMaze g) {
		this.modules = new ArrayList<HUDModule>();
		this.modules.add(new AButtonModule(g));
		this.modules.add(new BButtonModule(g));
		this.modules.add(new DPadModule(g));
		this.modules.add(new PauseModule(g));
	}
	
	public void render() {
		for (HUDModule module : modules)
			module.render();
	}
	
	public boolean actionedAt(int x, int y) {
		for (HUDModule module : this.modules)
			if (module.actionedAt(x, y))
				return true;
		
		return false;
	}
	
	public void stopAction(int x, int y) {
		for (HUDModule module : this.modules)
			module.stopAction(x, y);
	}
	
	public void dispose() {
		for (HUDModule module : this.modules)
			module.dispose();
	}
}
