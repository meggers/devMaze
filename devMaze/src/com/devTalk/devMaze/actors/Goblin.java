package com.devTalk.devMaze.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.devTalk.devMaze.maze.DevMaze;
import com.devTalk.devMaze.maze.Maze;
import com.devTalk.devMaze.maze.Tile;

public class Goblin implements Monster {

	private static final int FRAME_COLS = 8;
	private static final int FRAME_ROWS = 8;

	private DevMaze game;
	private Player player;
	private Maze maze;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	float stateTime;
	Animation[] walkAnimation = new Animation[8];
	static Texture walkSheet = new Texture(
			Gdx.files.internal("goblin-walking.png"));
	TextureRegion[][] walkFrames = new TextureRegion[8][8];
	Rectangle rectangle;

	float prevAngle;
	int velocityScale;
	Vector2 position;
	Vector2 velocity;
	Vector2 velocityLatch;
	Vector2 prevPosition;

	int currentHealth;
	int totalHealth;
	int hitRadius;
	int hitDamage;
	int attackFrequency;
	boolean sawPlayer;

	MonsterState state;
	List<Tile> path;
	Tile destination;
	int count;

	public Goblin(float xPos, float yPos, MonsterType type, DevMaze g) {
		this.game = g;
		this.player = g.player;
		this.maze = g.maze;
		this.camera = g.camera;
		this.batch = g.batch;

		this.state = MonsterState.AT_DESTINATION;
		this.path = new ArrayList<Tile>();
		this.count = 0;

		this.rectangle = new Rectangle(xPos, yPos, DevMaze.MONSTER_SIZE_PX,
				DevMaze.MONSTER_SIZE_PX);

		for (int i = 0; i < walkAnimation.length; i++) {
			TextureRegion[][] tmp = TextureRegion.split(walkSheet,
					walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight()
							/ FRAME_ROWS);

			for (int j = 0; j < FRAME_COLS; j++)
				this.walkFrames[i][j] = tmp[i][j];

			this.walkAnimation[i] = new Animation(0.025f, walkFrames[i]);
		}

		this.stateTime = 0.0f;
		this.position = new Vector2(xPos, yPos);
		this.prevPosition = position.cpy();
		this.velocity = new Vector2();
		this.velocityLatch = new Vector2();

		switch (type) {
		case EASY:
			this.hitRadius = DevMaze.MONSTER_SIZE_PX / 8;
			this.totalHealth = 5;
			this.hitDamage = 1;
			this.attackFrequency = 65;
			this.velocityScale = 2;
			break;
		case MEDIUM:
			this.hitRadius = DevMaze.MONSTER_SIZE_PX / 6;
			this.totalHealth = 10;
			this.hitDamage = 2;
			this.attackFrequency = 55;
			this.velocityScale = 3;
			break;
		case HARD:
			this.hitRadius = DevMaze.MONSTER_SIZE_PX / 4;
			this.totalHealth = 15;
			this.hitDamage = 4;
			this.attackFrequency = 45;
			this.velocityScale = 4;
			break;
		}

		// Start with full health
		this.currentHealth = this.totalHealth;
		this.sawPlayer = false;
	}

	public float angle() {
		return 0;
	}

	// This mapping relates to the spritesheet's layout
	public int dirIndex() {
		int angle = (int) myAngle();

		switch (angle) {
		case 0:
			return 2;
		case 45:
			return 1;
		case 90:
			return 0;
		case 135:
			return 7;
		case 180:
			return 6;
		case -45:
			return 3;
		case -90:
			return 4;
		case -135:
			return 5;
		case -180:
			return 6;
		default:
			return 6;
		}
	}

	public void dispose() {
		walkSheet.dispose();
	}

	public int getAttackFrequency() {
		return attackFrequency;
	}

	public int getCount() {
		return count;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public Tile getDestination() {
		return destination;
	}

	public int getHitDamage() {
		return hitDamage;
	}

	public Rectangle getHitRectangle() {
		return new Rectangle(this.position.x - hitRadius, this.position.y
				- hitRadius, this.rectangle.width + (2 * hitRadius),
				this.rectangle.height + (2 * hitRadius));
	}

	public List<Tile> getPath() {
		return path;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getPrevPosition() {
		return prevPosition;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public MonsterState getState() {
		return state;
	}

	public int getTotalHealth() {
		return totalHealth;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getVelocityLatch() {
		return velocityLatch;
	}

	public int getVelocityScale() {
		return velocityScale;
	}

	public boolean isAlive() {
		return this.currentHealth > 0;
	}

	public boolean isMoving() {
		return !(this.velocity.x == 0 && this.velocity.y == 0);
	}

	private float myAngle() {
		if (!isMoving())
			return prevAngle;

		return prevAngle = (float) (Math.toDegrees(Math.atan2(-velocity.x,
				velocity.y)));
	}

	public boolean sawPlayer() {
		return sawPlayer;
	}

	public void setCount(int count) {
		this.count = count;

	}

	public void setCurrentHealth(int health) {
		this.currentHealth = health;
	}

	public void setDestination(Tile destination) {
		this.destination = destination;
	}

	public void setPath(List<Tile> path) {
		this.path = path;
	}

	public void setState(MonsterState state) {
		this.state = state;
	}

	public TextureRegion texture(float stateTime) {
		this.stateTime += stateTime;

		if (isMoving() && !game.pause)
			return this.walkAnimation[dirIndex()].getKeyFrame(this.stateTime,
					true);
		else
			return this.walkFrames[dirIndex()][0];
	}

	public String toString() {
		return ": " + this.position + " v: " + this.velocity + "\n" + "lV: "
				+ this.velocityLatch;
	}

	public void updatePos() {
		this.prevPosition = this.position.cpy();
		this.position.add(this.velocity);
		this.rectangle.set(this.position.x, this.position.y,
				DevMaze.MONSTER_SIZE_PX, DevMaze.MONSTER_SIZE_PX);

		float xPos = this.position.x;
		float yPos = this.position.y;

		// TODO this can be more efficient by checking by tile
		if (!this.sawPlayer)
			if (this.velocity.x != 0 || this.velocity.y != 0)
				while (maze.tileAtLocation(xPos, yPos) != null
						&& maze.tileAtLocation(xPos, yPos).inMaze) {
					if (player.rectangle.contains(xPos, yPos)) {
						this.sawPlayer = true;
						break;
					}

					xPos += (velocity.x * (DevMaze.MONSTER_SIZE_PX / 2));
					xPos += (velocity.y * (DevMaze.MONSTER_SIZE_PX / 2));
				}
	}

	public void attack() {
		// TODO Auto-generated method stub
		
	}

	public void render() {
		Vector3 pos = new Vector3(this.getPosition().x, this.getPosition().y, 0);
		if (camera.frustum.sphereInFrustum(pos, this.getRectangle().getWidth())) {
			TextureRegion tmp = this.texture(Gdx.graphics.getDeltaTime());
			batch.draw(tmp, this.getPosition().x, this.getPosition().y,
					(tmp.getRegionWidth() / 2), (tmp.getRegionHeight() / 2),
					tmp.getRegionWidth(), tmp.getRegionHeight(), 1, 1,
					this.angle());

			if (DevMaze.DEBUG)
				game.font.draw(batch, "HP: " + this.getCurrentHealth() + "/"
						+ this.getTotalHealth(), this.getPosition().x,
						this.getPosition().y);
		}
	}

	public boolean attacking() {
		// TODO Auto-generated method stub
		return false;
	}

}