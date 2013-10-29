package com.devtalk.maze;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Monster {
	
	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 4;

	float stateTime;
	Animation walkAnimation;
	Texture walkSheet = new Texture(Gdx.files.internal("dude_sheet.png"));
	TextureRegion[] walkFrames;
	
	float prevAngle;
	Vector2 position;
	Vector2 velocity;
	Vector2 velocityLatch;
	Vector2 prevPosition;
	
	int currentHealth;
	int totalHealth;
	int hitRadius;
	int hitDamage;
	
	State walkState;
	List<Tile> path;
	Tile destination;
	int count;
	
	public static enum MonsterType {
		EASY,
		MEDIUM,
		HARD,
	};
	
	public static enum State {
		FOLLOWING_PLAYER,
		FINDING_DESTINATION,
		AT_DESTINATION,
		IN_COMBAT,
	};
	
	public Monster(float xPos, float yPos, MonsterType type) {
		this.walkState = State.AT_DESTINATION;
		this.path = new ArrayList<Tile>();
		this.count = 0;
		
		this.walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight()
						/ FRAME_ROWS);
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				this.walkFrames[index++] = tmp[i][j];
			}
		}

		this.stateTime = 0.0f;
		this.walkAnimation = new Animation(0.025f, walkFrames);

		this.position = new Vector2(xPos, yPos);
		this.prevPosition = position.cpy();
		this.velocity = new Vector2();
		this.velocityLatch = new Vector2();
		
		switch (type) {
		case EASY:
			this.hitRadius = GameScreen.PLAYER_SIZE_PX / 8;
			this.totalHealth = 5;
			this.hitDamage = 1;
			break;
		case MEDIUM:
			this.hitRadius = GameScreen.PLAYER_SIZE_PX / 6;
			this.totalHealth = 10;
			this.hitDamage = 2;
			break;
		case HARD:
			this.hitRadius = GameScreen.PLAYER_SIZE_PX / 4;
			this.totalHealth = 15;
			this.hitDamage = 4;
			break;
		}
		
		// Start with full health
		this.currentHealth = this.totalHealth;
	}
	
	public void updatePos() {
		this.prevPosition = this.position.cpy();
		this.position.add(this.velocity);
	}
	
	public boolean isAlive()
	{
		return this.currentHealth == 0;
	}
	
	public TextureRegion texture(float stateTime) {
		this.stateTime += stateTime;

		if (isMoving())
			return this.walkAnimation.getKeyFrame(this.stateTime, true);
		else
			return this.walkFrames[4];
	}
	
	public float angle() {

		if (!isMoving())
			return prevAngle;

		return prevAngle = (float) (Math.toDegrees(Math.atan2(
				-velocity.x, velocity.y)));
	}
	
	public boolean isMoving()
	{
		return !(this.velocity.x == 0 && this.velocity.y == 0);
	}
	
	public boolean seesPlayer()
	{
		return false;
	}
	
	public String toString() {
		return ": " + this.position + " v: " + this.velocity + "\n" + 
				"lV: " + this.velocityLatch;
	}
	
}
