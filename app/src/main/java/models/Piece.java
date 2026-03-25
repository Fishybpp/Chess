package models;
public class Piece {
	// Variables
	private final int WHITE = 1;
	private final int BLACK = 0;
	private String[][] pos = new String[8][8];
	private String currentType;
	private String[] promotionType = { "Queen", "Rook", "Knight", "Bishop" };
	private int team;
	private String currentPos;
	private int posRowIndex;
	private int posColIndex;
	private char row = 'A';

	// Constructor
	/**
	 * @param team indicates the piece's team via an int 1 for white, 0 for black
	*/
	public Piece(String type, String position, int team) {
		intitalizePos();
		currentType = type;
		setPosition(position);
		team = WHITE;
	}
	public Piece(String type) {
		intitalizePos();
		currentType = type;
	}

	// Methods

	private void intitalizePos() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pos[i][j] = row + Integer.toString((j + 1));
			}
			row++;
		}
		currentPos = "A1";
	}

	private void setPosition(String position) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pos[i][j].equals(position)) {
					posRowIndex = i;
					posColIndex = j;
				}
			}
		}
		currentPos = pos[posRowIndex][posColIndex];
	}

	public void movePiece(String position) throws IllegalArgumentException {
		switch (currentType) {
			case "King" -> {
				int colDiff = Math.abs(currentPos.charAt(0) - position.charAt(0));
				int rowDiff = Math.abs(currentPos.charAt(1) - position.charAt(1));
				if (colDiff <= 1 && rowDiff <= 1) {
					setPosition(position);
				} else {
					throw new IllegalArgumentException("The King cannot possibly do that Brotato");
				}
			}
			case "Pawn" -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);
				if (team == WHITE) {
					if (currentPos.charAt(1) == '2') {
						if (rowDiff <=2 && rowDiff > 0 && colDiff == 0) {
							setPosition(position);
							return;
						}
						else {
							throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
						}				
					}
					if (rowDiff <= 1 && rowDiff > 0 && colDiff == 0) {
						setPosition(position);
						return;
					}
					else {
						throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
					}	
				}
				if (team == BLACK) {
					if (currentPos.charAt(1) == '7') {
						if (rowDiff <=-2 && rowDiff < 0  && colDiff == 0) {
							setPosition(position);
							return;
						}
						else {
							throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
						}				
					}
					if (rowDiff <= -1 && rowDiff < 0 && colDiff == 0) {
						setPosition(position);
						return;
					}
					else {
						throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
					}	
				}				
			}
			default -> {
				setPosition(position);
			}
		}
		;
	}

	public String getPosition() {
		return currentPos;
	}

	public String getType() {
		return currentType;
	}

	public int getRowIndex() {
		return posRowIndex;
	}

	public int getColIndex() {
		return posColIndex;
	}

	public void setType(String type) {
		for (int i = 0; i < 8; i++) {
			if (promotionType[i].equals(type)) {
				currentType = promotionType[i];
				return;
			}
		}
		throw new RuntimeException("Cannot Find Piece Type");
	}

	public String displayPiece() {
		if (team == WHITE) {
			return switch (this.getType()) {
				case "Pawn" -> "P ";
				case "King" -> "K ";
				case "Knight" -> "KN";
				case "Queen" -> "Q ";
				default -> "K";
			};
		} else {
			return switch (this.getType()) {
				case "Pawn" -> "p ";
				case "King" -> "k ";
				case "Knight" -> "kn";
				case "Queen" -> "q ";
				default -> "K";
			};
		}
	}
}