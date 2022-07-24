package main;

import processing.core.PFont;

public class UILabel extends UIElement {
    
    public String text = "";
    public int textSize = 1;
    public int[] tc;
    public PFont font;

    public UILabel(Launcher l, float x, float y, float w, float h, int[] bg, int layer, Runnable func) {
        super(l, x, y, w, h, bg, layer, func);
    }

    public UILabel(Launcher l, float x, float y, float w, float h, int[] bg, int layer, String text, int[] tc, PFont font, Runnable func) {
        super(l, x, y, w, h, bg, layer, func);
        this.text = text;
        this.tc = tc;
        this.font = font;
        this.textSize = getTextSize();
    }

    public UILabel(Launcher l, float x, float y, float w, float h, int[] bg, int layer, String text, int[] tc, int textSize, PFont font, Runnable func) {
        super(l, x, y, w, h, bg, layer, func);
        this.text = text;
        this.tc = tc;
        this.font = font;
        this.textSize = textSize;
    }

    public int getTextSize() {
        l.textFont(font);
        l.textSize(1);
        int i = 1;
        while (l.textWidth(text) < w * 0.9) {
            i ++;
            l.textSize(i);
        }
        i --;
        return i;
    } 

    public void draw() {
        l.fill(bg[0],bg[1],bg[2]);
        l.rect(x, y, w, h);
        l.fill(0);
        l.textFont(font);
        l.textSize(textSize);
        l.textAlign(Launcher.CENTER, Launcher.CENTER);
        l.fill(tc[0],tc[1],tc[2]);
        l.text(text, x + w/2, y + h/2);
    }
}
