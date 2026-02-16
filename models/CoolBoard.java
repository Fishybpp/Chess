package Programming.SER120.Chess.models;
import Programming.SER120.Chess.models.Piece;

public class CoolBoard extends Board {

    // ANSI Escape Codes for Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BG = "\u001B[48;5;22m";
    public static final String BLACK_BG = "\u001B[40m";
    public static final String WHITE_TEXT = "\u001B[37m";
    public static final String YELLOW_TEXT = "\u001B[33m";
    private Piece piece1 = new Piece();

    public CoolBoard(int rows, int cols) {
        super(rows, cols); // Call the original Board constructor
    }

    private String displayPiece(){
       return switch(piece1.getType()) {
            case "Pawn" -> "P ";
            case "King" -> "K ";
            case "Knight" -> "N ";
            case "Queen" -> "Q ";
            default -> "K";
       };
    }

    @Override
    public void showBoard() {
        System.out.println(YELLOW_TEXT + "\n===   CHESS ADVENTURE BOARD  ===" + RESET);
        
        for (int i = 7; i >= 0; i--) { // Using 8 for standard chess
            for (int j = 0; j < 8; j++) {
                // Alternating background colors like a real chessboard
                String background = ((i + j) % 2 == 0) ? GREEN_BG : BLACK_BG;

                String piece = "--";
                if ((piece1.getRowIndex() == j) && (piece1.getColIndex() == i)) {
                    piece = displayPiece();
                }
                
                
                // Print the square with padding to make it look "chunky"
                System.out.print(background + " " + piece + " " + RESET);
            }
            System.out.println(); // New line after each row
        }
        System.out.println(YELLOW_TEXT + "================================" + RESET);
    }
    
    public void movePiece(String position){
        if (!((position.charAt(0) > 'H') || (Integer.parseInt(position.substring(1)) > 8))) {
            piece1.setPosition(position);
        }
        else{
            System.out.println("What thats not a thing");
        }
    }
    
}
