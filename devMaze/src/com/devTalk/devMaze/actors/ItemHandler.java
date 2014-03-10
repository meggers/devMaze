package com.devTalk.devMaze.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.devTalk.devMaze.maze.DevMaze;
import com.devTalk.devMaze.maze.Maze;
import com.devTalk.devMaze.maze.Tile;
import com.devTalk.devMaze.actors.Syringe;

public class ItemHandler {

	private DevMaze game;
	private Maze maze;
	private Player player;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	public List<Item> items;

	public ItemHandler(DevMaze g) {
		game = g;
		maze = g.maze;
		batch = g.batch;
		player = g.player;
		camera = g.camera;
		
		items = new ArrayList<Item>();
	}

	public void dispose() {
		for (Item item : items)
			item.dispose();
	}

	public void render() {
		batch.begin();
		for (Item item : items) 
		{
			Vector3 center = new Vector3(item.mapRectangle().x, item.mapRectangle().y, 0);
			if (camera.frustum.sphereInFrustum(center, item.mapRectangle().getWidth()))
				batch.draw(item.mapTexture(), item.mapRectangle().x, item.mapRectangle().y);
		}
		batch.end();
	}

	public void runOverItem(Rectangle playerArea) {
		List<Item> usedItems = new ArrayList<Item>();
		for (Item item : this.items) 
		{
			if (playerArea.overlaps(item.mapRectangle())) {
				player.pack.add(item);
				usedItems.add(item);
			}
		}
		items.removeAll(usedItems);
	}

	public void set(int numItems) {
		items.clear();
		Random r = new Random();
		for(int i = 0; i < numItems; i++){
			Tile openTile = maze.openTiles.get(r.nextInt(maze.openTiles.size()));
			items.add(new Syringe(openTile.center.x, openTile.center.y,  game));
		}
	}
	
	public Item chooseItem(int item_id) {
		return null;
	}

	public void updateItems() {
		runOverItem(player.rectangle);
	}
}
