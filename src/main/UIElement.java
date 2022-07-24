package main;

public class UIElement {

    public Launcher l;
    public float x;
    public float y;
    public float w;
    public float h;
    public int[] bg;
    public int layer;
    public Runnable func;

    public UIElement(Launcher l, float x, float y, float w, float h, int[] bg, int layer, Runnable func) {
        this.l = l;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bg = bg;
        this.layer = layer;
        this.func = func;
    }

    public void draw() {
        l.fill(bg[0],bg[1],bg[2]);
        l.rect(x, y, w, h);
    }

    public Boolean click() {
        if (l.mouseX > x && l.mouseX < x+w && l.mouseY > y && l.mouseY < y+h) {
            if (func != null) {
                func.run();
            }
			return true;
		}
		return false;
    }
}
