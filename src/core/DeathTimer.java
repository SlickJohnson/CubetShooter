package core;

public class DeathTimer implements Runnable {

	Sprite ghost;
	long timer = 2000;

	public DeathTimer(Sprite sprite) {
		ghost = sprite;
	}

	@Override
	public void run() {
		System.out.println("enemy is dead");
		ghost.setPosition(-1023012030, -12391293);
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		ghost.setPosition(GameLauncher.screenWidth - 50, GameLauncher.screenHeight / 2);
		ghost.revive();
		System.out.println("enemy is back " + ghost.isDead());

	}

}
