package com.turkishdelight.taxe.guiobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.ComponentBatch;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Pane extends Clickable {
	float previousX = 0;
	float previousY = 0;
	
	public Pane(Scene parentScene, Texture t, int z) {
		super(parentScene, t, z);
		components = new ComponentBatch(){
			@Override
			public void Update()
			{
				super.Update();
				if(getX() != previousX || getY() != previousY)
				{
					previousX = getX();
					previousY = getY();
					for(SpriteComponent c : this.spriteComponents)
					{
						c.setPosition(c.getLocalX() + getX(), c.getLocalY() + getY());
					}
				}
			}
		};
	}


	//This stores the active sprite components of the scene
	ComponentBatch components;
	ArrayList<Clickable> clickAbleObjects = new ArrayList<Clickable>();
	
	//This method is used to get the component batch
	public ComponentBatch getComponents()
	{
		return components;
	}
	
	//This method is used to set the component batch
	public void setComponents(ComponentBatch newComponents)
	{
		this.components = newComponents;
	}
	
	//This method draws the scene
	@Override
	public void draw(Batch batch)
	{
		components.Draw((SpriteBatch)batch);
	}
	
	//This method is used to add a sprite component to the game
	public void Add(SpriteComponent spriteComp)
	{
		System.out.println("Pane item added: " + spriteComp.getClass().getSimpleName());
		components.Add(spriteComp);
		if(spriteComp.isClickAble())
		{
			registerClickAble(spriteComp);
		}
	}
	
	//This method is used to remove a sprite component from the game
	public void Remove(SpriteComponent spriteComp)
	{
		components.Remove(spriteComp);
		//If the sprite is clickable, we acknowledge and remove it
		if(spriteComp.isClickAble())
		{
			removeClickAble(spriteComp);
		}
	}
	
	//This method is used to store a new clickable sprite
	public void registerClickAble(SpriteComponent spriteComp)
	{
		if(!clickAbleObjects.contains(spriteComp))
		{
			clickAbleObjects.add((Clickable)spriteComp);
		}
	}
	
	public void removeClickAble(SpriteComponent spriteComp)
	{
		if(clickAbleObjects.contains(spriteComp))
		{
			clickAbleObjects.remove((Clickable)spriteComp);
		}
	}
	
	@Override
	//This method is called every frame. It can be overridden with game logic
	public void update()
	{
		components.Update();
	}
	
	//This method is called if a sprite wishes to force a reorder (e.g. it's z order has changed) in the ComponentBatch
	public void postSpriteReorder()
	{
		components.Reorder();
	}
	
	@Override
	//This method is called when the mouse is clicked up. The location of the mouse click is sent to the method as parameters
	public boolean clickEnd(int posX, int posY)
	{
		boolean click = false;
		for(Clickable item : clickAbleObjects)
		{
			if(item.clickEnd(posX, posY))
			{
				click = true;
			}
		}
		return click;
	}
	
	@Override
	//This method is called when the mouse is clicked down. The location of the mouse click is sent to the method as parameters
	public boolean clickStart(int posX, int posY)
	{
		boolean click = false;
		for(Clickable item : clickAbleObjects)
		{
			if(item.clickStart(posX, posY))
			{
				click = true;
			}
		}
		return click;
	}
	
}
