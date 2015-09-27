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
import javafx.stage.Stage;

public class GameLauncher extends Application {
	public static final double HEIGHT = 720;
	public static final double WIDTH = 1366;
	public static GameLauncher game;
	Image space;
	Sprite player, bulletSp, enemy;
	public static Rectangle2D ground;
	long lastNanoTime;
	double speed = 300;

	ArrayList<Projectile> bulletList;
	public static Sound shoot = new Sound("/sounds/laser.wav");

	public static void main(String[] args) {
		launch(args);
	}

	public void init() {
		game = this;
		player = new Sprite();
		player.setImage(new Image("player.png"));
		enemy = new Sprite();
		enemy.setImage(new Image("enemy.png"));
		bulletSp = new Sprite();
		bulletSp.setImage(new Image("bullet.png"));
		space = new Image("map.png");
		player.setPosition(0, HEIGHT / 2);
		enemy.setPosition(WIDTH - 50, HEIGHT / 2);
		ground = new Rectangle2D(0, (HEIGHT / 3) + (HEIGHT / 2), WIDTH, 290);
		lastNanoTime = System.nanoTime();
		bulletList = new ArrayList<Projectile>();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("LOL");

		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
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

			@Override
			public void handle(long currentNanoTime) {
				double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				player.setVelocity(0, 0);
				if (input.contains("D"))
					player.addVelocity(speed, 0);
				if (input.contains("A"))
					player.addVelocity(-1 * speed, 0);
				if (input.contains("J")) {
					if (!shoot.isActive()) {
						shoot.play(1);
						Projectile bullet = new Projectile(player.getPositionX() + 50, player.getPositionY() + 20, gc,
								WIDTH);
						bullet.setImage(new Image("bullet.png"));
						bulletList.add(bullet);
						bullet.run();
					}

				}
				if (input.contains("K") && !player.isFalling()) {

					player.addVelocity(0, -1000);

					player.jump(true);

					System.out.println(player.jump);
				} else {
					player.jump(false);
				}

				player.update(elapsedTime);

				Iterator<Projectile> bulletIter = bulletList.iterator();
				while (bulletIter.hasNext()) {
					Sprite bullet = bulletIter.next();
					if ((enemy.intersects(bullet))) {
						bulletIter.remove();
						enemy.damage();
					} else if ((bullet.getPositionX() > WIDTH)) {
						bulletIter.remove();
					}
				}

				// render
				gc.drawImage(space, 0, 0);
				gc.fillRect(0, (HEIGHT / 3) + (HEIGHT / 2), WIDTH, 290);
				player.render(gc);

				enemy.render(gc);

				for (Projectile b : bulletList) {

					b.render(gc);

				}

			}

		}.start();

		stage.show();

	}

}