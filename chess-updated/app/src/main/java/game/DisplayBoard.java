package game;

import models.Board;

public class DisplayBoard {

    // ANSI Escape Codes
    public static final String RESET       = "\u001B[0m";
    public static final String GREEN_BG    = "\u001B[48;5;22m";
    public static final String WHITE_BG    = "\u001B[48;5;248m";
    public static final String YELLOW_TEXT = "\u001B[33m";

    private Board board;

    public DisplayBoard(Board board) {
        this.board = board;
    }

    public void showBoard() {
        System.out.println(YELLOW_TEXT + "\n  ========= CHESS BOARD ==========" + RESET);

        // i counts display rank: 7 (top, rank 8) down to 0 (bottom, rank 1)
        // boardData[0] = rank 8, so array row = 7 - i
        for (int i = 7; i >= 0; i--) {
            int arrayRow = 7 - i;

            for (int j = 0; j < 8; j++) {
                String background = ((arrayRow + j) % 2 == 0) ? GREEN_BG : WHITE_BG;
                String piece = board.getPieceAtIndex(arrayRow, j).toString();

                if (j == 0) {
                    System.out.print(YELLOW_TEXT + (i + 1) + RESET + " "
                            + background + " " + piece + " " + RESET);
                } else {
                    System.out.print(background + " " + piece + " " + RESET);
                }
            }
            System.out.println();
        }

        System.out.println(YELLOW_TEXT + "  ================================" + RESET);
        System.out.println(YELLOW_TEXT + "  A    B    C   D   E   F   G   H" + RESET);
    }
}