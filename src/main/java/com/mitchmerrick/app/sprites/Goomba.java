package com.mitchmerrick.app.sprites;

import com.mitchmerrick.app.model.Model;
import com.mitchmerrick.app.view.View;
import com.mitchmerrick.app.Json;

import java.awt.Graphics;
import java.util.Iterator;

public class Goomba extends Sprite {
	int goombaFrame;
	int delay;
	int groundLevel;
	boolean killed;

	Model model;

	public Goomba(Model m, int xx, int yy) {
	    model = m;
	    x = xx;
	    y = yy;
	    w = 50;
	    h = 50;
	    vert_vel = 0;
	    groundLevel = 585;
	    goombaFrame = 0;
	    delay = 0; // delay in changing pictures 
	    direction = Sprite.Directions.RIGHT; // 1 = right; 0 = left; 2 = squashed
	    killed = false;
	}
	
	boolean isGoomba() 	{ return true; }

	public void draw(Graphics g) {
		// Draw current goomba
		g.drawImage(View.goombaImages[goombaFrame], x - model.scrollPos, y, w, h, null);
	}

	public void update() {
		prevLocation();
		
		// Gravity
		vert_vel += 5;
		y += vert_vel;
		
		if(!killed) {
			// Floor
			if(y >= groundLevel) {
				vert_vel = 0.0;
				y = groundLevel; // Send back to ground
			}

			// Movement and Picture change
			Move();
			
			// Is goomba colliding
			for(int i = 0; i < model.sprites.size(); i++) {
				Sprite s = model.sprites.get(i);
				if((s.isBrick() || s.isCoinBlock() || s.isFlagPole() || s.isTurtle()) && doesCollide(0, this, s))
					barrier(0, 0, this, s);
				else if (s.isMario() && doesCollide(model.scrollPos, s, this))
					barrier(model.scrollPos, model.prev_scrollPos, this, s);
			}
		}
		else {
			// Remove goomba from list after it falls off screen
			Iterator<Sprite> it = model.sprites.iterator();
			while(it.hasNext()) {
				Sprite s = it.next();
				if(s.isGoomba() && s.y > 700)
					it.remove();
			}
		}
	}

	void Move()
	{
		// Move left or right 
		if(direction == Sprite.Directions.RIGHT)
			x+=4;
		else
			x-=4;
		
		// Change picture
		delay+=3;
		if(delay % 2 == 0){
			if(goombaFrame == 0)
				goombaFrame++;
			else if(goombaFrame == 1)
				goombaFrame--;
		}
	}
	
	void prevLocation() {
		prev_x = x;
		prev_y = y;
	}

	public void squish(Mario mario) {
		this.goombaFrame = 2;
		this.groundLevel = 605;
		this.h = 30;
		// Jump after kill
		if(!(this.killed)) {
			mario.vert_vel-=30;
			Mario.bump.play();
		}
		this.killed = true;
	}
	
	public Goomba(Json ob, Model m) {
		model = m;
		// Change values back to ints
		x = (int)ob.getLong("x");
		y = (int)ob.getLong("y");
		w = (int)ob.getLong("w");
		h = (int)ob.getLong("h");
		vert_vel = (int)ob.getDouble("vert_vel");
		goombaFrame = (int)ob.getLong("goombaFrame");
		direction = Sprite.Directions.RIGHT;
		killed = false;
		groundLevel = 585;
	}

	public Json marshall() {
		// Put values into Jason object
		Json ob = Json.newObject();
		ob.add("x", x);
		ob.add("y", y);
		ob.add("w", w);
		ob.add("h", h);
		ob.add("vert_vel", vert_vel);
		ob.add("goombaFrame", goombaFrame);
		ob.add("type", "src.main.java.sprites.Goomba");
		return ob;
	}
}
