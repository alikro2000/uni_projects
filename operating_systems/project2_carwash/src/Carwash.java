import java.util.concurrent.Semaphore;

public class Carwash {
    public final Semaphore capacity;
    public final Semaphore boothA;
    public final Semaphore boothB;
    public final Semaphore boothC;
    public final Worker worker;

    private long washingSpan;
    private long dryingSpan;

    public Carwash(int carwashCapacity, int boothACapacity, int boothBCapacity, int boothCCapacity, boolean isCAutomated) {
        this.capacity = new Semaphore(carwashCapacity, true);
        this.boothA = new Semaphore(boothACapacity, true);
        this.boothB = new Semaphore(boothBCapacity, true);
        this.boothC = new Semaphore(boothCCapacity, true);
        this.worker = isCAutomated ? null : new Worker(this);
        this.washingSpan = 100;
        this.dryingSpan = 300;
    }

    public long getWashingSpan() {
        return washingSpan;
    }

    public long getDryingSpan() {
        return dryingSpan;
    }
}
