import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("How many cars would you like to have in the simulation: ");
        int carsCount = Integer.parseInt(sc.nextLine());

        System.out.print("Is booth C (drying booth), 1. Automated \t 2.Has a worker : ");
        boolean isCAutomated = Integer.parseInt(sc.nextLine()) == 1;

        Car[] cars = new Car[carsCount];
        Carwash carwash = new Carwash(3, 1, 1, 1, isCAutomated);

        for (int i = 0; i < carsCount; ++i) {
            cars[i] = new Car(String.format("Car %d", i), carwash);
            cars[i].start();
        }

        try {
            for (Car c : cars) {
                c.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("DONE.");
    }
}
