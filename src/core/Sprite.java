package core;

import javafx.scene.image.Image;

public class Sprite {
	public Image image;
	public double width;
	public double height;

	public Sprite(String imgName) {
		setImage(imgName);
	}

	public Sprite(Image img) {
		setImage(img);
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

	public Image getImage() {
		return image;
	}
}