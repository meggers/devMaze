package com.devtalk.maze;

public interface HUDModule {

	public boolean actionedAt(int x, int y);

	public void dispose();

	public void render();

	public void stopAction(int x, int y);

}
