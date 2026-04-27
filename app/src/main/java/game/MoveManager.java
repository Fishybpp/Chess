package game;

import models.Piece;
import models.Board;
import models.Piece.Type;
import models.Piece.Team;
import java.util.ArrayList;

public class MoveManager {
	
	private Board board = new Board();

	public ArrayList<String> calculateLegalMoves(String position) throws RuntimeException {
		Piece piece;
		try {
			piece = board.getPieceAt(position);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		switch (piece.getType()) {
			case KING -> {
				char col = position.charAt(0);
				char row = position.charAt(1);


				String up = "" + col + (char)(row + 1);
				String down = "" + col + (char)(row - 1);
				String left = "" + (char)(col - 1) + row;
				String right = "" + (char)(col + 1) + row;
				ArrayList<String> legalMoves = new ArrayList<String>();
				legalMoves.add(up);
				legalMoves.add(down);
				legalMoves.add(left);
				legalMoves.add(right); 
				for (int i = 0; i < legalMoves.size(); i++) {
					try {
						if (board.getPieceAt(legalMoves.get(i)).getType() == piece.getType()) {
							legalMoves.remove(i);
						}
					} catch (Exception e) {
					}
				}
				return legalMoves;
			}

			case PAWN -> {

				char col = position.charAt(0);
				char row = position.charAt(1);

				String up = "" + col + (char)(row + 1);
				String down = "" + col + (char)(row - 1);
				ArrayList<String> legalMoves = new ArrayList<String>();
				legalMoves.add(up);
				legalMoves.add(down);
				for (int i = 0; i < legalMoves.size(); i++) {
					try {
						if (board.getPieceAt(legalMoves.get(i)).getType() == piece.getType()) {
							legalMoves.remove(i);
						} 
					} catch (Exception e) {
					}
				}
				return legalMoves;
			} 

			case QUEEN -> {
				char col = position.charAt(0);
				char row = position.charAt(1);

				ArrayList<String> legalMoves = new ArrayList<String>();
				String up = "" + col + (char)(row + 1);
				String down = "" + col + (char)(row - 1);
				String right = "" + (char)(col + 1) + row;
				String left = "" + (char)(col - 1) + row;
				while (left.charAt(0) <= 'A') {
					left = "" + (char)(col - 1) + row;
					legalMoves.add(left);
				}
			} 
		};
		return null;
	}
}