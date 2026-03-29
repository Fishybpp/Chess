package models;

public class Piece {
	// Variables

	private String[][] pos = new String[8][8];
	private Type currentType;
	private Team team;
	private String currentPos;
	private int posRowIndex;
	private int posColIndex;
	private char row = 'A';

	/** The type of the piece (KING, QUEEN, PAWN, KNIGHT, ROOK, BISHOP) */
	public enum Type {
		QUEEN, ROOK, BISHOP, KNIGHT, KING, PAWN
		/**
		 * @param type tyoe of Currnt Chess Piece
		 * @param team tean of Current Chess Piece
		 */
	}

	/** The team of the piece (WHITTE, BLACK) */
	public enum Team {
		WHITE, BLACK
	}

	/**
	 * Constructs a Piece with the given type, starting position, and team.
	 * 
	 * @param type The type of the piece (KING, QUEEN, PAWN, KNIGHT, ROOK, BISHOP)
	 * @param team The team of the piece (WHITE, BLACK)
	 */
	public Piece(Type type, String position, Team team) {
		intitalizePos();
		currentType = type;
		setPosition(position);
		this.team = team;
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
		position = position.toUpperCase();
		currentPos = currentPos.toUpperCase();
		switch (currentType) {

			case KING -> {
				int colDiff = Math.abs(currentPos.charAt(0) - position.charAt(0));
				int rowDiff = Math.abs(currentPos.charAt(1) - position.charAt(1));
				if (colDiff <= 1 && rowDiff <= 1) {
					setPosition(position);
				} else {
					throw new IllegalArgumentException("The King cannot possibly do that Brotato");
				}
			}

			case PAWN -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);
				if (team == Team.WHITE) {
					if (currentPos.charAt(1) == '2') {
						if (rowDiff <= 2 && rowDiff > 0 && colDiff == 0) {
							setPosition(position);
							return;
						} else {
							throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
						}
					}
					if (rowDiff <= 1 && rowDiff > 0 && colDiff == 0) {
						setPosition(position);
						return;
					} else {
						throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
					}
				}

				if (team == Team.BLACK) {
					if (currentPos.charAt(1) == '7') {
						if (rowDiff <= -2 && rowDiff < 0 && colDiff == 0) {
							setPosition(position);
							return;
						} else {
							throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
						}
					}
					if (rowDiff <= -1 && rowDiff < 0 && colDiff == 0) {
						setPosition(position);
						return;
					} else {
						throw new IllegalArgumentException("Brother its a Pawn, it can't do allat");
					}
				}
			}

			case KNIGHT -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);
				if (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2) {
					setPosition(position);
					return;
				}
				if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1) {
					setPosition(position);
					return;
				}
				throw new IllegalArgumentException("Thats not really in the Knight's abilty");
			}

			case BISHOP -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);
				if (Math.abs(((double) rowDiff / colDiff)) == 1) {
					setPosition(position);
					return;
				}
				throw new IllegalArgumentException("Thats not really in the Knight's abilty");
			}

			case QUEEN -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);

				if (Math.abs(((double) rowDiff / colDiff)) == 1) {
					setPosition(position);
					return;
				}

				if (colDiff == 0) {
					setPosition(position);
					return;
				}

				if (rowDiff == 0) {
					setPosition(position);
					return;
				}

				throw new IllegalArgumentException("Thats not really in the Knight's abilty");
			}

			case ROOK -> {
				int colDiff = position.charAt(0) - currentPos.charAt(0);
				int rowDiff = position.charAt(1) - currentPos.charAt(1);

				if (colDiff == 0) {
					setPosition(position);
					return;
				}

				if (rowDiff == 0) {
					setPosition(position);
					return;
				}

				throw new IllegalArgumentException("Thats not really in the Knight's abilty");
			}

			default -> {
				throw new IllegalArgumentException("That's not a valid piece twin");
			}
		};
	}

	public String getPosition() {
		return currentPos;
	}

	public Type getType() {
		return currentType;
	}

	public int getRowIndex() {
		return posRowIndex;
	}

	public int getColIndex() {
		return posColIndex;
	}

	public void setType(Type type) {
		if (type == type.BISHOP || type == type.KNIGHT || type == type.QUEEN) {
			currentType = type;
		} else {
			throw new IllegalArgumentException("That Piece Type Does Not Exist");
		}
	}

	public String displayPiece() {
		if (team == Team.WHITE) {
			return switch (this.getType()) {
				case Type.PAWN -> "P ";
				case Type.KING -> "K ";
				case Type.KNIGHT -> "KN";
				case Type.QUEEN -> "Q ";
				default -> "K";
			};
		} else {
			return switch (this.getType()) {
				case Type.PAWN -> "p ";
				case Type.KING -> "k ";
				case Type.KNIGHT -> "kn";
				case Type.QUEEN -> "q ";
				default -> "K";
			};
		}
	}
}