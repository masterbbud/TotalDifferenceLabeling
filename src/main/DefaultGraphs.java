package main;

import java.util.ArrayList;

public class DefaultGraphs {
    public static ArrayList<Vertex> initCycle(int sX, int sY, int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initStar(int sX, int sY, int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initWheel(int sX, int sY, int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initComplete(int sX, int sY, int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initPath(int sX, int sY, int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initInfiniteSquare(int defaultSize, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initInfiniteHex(Boolean shift, int defaultSize, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initInfiniteTri(Boolean shift, int defaultSize, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initBinaryTree(int defaultSize, int displayWidth, int displayHeight, int lengthOption, ArrayList<Vertex> vertices) {
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
        return vertices;
	}
	public static ArrayList<Vertex> initInfiniteOctagram(Boolean shift, int defaultSize, int lengthOption, ArrayList<Vertex> vertices) {
        // Is this used?
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
        return vertices;
	}
}
