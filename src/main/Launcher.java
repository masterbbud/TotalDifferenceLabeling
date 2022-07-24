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

enum mode {
	place,
	connect,
	find,
	check,
	numbers
}

public class Launcher extends PApplet {

	//place all infinites
	//save state
	//check for supersaturable graphs
	//improve ability to delete edges
	
	enum graph {
		none,
		cycle,
		star,
		wheel,
		complete,
		path
	}
	mode myMode = mode.place;
	graph myGraph = graph.none;
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	//ArrayList<graph> graphs = new ArrayList<graph>();
	ArrayList<String> graphNames = new ArrayList<String>();
	ArrayList<ArrayList<Vertex>> storeAll = new ArrayList<ArrayList<Vertex>>();
	int currentView = 0;
	
	Vertex currentSelected = null;
	int pointSize = 10;
	int lengthOption = 1;
	int testXtd = 1;
	Boolean stageMenu = false;

	UIManager ui = new UIManager(this);
	
	public static void main(String[] args) {
		System.out.println("-------------\nINPUT NAME\n-------------\n");
		PApplet.main("main.Launcher");
	}

	public void settings() {
		size(1440, 800,P2D);
	}

	public void setup() {
		graphNames.add("Cycle");
		graphNames.add("Star");
		graphNames.add("Wheel");
		graphNames.add("Complete");
		graphNames.add("Path");
		graphNames.add("Square Lattice");
		graphNames.add("Hex Lattice");
		graphNames.add("Hex Lattice 2");
		graphNames.add("Tri Lattice");
		graphNames.add("Tri Lattice 2");
		graphNames.add("Binary Tree");
		graphNames.add("Octagram Lattice");
	}

