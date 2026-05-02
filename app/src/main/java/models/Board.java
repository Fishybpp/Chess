package models;
import models.Piece;
import models.Piece.Type;
import models.Piece.Team;

public class Board {

    private Piece[][] boardData = new Piece[8][8];
    private String[][] positions = new String[8][8];

    public Board() {
        initializePositions();
        initializeBoard();
    }


    /**
     * Maps every [i][j] index to its chess position string, e.g. [0][0] → "A8".
     * Row index 0 = rank 8 (black's back rank), index 7 = rank 1 (white's back rank).
     */
    private void initializePositions() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char col = (char) ('A' + j);
                int  rank = 8 - i;
                positions[i][j] = "" + col + rank;
            }
        }
    }

    private void initializeBoard() {
        // Fill every square with an empty placeholder first
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardData[i][j] = new Piece(Type.EMPTY, Team.BOARD);
            }
        }

        // --- Pawns ---
        for (int j = 0; j < 8; j++) {
            boardData[1][j] = new Piece(Type.PAWN, Team.BLACK);
            boardData[6][j] = new Piece(Type.PAWN, Team.WHITE);
        }

        setPieceAt("A8", new Piece(Type.ROOK,   Team.BLACK));
        setPieceAt("B8", new Piece(Type.KNIGHT, Team.BLACK));
        setPieceAt("C8", new Piece(Type.BISHOP, Team.BLACK));
        setPieceAt("D8", new Piece(Type.QUEEN,  Team.BLACK));
        setPieceAt("E8", new Piece(Type.KING,   Team.BLACK));
        setPieceAt("F8", new Piece(Type.BISHOP, Team.BLACK));
        setPieceAt("G8", new Piece(Type.KNIGHT, Team.BLACK));
        setPieceAt("H8", new Piece(Type.ROOK,   Team.BLACK));

        setPieceAt("A1", new Piece(Type.ROOK,   Team.WHITE));
        setPieceAt("B1", new Piece(Type.KNIGHT, Team.WHITE));
        setPieceAt("C1", new Piece(Type.BISHOP, Team.WHITE));
        setPieceAt("D1", new Piece(Type.QUEEN,  Team.WHITE));
        setPieceAt("E1", new Piece(Type.KING,   Team.WHITE));
        setPieceAt("F1", new Piece(Type.BISHOP, Team.WHITE));
        setPieceAt("G1", new Piece(Type.KNIGHT, Team.WHITE));
        setPieceAt("H1", new Piece(Type.ROOK,   Team.WHITE));
    }


    /**
     * Returns the piece at the given position string (e.g. "E4").
     * Throws IllegalArgumentException if the position is not on the board.
     */
    public Piece getPieceAt(String position) throws IllegalArgumentException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j].equals(position)) {
                    return boardData[i][j];
                }
            }
        }
        throw new IllegalArgumentException("Position off board: " + position);
    }

    /** Returns the piece at the given array indices directly. */
    public Piece getPieceAtIndex(int row, int col) {
        return boardData[row][col];
    }

    /**
     * Places a piece at the given position string.
     * Throws IllegalArgumentException if the position is not on the board.
     */
    public void setPieceAt(String position, Piece piece) throws IllegalArgumentException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j].equals(position)) {
                    boardData[i][j] = piece;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Position off board: " + position);
    }

    /**
     * Returns true if the given team still has a king on the board.
     * Useful for detecting a king capture (e.g. in simplified game-over logic).
     */
    public boolean hasKing(Team team) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = boardData[i][j];
                if (p.getType() == Type.KING && p.getTeam() == team) {
                    return true;
                }
            }
        }
        return false;
    }
}