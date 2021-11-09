public class Car extends Thread {
    private String carName;
    private Carwash carwash;
    private Message messageTerminal;
    private CarState carState;

    enum CarState {BORN, IN_LINE, ENTERED, BOOTH_A, BOOTH_B, BOOTH_C, DONE}

    public Car(String carName, Carwash carwash) {
        super(carName);
        this.carName = carName;
        this.carwash = carwash;
        this.messageTerminal = new Message(carName);
        this.carState = CarState.BORN;
    }

    @Override
    public void run() {
        //First the car arrives at the carwash and waits for its turn.
        this.carState = CarState.IN_LINE;
        this.messageTerminal.waitingForEnterCarWash();

        try {
            // Wait until its this car's turn to enter the carwash.
            carwash.capacity.acquire();
            messageTerminal.enteredCarWash();
            carState = CarState.ENTERED;

            // in between booths A & B, queue this car in the one with lighter traffic.
            String boothName = carwash.boothA.getQueueLength() <= carwash.boothB.getQueueLength() ? "A" : "B";
            if (boothName.equals("A")) {
                carwash.boothA.acquire();
                carState = CarState.BOOTH_A;
                messageTerminal.enterBoothA();
            } else { // boothName is B
                carwash.boothB.acquire();
                messageTerminal.enterBoothB();
                carState = CarState.BOOTH_B;
            }

            // Wash the car in the chosen booth & exit afterwards.
            Thread.sleep(carwash.getWashingSpan());
            if (boothName.equals("A")) {
                messageTerminal.exitBoothA();
                carwash.boothA.release();
            } else { // boothName is B
                messageTerminal.exitBoothB();
                carwash.boothB.release();
            }

            // Wait for the drying booth to empty & enter it.
            carwash.boothC.acquire();
            messageTerminal.enterBoothC();
            carState = CarState.BOOTH_C;

            // Wait for the worker to finish resting.
            if (carwash.worker != null)
                carwash.worker.join();

            // Dry the car & exit booth C afterwards.
            Thread.sleep(carwash.getDryingSpan());
            messageTerminal.exitBoothC();
            carwash.boothC.release();

            // The worker sleeps afterwards.
            if (carwash.worker != null) {
                messageTerminal.sleep();
                carwash.worker.run();
            }

            // Exit the carwash.
            messageTerminal.exitCarWash();
            carwash.capacity.release();
            carState = CarState.DONE;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
