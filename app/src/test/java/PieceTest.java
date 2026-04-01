import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import models.Piece;
import models.Piece.Team;
import models.Piece.Type;

class PieceTest {
    @ParameterizedTest
    @ValueSource(strings = { "A3", "A4" })
    void testWhitePawnMoves(String move) {
        Piece piece = new Piece(Type.PAWN, "A2", Team.WHITE);
        assertDoesNotThrow(() -> piece.movePiece(move));
    }

    @ParameterizedTest
    @ValueSource(strings = { "A5", "C7", "Q308" })
    void testInvalidWhitePawnMoves(String move) {
        Piece piece = new Piece(Type.PAWN, "A2", Team.WHITE);
        try {
            piece.movePiece(move);
        } catch (Exception e) {
            assertThrows(IllegalArgumentException.class, () -> piece.movePiece(move));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "C5", "D6", "F6", "G5", "C3", "D2", "F2", "G3" })
    void testKnightMoves(String moves) {
        Piece piece = new Piece(Type.KNIGHT, "E4", Team.WHITE);
        assertDoesNotThrow(() -> piece.movePiece(moves));
    }

    @ParameterizedTest
    @ValueSource(strings = { "E5", "F4", "D4", "E3", "G4", "F3" })
    void testInvalidKnightMoves(String moves) {
        Piece piece = new Piece(Type.KNIGHT, "E4", Team.WHITE);
        assertThrows(IllegalArgumentException.class, () -> piece.movePiece(moves));
    }

    @ParameterizedTest
    @ValueSource(strings = { "F5", "G6", "H7", "F3", "G2", "H1", "D5", "C6", "B7", "A8", "D3", "C2", "B1" })
    void testBishopMoves(String moves) {
        Piece piece = new Piece(Type.BISHOP, "E4", Team.WHITE);
        assertDoesNotThrow(() -> piece.movePiece(moves));
    }

    @ParameterizedTest
    @ValueSource(strings = { "E5", "E6", "E7", "E8", "E3", "E2", "E1", "F4", "G4", "H4", "D4", "C4", "B4", "A4", "F5",
            "G6", "H7", "D5", "C6", "B7", "A8", "F3", "G2", "H1", "D3", "C2", "B1" })
    void testQueenMoves(String moves) {
        Piece piece = new Piece(Type.QUEEN, "E4", Team.WHITE);
        assertDoesNotThrow(() -> piece.movePiece(moves));
    }

    @ParameterizedTest
    @ValueSource(strings = { "E5", "E6", "E7", "E8", "E3", "E2", "E1", "F4", "G4", "H4", "D4", "C4", "B4", "A4" })
    void testRookMoves(String moves) {
        Piece piece = new Piece(Type.ROOK, "E4", Team.WHITE);
        assertDoesNotThrow(() -> piece.movePiece(moves));
    }
}