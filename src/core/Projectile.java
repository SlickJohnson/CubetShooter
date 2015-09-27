package core;

import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Projectile extends Sprite implements Runnable {

	GraphicsContext gc;
	private long lastNanoTime = System.nanoTime();
	Random random = new Random();
	int spread;
	double maxRange;

	public Projectile(double startX, double startY, GraphicsContext gc, double maxRange) {
		setPosition(startX, startY);
		this.gc = gc;
		this.maxRange = maxRange;
		spread = random.nextInt(16);
		gravity = 0.25;
		groundCollision = false;
		if (spread < 5) {
			spread = -1;
		} else if (spread > 5 && spread < 10) {
			spread = 1;
		} else {
			spread = 0;
		}

	}

	@Override
	public void run() {
		new AnimationTimer() {

			@Override
			public void handle(long currentNanoTime) {
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				addVelocity(50, (10 * (float) Math.sin(Math.toRadians(spread * (random.nextInt(30))))));
				update(elapsedTime);
				if (getPositionX() >= maxRange) {
					stop();
				}

			}

		}.start();
	}

}