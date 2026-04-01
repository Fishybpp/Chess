package models;

abstract class Piece {

	enum Type {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
	}

	enum Team {
		WHITE, BLACK
	}

	boolean isCaptured;
	Type type;

	public void setCaptured(boolean state) {
		isCaptured = state;
	}

	abstract boolean isCaptured();

	public Type getType() {
		return type;
	}
}