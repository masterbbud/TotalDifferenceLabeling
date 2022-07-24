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

public class Checks {

    public static Boolean graphConnected(ArrayList<Vertex> ver) {
		for (Vertex v : ver) {
			if (v.connections.size() == 0) {
				return false;
			}
		}
		return true;
	}

	public static int graphSaturable(ArrayList<Vertex> ver) {
		//complete search try
		//find all solutions
		//if all are saturated, return 2
		//if any are saturated, return 1
		//if none are saturated, return 0
		
		//start with ver(0), place, go to ver(1), until you get to ver(len-1), then keep trying similar to minwsr func
		
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

	public static Boolean checkSat(ArrayList<Vertex> ver) {
		// returns if the input graph is saturated
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

    public static ArrayList<Vertex> allBadVertices(ArrayList<Vertex> ver) {
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
	
	public static ArrayList<Vertex> checkSames(Vertex v) {
		ArrayList<Vertex> sames = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			if (i.num == v.num && i.num != 0 && v.num != 0) {
				sames.add(i);
				
			}
		}
		return sames;
	}

	public static ArrayList<Vertex> checkDoubles(Vertex v) {
		ArrayList<Vertex> doubles = new ArrayList<Vertex>();
		for (Vertex i : v.connections) {
			if ((i.num == 2*v.num || 2*i.num == v.num) && i.num != 0 && v.num != 0) {
				doubles.add(i);
				
			}
		}
		return doubles;
	}

	public static ArrayList<Vertex> checkSandies(Vertex v) {
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

	public static ArrayList<Vertex> checkStairs(Vertex v) {
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
    
    public static int countEdges(ArrayList<Vertex> vertices) {
        int count = 0;
        for (Vertex v : vertices) {
            count += v.connections.size();
        }
        return (int) count/2;
    }
}
