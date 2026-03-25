package game;

import models.Board;
import models.CoolBoard;
import java.util.Scanner;

public class GameManager {
    private CoolBoard board;

    public GameManager() {
        this.board = new CoolBoard(8,8);
        
    }

    public String runGame() {
        Scanner scan = new Scanner(System.in);
        board.showBoard(); // Assuming Board has a print/show method
        System.out.println("GameManager: Ready for White's move.");
        System.out.print("GameManager: Enter move: ");
        String status = board.movePiece(scan.nextLine());
        scan.close();
        if (status.equalsIgnoreCase("EXIT")) {
            return "EXIT";
        }
        else {
            System.out.println("GameManager: " + status);
        }
        return "continue";

    }
}