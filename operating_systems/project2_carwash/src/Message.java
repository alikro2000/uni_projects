
public class Message {

    private String nameThread;

    public Message(String nameThread) {
        this.nameThread = nameThread;
    }

    public void waitingForEnterCarWash() {
        System.out.println(nameThread + " Waiting For Enter The CarWash ");
    }

    public void enteredCarWash() {
        System.out.println(nameThread + " Enetered CarWash");
    }

    public void exitCarWash() {
        System.out.println(nameThread + " Exit CarWash");
    }

    public void enterBoothA() {
        System.out.println(nameThread + " Enter Booth A");
    }

    public void exitBoothA() {
        System.out.println(nameThread + " Exit Booth A");
    }

    public void enterBoothB() {
        System.out.println(nameThread + " Enter Booth B");
    }

    public void exitBoothB() {
        System.out.println(nameThread + " Exit Booth B");
    }

    public void enterBoothC() {
        System.out.println(nameThread + " Enter Booth C");
    }

    public void exitBoothC() {
        System.out.println(nameThread + " Exit Booth C");
    }

    public void sleep() {
        System.out.println("Worker is Sleeping");
    }
}
