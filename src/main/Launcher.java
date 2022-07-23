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

public class Launcher extends PApplet {

	//place all infinites
	//save state
	//check for supersaturable graphs
	//improve ability to delete edges
	
	enum mode {
		place,
		connect,
		find,
		check,
		numbers
	}
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
	int defaultSize = 100;
	int scrollAmt = 0;
	int testXtd = 1;
	Boolean stageMenu = false;
	
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
	public Boolean click(float x, float y, float x2, float y2) {
		if (mouseX > x && mouseX < x+x2 && mouseY > y && mouseY < y+y2) {
			return true;
		}
		return false;
	}
	public void mouseWheel(MouseEvent event) {
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
	public void keyPressed() {
		if (keyCode == ENTER) {
			if (myMode == mode.place) {
				myMode = mode.connect;
			}
			else if (myMode == mode.connect) {
				myMode = mode.numbers;
			}
//			else if (myMode == mode.numbers) {
//				currentSelected.num = Integer.parseInt(currentType);
//				currentType = "";
//			}
		}
		if (key == ' ') {
			currentSelected = null;
			
		}
		if (key == '1' || key == '2' || key == '3' || key == '4' || key == '5' || key == '6' || key == '7' || key == '8' || key == '9' || key == '0') {
			if (myMode == mode.numbers) {
				currentSelected.num = Integer.parseInt(String.valueOf(currentSelected.num)+key);

			}
		}
		if (keyCode == BACKSPACE) {
			if (myMode == mode.place) {
				if (vertices.size() > 0) {
					vertices.remove(vertices.size()-1);
				}
			}
			if (myMode == mode.connect) {
				if (currentSelected != null) {
					if (currentSelected.connections.size() > 0) {
						currentSelected.connections.get(currentSelected.connections.size()-1).connections.remove(currentSelected.connections.get(currentSelected.connections.size()-1).connections.size()-1);
						currentSelected.connections.remove(currentSelected.connections.size()-1);
					}
				}
			}
			if (myMode == mode.numbers) {
				currentSelected.num = 0;
			}
		}
		if (key == 'd') {
			myMode = mode.place;
			currentSelected = null;
			vertices = new ArrayList<Vertex>();
		}
		if (key == 's') {
			initInfiniteSquare();
		}
		if (key == 'h') {
			initInfiniteHex(false);
		}
		if (key == 't') {
			initInfiniteTri(false);
		}
		if (key == 'b') {
			initBinaryTree();
		}
		if (key == 'p') {
			ArrayList<ArrayList<Vertex>> all = lookForSupersat(6,6);
			System.out.println(all.size());
			currentView = 0;
			vertices = all.get(all.size()-1);
			storeAll = all;
		}
		if (key == 'l') {
			if (currentView < storeAll.size()) {
				currentView++;
			}
			vertices = storeAll.get(currentView);
		}
		if (key == 'k') {
			if (currentView > 0) {
				currentView --;
			}
			vertices = storeAll.get(currentView);
		}
		if (key == 'a') {
			ArrayList<Vertex> nw = new ArrayList<Vertex>();
			for (Vertex v : vertices) {
				nw.add(v);
			}
			int answer = graphSaturable(nw);
			System.out.println(answer);
		}
		if (key == 'm') {
			try {
				saveVerticesToFile("SavedGraphs/file.txt", false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (key == 'n') {
			try {
				vertices = loadVerticesFromFile("SavedGraphs/file.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setBadVertices();
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
				initInfiniteSquare();
			}
			else if (clicked == 6) {
				initInfiniteHex(false);
			}
			else if (clicked == 7) {
				initInfiniteHex(true);
			}
			else if (clicked == 8) {
				initInfiniteTri(false);
			}
			else if (clicked == 9) {
				initInfiniteTri(true);
			}
			else if (clicked == 10) {
				initBinaryTree();
			}
			else if (clicked == 11) {
				initInfiniteOctagram();
			}
			if (mouseX > 130) {
				if (myGraph != graph.none) {
					if (myGraph == graph.cycle) {
						initCycle(mouseX,mouseY);
					}
					if (myGraph == graph.star) {
						initStar(mouseX,mouseY);
					}
					if (myGraph == graph.wheel) {
						initWheel(mouseX,mouseY);
					}
					if (myGraph == graph.complete) {
						initComplete(mouseX,mouseY);
					}
					if (myGraph == graph.path) {
						initPath(mouseX,mouseY);
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
	public Boolean check(int testXtd, ArrayList<Vertex> vertices) {
		//System.out.println(vertices.get(0).possibles);
		Vertex toCheck = vertices.get(0);
		for (Vertex v : vertices) {
			v.possibles = new ArrayList<Integer>();
			v.notPossible = new ArrayList<Integer>();
			if (v.num != 0) {
				continue;
			}
			for (int i = 1; i <= testXtd; i++) {
				v.possibles.add(i);
				for (Vertex adj : v.connections) {
					if (i == adj.num && adj.num != 0) {
						v.possibles.remove(Integer.valueOf(i));
						break;
					}
					if (i == 2*adj.num || 2*i == adj.num && adj.num != 0) {
						v.possibles.remove(Integer.valueOf(i));
						break;
					}
					for (Vertex adj2 : adj.connections) {
						if (adj2 != v) {
							if (i == adj2.num && adj2.num != 0) {
								v.possibles.remove(Integer.valueOf(i));
								break;
							}
							if (adj.num * 2 == i + adj2.num && adj.num != 0 && adj2.num != 0) {
								v.possibles.remove(Integer.valueOf(i));
								break;
							}
						}
					}
					if (!v.possibles.contains(i)) {
						break;
					}
				}
			}
			if (((v.num == 0) && (v.possibles.size() < toCheck.possibles.size()) || toCheck.num != 0) && v.possibles.size() != 0) {
				toCheck = v;
			}
			//System.out.println(v.possibles.size());
		}
		ArrayList<Vertex> checkedlist = new ArrayList<Vertex>();
		ArrayList<Vertex> saveVertices = vertices;
		while (true) {
			//try place
			//add to checked list
			//adjust possibles
			//if impossible config, delete possible for last checked vertex and try next one
			//if no more vertices on current vertex, back up 1
			//System.out.println("TRYING "+toCheck.possibles);
			toCheck.num = toCheck.possibles.get(0);
			//
			//toCheck.possibles.remove(0);
			//
			//maybe remove num from possibles?
			vertices = fix_possibles(toCheck, vertices);
			checkedlist.add(toCheck);
			Boolean works = true;
			for (Vertex v : vertices) {
				if (v.num == 0) {
					works = false;
				}
			}
			if (works) {
				//System.out.println("AFAFFSFAFAUGBSU(GSEUGBUSGBUISGBIUSGBIUSBUGIBUGIS I LOVE YOU");
				this.vertices = vertices;
				return true;
			}
			Vertex prevCheck = toCheck;
			Boolean foundOne = false;
			for (Vertex v : vertices) {
				if (((v.num == 0) && (v.possibles.size() < toCheck.possibles.size() || toCheck == prevCheck)) && v.possibles.size() != 0) {
					toCheck = v;
					foundOne = true;
					//System.out.println(v.possibles+" "+v.x+" "+v.y);
				}
			}
			int oldCheckNum = toCheck.num;
			if (!foundOne) {
				//System.out.println("     "+toCheck.num);
				oldCheckNum = toCheck.num;
				toCheck.num = 0;
				vertices = fixedList(checkedlist, vertices);
				toCheck.possibles.remove(Integer.valueOf(oldCheckNum));
				toCheck.notPossible.add(oldCheckNum);
				checkedlist.remove(checkedlist.size()-1);
				while (toCheck.possibles.size() == 0) {
					if (checkedlist.size() > 0) {
						
						toCheck = checkedlist.get(checkedlist.size()-1);
						oldCheckNum = toCheck.num;
						toCheck.num = 0;
						vertices = fixedList(checkedlist, vertices);
						toCheck.possibles.remove(Integer.valueOf(oldCheckNum));
						toCheck.notPossible.add(oldCheckNum);
						
						checkedlist.remove(checkedlist.size()-1);
					}
					else {
						System.out.println("NO"+testXtd);
						//this.vertices = saveVertices;
						return false;
					}
				}
				
			}
		}
		
		/*ArrayList<Vertex> finalList = check_recursive(toCheck, (ArrayList<Vertex>)vertices.clone());
		if (testAll == true) {
			for (Vertex v : finalList) {
				if (v.num == 0) {
					check(true, testXtd+1);
					return;
				}
			}
			
		}
		vertices = finalList;*/
		//now try to fill one out
		//fix all vertices in r2 that still have improper possibles
		//repeat
	}
	public ArrayList<Vertex> fixedList(ArrayList<Vertex> toCheck, ArrayList<Vertex> vertices){
		for (Vertex v : vertices) {
			v.possibles = new ArrayList<Integer>();
			if (! toCheck.contains(v)) {
				v.notPossible = new ArrayList<Integer>();
			}
			if (v.num != 0) {
				continue;
			}
			for (int i = 1; i <= testXtd; i++) {
				v.possibles.add(i);
				for (Vertex adj : v.connections) {
					if (i == adj.num && adj.num != 0) {
						v.possibles.remove(Integer.valueOf(i));
						break;
					}
					if (i == 2*adj.num || 2*i == adj.num && adj.num != 0) {
						v.possibles.remove(Integer.valueOf(i));
						break;
					}
					for (Vertex spadj : v.connections) {
						if (i * 2 == adj.num + spadj.num && adj.num != 0 && spadj.num != 0) {
							v.possibles.remove(Integer.valueOf(i));
							break;
						}
					}
					for (Vertex adj2 : adj.connections) {
						if (adj2 != v) {
							if (i == adj2.num && adj2.num != 0) {
								v.possibles.remove(Integer.valueOf(i));
								break;
							}
							if (adj.num * 2 == i + adj2.num && adj.num != 0 && adj2.num != 0) {
								v.possibles.remove(Integer.valueOf(i));
								break;
							}
							//
						}
						
					}
					if (!v.possibles.contains(i)) {
						break;
					}
				}
			}
			for (int i : v.notPossible) {
				v.possibles.remove(Integer.valueOf(i));
			}
			//System.out.println(v.possibles.size());
		}
		return vertices;
	}
	public ArrayList<Vertex> check_recursive(Vertex check, ArrayList<Vertex> tempVertices) {
		/*if (check.possibles.size() == 0) {
			if (checkList.size() > 0) {
				Vertex toCheck = checkList.get(checkList.size()-1);
				toCheck.possibles.remove(Integer.valueOf(toCheck.num));
				toCheck.num = 0;
				checkList.remove(checkList.size()-1);
				check.num = 0;
				resetPossibles(check, tempVertices);
				
				return check_recursive(checkList, toCheck, tempVertices);
			}
			else {
				return tempVertices;
			}
		}
		
		check.num = check.possibles.get(0);
		//check.possibles = new ArrayList<Integer>();
		checkList.add(check);
		//find the next one
		tempVertices = fix_possibles(check, tempVertices);
		Vertex toCheck = tempVertices.get(0);
		Boolean foundOne = false;
		for (Vertex v : tempVertices) {
			if ((v.num == 0) && v.possibles.size() < toCheck.possibles.size() || toCheck.num != 0) {
				toCheck = v;
				foundOne = true;
			}
		}
		if (foundOne == false) {
			return tempVertices;
		}
		System.out.println(toCheck.possibles);
		return check_recursive(checkList, toCheck, tempVertices);*/
		//System.out.println(check.x);
		System.out.println(check.possibles);
		check.num = check.possibles.get(0);
		System.out.println(check.num+" "+check.x);
		tempVertices = fix_possibles(check, tempVertices);
		
		Vertex toCheck = tempVertices.get(0);
		Boolean foundOne = false;
		for (Vertex v : tempVertices) {
			if (((v.num == 0) && (v.possibles.size() < toCheck.possibles.size()) || toCheck.num != 0) && v.possibles.size() != 0) {
				toCheck = v;
				foundOne = true;
				//System.out.println(v.possibles+" "+v.x+" "+v.y);
			}
		}
		//if (foundOne == false) {
		//	return tempVertices;
		//}
		Boolean works = true;
		for (Vertex v : tempVertices) {
			if (v.num == 0 && v.possibles.size() != 0) {
				works = false;
			}
		}
		if (works) {
			return tempVertices;
		}
		System.out.println(toCheck.possibles+" ASDASDSADSA");
		ArrayList<Vertex> finalVertices = check_recursive(toCheck, tempVertices);
		works = true;
		
		for (Vertex v : tempVertices) {
			if (v.num == 0) {
				works = false;
			}
		}
		if (works) {
			return finalVertices;
		}
		else {
			check.possibles.remove(Integer.valueOf(check.num));
			//return_possibles(check,tempVertices);
			check.num = 0;
			System.out.println("A");
			toCheck = tempVertices.get(0);
			foundOne = false;
			for (Vertex v : tempVertices) {
				if (((v.num == 0) && (v.possibles.size() < toCheck.possibles.size()) || toCheck.num != 0) && v.possibles.size() != 0) {
					toCheck = v;
					foundOne = true;
					System.out.println(v.possibles);
				}
			}
			
			if (foundOne == false) {
				return tempVertices;
			}
			return check_recursive(toCheck, tempVertices);
		}
	}
	public void resetPossibles(int testXtd, Vertex v, ArrayList<Vertex> tempVertices) {
		for (int i = 1; i < testXtd; i++) {
			v.possibles.add(i);
			for (Vertex adj : v.connections) {
				if (i == adj.num && adj.num != 0) {
					v.possibles.remove(Integer.valueOf(i));
					break;
				}
				if ((i == 2*adj.num || 2*i == adj.num) && adj.num != 0) {
					v.possibles.remove(Integer.valueOf(i));
					break;
				}
				for (Vertex adj2 : adj.connections) {
					if (adj2 != v) {
						if (i == adj2.num && adj2.num != 0) {
							v.possibles.remove(Integer.valueOf(i));
							break;
						}
						if (adj.num * 2 == i + adj2.num && adj.num != 0 && adj2.num != 0) {
							v.possibles.remove(Integer.valueOf(i));
							break;
						}
						//if (adj)
					}
				}
				if (!v.possibles.contains(i)) {
					break;
				}
			}
		}
			/*for (Vertex adj : v.connections) {
				if (!adj.possibles.contains(v.num)) {
					adj.possibles.add(Integer.valueOf(v.num));
				}
				if (!adj.possibles.contains(2*v.num)) {
					adj.possibles.add(Integer.valueOf(2*v.num));
				}
				if (v.num%2 == 0 && !adj.possibles.contains(v.num/2)) {
					adj.possibles.add(Integer.valueOf(v.num/2));
				}
				for (Vertex adj2 : adj.connections) {
					if (adj2 != v) {
						if (!adj2.possibles.contains(v.num)) {
							adj2.possibles.add(Integer.valueOf(v.num));
						}
						if ((v.num+adj2.num)%2 == 0 && adj2.num != 0 && !adj.possibles.contains((v.num+adj2.num)/2)) {
							adj.possibles.add((v.num+adj2.num)/2);
						}
						if (!adj2.possibles.contains(adj.num*2-v.num) && adj.num != 0) {
							adj2.possibles.add(Integer.valueOf(2*adj.num-v.num));
						}
					}
				}
			}*/
		
	}
	public ArrayList<Vertex> fix_possibles(Vertex placed, ArrayList<Vertex> tempVertices) {
		for (Vertex v : placed.connections) {
			v.possibles.remove(Integer.valueOf(placed.num));
			if (placed.num%2 == 0) {
				v.possibles.remove(Integer.valueOf(placed.num/2));
			}
			v.possibles.remove(Integer.valueOf(placed.num*2));
			for (Vertex i : placed.connections) {
				if (i != v && i.num != 0) {
					v.possibles.remove(Integer.valueOf(2*(placed.num) - i.num));
				}
			}
			for (Vertex v2 : v.connections) {
				if (v2 != placed) {
					v2.possibles.remove(Integer.valueOf(2*v.num - placed.num));
					v2.possibles.remove(Integer.valueOf(placed.num));
					if ((placed.num + v2.num)%2 == 0 && v2.num != 0) {
						v.possibles.remove(Integer.valueOf((placed.num+v2.num)/2));
					}
				}
			}
		}
		
		
		return tempVertices;
	}
	public ArrayList<Vertex> return_possibles(Vertex placed, ArrayList<Vertex> tempVertices, int Xtd){
		for (Vertex v : placed.connections) {
			if (!v.possibles.contains(placed.num)) {
				v.possibles.add(Integer.valueOf(placed.num));
			}
			if (placed.num%2 == 0 && !v.possibles.contains(placed.num/2)) {
				v.possibles.add(Integer.valueOf(placed.num/2));
			}
			if (!v.possibles.contains(placed.num*2) && placed.num*2 <= Xtd) {
				v.possibles.add(Integer.valueOf(placed.num*2));
			}
			for (Vertex i : placed.connections) {
				if (i != v) {
					i.possibles.add(Integer.valueOf(2*placed.num - v.num));
				}
			}
			for (Vertex v2 : v.connections) {
				if (v2 != placed) {
					v2.possibles.add(Integer.valueOf(2*v.num - placed.num));
					v2.possibles.add(Integer.valueOf(placed.num));
					if ((placed.num + v2.num)%2 == 0 && v2.num != 0) {
						v.possibles.add(Integer.valueOf((placed.num+v2.num)/2));
					}
				}
			}
		}
		return tempVertices;
	}
	public void initCycle(int sX, int sY) {
		int tempSize = defaultSize;
		if (sX+defaultSize > displayWidth*13/14) {
			defaultSize = displayWidth*13/14 - sX - 20;
		}
		if (sX-defaultSize < 0) {
			defaultSize = sX - 20;
		}
		if (sY+defaultSize > displayHeight*13/14) {
			defaultSize = displayHeight*13/14 - sY - 20;
		}
		if (sY-defaultSize < 0) {
			defaultSize = sY - 20;
		}
		int sL = vertices.size();
		for (int i = 0; i < lengthOption; i++) {
			vertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/lengthOption)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/lengthOption)*defaultSize)));
			if (i > 0) {
				vertices.get(sL+i-1).connections.add(vertices.get(sL+i));
				vertices.get(sL+i).connections.add(vertices.get(sL+i-1));
			}
		}
		vertices.get(sL+lengthOption-1).connections.add(vertices.get(sL));
		vertices.get(sL).connections.add(vertices.get(sL+lengthOption-1));
		defaultSize = tempSize;
	}
	public void initStar(int sX, int sY) {
		int tempSize = defaultSize;
		if (sX+defaultSize > displayWidth*13/14) {
			defaultSize = displayWidth*13/14 - sX - 20;
		}
		if (sX-defaultSize < 0) {
			defaultSize = sX - 20;
		}
		if (sY+defaultSize > displayHeight*13/14) {
			defaultSize = displayHeight*13/14 - sY - 20;
		}
		if (sY-defaultSize < 0) {
			defaultSize = sY - 20;
		}
		int sL = vertices.size();
		vertices.add(new Vertex(sX,sY));
		for (int i = 0; i < lengthOption; i++) {
			vertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/lengthOption)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/lengthOption)*defaultSize)));
			vertices.get(sL).connections.add(vertices.get(sL+i+1));
			vertices.get(sL+i+1).connections.add(vertices.get(sL));
		}
		defaultSize = tempSize;
	}
	public void initWheel(int sX, int sY) {
		int tempSize = defaultSize;
		if (sX+defaultSize > displayWidth*13/14) {
			defaultSize = displayWidth*13/14 - sX - 20;
		}
		if (sX-defaultSize < 0) {
			defaultSize = sX - 20;
		}
		if (sY+defaultSize > displayHeight*13/14) {
			defaultSize = displayHeight*13/14 - sY - 20;
		}
		if (sY-defaultSize < 0) {
			defaultSize = sY - 20;
		}
		int sL = vertices.size();
		vertices.add(new Vertex(sX,sY));
		for (int i = 0; i < lengthOption; i++) {
			vertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/lengthOption)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/lengthOption)*defaultSize)));
			if (i > 0) {
				vertices.get(sL+i).connections.add(vertices.get(sL+i+1));
				vertices.get(sL+i+1).connections.add(vertices.get(sL+i));
			}
			vertices.get(sL).connections.add(vertices.get(sL+i+1));
			vertices.get(sL+i+1).connections.add(vertices.get(sL));
		}
		vertices.get(sL+lengthOption).connections.add(vertices.get(sL+1));
		vertices.get(sL+1).connections.add(vertices.get(sL+lengthOption));
		defaultSize = tempSize;
	}
	public void initComplete(int sX, int sY) {
		int tempSize = defaultSize;
		if (sX+defaultSize > displayWidth*13/14) {
			defaultSize = displayWidth*13/14 - sX - 20;
		}
		if (sX-defaultSize < 0) {
			defaultSize = sX - 20;
		}
		if (sY+defaultSize > displayHeight*13/14) {
			defaultSize = displayHeight*13/14 - sY - 20;
		}
		if (sY-defaultSize < 0) {
			defaultSize = sY - 20;
		}
		int sL = vertices.size();
		for (int i = 0; i < lengthOption; i++) {
			vertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/lengthOption)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/lengthOption)*defaultSize)));
			
		}
		for (int i = sL; i < sL + lengthOption; i++) {
			for (int z = sL; z < sL + lengthOption; z++) {
				if (z != i) {
					vertices.get(i).connections.add(vertices.get(z));
					vertices.get(z).connections.add(vertices.get(i));
				}
			}
		}
		defaultSize = tempSize;
	}
	public void initPath(int sX, int sY) {
		int tempSize = defaultSize;
		if (sX+defaultSize > displayWidth*13/14) {
			defaultSize = displayWidth*13/14 - sX - 20;
		}
		if (sX-defaultSize < 0) {
			defaultSize = sX - 20;
		}
		int sL = vertices.size();
		for (int i = 0; i < lengthOption; i++) {
			vertices.add(new Vertex((float)(sX-defaultSize+defaultSize*2*i/lengthOption),(float)(sY)));
			if (i > 0) {
				vertices.get(sL+i-1).connections.add(vertices.get(sL+i));
				vertices.get(sL+i).connections.add(vertices.get(sL+i-1));
			}
		}
		defaultSize = tempSize;
	}
	public void initInfiniteSquare() {
		int sL = vertices.size();
		int hori = lengthOption;
		int vert = lengthOption;
		for (int i = 0; i < vert; i++) {
			for (int z = 0; z < hori; z++) {
				vertices.add(new Vertex(150+defaultSize*z,20+defaultSize*i));
				if (z > 0) {
					vertices.get(sL+z+hori*i-1).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*i-1));
				}
				if (i > 0) {
					vertices.get(sL+z+hori*(i-1)).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)));
				}
			}
		}
	}
	public void initInfiniteHex(Boolean shift) {
		int sL = vertices.size();
		int hori = lengthOption;
		int vert = lengthOption;
		for (int i = 0; i < vert; i++) {
			for (int z = 0; z < hori; z++) {
				if (shift) {
					vertices.add(new Vertex(150+defaultSize*(z-(float)(i%2)/2+(int)((z+(i%2))/2)),20+(float)(defaultSize*Math.sqrt(3)/2)*i));
				}
				else {
					vertices.add(new Vertex(150+defaultSize*(z),20+(float)(defaultSize*Math.sqrt(3)/2)*i));
				}
				if (z > 0) {
					if (((z%2)+(i%2))%2 == 1) {
						vertices.get(sL+z+hori*i-1).connections.add(vertices.get(sL+z+hori*i));
						vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*i-1));
					}
				}
				if (i > 0) {
					vertices.get(sL+z+hori*(i-1)).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)));
				}
			}
		}
	}
	public void initInfiniteTri(Boolean shift) {
		int sL = vertices.size();
		int hori = lengthOption;
		int vert = lengthOption;
		for (int i = 0; i < vert; i++) {
			for (int z = 0; z < hori; z++) {
				if (shift) {
					vertices.add(new Vertex(150+defaultSize*(z-(float)(i%2)/2-i/2),20+(float)(defaultSize*Math.sqrt(3)/2)*i));
				}
				else {
					vertices.add(new Vertex(150+defaultSize*(z),20+(float)(defaultSize*Math.sqrt(3)/2)*i));
				}
				if (z > 0) {
					vertices.get(sL+z+hori*i-1).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*i-1));
				}
				if (i > 0 && z > 0) {
					vertices.get(sL+z+hori*(i-1)).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)));
					if (z > 0 && z < hori-1) {
						vertices.get(sL+z+hori*(i-1)-1).connections.add(vertices.get(sL+z+hori*i));
						vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)-1));
					}
					
				}
			}
		}
	}
	public void initBinaryTree() {
		int sL = vertices.size();
		for (int i = 0; i < lengthOption; i++) {
			for (int z = 0; z < Math.pow(2, i); z++) {
				System.out.println(i*defaultSize*Math.sin(z*Math.PI/Math.pow(2, i)));
				
				vertices.add(new Vertex(
						(float)(displayWidth/2 + Math.pow(i,.85)*defaultSize * (float)Math.cos((z+0.5)*Math.PI/Math.pow(2, i))),
						(float)((float)(displayHeight * 9/10) - Math.pow(i,.85)*defaultSize * (float)Math.sin((z+0.5)*Math.PI/Math.pow(2, i)))
						));
				if (i > 0) {
					vertices.get((int)(vertices.size()-1-z-(Math.pow(2, i-1)-Math.ceil(z/2)))).connections.add(vertices.get(vertices.size()-1));
					vertices.get(vertices.size()-1).connections.add(vertices.get((int)(vertices.size()-1-z-(Math.pow(2, i-1)-Math.ceil(z/2)))));

				}
			}
			
		}
	}
	public void initInfiniteOctagram() {
		int sL = vertices.size();
		int hori = lengthOption;
		int vert = lengthOption;
		for (int i = 0; i < vert; i++) {
			for (int z = 0; z < hori; z++) {
				vertices.add(new Vertex(150+defaultSize*(z),20+(float)(defaultSize*Math.sqrt(3)/2)*i));
				if (z > 0) {
					vertices.get(sL+z+hori*i-1).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*i-1));
				}
				if (i > 0 && z > 0) {
					vertices.get(sL+z+hori*(i-1)).connections.add(vertices.get(sL+z+hori*i));
					vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)));
					if (z > 0 && z < hori-1) {
						vertices.get(sL+z+hori*(i-1)-1).connections.add(vertices.get(sL+z+hori*i));
						vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)-1));
						vertices.get(sL+z+hori*(i-1)+1).connections.add(vertices.get(sL+z+hori*i));
						vertices.get(sL+z+hori*i).connections.add(vertices.get(sL+z+hori*(i-1)+1));
					}
					
				}
			}
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
	public ArrayList<Vertex> allBadVertices(ArrayList<Vertex> ver) {
		ArrayList<Vertex> bads = new ArrayList<Vertex>();
		for (Vertex v : ver) {
			for (Vertex i : checkSames(v)) {
				if (!bads.contains(i)) {
					bads.add(i);
				}
			}
			for (Vertex i : checkDoubles(v)) {
				if (!bads.contains(i)) {
					bads.add(i);
				}
			}
			for (Vertex i : checkSandies(v)) {
				if (!bads.contains(i)) {
					bads.add(i);
				}
			}
			for (Vertex i : checkStairs(v)) {
				if (!bads.contains(i)) {
					bads.add(i);
				}
			}
		}
		return bads;
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
	public ArrayList<Vertex> checkSames(Vertex v) {
		ArrayList<Vertex> sames = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			if (i.num == v.num && i.num != 0 && v.num != 0) {
				sames.add(i);
				
			}
		}
		return sames;
	}
	public ArrayList<Vertex> checkDoubles(Vertex v) {
		ArrayList<Vertex> doubles = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			if ((i.num == 2*v.num || 2*i.num == v.num) && i.num != 0 && v.num != 0) {
				doubles.add(i);
				
			}
		}
		return doubles;
	}
	public ArrayList<Vertex> checkSandies(Vertex v) {
		ArrayList<Vertex> sandies = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			for (Vertex c : i.connections) {
				if (c != v && c.num == v.num && v.num != 0 && c.num != 0) {
					sandies.add(c);
					if (!sandies.contains(i)) {
						sandies.add(i);
					}
					
				}
			}
		}
		return sandies;
	}
	public ArrayList<Vertex> checkStairs(Vertex v) {
		ArrayList<Vertex> stairs = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			for (Vertex c : i.connections) {
				if (c != v && c.num + v.num == 2*i.num && i.num != 0 && v.num != 0 && c.num != 0) {
					stairs.add(c);
					if (!stairs.contains(i)) {
						stairs.add(i);
					}
					
				}
			}
		}
		return stairs;
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
	public Boolean graphConnected(ArrayList<Vertex> ver) {
		for (Vertex v : ver) {
			if (v.connections.size() == 0) {
				return false;
			}
		}
		return true;
	}
	public int graphSaturable(ArrayList<Vertex> ver) {
		//complete search try
		//find all solutions
		//if all are saturated, return 2
		//if any are saturated, return 1
		//if none are saturated, return 0
		
		//start with ver(0), place, go to ver(1), until you get to ver(len-1), then keep trying similar to minwsr func
		//System.out.println("Ad");
		
		//MAYBE ADD A FUNCTION TO CHECK IF XTD = ORDER
		
		Boolean foundOne = false;
		Boolean foundNonSat = false;
		for (Vertex v : ver) {
			v.num = 1;
		}
		int indexCheck = 0;
		while (true) {
			if (ver.get(indexCheck).num < ver.size()) {
				ver.get(indexCheck).num ++;
				//System.out.println(ver.get(indexCheck).num);
				if (allBadVertices(ver).size() == 0) {
					//Boolean a = checkSat(ver);
					//System.out.println("gotone");
					if (checkSat(ver)) {
						foundOne = true;
					}
					else {
						foundNonSat = true;
					}
				}
				
			}
			else {
				ver.get(indexCheck).num = 1;
				indexCheck++;
				continue;
			}
			indexCheck = 0;
			
			for (Vertex v : ver) {
				if (v.num < ver.size()) {
					indexCheck = 1;
					break;
				}
			}
			if (indexCheck == 1) {
				indexCheck = 0;
				continue;
			}
			break;
		}
		if (!foundNonSat) {
			return 2;
		}
		if (foundOne) {
			return 1;
		}
		System.out.println("Adadadad");
		return 0;
	}
	public Boolean checkSat(ArrayList<Vertex> ver) {
		for (int i = 1; i <= ver.size(); i++) {
			Boolean foundOne = false;
			for (Vertex v : ver) {
				
				if (v.num == i) {
					
					foundOne = true;
				}
			}
			if (!foundOne) {
				
				return false;
			}
		}
		return true;
	}
	public void saveVerticesToFile(String filename, Boolean saveXtd) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		pw.print(vertices.size()+"\n");
		for (Vertex v : vertices) {
			//x
			//y
			//connections (indices)
			//number
			ArrayList<Integer> connect = new ArrayList<Integer>();
			for (int i = 0; i < vertices.size(); i++) {
				if (v.connections.contains(vertices.get(i))) {
					connect.add(i);
				}
			}
			String connectString = "";
			for (Integer i : connect) {
				connectString += i+" ";
			}
			pw.print(v.x+"!"+v.y+"!"+connectString+"!"+v.num);
			pw.print("\n");
		}
		if (saveXtd) {
			pw.print(testXtd);
		}
		pw.close();
	}
	public ArrayList<Vertex> loadVerticesFromFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		int lines = 0;
		lines = Integer.parseInt(br.readLine());
		ArrayList<ArrayList<Integer>> toConnect = new ArrayList<ArrayList<Integer>>();
		ArrayList<Vertex> tempVertices = new ArrayList<Vertex>();
		for (int i = 0; i < lines; i++) {
			String line = br.readLine();
			String[] data = line.split("!");
			//add them
			tempVertices.add(new Vertex(Float.parseFloat(data[0]),Float.parseFloat(data[1])));
			tempVertices.get(tempVertices.size()-1).num = Integer.parseInt(data[3]);
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for (String z : data[2].split(" ")) {
				toAdd.add(Integer.parseInt(z));
			}
			toConnect.add(toAdd);
			//then connect them later
		}
		for (int i = 0; i < toConnect.size(); i++) {
			for (int z = 0; z < toConnect.get(i).size(); z++) {
				if (!tempVertices.get(i).connections.contains(tempVertices.get(toConnect.get(i).get(z)))) {
					tempVertices.get(i).connections.add(tempVertices.get(toConnect.get(i).get(z)));
					tempVertices.get(toConnect.get(i).get(z)).connections.add(tempVertices.get(i));
				}
			}
		}
		return tempVertices;
	}
}
