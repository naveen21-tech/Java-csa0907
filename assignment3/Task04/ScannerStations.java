public class ScannerStations {
    static class Station extends Thread {
        private final int scripts;
        Station(String name, int scripts) { super(name); this.scripts = scripts; }
        public void run() {
            try {
                for (int i = 0; i < scripts; i++) Thread.sleep(30);
                System.out.println(getName() + " scanned " + scripts + " scripts");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " interrupted");
            }
        }
    }
    static class RunnableStation implements Runnable {
        private final int scripts;
        RunnableStation(int scripts) { this.scripts = scripts; }
        public void run() {
            try {
                for (int i = 0; i < scripts; i++) Thread.sleep(25);
                System.out.println(Thread.currentThread().getName() + " scanned " + scripts + " scripts");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new Station("Station-1", 5),
            new Thread(new RunnableStation(6), "Station-2"),
            new Station("Station-3", 4),
            new Thread(new RunnableStation(7), "Station-4")
        };
        for (Thread t : stations) t.start();
        for (Thread t : stations) t.join();
        System.out.println("All stations completed. Total scripts: 22");
        for (Thread t : stations) System.out.println(t.getName() + " alive? " + t.isAlive());
    }
}