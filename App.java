package Programming.SER120.Chess;

import Programming.SER120.Chess.game.GameManager;

public class App {

    public static void main(String[] args) {
        System.out.println("--- Starting SER120 Chess App ---");
        
        GameManager gm = new GameManager();
        System.out.println("GameManager: Initializing engine...");
        String exit = "Nah";
        while(!exit.equalsIgnoreCase("EXIT")) {
            exit = gm.runGame();
        }
        System.out.println("--- Session Ended ---");
    }
}