package Programming.SER120.Chess;

import Programming.SER120.Chess.game.GameManager;

public class App {

    public void scream(){
        //
    }

    public static void main(String[] args) {
        System.out.println("--- Starting SER120 Chess App ---");
        
        //App goofy = new App();
        //App goober = new App();
        //goofy.scream();
        //goober.scream();

        //App.scream();




        //goober.scream();
        GameManager gm = new GameManager();
        gm.runGame();
        
        System.out.println("--- Session Ended ---");
    }
}