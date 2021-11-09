import Utils.NameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The game class is responsible for processing the game status & displaying the results to output (terminal).
 * Tasks like assigning turns to ships and fetching targets for attacking ships are managed here.
 */
public class Game {
    private final int numberOfCountries;
    private final List<Ship> allShips;
    private int shipTurn;

    enum GameType {PRESENT, FUTURE}

    private GameType gameType;

    public Game(int numberOfCountries) {
        this.numberOfCountries = numberOfCountries;
        this.allShips = new ArrayList<>();
        this.shipTurn = 0;
        gameType = GameType.PRESENT;
    }

    public Game(int numberOfCountries, GameType gameType) {
        this.numberOfCountries = numberOfCountries;
        this.allShips = new ArrayList<>();
        this.shipTurn = 0;
        this.gameType = gameType;
    }

    /**
     * Prepares the game to be started.
     */
    public void initializeGame() {
        System.out.println("Initializing the game.");

        int startingHealth = 1;
        int bulletDamage = 1;
        if (gameType == GameType.FUTURE) {
            startingHealth = 2;
        }

        for (int i = 0; i < this.numberOfCountries; ++i) {
            Ship ship = new Ship(i, NameUtils.getNextName(), startingHealth, bulletDamage, this);
            allShips.add(ship);
        }
    }

    /**
     * Starts the game (war) & processes it until it ends.
     */
    public void startGame() {
        //Start the ships.
        System.out.println("START WAR");
        for (Ship s : allShips) {
            s.start();
        }

        //Wait until the game ends.
        while (!gameHasEnded())
            updateShipTurn();

        System.out.println("END WAR");

        //Announce the winner
        Ship winner = allShips.stream()
                .filter(Ship::isShipAlive)
                .findAny()
                .orElse(null);
        if (winner != null)
            System.out.printf("Winner: ship [%s]", winner.getShipName());
        else
            System.out.println("No winner! Everyone died.");
    }

    /**
     * The war ends only if the number of ships alive is one or zero.
     *
     * @return true if the game has ended
     */
    public boolean gameHasEnded() {
        return allShips.stream().filter(Ship::isShipAlive).count() <= 1;
    }

    /**
     * Finds a living ship as a target for an attacking ship. Returns null if no match is found.
     *
     * @param attacker The attacking ship. The target ship is not the same as the attacker ship.
     * @return a living ship not equal to the attacker, or null if no ship can be found.
     */
    public Ship getTargetShipForAttacker(Ship attacker) {
        Random r = new Random();
        List<Ship> chooseShips = allShips.stream()
                .filter(s -> s.isShipAlive() && !s.equals(attacker))
                .collect(Collectors.toList());
        return chooseShips.stream()
                .skip(r.nextInt(chooseShips.size()))
                .findAny()
                .orElse(null);
    }

    /**
     * Updates the ship turn so that every ship can make a move.
     */
    private void updateShipTurn() {
        //If the current ship is not shooting or is dead, then we should give the turn to the next ship.
        Ship s = allShips.get(shipTurn);
        if (s.getShipState() != Ship.ShipState.SHOOTING || !s.isShipAlive()) {
            shipTurn = (shipTurn + 1) % allShips.size();
        }

    }

    public GameType getGameType() {
        return this.gameType;
    }

    /**
     * Returns the index of the ship which has the turn.
     *
     * @return An integer for index between 0 and the total number of ships in the game (dead or alive).
     */
    public int getShipTurn() {
        return shipTurn;
    }
}
