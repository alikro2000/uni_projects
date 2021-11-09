public class Worker extends Thread {
    private Carwash carwash;

    public Worker (Carwash carwash) {
        this.carwash = carwash;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
