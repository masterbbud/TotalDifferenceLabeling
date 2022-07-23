package main;

import java.util.ArrayList;

public class Vertex {

	public float x;
	public float y;
	public int num = 0;
	public ArrayList<Vertex> connections = new ArrayList<Vertex>();
	public ArrayList<Integer> possibles = new ArrayList<Integer>();
	public ArrayList<Integer> notPossible = new ArrayList<Integer>();
	public Boolean isBad = false;
	
	public Vertex(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
}
