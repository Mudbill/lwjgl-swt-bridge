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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

/**
 * Instances of this class contain full-sized canvases for drawing OpenGL content to.
 * @author Mudbill
 * @version 1.0.0
 */
public class GLComposite extends Composite {
	
	//==============================================================================
	// Private fields
	//==============================================================================
	
	/** The canvas widget in SWT that the OpenGL calls are rendered to */
	private GLCanvas canvas;
	
	/** The OpenGL handler that is drawn in the canvas. */
	private GLDrawLoop glHandler;
	
	/** The input handler linked to this GLComposite. */
	private InputHandler hid;
	
	/** Fix for scroll wheel activity */
	private boolean scrollActive = false;

	/** The keyboard listener for capturing and registering keyboard events
	 * <br>NOTE: There is a bug in SWT KeyReleased event that can cause buttons to appear pressed even after they have been released! */
	private Listener kbListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if(event.type == SWT.KeyDown) {
				hid.setKeyPressed(event.character, true);
				hid.setKeyPressed(event.keyCode, true);
				hid.setMaskPressed(event.stateMask, true);
			}
			if(event.type == SWT.KeyUp) {
				hid.setKeyPressed(event.character, false);
				hid.setKeyPressed(event.keyCode, false);
				hid.setMaskPressed(event.stateMask, false);
			}
		}
	};
	
	/** The mouse listener for capturing and registering mouse button input events. */
	private Listener mouseListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			hid.updateMousePos(event.x, event.y);
			if(event.type == SWT.MouseDown)
				hid.setMouseButtonPressed(event.button, true);
			if(event.type == SWT.MouseUp)
				hid.setMouseButtonPressed(event.button, false);
		}
	};
	
	/** The mouse scroll wheel listener for capturing the scroll amount delta values. */
	private Listener mouseScrollListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if(event.type == SWT.MouseWheel) {
				hid.setScrollDelta(event.count);
				scrollActive = true;
			}
		}
	};
	
	/** The dispose listener for capturing when this <code>GLComposite</code> has been disposed, in order to trigger the <code>shutdown()</code> function for the OpenGL handler. */
	private Listener disposeListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			glHandler.shutdown();
		}
	};

	//==============================================================================
	// Public methods
	//==============================================================================
	
	/**
	 * Constructs a new instance of this class given its parent, a style value and the OpenGL handler describing its behavior, appearance and drawing calls.<br><br>
	 * The style value is either one of the style constants defined in class SWT which is applicable to instances of {@link Composite}, or must be built by bitwise OR'ing together (that is, using the int "|" operator) two or more of those SWT style constants. The class description of <link>Composite</link> lists the style constants that are applicable to the class. Style bits are also inherited from superclasses. 
	 * @see org.eclipse.swt.widgets.Composite
	 * @param parent - the parent of this widget (cannot be null).
	 * @param style - the styles applied to this widget.
	 * @param glHandler - the handler used to call OpenGL render calls.
	 */
	public GLComposite(Composite parent, int style, GLDrawLoop glHandler) {
		super(parent, style);
		this.glHandler = glHandler;
		this.hid = glHandler.getInputHandler();
		setLayout(new FillLayout());
	}
		
	/**
	 * Initializes the OpenGL embedment in a {@link GLCanvas}. The OpenGL update loop is not started until this has been called.
	 */
	public void init() {
		GLData data = new GLData();
		data.doubleBuffer = true;
		canvas = new GLCanvas(this, SWT.NONE, data);
		canvas.setCurrent();
		
		try {
			GLContext.useContext(canvas);
		} catch(LWJGLException e) {
			e.printStackTrace();
		}
		
		glHandler.init();
		
		canvas.addListener(SWT.Resize, event -> {
			Rectangle bounds = canvas.getBounds();
			canvas.setCurrent();
			
			try {
				GLContext.useContext(canvas);
			} catch(LWJGLException e) { 
				e.printStackTrace(); 
			}
			
			GL11.glViewport(0, 0, bounds.width, bounds.height);
		});		
		
		final Runnable run = new Runnable() {
			@Override
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					
					try {
						GLContext.useContext(canvas);
					} catch(LWJGLException e) { 
						e.printStackTrace(); 
					}
					
					/* Fix for scroll wheel input */
					if(scrollActive) scrollActive = false;
					else hid.setScrollDelta(0);
					
					glHandler.update();
					canvas.swapBuffers();
					getParent().getDisplay().asyncExec(this);
				}
			}
		};
		
		canvas.addListener(SWT.MouseMove, mouseListener);
		canvas.addListener(SWT.MouseDown, mouseListener);
		canvas.addListener(SWT.MouseUp, mouseListener);
		canvas.addListener(SWT.MouseWheel, mouseScrollListener);
		canvas.addListener(SWT.KeyDown, kbListener);
		canvas.addListener(SWT.KeyUp, kbListener);
		canvas.addListener(SWT.Dispose, disposeListener);
		canvas.addListener(SWT.Paint, event -> run.run());
		
		this.getParent().getDisplay().asyncExec(run);
	}
	
}
