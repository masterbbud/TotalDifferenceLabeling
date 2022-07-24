package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SaveManager {

    public static void saveVerticesToFile(String filename, ArrayList<Vertex> vertices, int testXtd){
		try {
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
			if (testXtd != 0) {
				pw.print(testXtd);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Saving vertices to file failed. See stack trace above");
		}
	}

    public static void saveVerticesToFile(String filename, ArrayList<Vertex> vertices){
        saveVerticesToFile(filename, vertices, 0);
    }

	public static ArrayList<Vertex> loadVerticesFromFile(String filename){
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Loading vertices from file failed. See stack trace above");
		}
		return new ArrayList<Vertex>();
	}
}