	public void draw() {
		ui.drawAll(vertices, currentSelected, pointSize);
	}
	public Boolean click(float x, float y, float x2, float y2) {
		if (mouseX > x && mouseX < x+x2 && mouseY > y && mouseY < y+y2) {
			return true;
		}
		return false;
	}
	public void mouseWheel(MouseEvent event) {
		// Handles scrolling on the list of Default Graphs
		if (click(10,150,110,200)) {
			float f = event.getCount();
			//System.out.println(f);
			scrollAmt += f;
			if (scrollAmt < 0) {
				scrollAmt = 0;
			}
			if (scrollAmt > graphNames.size() - 5) {
				scrollAmt = graphNames.size() - 5;
			}
		}
	}
	public void nextMode(){
		if (myMode == mode.place) {
			myMode = mode.connect;
		}
		else if (myMode == mode.connect) {
			myMode = mode.numbers;
		}
//		else if (myMode == mode.numbers) {
//			currentSelected.num = Integer.parseInt(currentType);
//			currentType = "";
//		}
	}
	public void keyPressed() {
		if (keyCode == ENTER) {
			nextMode();
		}
		if (key == ' ') {
			currentSelected = null;
		}
		if (Character.isDigit(key) && myMode == mode.numbers) {
			setCurrentVertex();
		}
		if (keyCode == BACKSPACE) {
			handleBackspace();
		}
		if (key == 'd') {
			deleteAllVertices();
		}
		if (key == 'p') {
			getAllSupersats();
		}
		if (key == 'l') {
			moveViewRight();
		}
		if (key == 'k') {
			moveViewLeft();
		}
		if (key == 'a') {
			printIsSaturable();
		}
		if (key == 'm') {
			SaveManager.saveVerticesToFile("SavedGraphs/file.txt", vertices);
		}
		if (key == 'n') {
			vertices = SaveManager.loadVerticesFromFile("SavedGraphs/file.txt");
		}
		setBadVertices();
	}
	public void setCurrentVertex(){
		currentSelected.num = Integer.parseInt(String.valueOf(currentSelected.num)+key);
	}
	public void handleBackspace(){
		if (myMode == mode.place) {
			removeLastVertex();
		}
		if (myMode == mode.connect) {
			removeLastEdge();
		}
		if (myMode == mode.numbers) {
			currentSelected.num = 0;
		}
	}
	public void removeLastVertex(){
		if (vertices.size() > 0) {
			vertices.remove(vertices.size()-1);
		}
	}
	public void removeLastEdge(){
		if (currentSelected != null) {
			if (currentSelected.connections.size() > 0) {
				currentSelected.connections.get(currentSelected.connections.size()-1).connections.remove(currentSelected.connections.get(currentSelected.connections.size()-1).connections.size()-1);
				currentSelected.connections.remove(currentSelected.connections.size()-1);
			}
		}
	}
	public void deleteAllVertices(){
		myMode = mode.place;
		currentSelected = null;
		vertices = new ArrayList<Vertex>();
		// May want to remove from views
	}
	public void getAllSupersats(){
		ArrayList<ArrayList<Vertex>> all = lookForSupersat(6,6);
		System.out.println(all.size());
		currentView = 0;
		vertices = all.get(all.size()-1);
		storeAll = all;
	}
	public void moveViewRight(){
		if (currentView < storeAll.size()) {
			currentView++;
		}
		vertices = storeAll.get(currentView);
	}
	public void moveViewLeft(){
		if (currentView > 0) {
			currentView --;
		}
		vertices = storeAll.get(currentView);
	}
	public void printIsSaturable(){
		ArrayList<Vertex> nw = new ArrayList<Vertex>();
		for (Vertex v : vertices) {
			nw.add(v);
		}
		int answer = graphSaturable(nw);
		System.out.println(answer);
	}
	public void mousePressed() {
		if (click(1320,20,100,30)) {
			stageMenu = ! stageMenu;
			return;
		}
		if (click(1320,60,100,30) && stageMenu) {
			if (myMode == mode.place) {
				myMode = mode.connect;
			}
			else if (myMode == mode.connect) {
				myMode = mode.place;
			}
			else if (myMode == mode.numbers) {
				myMode = mode.place;
			}
			return;
		}
		if (click(1320,100,100,30) && stageMenu) {
			if (myMode == mode.place) {
				myMode = mode.numbers;
			}
			else if (myMode == mode.connect) {
				myMode = mode.numbers;
			}
			else if (myMode == mode.numbers) {
				myMode = mode.connect;
			}
			return;
		}
		if (myMode == mode.place) {
			if (click(15,30,15,30)) {
				if (lengthOption > 1) {
					lengthOption --;
				}
			}
			else if (click(100,30,15,30)) {
				lengthOption ++;
			}
			else if (click(15,90,15,30)) {
				if (pointSize > 5) {
					pointSize --;
				}
			}
			else if (click(100,90,15,30)) {
				if (pointSize < 30) {
					pointSize ++;
				}
			}
			int clicked = -1;
			if (click(15,155,100,30)) {
				clicked = scrollAmt;
			}
			else if (click(15,195,100,30)) {
				clicked = scrollAmt + 1;
			}
			else if (click(15,235,100,30)) {
				clicked = scrollAmt + 2;
			}
			else if (click(15,275,100,30)) {
				clicked = scrollAmt + 3;
			}
			else if (click(15,315,100,30)) {
				clicked = scrollAmt + 4;
			}
			if (clicked == 0) {
				myGraph = graph.cycle;
			}
			else if (clicked == 1) {
				myGraph = graph.star;
			}
			else if (clicked == 2) {
				myGraph = graph.wheel;
			}
			else if (clicked == 3) {
				myGraph = graph.complete;
			}
			else if (clicked == 4) {
				myGraph = graph.path;
			}
			else if (clicked == 5) {
				vertices = DefaultGraphs.initInfiniteSquare(defaultSize, lengthOption, vertices);
			}
			else if (clicked == 6) {
				vertices = DefaultGraphs.initInfiniteHex(false, defaultSize, lengthOption, vertices);
			}
			else if (clicked == 7) {
				vertices = DefaultGraphs.initInfiniteHex(true, defaultSize, lengthOption, vertices);
			}
			else if (clicked == 8) {
				vertices = DefaultGraphs.initInfiniteTri(false, defaultSize, lengthOption, vertices);
			}
			else if (clicked == 9) {
				vertices = DefaultGraphs.initInfiniteTri(true, defaultSize, lengthOption, vertices);
			}
			else if (clicked == 10) {
				vertices = DefaultGraphs.initBinaryTree(defaultSize, displayWidth, displayHeight, lengthOption, vertices);
			}
			else if (clicked == 11) {
				vertices = DefaultGraphs.initInfiniteOctagram(false, defaultSize, lengthOption, vertices);
			}
			if (mouseX > 130) {
				if (myGraph != graph.none) {
					if (myGraph == graph.cycle) {
						vertices = DefaultGraphs.initCycle(mouseX, mouseY, defaultSize, displayWidth, displayHeight, lengthOption, vertices);
					}
					if (myGraph == graph.star) {
						vertices = DefaultGraphs.initStar(mouseX, mouseY, defaultSize, displayWidth, displayHeight, lengthOption, vertices);
					}
					if (myGraph == graph.wheel) {
						vertices = DefaultGraphs.initWheel(mouseX, mouseY, defaultSize, displayWidth, displayHeight, lengthOption, vertices);
					}
					if (myGraph == graph.complete) {
						vertices = DefaultGraphs.initComplete(mouseX, mouseY, defaultSize, displayWidth, displayHeight, lengthOption, vertices);
					}
					if (myGraph == graph.path) {
						vertices = DefaultGraphs.initPath(mouseX, mouseY, defaultSize, displayWidth, displayHeight, lengthOption, vertices);
					}
					myGraph = graph.none;
				}
				else {
					vertices.add(new Vertex(mouseX,mouseY));
				}
			}
		}
		else {
			if (click(15,90,15,30)) {
				if (pointSize > 5) {
					pointSize --;
				}
			}
			else if (click(100,90,15,30)) {
				if (pointSize < 30) {
					pointSize ++;
				}
			}
		}
		if (myMode == mode.connect) {
			
			Vertex clicked = getVClicked();
			if (clicked != null) {
				if (currentSelected != null) {
					if (! currentSelected.connections.contains(clicked)) {
						currentSelected.connections.add(clicked);
					}
					if (! clicked.connections.contains(currentSelected)) {
						clicked.connections.add(currentSelected);
					}
				}
				currentSelected = clicked;
				
			}
			if (click(15,30,100,30)) {
				for (Vertex v : vertices) {
					for (Vertex i : vertices) {
						if (i != v) {
							if (!v.connections.contains(i)) {
								v.connections.add(i);
							}
							if (!i.connections.contains(v)) {
								i.connections.add(v);
							}
						}
					}
				}
			}
		}
		if (myMode == mode.numbers) {
			Vertex clicked = getVClicked();
			if (clicked != null) {
				currentSelected = clicked;
				currentSelected.num = 0;
			}
			if (click(15,30,100,30)) {
				for (Vertex v : vertices) {
					v.num = 0;
				}
			}
			if (click(15,150,15,30)) {
				if (testXtd > 0) {
					testXtd --;
				}
			}
			if (click(100,150,15,30)) {
				testXtd ++;
			}
			if (click(15,200,100,30)) {
				if (testXtd == 0) {
					testXtd = 1;
					while (! check(testXtd, (ArrayList<Vertex>)vertices.clone())) {
						testXtd++;
					}
				}
				else {
					check(testXtd, (ArrayList<Vertex>)vertices.clone());
				}
			}
		}
		
		setBadVertices();
		//place a point if placing points
		//connect points if connecting points
		
	}
	
