package core;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Projectile extends Sprite implements Runnable {

	GameLauncher game;
	private long lastNanoTime = System.nanoTime();
	Random random = new Random();
	int spread;
	public double maxRange;
	public double damage;
	public double x, y;

	public Projectile(String img, double startX, double startY, double maxRange, double damage) {
		super(img);
		startPosition(startX, startY);
		this.damage = damage;
		this.maxRange = maxRange;
		spread = random.nextInt(16);
		if (spread < 5) {
			spread = -1;
		} else if (spread > 5 && spread < 10) {
			spread = 1;
		} else {
			spread = 0;
		}
		GameLauncher.bulletList.add(this);
	}

	private void startPosition(double startX, double startY) {
		x = startX;
		y = startY;

	}

	@Override
	public void run() {
		new AnimationTimer() {

			@Override
			public void handle(long currentNanoTime) {
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;
				
				addVelocity(3000/damage, elapsedTime);
				y += (10 * (float) Math.sin(Math.toRadians(spread * (random.nextInt((int) (30/damage))))));
				// update(elapsedTime);
				if (x >= maxRange) {
					stop();
				}
			}

		}.start();
	}

	protected void addVelocity(double x, double t) {
		this.x += x * t;
	}

	public Rectangle2D getHitBox() {
		return new Rectangle2D(x, y, width, height);

	}

	public void render(GraphicsContext gc) {
		gc.drawImage(image, x, y);

	}

}