package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;
import processing.core.PFont;

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
    int defaultSize = 100;
	int scrollAmt = 0;
	Boolean stageMenu = false;

	Boolean mouseUp = true;

	String[] defaultGraphs = new String[]{
		"Cycle",
		"Star",
		"Wheel",
		"Complete",
		"Path",
		"Square Lattice",
		"Hex Lattice",
		"Hex Lattice 2",
		"Tri Lattice",
		"Tri Lattice 2",
		"Binary Tree",
		"Octagram Lattice",
	};

	ArrayList<ThreadData> threads = new ArrayList<ThreadData>();

	Map<String, PFont> fonts = new HashMap<String, PFont>();

	ArrayList<UIElement> UIElements = new ArrayList<UIElement>();
	ArrayList<ArrayList<UIElement>> drawLayers = new ArrayList<ArrayList<UIElement>>();
	ArrayList<ArrayList<UIElement>> clickLayers = new ArrayList<ArrayList<UIElement>>();
	
	public static void main(String[] args) {
		System.out.println("-------------\nINPUT NAME\n-------------\n");
		PApplet.main("main.Launcher");
	}

	public void settings() {
		size(1440, 800,P2D);
	}

	public void setup() {
		for (String s : defaultGraphs){
			graphNames.add(s);
		}
		fonts.put("Lato", createFont("data/Lato.ttf", 128));
		fonts.put("Mono", createFont("data/Mono.ttf", 128));
		UIElements.add(new UILabel(this, 50, 50, 50, 50, new int[]{40, 200, 40}, 1, "Next", new int[]{0, 0, 0}, fonts.get("Lato"), this::nextMode));
		UIElements.add(new UILabel(this, 75, 75, 50, 50, new int[]{200, 40, 40}, 2, "Print", new int[]{0, 0, 0}, fonts.get("Lato"), this::doThing));
		drawLayers = getLayers(false);
		clickLayers = getLayers(true);
	}

	public void doThing() {
		System.out.println("ATHING");
	}

	public Boolean click(float x, float y, float x2, float y2) {
		if (mouseX > x && mouseX < x+x2 && mouseY > y && mouseY < y+y2) {
			return true;
		}
		return false;
	}

	public void nextMode(){
		if (myMode == mode.place) {
			myMode = mode.connect;
		}
		else if (myMode == mode.connect) {
			myMode = mode.numbers;
		}
	}
	
	public void setCurrentVertex(){
		currentSelected.num = Integer.parseInt(String.valueOf(currentSelected.num)+key);
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

	public void moveViewRight(){
		if (currentView < storeAll.size()-1) {
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
		int answer = Checks.graphSaturable(nw);
		System.out.println(answer);
	}

	public void connectAllVertices() {
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

	public void setBadVertices() {
		for (Vertex v : vertices) {
			v.isBad = false;
		}
		for (Vertex v : Checks.allBadVertices(vertices)) {
			v.isBad = true;
			//System.out.println(v.num);
		}
	}

	// ---------------------------------------------------------- KEY MANAGEMENT ---------------------------------------------------------------- //
	
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
			checkSupersatsThread(6, 6, displayWidth/2, displayHeight/2, defaultSize);
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

	// --------------------------------------------------------- CLICK MANAGEMENT --------------------------------------------------------------- //

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

	public void mouseReleased() {
		mouseUp = true;
	}

	public ArrayList<ArrayList<UIElement>> getLayers(Boolean flip) {
		TreeMap<Integer, ArrayList<UIElement>> layers = new TreeMap<Integer, ArrayList<UIElement>>();
		for (UIElement i : UIElements) {
			if (layers.containsKey(i.layer)) {
				layers.get(i.layer).add(i);
			}
			else {
				layers.put(i.layer, new ArrayList<UIElement>());
				layers.get(i.layer).add(i);
			}
		}
		ArrayList<ArrayList<UIElement>> retList = new ArrayList<ArrayList<UIElement>>();
		for (ArrayList<UIElement> layer : layers.values()) {
			retList.add(layer);
		}
		if (flip) {
			ArrayList<ArrayList<UIElement>> flipped = new ArrayList<ArrayList<UIElement>>();
			for (int i = retList.size()-1; i >= 0; i--) {
				flipped.add(retList.get(i));
			}
			return flipped;
		}
		return retList;
	}

	public void mousePressed() {
		for (ArrayList<UIElement> layer : clickLayers) {
			for (UIElement i : layer) {
				if (i.click()) {
					return;
				}
			}
		}
		if (click(1320,20,100,30)) {
			stageMenu = ! stageMenu;
			return;
		}
		if (click(1320,60,100,30) && stageMenu) {
			topModeClick();
			return;
		}
		if (click(1320,100,100,30) && stageMenu) {
			bottomModeClick();
			return;
		}
		if (myMode == mode.place) {
			if (clickLeftUI()){
				return;
			}
			if (mouseX > 130) {
				placeElement();
				return;
			}
		}
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
		if (myMode == mode.connect) {
			if (clickVertex()){
				return;
			}
			if (click(15,30,100,30)) {
				connectAllVertices();
			}
		}
		if (myMode == mode.numbers) {
			handleNumbersClick();
		}
		
		setBadVertices();
		//place a point if placing points
		//connect points if connecting points
		
	}

	public void topModeClick(){
		if (myMode == mode.place) {
			myMode = mode.connect;
		}
		else if (myMode == mode.connect) {
			myMode = mode.place;
		}
		else if (myMode == mode.numbers) {
			myMode = mode.place;
		}
	}

	public void bottomModeClick(){
		if (myMode == mode.place) {
			myMode = mode.numbers;
		}
		else if (myMode == mode.connect) {
			myMode = mode.numbers;
		}
		else if (myMode == mode.numbers) {
			myMode = mode.connect;
		}
	}

	public void handleNumbersClick() {
		Vertex clicked = getVClicked();
		if (clicked != null) {
			currentSelected = clicked;
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
			checkXtd(testXtd);
		}
	}

	public Boolean clickVertex() {
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
			return true;
		}
		return false;
	}

	public Boolean clickLeftUI() {
		if (click(15,30,15,30)) {
			if (lengthOption > 1) {
				lengthOption --;
			}
			return true;
		}
		else if (click(100,30,15,30)) {
			lengthOption ++;
			return true;
		}
		if (clickScrollBar()){
			return true;
		}
		return false;
	}

	public Boolean clickScrollBar() {
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
		if (clicked == -1){
			return false;
		}
		return true;
	}

	public void placeElement() {
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

	public Vertex getVClicked() {
		for (Vertex v : vertices) {
			if ((v.x-mouseX)*(v.x-mouseX)+(v.y-mouseY)*(v.y-mouseY) < pointSize*pointSize) {
				return v;
			}
		}
		return null;
	}

	// ----------------------------------------------------------- CHECK THREADS ---------------------------------------------------------------- //

	public ArrayList<Vertex> checkXtd(int xtd) {
		ThreadData sendTd;
		if (xtd == 0) {
			sendTd = new ThreadData("???", vertices.size(), Checks.countEdges(vertices));
		}
		else {
			sendTd = new ThreadData("???", vertices.size(), Checks.countEdges(vertices), xtd);
		}
		threads.add(sendTd);
		Thread thread = new Thread(new CheckXtdThread(xtd, vertices, sendTd, true, true, this));
		thread.start();
		return new ArrayList<Vertex>();
	}

	public void xtdThreadDone(ArrayList<Vertex> result, ThreadData thread) {
		if (result == null){
			thread.failed = true;
		}
		else if (result.size() == 0) {
			thread.failed = true;
		}
		else {
			storeAll.add(result);
			thread.done = true;
		}
	}

	public void updateThreadTestXtd(ThreadData thread, int xtd) {
		thread.currXtd = xtd;
	}

	public void updateThreadCompletes(ThreadData thread, int complete, int found) {
		thread.currComplete = complete;
		thread.currFound = found;
	}

	public void closeThread(ThreadData thread) {
		threads.remove(thread);
	}

	public ArrayList<ArrayList<Vertex>> checkSupersatsThread(int start, int end, float sX, float sY, int defaultSize) {
		ThreadData sendTd = new ThreadData("Checking Supersats");
		threads.add(sendTd);
		Thread thread = new Thread(new CheckSetThread(sendTd, start, end, sX, sY, defaultSize, this));
		thread.start();
		return new ArrayList<ArrayList<Vertex>>();
	}

	public void setThreadDone(ArrayList<ArrayList<Vertex>> result, ThreadData thread) {
		if (result == null){
			thread.failed = true;
		}
		else if (result.size() == 0) {
			thread.failed = true;
		}
		else {
			for (ArrayList<Vertex> g : result) {
				storeAll.add(g);
			}
			thread.currXtd = 0;
			thread.done = true;
		}
	}

	// ---------------------------------------------------------------- UI ---------------------------------------------------------------------- //

	public void draw(){
		textFont(fonts.get("Lato"));
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
        textSize(12);
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
		textFont(fonts.get("Mono"));
		drawThreadData();
		
		for (ArrayList<UIElement> layer : drawLayers) {
			for (UIElement i : layer) {
				i.draw();
			}
		}

	}

	public void drawThreadData() {
		textAlign(RIGHT, TOP);
		textSize(15);
		strokeWeight(1);
		int h = 750;
		ArrayList<ThreadData> toRemove = new ArrayList<ThreadData>();
		for (ThreadData td : threads) {
			if (td.done) {
				fill(0,255,0);
				stroke(0,255,0);
			}
			else if (td.failed) {
				fill(255,0,0);
				stroke(255,0,0);
			}
			else {
				fill(255);
				stroke(255);
			}
			String print = td.getString();
			writeMonospace(print, 1400, h, true);
			float tw = textWidth(print);
			if (click(1400-tw, h+2, tw, 15)) {
				line(1400-tw, h+10, 1402, h+10);
				if (mousePressed && mouseUp) {
					if (td.done || td.failed) {
						toRemove.add(td);
					}
					else {
						td.thread.stop();
					}
				}
			}
			h -= 20;
		}
		for (ThreadData td : toRemove) {
			threads.remove(td);
		}
		if (mousePressed) {
			mouseUp = false;
		}
	}
	public void writeMonospace(String text, float x, float y, Boolean right) {
		// Please use a monospace type font, otherwise the result may be messy
		float size = 0;
		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (textWidth(c) > size) {
				size = textWidth(c);
			}
		}
		if (right) {
			for (int i = chars.length - 1; i >= 0; i--) {
				text(chars[i], x-size*(chars.length-i-1), y);
			}
		}
		else {
			for (int i = 0; i < chars.length; i++) {
				text(chars[i], x+size*i, y);
			}
		}
	}
}
