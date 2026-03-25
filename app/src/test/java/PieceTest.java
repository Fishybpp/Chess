import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import models.Piece;

class PieceTest {
	Piece piece;

	@ParameterizedTest
    @ValueSource(strings = {"A3", "A4"})
    void testPawnMoves(String move) {
    	piece = new Piece("Pawn", "A2", 1);
    	assertDoesNotThrow(() -> piece.movePiece(move));
    }

}