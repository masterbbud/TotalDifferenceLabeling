package main;

public class DefaultThread {
    public Boolean stopASAP = false;
    public void stop() {
        System.out.println("TOLD TO STOP");
        stopASAP = true;
    }
}
