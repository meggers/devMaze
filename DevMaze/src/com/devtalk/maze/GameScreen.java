package com.devtalk.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
	final DevMaze game;

	public static final int EDGE_SIZE_PX = 64;
	public static final int PLAYER_SIZE_PX = 32;
	public static final int KEY_VEL_PxPer60S = 5;

	OrthographicCamera camera;
	Maze maze;
	MazeInputProcessor inputProcessor;
	Player player;
	float a, b;
	int x, y;

	public GameScreen(final DevMaze g) {
		
		// Create game
		this.game = g;
		maze = new Maze(53, 31); // must be prime

		// Create Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// Create player 		
		// Find an open tile (currently default to 0, 1)
		player = new Player(2, EDGE_SIZE_PX + 2, maze);

		// Set our input processor
		Gdx.input.setInputProcessor(new MazeInputProcessor(player));
		
	}

	// The main loop, fires @ 60 fps
	// LibGDX combines the main and user input threads
	public void render(float delta) {
		
		// Set the camera on the player's current position
		player.updatePos();
		camera.position.set(player.position);

		// Clear the screen to deep blue and update the camera
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();

		// Tell batch to use the same coordinates as the camera
		game.batch.setProjectionMatrix(camera.combined);
		
		// Draw everything
		game.batch.begin();
		{
			
			// **DRAW MAZE** //
			for (int i = 0; i < maze.tiles.length; i++)
				for (int j = 0; j < maze.tiles[0].length; j++) {
					a = maze.tiles[i][j].rectangle().x;
					b = maze.tiles[i][j].rectangle().y;
					Vector3 tile = new Vector3(a, b, 0);
		
					if (camera.frustum.sphereInFrustum(tile, EDGE_SIZE_PX))
						game.batch.draw(maze.tiles[i][j].texture(), 
								j * EDGE_SIZE_PX, i * EDGE_SIZE_PX);
				}
		
			// **DRAW PLAYER** //
			TextureRegion tmp = player.texture(Gdx.graphics.getDeltaTime());
			game.batch.draw(tmp, camera.position.x, camera.position.y,
					(tmp.getRegionWidth() / 2), (tmp.getRegionHeight() / 2),
					tmp.getRegionWidth(), tmp.getRegionHeight(), 1, 1,
					player.angle());
		
			// **DRAW ITEMS** //
			
			// **DRAW MONSTERS** //
			
		}
		game.batch.end();

		boolean space = Gdx.input.isKeyPressed(Keys.SPACE);
		if (Gdx.input.justTouched()) {
			x = Gdx.input.getX();
			y = Gdx.input.getY();

			if ((x < 50 && y < 50) || space) {
				game.setScreen(new PauseScreen(game, this));
				this.dispose();
			}
		}
	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	// Save user app information
	public void pause() {

	}

	// Return from pause
	public void resume() {

	}

	// Kills the app. Calls a pause first
	public void dispose() {

	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}
}
