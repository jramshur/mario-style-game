package com.mitchmerrick.app.sprites;
import com.mitchmerrick.app.Json;
import java.awt.*;

public abstract class Sprite {
	int x;
	int y;
	int w;
	int h;
	int prev_x;
	int prev_y;
	double vert_vel;
	Directions direction;

	public abstract void update();
	public abstract void draw(Graphics g);
	public abstract Json marshall();

	public void squish(Mario mario) {}

	boolean isMario() 		{ return false; }
	boolean isTurtle() 		{ return false; }
	boolean isGoomba() 		{ return false; }
	boolean isBrick() 		{ return false; }
	boolean isCoinBlock() 	{ return false; }
	boolean isCoin() 		{ return false; }
	boolean isFlagPole() 	{ return false; }

	public enum Directions {
		LEFT,
		RIGHT,
		STOPPED
	}
	
	// Collision detection for sprites
	boolean doesCollide(int scrollPos, Sprite a, Sprite b) {
		// Is sprite A colliding with sprite B?
		// From left
		if(a.x + a.w  + scrollPos < b.x)
			return false;
		// From right
		else if(a.x + scrollPos > b.x + b.w)
			return false;
		// From top
		else if(a.y + a.h < b.y)
			return false;
		// From bottom
		else if(a.y > b.y + b.h)
			return false;
		else
			return true;
	}

	void barrier(int scrollPos, int prevScrollPos, Sprite a, Sprite s) {
		// From left
		if(a.x + a.w >= s.x  + scrollPos && a.prev_x + a.w < s.x + prevScrollPos)  {
			direction = Directions.LEFT;
		}
		// From right
		else if(a.x <= s.x + s.w + scrollPos && a.prev_x > s.x + s.w + prevScrollPos) {
			direction = Directions.RIGHT;
		}
		// From top
		else if(a.y + a.h >= s.y && a.prev_y + a.h < s.y) {
			a.y = s.y - a.h - 1;
			a.vert_vel = 0;
		}
		// From bottom
		else if(a.y <= s.y + s.h && a.prev_y > s.y + s.h) {
			y = s.y + s.h + 1;
			a.vert_vel = 0;
		}
	}

	public void beKicked(Sprite.Directions d) {
		this.direction = d;
	}
}
