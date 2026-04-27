package models;
import models.Piece.Type;
import models.Piece.Team;  

public class CoolBoard extends Board {

    // ANSI Escape Codes for Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BG = "\u001B[48;5;22m";
    public static final String BLACK_BG = "\u001B[40m";
    public static final String WHITE_TEXT = "\u001B[37m";
    public static final String YELLOW_TEXT = "\u001B[33m";

    public CoolBoard(int rows, int cols) {
        super(rows, cols); // Call the original Board constructor
    }

    public Piece piece1 = new Piece(Type.PAWN, "A1", Team.WHITE);

    @Override
    public void showBoard() {
        System.out.println(YELLOW_TEXT + "\n  ========= CHESS BOARD ==========" + RESET);
        
        for (int i = 7; i >= 0; i--) { // Using 8 for standard chess
            for (int j = 0; j < 8; j++) {
                // Alternating background colors like a real chessboard
                String background = ((i + j) % 2 == 0) ? GREEN_BG : BLACK_BG;

                String piece = "--";
                if ((piece1.getRowIndex() == j) && (piece1.getColIndex() == i)) {
                    piece1.toString();
                }
                
                
                // Print the square with padding to make it look "chunky"
                if (j == 0) {
                  System.out.print(YELLOW_TEXT + (i + 1) + WHITE_TEXT + " " + background + " " + piece + " " + RESET);  
                }
                else {
                    System.out.print(background + " " + piece + " " + RESET);
                }
            }
            System.out.println(); // New line after each row
        }
        System.out.println(YELLOW_TEXT + "  ================================" + RESET);
        System.out.println(YELLOW_TEXT + "  A    B    C   D   E   F   G   H" + RESET);
    }
    
    public String movePiece(String position){
         if (position.equalsIgnoreCase("exit")) {
            return "exit";
        }
        try {
            piece1.movePiece(position.toUpperCase());
            return "Piece moved to position.toUpperCase()";
        }
        catch(IllegalArgumentException e) {
            return e.getMessage();
        }
    }
    
}
