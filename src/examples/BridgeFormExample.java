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
 *  along with lwjgl-swt-bridge.  If not, see <https://www.gnu.org/licenses/>. 
 */

package examples;

import net.buttology.lwjgl.swt.GLComposite;
import net.buttology.lwjgl.swt.GLDrawLoop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL11;

/**
 * This class is a proof-of-concept for lwjgl-swt-bridge which allows easy integration of OpenGL in an SWT-based application.<br>
 * All you need is a new instance of GLDrawLoop which overrides the three methods used to call your OpenGL calls.<br>
 * Don't forget to call init() before you open your Shell.
 * @author Mudbill
 */
public class BridgeFormExample {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BridgeFormExample window = new BridgeFormExample();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT <> LWJGL bridge");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		/* Preferrably you'd want to create your own class which extends GLDrawLoop, then implement your OpenGL in there and instantiate it here. */
		new GLComposite(shell, SWT.BORDER, new GLDrawLoop() {
			@Override
			protected void init() {
				GL11.glClearColor(1, 0, 0, 1);
				System.out.println("Init first context, with clear color set to red.");
			}
			@Override
			protected void update() {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			}
			@Override
			protected void shutdown() {
				System.out.println("Shutting down first context");
			}
		}).init();
		
		/* If you need multiple contexts, you can do so. */
		new GLComposite(shell, SWT.BORDER, new GLDrawLoop() {
			@Override
			protected void init() {
				GL11.glClearColor(0, 0, 1, 1);
				System.out.println("Init first context, with clear color set to blue.");
			}
			@Override
			protected void update() {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			}
			@Override
			protected void shutdown() {
				System.out.println("Shutting down second context");
			}
		}).init();
	}
}