	public void setBadVertices() {
		for (Vertex v : vertices) {
			v.isBad = false;
		}
		for (Vertex v : allBadVertices(vertices)) {
			v.isBad = true;
			//System.out.println(v.num);
		}
	}
	public Vertex getVClicked() {
		for (Vertex v : vertices) {
			if ((v.x-mouseX)*(v.x-mouseX)+(v.y-mouseY)*(v.y-mouseY) < pointSize*pointSize) {
				return v;
			}
		}
		return null;
	}
	public ArrayList<ArrayList<Vertex>> lookForSupersat(int start, int end) {
		//order = 5;
		int order = start;
		float sX = displayWidth/2;
		float sY = displayHeight/2;
		ArrayList<ArrayList<Vertex>> all = new ArrayList<ArrayList<Vertex>>();
		
		while (order <= end) {
			vertices = new ArrayList<Vertex>();
			for (int i = 0; i < order; i++) {
				vertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/order)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/order)*defaultSize)));
			}
			
			//now try each config of edges
			ArrayList<int[]> allEdges = new ArrayList<int[]>();
			for (int a = 0; a < order; a++) {
				for (int b = 0; b < order; b++) {
					if (a < b) {
						allEdges.add(new int[] {a,b});
					}
				}
			}
			int perms = 1<<(allEdges.size());
			for (int i = 0; i < perms; i++) {
				String nowPerm = Integer.toBinaryString(i);
				char[] chars = nowPerm.toCharArray();
				for (int c = 0; c < chars.length; c++) {
					if (chars[c] == '1') {
						vertices.get(allEdges.get(c)[0]).connections.add(vertices.get(allEdges.get(c)[1]));
						vertices.get(allEdges.get(c)[1]).connections.add(vertices.get(allEdges.get(c)[0]));
					}
				}
				//check(testXtd, vertices);
				//if (true) {
				//	return null;
				//}
				testXtd = 1;
				while (! check(testXtd, vertices)) {
					testXtd++;
					//System.out.println("ad");
				}
				//System.out.println(i+" "+nowPerm+" "+perms);
				
				if (testXtd == order && graphConnected(vertices)) {
					//System.out.println("orderstf"+testXtd+" "+order);
					ArrayList<Vertex> neww = vertices;
					all.add(copy(neww));
					for (Vertex v : vertices) {
						v.connections = new ArrayList<Vertex>();
						v.num = 0;
					}
				}
				else {
					//reset + continue
					for (Vertex v : vertices) {
						v.connections = new ArrayList<Vertex>();
						v.num = 0;
					}
				}
				
				//check, reset, continue
				
				 
			}
			//System.out.println(perms);
			
			order++;
		}
		ArrayList<ArrayList<Vertex>> alltwo = new ArrayList<ArrayList<Vertex>>();
		for (ArrayList<Vertex> a : all) {
			if (graphSaturable(a) == 2) {
				alltwo.add(a);
				//System.out.println("added");
			}
		}
		return alltwo;
	}
	public ArrayList<Vertex> copy(ArrayList<Vertex> ver){
		ArrayList<Vertex> newL = new ArrayList<Vertex>();
		for (Vertex v : ver) {
			newL.add(new Vertex(v.x, v.y));
			newL.get(newL.size()-1).num = v.num;
		}
		for (int v = 0; v < ver.size(); v++) {
			for (int i = 0; i < ver.size(); i++) {
				if (ver.get(v).connections.contains(ver.get(i))) {
					newL.get(v).connections.add(newL.get(i));
				}
			}
		}
		return newL;
		
	}
}
