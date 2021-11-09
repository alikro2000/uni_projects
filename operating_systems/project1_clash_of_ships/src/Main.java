import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("How many countries are there around the lake? ");
        int numberOfCountries = Integer.parseInt(sc.nextLine());
        System.out.print("Please select the game mode: 1.Present 2.Future(Ships take 2 bullets to die): ");
        int timeLineChoice = Integer.parseInt(sc.nextLine());
        System.out.println();

        Game newGame = new Game(numberOfCountries,
                timeLineChoice == 1 ? Game.GameType.PRESENT : Game.GameType.FUTURE);
        newGame.initializeGame();
        newGame.startGame();
    }
}
