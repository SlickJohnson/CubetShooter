package core;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class Mob extends Sprite {
	private double positionX;
	private double positionY;
	private double velocityX;
	private double velocityY;
	private static final double MAX_HEATLH = 30;
	private double health = MAX_HEATLH;
	public boolean isDead = false;
	double dropSpeed = 0;
	private boolean isJumping = false;
	private static final double TERMINAL_VELOCITY = 600;
	double gravity = 3;
	private boolean isFalling = false;
	public int jump = 0;
	boolean groundCollision = true;

	public Mob(Sprite sp, String name) {
		super(sp.getImage());
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
		GameLauncher.mobList.add(this);
		// TODO Auto-generated constructor stub
	}

	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}

	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;

	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
	}

	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), positionX, positionY);
		if (!this.getBoundary().intersects(GameLauncher.ground) && !isJumping) {
			setFalling(true);
			this.dropSpeed = this.dropSpeed + gravity;
			if (this.dropSpeed > TERMINAL_VELOCITY) {
				this.dropSpeed = TERMINAL_VELOCITY;
			}
			this.positionY += this.dropSpeed;
		} else {
			isFalling = false;
			if (this.positionY > GameLauncher.ground.getMinY() - 40 && groundCollision)
				this.positionY = GameLauncher.ground.getMinY() - 45;
		}
	}

	public void damage(double damage) {
		health -= damage;
		if (health <= 0) {
			isDead = true;
			new Thread(new DeathTimer(this)).start();
		}
		System.out.println(health);
	}

	public boolean isDead() {
		return isDead;
	}

	public void revive() {
		isDead = false;
		health = MAX_HEATLH;
		dropSpeed = 0;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void jump(boolean isJumping) {
		if (isJumping)
			dropSpeed = 0;
		this.isJumping = isJumping;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

}
