lwjgl-swt-bridge
======

lwjgl-swt-bridge is a small library intended for simple integration between LWJGL (the Light-Weight Java Game Library) and SWT (the Standard Widget Toolkit). The goal of this library is to allow users to embed their OpenGL palettes into an existing SWT application.

Features
------------

lwjgl-swt-bridge is a young library with limited configurability. With feedback (or contributions) I may add more useful stuff, but as of now it attempts to simply get a rendering canvas available for use with some default settings. For now it features:

* A single widget class which you can apply in the same situations as a regular SWT Composite (including WindowBuilderPro).
* An input handler for getting user input (please check the bug section regarding keyboard keys)
* An extendable class used for containing the OpenGL code.

Usage
-----

[Download the jar](https://github.com/Mudbill/lwjgl-swt-bridge/releases) from the releases page and include it in your project's build path, and you'll be able to access the GLComposite class and the GLDrawLoop class.

```java
public class MyOpenGLClass extends GLDrawLoop {
	@Override
	protected void init() {
		GL11.glClearColor(0, 1, 0, 1); // green
	}
	@Override
	protected void update() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	@Override
	protected void shutdown() {
		
	}
}
```

The GLComposite class requires an instance of GLDrawLoop, which you can extend. You'll be given these three methods to override and use for whatever OpenGL calls you'd like.

```java
GLComposite composite = new GLComposite(shell, SWT.NONE, new MyOpenGLClass());
composite.init();
```

Constructing the composite is similar to constructing a regular Composite widget, except you also need your loop class.
Don't forget to call the `init()` method of the GLComposite widget before you open your Shell.
PS: This is not the same `init()` method as in your custom class, however it is called during it.

When you now call your Shell's `open()` method to display your GUI, the GLComposite class will activate the `update()` method every frame. Therefore you don't explicitly define a rendering loop, you only specify a single iteration of it and the GLComposite will take care of updating it based on the refresh rate of the GUI.

Feel free to study at the BridgeFormExample.java class included in the source code for reference.

Bugs
-------

Due to a long-lasting bug in SWT regarding KeyReleased events, this impacts the input handler used in this library. The result is that holding multiple keys and releasing them may fail to trigger the release event for the keys following the first. For instance, holding Q and E, then releasing Q followed by E will trigger the release event for Q, but drop the release event for E. It's unlikely that this will be fixed in SWT, so I may have to find some other way of capturing user input more effectively, in order to avoid this.

License & Credit
-------

Some of the code used in this library is based on the example for GLCanvas (which is used internally as well) found in the SWT documentation [here](http://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet195.java).

This library is free to use as long as it complies with the GNU GPL license. Please see https://www.gnu.org/licenses/ for more information.
