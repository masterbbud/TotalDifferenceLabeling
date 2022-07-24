package main;

public class ThreadData {
    
    public CheckXtdThread thread;
    public String name; // should be unique
    public int n;
    public int e;
    public int currXtd = 0;
    public Boolean done = false;
    public Boolean failed = false;

    public ThreadData(String name, int n, int e) {
        this.name = name;
        this.n = n;
        this.e = e;
    }
}
