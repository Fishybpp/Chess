package Programming.SER120.Chess.game;

import Programming.SER120.Chess.models.Board;
import Programming.SER120.Chess.models.CoolBoard;

public class GameManager {
    private CoolBoard board;

    public GameManager() {
        this.board = new CoolBoard(8,8);
    }

    public void runGame() {
        System.out.println("GameManager: Initializing engine...");
        board.showBoard(); // Assuming Board has a print/show method
        System.out.println("GameManager: Ready for White's move.");
    }
}