package models;

 public class Piece {

	enum Type {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
	}

	enum Team {
		WHITE, BLACK
	}

	public Piece(Type type, Team team) {
		this.type = type;
		this.team = team;
	}

	boolean isCaptured;
	Team team;
	Type type;

	public void setCaptured(boolean state) {
		isCaptured = state;
	}

	public boolean isCaptured() {
		return isCaptured;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}