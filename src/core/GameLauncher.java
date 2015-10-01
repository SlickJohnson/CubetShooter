package core;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameLauncher extends Application {
	public static double screenHeight;
	public static double screenWidth;
	public static GameLauncher game;
	Image space, playerSp, enemySp;
	Sprite player, enemy;
	public static Rectangle2D ground;
	long lastNanoTime;
	double speed = 20;
	double velocity = 0;

	public static ArrayList<Projectile> bulletList;
	public static ArrayList<Sprite> spriteList;
	private boolean shot = true;
	public static Sound shoot = new Sound("/sounds/laser.wav");

	public static void main(String[] args) {
		launch(args);
	}

	public void init() {
		// init
		game = this;
		spriteList = new ArrayList<Sprite>();
		bulletList = new ArrayList<Projectile>();
		lastNanoTime = System.nanoTime();
		
		
		// environment
		space = new Image("map.png");

		// sprites
		playerSp = new Image("player.png");
		enemySp = new Image("enemy.png");

		// entities
		player = new Sprite(playerSp);
		enemy = new Sprite(enemySp);

	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("LOL");
		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setMaximized(true);

		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		System.out.println(screenHeight);
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

		Canvas canvas = new Canvas(screenWidth, screenHeight);
		root.getChildren().add(canvas);

		ground = new Rectangle2D(0, (screenHeight / 3) + (screenHeight / 2), screenWidth, 290);
		player.setPosition(0, screenHeight / 2);
		enemy.setPosition(screenWidth - 50, screenHeight / 2);

		ArrayList<String> input = new ArrayList<String>();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();

				// only add once... prevent duplicates
				if (!input.contains(code))
					input.add(code);
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				input.remove(code);
			}
		});

		GraphicsContext gc = canvas.getGraphicsContext2D();

		new AnimationTimer() {

			private int dir;
			private double xVel;

			@Override
			public void handle(long currentNanoTime) {
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				player.setVelocity(0, 0);
				if (input.contains("D") || input.contains("A")) {
					if (input.contains("D")) {
						if (dir == 1 || velocity <= speed) {
							velocity += speed;
							dir = 1;
						} else {
							velocity -= (int) velocity / 10;
						}
					}

					if (input.contains("A")) {
						if (dir == -1 || velocity <= speed) {
							velocity += speed;
							dir = -1;
						} else {
							velocity -= (int) velocity / 10;
						}
					}

					System.out.println(velocity);
				} else {
					if (velocity != 0)
						velocity -= 5;
					if (velocity < 0)
						velocity = 0;
				}
				if (input.contains("K")) {
					if (!shot) {

						shoot.play(1);

						if (dir == 1)
							velocity -= 300;
						else
							velocity += 60;

						player.addVelocity(-500, 0);
						Projectile bullet = new Projectile(new Image("bullet.png"), player.getPositionX() + 50,
								player.getPositionY() + 20, gc, screenWidth);
						bullet.run();
						shot = true;
					}
				} else
					shot = false;

				if (input.contains("J") && !player.isJumping() && !player.isFalling()) {

					xVel = -2000;

					player.jump(true);

					System.out.println(player.jump);
				} else {
					player.jump(false);
					if (player.getBoundary().intersects(ground))
						xVel = 0;
				}
				// System.out.println(xVel);
				player.addVelocity(dir * velocity, xVel);
				player.update(elapsedTime);

				Iterator<Projectile> bulletIter = bulletList.iterator();
				while (bulletIter.hasNext()) {
					Sprite bullet = bulletIter.next();
					if ((enemy.intersects(bullet))) {
						bulletIter.remove();
						enemy.damage();
					} else if ((bullet.getPositionX() > screenWidth)) {
						bulletIter.remove();
					}
				}

				// render
				gc.drawImage(space, 0, 0);
				gc.fillRect(0, (screenHeight / 3) + (screenHeight / 2), screenWidth, 290);
				for (Sprite s : spriteList)
					s.render(gc);
				for (Projectile b : bulletList)
					b.render(gc);
			}
		}.start();
		stage.show();
	}

}