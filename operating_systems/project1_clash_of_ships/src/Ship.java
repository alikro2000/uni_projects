import java.util.Random;

/**
 * The ship class, other than being a map of the ship objects, is also responsible for handling its thread.
 * Note that every ship is responsible for its own thread and no other ship other than itself can stop it.
 */
public class Ship extends Thread {
    private int id;
    private String name;
    private int health;
    private int bulletDamage;
    private Game parentGame;
    private boolean isShooting;

    enum ShipState {IDLE, RELOADING, WAITING, SHOOTING}

    private ShipState shipState;

    public Ship(int id, String name, Game parentGame) {
        this.id = id;
        this.name = name;
        this.parentGame = parentGame;
        this.health = 1;
        this.bulletDamage = 1;
        this.isShooting = false;
        this.shipState = ShipState.IDLE;
    }

    public Ship(int id, String name, int health, int bulletDamage, Game parentGame) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.bulletDamage = bulletDamage;
        this.parentGame = parentGame;
        this.isShooting = false;
        this.shipState = ShipState.IDLE;
    }

    public String getShipName() {
        return name;
    }

    public boolean isShipAlive() {
        return this.health > 0;
    }

    public ShipState getShipState() {
        return this.shipState;
    }

    /**
     * Decreases an amount from Ship's health & Kills the thread if the ship dies.
     *
     * @param damageAmount the damage to decrease from health.
     */
    public void takeDamage(int damageAmount) {
        this.health = Math.max(0, this.health - damageAmount);
        if (!isShipAlive()) this.stop();
    }

    /**
     * Shoots at a target ship & damages it by the bulletDamage value.
     *
     * @param target target to shoot at.
     */
    public void shootAt(Ship target) {
        target.takeDamage(this.bulletDamage);
        System.out.printf("Ship [%s] fired at the ship [%s].\n", this.name, target.getShipName());
        if (!target.isShipAlive() && this.parentGame.getGameType() == Game.GameType.FUTURE) {
            System.out.printf("Ship [%s] was drowned by ship [%s].\n", target.getShipName(), this.name);
        }
    }

    /**
     * Two ships are equal if they have the same name.
     */
    public boolean equals(Ship other) {
        return this.name.equals(other.name);
    }

    @Override
    public void run() {
        //While the ship is alive and the game hasn't ended, the ship must continue reloading & attacking.
        while (this.isShipAlive() && !parentGame.gameHasEnded()) {
            //Reload
            this.shipState = ShipState.RELOADING;
            int reloadTime = new Random().nextInt(3) + 1;//seconds
            //int reloadTime = RandomUtils.getRandInRange(1, 3); //seconds
            System.out.printf("Ship [%s] takes [%d seconds] to prepare.\n", this.name, reloadTime);
            try {
                sleep(reloadTime * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Wait until its your turn
            this.shipState = ShipState.WAITING;
            while (id != parentGame.getShipTurn()) ;

            this.shipState = ShipState.SHOOTING;
            //Attack only if the ship survived throughout the reload time.
            if (this.isShipAlive()) {
                Ship target = parentGame.getTargetShipForAttacker(this);
                if (target != null)
                    shootAt(target);
            }
            this.shipState = ShipState.IDLE;
        }
    }
}
