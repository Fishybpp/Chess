package models;

public class Board {

	Piece[][] boardData = new Piece[8][8];
	String[][] positions = new String[8][8];

    public Board() {
    	initializePositions();
    	initializeBoard();
    }
    
	private void initializePositions() {
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                char column = (char) ('A' + j); 
                int row = 8 - i;
                positions[i][j] = "" + column + row;
            }
        }
    }

    private void initializeBoard() {
    	for (int i = 0; i < boardData[i].length; i++) {
    		boardData[0][i] = new Piece(Piece.Type.PAWN, Piece.Team.WHITE); 
    	}
    }

    public Piece getPieceAt(String position) throws IllegalArgumentException {
    	for (int i = 0; i < boardData.length; i++) {
	    	for (int j = 0; j < boardData.length; j++) {
	    		if (positions[i][j].equals(position)) {
	    			return boardData[i][j];
	    		}
	    	}
    	}
    	throw new IllegalArgumentException("No Piece Found at " + position);
    }

    public void setPieceAt(String position, Piece piece) throws IllegalArgumentException {
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData.length; j++) {
                if (positions[i][j].equals(position)) {
                    boardData[i][j] = piece;
                }
            }
        }
        throw new IllegalArgumentException("Invalid position: " + position);
    }

}