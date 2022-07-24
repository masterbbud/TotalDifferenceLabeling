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

public class XTDCheck {

    public static ArrayList<Vertex> check(int testXtd, ArrayList<Vertex> vertices, DefaultThread thread) {
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

            if (thread.stopASAP) {
                return new ArrayList<Vertex>();
            }

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
				return vertices;
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
				vertices = fixedList(checkedlist, vertices, testXtd);
				toCheck.possibles.remove(Integer.valueOf(oldCheckNum));
				toCheck.notPossible.add(oldCheckNum);
				checkedlist.remove(checkedlist.size()-1);
				while (toCheck.possibles.size() == 0) {
					if (checkedlist.size() > 0) {
						
						toCheck = checkedlist.get(checkedlist.size()-1);
						oldCheckNum = toCheck.num;
						toCheck.num = 0;
						vertices = fixedList(checkedlist, vertices, testXtd);
						toCheck.possibles.remove(Integer.valueOf(oldCheckNum));
						toCheck.notPossible.add(oldCheckNum);
						
						checkedlist.remove(checkedlist.size()-1);
					}
					else {
						System.out.println("NO"+testXtd);
						//this.vertices = saveVertices;
						return null;
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
	public static ArrayList<Vertex> fixedList(ArrayList<Vertex> toCheck, ArrayList<Vertex> vertices ,int testXtd){
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
	public static ArrayList<Vertex> check_recursive(Vertex check, ArrayList<Vertex> tempVertices) {
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
	public static void resetPossibles(int testXtd, Vertex v, ArrayList<Vertex> tempVertices) {
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
	public static ArrayList<Vertex> fix_possibles(Vertex placed, ArrayList<Vertex> tempVertices) {
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
	public static ArrayList<Vertex> return_possibles(Vertex placed, ArrayList<Vertex> tempVertices, int Xtd){
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

    public static ArrayList<ArrayList<Vertex>> lookForSupersat(int start, int end, float sX, float sY, int defaultSize, CheckSetThread thread) {

        // What we want to do:
        // Get all graphs for our start and end
        // Check by graph

        

		//order = 5;

        ArrayList<ArrayList<Vertex>> all = DefaultGraphs.getAllGraphsForRange(start, end, sX, sY, defaultSize);
        System.out.println("N Graphs :  "+all.size());
        ArrayList<ArrayList<Vertex>> superSats = new ArrayList<ArrayList<Vertex>>();

        for (ArrayList<Vertex> graph : all) {
            System.out.println("RUNNING FOR GRAPH SIZE "+graph.size());
            int testXtd = 1;
            ArrayList<Vertex> oneTest = check(testXtd, (ArrayList<Vertex>)graph.clone(), thread);
            while (oneTest == null) {
                testXtd++;
                oneTest = check(testXtd, (ArrayList<Vertex>)graph.clone(), thread);
            }
            graph = oneTest;
            //System.out.println(i+" "+nowPerm+" "+perms);
            
            if (testXtd == graph.size() && Checks.graphConnected(graph)) {
                //System.out.println("orderstf"+testXtd+" "+order);
                ArrayList<Vertex> neww = graph;
                superSats.add(copy(neww));
            }
        }
        ArrayList<ArrayList<Vertex>> alltwo = new ArrayList<ArrayList<Vertex>>();
		for (ArrayList<Vertex> a : superSats) {
			if (Checks.graphSaturable(a) == 2) {
				alltwo.add(a);
				//System.out.println("added");
			}
		}
		return alltwo;
		/*
		while (order <= end) {
			ArrayList<Vertex> tempVertices = new ArrayList<Vertex>();
			for (int i = 0; i < order; i++) {
				tempVertices.add(new Vertex((float)(sX+Math.cos(i*2*Math.PI/order)*defaultSize),(float)(sY+Math.sin(i*2*Math.PI/order)*defaultSize)));
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
						tempVertices.get(allEdges.get(c)[0]).connections.add(tempVertices.get(allEdges.get(c)[1]));
						tempVertices.get(allEdges.get(c)[1]).connections.add(tempVertices.get(allEdges.get(c)[0]));
					}
				}
				//check(testXtd, tempVertices);
				//if (true) {
				//	return null;
				//}
				testXtd = 1;
				ArrayList<Vertex> oneTest = XTDCheck.check(testXtd, (ArrayList<Vertex>)tempVertices.clone());
					while (oneTest == null) {
						testXtd++;
						oneTest = XTDCheck.check(testXtd, (ArrayList<Vertex>)tempVertices.clone());
					}
					tempVertices = oneTest;
				//System.out.println(i+" "+nowPerm+" "+perms);
				
				if (testXtd == order && Checks.graphConnected(tempVertices)) {
					//System.out.println("orderstf"+testXtd+" "+order);
					ArrayList<Vertex> neww = tempVertices;
					all.add(copy(neww));
					for (Vertex v : tempVertices) {
						v.connections = new ArrayList<Vertex>();
						v.num = 0;
					}
				}
				else {
					//reset + continue
					for (Vertex v : tempVertices) {
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
			if (Checks.graphSaturable(a) == 2) {
				alltwo.add(a);
				//System.out.println("added");
			}
		}
		return alltwo;*/
	}

    public static ArrayList<Vertex> copy(ArrayList<Vertex> ver){
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
