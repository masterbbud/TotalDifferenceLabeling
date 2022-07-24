package main;

import java.util.ArrayList;

public class CheckSetThread extends DefaultThread implements Runnable{
    
    private ThreadData td;
    private int start;
    private int end;
    private float sX;
    private float sY;
    private int defaultSize;
    private Launcher launcher;

    public CheckSetThread(ThreadData td, int start, int end, float sX, float sY, int defaultSize, Launcher launcher) {
        this.td = td;
        this.start = start;
        this.end = end;
        this.sX = sX;
        this.sY = sY;
        this.defaultSize = defaultSize;
        this.launcher = launcher;
    }
    public void run() {
        launcher.setThreadDone(checkSet(), td);
    }
    public ArrayList<ArrayList<Vertex>> checkSet() {
        return XTDCheck.lookForSupersat(start, end, sX, sY, defaultSize, this);
    }
}
