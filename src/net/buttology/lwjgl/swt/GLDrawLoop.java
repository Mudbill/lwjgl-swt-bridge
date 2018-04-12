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

/**
 * This abstract class features three methods to override, <code>init()</code>, <code>update()</code> and <code>shutdown()</code>, where you can specify your OpenGL rendering calls.<br>
 * It also keeps track of user input from the keyboard and mouse, which you can access with <code>getInputHandler()</code>. 
 * @author Mudbill
 *
 */
public abstract class GLDrawLoop {

	private InputHandler im = new InputHandler();
	
	/**
	 * Returns the <code>InputHandler</code> associated with the {@link GLComposite} this class is linked to.
	 * @return
	 */
	protected InputHandler getInputHandler() {
		return im;
	}
	
	/**
	 * Initialize components required to start looping through the update function.
	 */
	protected abstract void init();
	
	/**
	 * Update the OpenGL context and draw everything to the framebuffer once.
	 */
	protected abstract void update();
	
	/**
	 * Destroy all created OpenGL objects and clear the memory.
	 */
	protected abstract void shutdown();
	
}
