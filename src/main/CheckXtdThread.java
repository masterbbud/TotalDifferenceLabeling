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
    private Boolean auto;
    private Boolean back;

    public CheckXtdThread(int xtd, ArrayList<Vertex> vertices, ThreadData td, Boolean auto, Boolean back, Launcher launcher) {
        this.xtd = xtd;
        this.vertices = vertices;
        this.td = td;
        td.thread = this;
        this.auto = auto;
        this.back = back;
        this.launcher = launcher;
    }

    public void run() {
        launcher.xtdThreadDone(checkXtd(xtd, vertices, auto, back), td);
    }

    public ArrayList<Vertex> checkXtd(int xtd, ArrayList<Vertex> vertices, Boolean auto, Boolean back) {
        if (auto && ! back) {
			if (xtd == 0) {
                xtd = 1;
            }
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
        else if (auto) {
            if (xtd == 0) {
                xtd = getGreedyWSR(vertices.size());
            }
            ArrayList<Vertex> saveSolution = XTDCheck.check(xtd, XTDCheck.copy(vertices), this);
            ArrayList<Vertex> tempVertices = new ArrayList<Vertex>();
            while (tempVertices != null) {
                saveSolution = XTDCheck.copy(tempVertices);
                launcher.updateThreadTestXtd(td, xtd);
                xtd--;
                tempVertices = XTDCheck.check(xtd, XTDCheck.copy(vertices), this);
                if (tempVertices != null && tempVertices.size() == 0) {
                    return tempVertices;
                }
            }
            return saveSolution;
        }
		else {
			return XTDCheck.check(xtd, (ArrayList<Vertex>)vertices.clone(), this);
		}
    }

    public int getGreedyWSR(int n) {
        return Integer.parseInt(Integer.toBinaryString(n), 3);
    }
}
