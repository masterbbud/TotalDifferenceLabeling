package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class CheckXtdThread extends DefaultThread implements Runnable{
    
    private int xtd;
    private ArrayList<Vertex> vertices;
    private ThreadData td;
    private Launcher launcher;

    public CheckXtdThread(int xtd, ArrayList<Vertex> vertices, ThreadData td, Launcher launcher) {
        this.xtd = xtd;
        this.vertices = vertices;
        this.td = td;
        td.thread = this;
        this.launcher = launcher;
    }
    public void run() {
        launcher.xtdThreadDone(checkXtd(xtd, vertices), td);
    }
    public ArrayList<Vertex> checkXtd(int xtd, ArrayList<Vertex> vertices) {
        if (xtd == 0) {
			xtd = 1;
			ArrayList<Vertex> tempVertices = XTDCheck.check(xtd, (ArrayList<Vertex>)vertices.clone(), this);
			while (tempVertices == null) {
                launcher.updateThreadTestXtd(td, xtd);
				xtd++;
				tempVertices = XTDCheck.check(xtd, (ArrayList<Vertex>)vertices.clone(), this);
                if (tempVertices != null && tempVertices.size() == 0) {
                    return tempVertices;
                }
			}
			return tempVertices;
		}
		else {
			return XTDCheck.check(xtd, (ArrayList<Vertex>)vertices.clone(), this);
		}
    }
}
