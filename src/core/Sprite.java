package core;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	private Image image;
	private double positionX;
	private double positionY;
	private double velocityX;
	private double velocityY;
	private double width;
	private double height;
	private double HEALTH = 5;
	private boolean isDead = false;
	double dropSpeed = 0;
	private boolean isJumping = false;
	private double TERMINAL_VELOCITY = 30;
	double gravity = 1;
	private boolean isFalling = false;
	public int jump = 0;
	boolean groundCollision = true;

	public Sprite() {
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}

	public void setImage(Image i) {
		image = i;
		width = i.getWidth();
		height = i.getHeight();
	}

	public void setImage(String filename) {
		Image i = new Image(filename);
		setImage(i);
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

	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
		if (!this.getBoundary().intersects(GameLauncher.ground) && !isJumping) {
			// if (jump < 2)
			setFalling(true);
			this.dropSpeed = this.dropSpeed + gravity;
			if (this.dropSpeed > TERMINAL_VELOCITY) {
				this.dropSpeed = TERMINAL_VELOCITY;
			}
			this.positionY += this.dropSpeed;
		} else {
			isFalling = false;
			if (this.positionY > GameLauncher.ground.getMinY()-40 && groundCollision) this.positionY = GameLauncher.ground.getMinY()-45;
		}
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
	}

	public void damage() {
		HEALTH--;
		if (HEALTH <= 0) {
			isDead = true;
			new Thread(new DeathTimer(this)).start();
		}
		System.out.println(HEALTH);
	}

	public boolean isDead() {
		return isDead;
	}

	public void revive() {
		isDead = false;
		HEALTH = 5;
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