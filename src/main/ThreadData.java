package main;

public class ThreadData {
    
    public DefaultThread thread;
    public String name;
    public String type;
    public int n;
    public int e;
    public int currXtd = 0;
    public int currComplete = 0;
    public int currFound = 0;
    public Boolean done = false;
    public Boolean failed = false;

    public ThreadData(String name, int n, int e, int xtd) {
        this.name = name;
        this.type = "XTD (ONE)";
        this.currXtd = xtd;
        this.n = n;
        this.e = e;
    }

    public ThreadData(String name, int n, int e) {
        this.name = name;
        this.type = "XTD (ANY)";
        this.n = n;
        this.e = e;
    }

    public ThreadData(String name) {
        this.name = name;
        this.type = "SET";
        this.n = 0;
        this.e = 0;
    }

    public String getString() {
        if (type == "XTD (ANY)") {
            return name + ": XTD (ANY) n " + buffer(n, 3) + " e " + buffer(e, 3) + " xtd " + buffer(currXtd,2);
        }
        if (type == "XTD (ONE)") {
            return name + ": XTD (ONE) n " + buffer(n, 3) + " e " + buffer(e, 3) + " xtd " + buffer(currXtd,2);
        }
        if (type == "SET") {
            return name + ": SET done " + buffer(currComplete, 6) + " found " + buffer(currFound, 6);
        }
        return "";
    }

    private String buffer(int toBuffer, int amt) {
        String f = "";
        while (f.length() < amt - Integer.toString(toBuffer).length()) {
            f += " ";
        }
        return f+toBuffer;
    }
    private String bufferRight(int toBuffer, int amt) {
        String f = "";
        while (f.length() < amt - Integer.toString(toBuffer).length()) {
            f += " ";
        }
        return toBuffer+f;
    }
}
