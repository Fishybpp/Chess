package models;

 public class Piece {

	boolean isCaptured;
	Team team;
	Type type;
	public enum Type {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EMPTY
	}

	public enum Team {
		WHITE, BLACK, BOARD
	}

	public Piece(Type type, Team team) {
		this.type = type;
		this.team = team;
	}

	public void setCaptured(boolean state) {
		isCaptured = state;
	}

	public boolean isCaptured() {
		return isCaptured;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Team getTeam() {
		return team;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {

	    String white = "\u001B[38;5;255m";
		String black = "\u001B[38;5;235m";

	    return switch (type) {
	        case KING   -> team == Team.WHITE ? white+ "♔ ": black + "♚ ";
	        case QUEEN  -> team == Team.WHITE ? white+ "♕ ": black + "♛ ";
	        case ROOK   -> team == Team.WHITE ? white+ "♖ ": black + "♜ ";
	        case BISHOP -> team == Team.WHITE ? white+ "♗ ": black + "♝ ";
	        case PAWN   -> team == Team.WHITE ? white+ "♙ ": black + "♙ ";
	        case KNIGHT -> team == Team.WHITE ? white+ "♘ ": black + "♞ ";
	        default     -> "  ";
	    };
	}

}