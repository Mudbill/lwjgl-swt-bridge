/*
 * Copyright (C) 2018  Magnus Bull
 *
 *  This file is part of lwjgl-swt-bridge.
 *
 *  lwjgl-swt-bridge is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  lwjgl-swt-bridge is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with dds-lwjgl.  If not, see <https://www.gnu.org/licenses/>. 
 */

package net.buttology.lwjgl.swt;

import java.util.HashMap;
import java.util.Map;

/**
 * This simple class contains several instances of {@link Map} which can be polled to check the state of keys. 
 * The Maps are filled with states dependent on the SWT listeners for the canvas within {@link GLComposite}.
 * @author Mudbill
 *
 */
public class InputHandler {
	
	//==============================================================================
	// Private fields
	//==============================================================================
	
	/** Map used to track state of keyboard keys, using the <code>char</code> representation of it. */
	private Map<Character, Boolean> keyMap = new HashMap<Character, Boolean>();
	
	/** Map used to track state of keyboard keys, using the <code>keyCode</code> representation of it. */
	private Map<Integer, Boolean> keyMapI = new HashMap<Integer, Boolean>();
	
	/** Map used to track state of special keyboard keys which SWT uses integers for, such as modifier keys. */
	private Map<Integer, Boolean> maskMap = new HashMap<Integer, Boolean>();
	
	/** Map used to track state of mouse buttons. */
	private Map<Integer, Boolean> mouseKeyMap = new HashMap<Integer, Boolean>();
	
	/** The X position of the mouse within the widget. */
	private int mouseX;
	
	/** The Y position of the mouse within the widget. */
	private int mouseY;
	
	/** The change in the amount of mouse wheel scrolling since last poll. */
	private int scrollDelta;
	
	//==============================================================================
	// Public methods
	//==============================================================================
	
	/**
	 * Get the current state of a keyboard key based on the <code>char</code> representation of it.<br><br>
	 * NOTE: There is a bug in the SWT KeyReleased event which fails to trigger when multiple keys are held down at the same time. 
	 * Because of this, this method may incorrectly return <code>true</code> even when a key is no longer held down, if it was held simultaneously as another key. This makes this function unreliable at times.
	 * I hope to find an alternative solution to this.
	 * @param key
	 * @return
	 */
	public boolean getKeyPressed(char key) {
		if(keyMap.containsKey(key))	return keyMap.get(key);
		return false;
	}
	
	/**
	 * Get the current state of a keyboard key based on the <code>keyCode</code> representation of it.<br><br>
	 * NOTE: There is a bug in the SWT KeyReleased event which fails to trigger when multiple keys are held down at the same time. 
	 * Because of this, this method may incorrectly return <code>true</code> even when a key is no longer held down, if it was held simultaneously as another key. This makes this function unreliable at times.
	 * I hope to find an alternative solution to this.
	 * @param key
	 * @return
	 */
	public boolean getKeyPressed(int key) {
		if(keyMapI.containsKey(key)) return keyMapI.get(key);
		return false;
	}
	
	/**
	 * Get the current state of a special keyboard key, such as modifier keys, based on the <code>keyCode</code> representation of it.<br><br>
	 * NOTE: There is a bug in the SWT KeyReleased event which fails to trigger when multiple keys are held down at the same time. 
	 * Because of this, this method may incorrectly return <code>true</code> even when a key is no longer held down, if it was held simultaneously as another key. This makes this function unreliable at times.
	 * I hope to find an alternative solution to this.
	 * @param key
	 * @return
	 */
	public boolean getMaskPressed(int key) {
		if(maskMap.containsKey(key)) return maskMap.get(key);
		return false;
	}
	
	/**
	 * Get the current state of a mouse button. Starts at 1 for M1.
	 * @param key
	 * @return
	 */
	public boolean getMouseButtonPressed(int button) {
		if(mouseKeyMap.containsKey(button)) return mouseKeyMap.get(button);
		return false;
	}
	
	/**
	 * Returns the last known X position of the mouse within the canvas.
	 * @return
	 */
	public int getMousePosX() {
		return mouseX;
	}
	
	/**
	 * Returns the last known Y position of the mouse within the canvas.
	 * @return
	 */
	public int getMousePosY() {
		return mouseY;
	}
	
	/**
	 * Returns the amount the scroll wheel changed since last poll (frame).
	 * @return
	 */
	public int getScrollAmount() {
		return scrollDelta;
	}

	//==============================================================================
	// Package private methods
	//==============================================================================
	
	/**
	 * Creates a new instance with all associated maps.
	 */
	InputHandler() {}
	
	/** Set the pressed state for this char-based key */
	void setKeyPressed(char key, boolean pressed) {
		keyMap.put(key, pressed);
	}
	
	/** Set the pressed state for this integer-based key */
	void setKeyPressed(int key, boolean pressed) {
		keyMapI.put(key, pressed);
	}
	
	/** Set the pressed state for this mask/modifier key */
	void setMaskPressed(int key, boolean pressed) {
		maskMap.put(key, pressed);
	}
	
	/** Set the pressed state for this mouse button */
	void setMouseButtonPressed(int button, boolean pressed) {
		mouseKeyMap.put(button, pressed);
	}
	
	/** Update the mouse position */
	void updateMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
	}
	
	/** Set the amount scrolled with the mouse wheel */
	void setScrollDelta(int delta) {
		scrollDelta = delta;
	}
}
