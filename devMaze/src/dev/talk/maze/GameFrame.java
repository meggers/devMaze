package dev.talk.maze;

import com.badlogic.gdx.ApplicationListener;

public class GameFrame implements ApplicationListener {
	/* This is a gaming outline that covers the six major notifications that
	 * Android sends an application. We can pretty much ignore Pause, Resume,
	 * and Resize until we target Android. 
	 */
	
	// Runs when the application is first instantiated
	public void create() {
	/* Load the Images and the sounds
	 * Create the Camera object and position it at the center of the screen 
	 * Create the SpriteBatch object
	 * Set the screen to the MenuScreen
	 */
		
	}
	
	// The main loop, fires up to 60 fps 
	// LibGDX combines the main and user input threads
	public void render() {
		
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
}
