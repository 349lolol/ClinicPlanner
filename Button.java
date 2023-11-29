import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Button {
	private static final Color BOX_SHADOW = new Color(0, 0, 0, 128);
	private static final int CORNER_ROUNDING = 10;
	private static final Font font = new Font("Arial", Font.BOLD, 12);
	
	private final Rectangle bounds;
	private final String text;
	private boolean isPressed;

	public Button(Rectangle bounds, String text) {
		this.bounds = bounds;
		this.text = text;
		this.isPressed = false;
	}

	public void draw(Graphics g) {
		g.setFont(font);

		((Graphics2D) g).setStroke(new BasicStroke(2));

		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);

		int fontCornerX = (int) this.bounds.getCenterX() - (metrics.stringWidth(this.text) / 2);
		int fontCornerY = (int) this.bounds.getCenterY() - (metrics.getHeight() / 2) + metrics.getAscent();

		// Draw box shadow
		g.setColor(BOX_SHADOW);
		g.fillRoundRect(
			(int) this.bounds.getLocation().getX() + 2,
			(int) this.bounds.getLocation().getY() + 2,
			(int) this.bounds.getWidth(),
			(int) this.bounds.getHeight(),
			CORNER_ROUNDING,
			CORNER_ROUNDING
		);

		int offset = 0;

		if (this.isPressed) {
			offset = 2;
		}

		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(
			(int) this.bounds.getLocation().getX() + offset,
			(int) this.bounds.getLocation().getY() + offset,
			(int) this.bounds.getWidth(),
			(int) this.bounds.getHeight(),
			CORNER_ROUNDING,
			CORNER_ROUNDING
		);
			
		g.setColor(Color.BLACK);
		g.drawString(this.text, fontCornerX, fontCornerY);
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}
}
