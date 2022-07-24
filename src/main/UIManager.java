package main;

import java.io.BufferedReader;
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

public class UIManager extends PApplet{

    Launcher main;

    int defaultSize = 100;
	int scrollAmt = 0;

    public UIManager(Launcher launcher){
        main = launcher;
    }

    public void drawAll(ArrayList<Vertex> vertices, Vertex currentSelected, float pointSize){
        background(50);
        //point(displayWidth*15/16,10);
        ellipseMode(CENTER);
        fill(255);
        stroke(255);
        strokeWeight(2);
        textAlign(CENTER,CENTER);
        for (Vertex v : vertices) {
            stroke(255);
            for (Vertex i : v.connections) {
    //				if (currentSelected.connections.contains(i) || currentSelected.connections.contains(v)) {
    //					stroke(180,255,180);
    //				} // could be used for an option
                if (i == currentSelected || v == currentSelected) {
                    stroke(0,180,0);
                }
                
                line(v.x,v.y,i.x,i.y);
                stroke(255);
            }
        }
        for (Vertex v : vertices) {
            if (v == currentSelected) {
                fill(150,150,250);
                stroke(150,150,250);
            }
            
            ellipse(v.x,v.y,pointSize*2,pointSize*2);
            fill(255);
            stroke(255);
        }
        
        for (Vertex v : vertices) {
            fill(0);
            if (v.num > 0) {
                text(v.num,v.x,v.y-2);
            }
        }
        strokeWeight(pointSize*2/5);
        for (Vertex v : vertices) {
            if (v.isBad) {
                fill(255,0);
                stroke(250,50,50);
                ellipse(v.x,v.y,pointSize*2,pointSize*2);
            }
        }
        stroke(255);
        drawUI();
        
        //draw errors
    }

    public void drawUI() {
		mode myMode = main.myMode;
		noStroke();
		
		fill(220);
		rect(1320,20,100,30);
		rect(0,0,130,displayHeight);
		fill(255);
		if (myMode == mode.place) {
			fill(100);
			rect(10,150,110,200);
			fill(255);
			
			
			rect(40,20,50,50);
			rect(15,30,15,30);
			rect(100,30,15,30);
			
			rect(40,80,50,50);
			rect(15,90,15,30);
			rect(100,90,15,30);
			
			rect(15,155,100,30);
			rect(15,195,100,30);
			rect(15,235,100,30);
			rect(15,275,100,30);
			rect(15,315,100,30);
			
			fill(0);
			textSize(12);
			text(lengthOption,65,43);
			text(pointSize,65,103);
			text("<",22,43);
			text(">",108,43);
			text("<",22,103);
			text(">",108,103);
			fill(160);
			text("n",80,60);
			text("size",80,120);
			fill(0);
			for (int i = 0; i < 5; i++) {
				text(graphNames.get(scrollAmt+i),65,168+40*i);
			}
			/*text("Cycle",65,93);
			text("Star",65,133);
			text("Wheel",65,173);
			text("Complete",65,213);
			text("Path",65,253);*/
		}
		else if (myMode == mode.connect) {
			rect(40,80,50,50);
			rect(15,90,15,30);
			rect(100,90,15,30);
			
			rect(15,30,100,30);
			fill(0);
			textSize(12);
			text(pointSize,65,103);
			text("<",22,103);
			text(">",108,103);
			fill(160);
			text("size",80,120);
			fill(0);
			text("Connect All",65,43);
		}
		else {
			rect(40,80,50,50);
			rect(15,90,15,30);
			rect(100,90,15,30);
			
			rect(15,40,100,30);
			
			rect(40,140,50,50);
			rect(15,150,15,30);
			rect(100,150,15,30);
			
			rect(15,200,100,30);
			
			fill(0);
			textSize(12);
			text(pointSize,65,103);
			text("<",22,103);
			text(">",108,103);
			if (testXtd > 0) {
				text(testXtd,65,163);
			}
			else {
				text("Any",65,163);
			}
			text("<",22,163);
			text(">",108,163);
			fill(160);
			text("size",80,120);
			text("Xtd",80,180);
			fill(0);
			text("Clear Labels",65,53);
			text("Search",65,213);
		}
		
		if (myMode == mode.place) {
			text("Place Vertices",1372,33);
		}
		if (myMode == mode.connect) {
			text("Connect Vertices",1372,33);
		}
		if (myMode == mode.numbers) {
			text("Assign Labels",1372,33);
		}
		
		if (stageMenu) {
			fill(160);
			rect(1320,60,100,30);
			rect(1320,100,100,30);
			fill(0);
			if (myMode == mode.place) {
				text("Connect Vertices",1372,73);
				text("Assign Labels", 1372, 113);
			}
			if (myMode == mode.connect) {
				text("Place Vertices",1372,73);
				text("Assign Labels", 1372, 113);
			}
			if (myMode == mode.numbers) {
				text("Place Vertices",1372,73);
				text("Connect Vertices", 1372, 113);
			}
			
		}
		
		
		
	}
}
