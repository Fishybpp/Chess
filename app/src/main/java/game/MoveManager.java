package game;

import models.Piece;
import models.Piece.Team;
import models.Piece.Type;
import models.Board;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MoveManager {

    private Board board;
    private ArrayList<String> moveHistory = new ArrayList<>();

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, UPR, UPL, DWNL, DWNR
    }

    public MoveManager(Board board) {
        this.board = board;
    }

    // ---------------------------------------------------------------------------
    // Position helpers
    // ---------------------------------------------------------------------------

    private String move(Direction dir, String position) {
        char col = position.charAt(0);
        char row = position.charAt(1);

        return switch (dir) {
            case UP -> "" + col + (char) (row + 1);
            case DOWN -> "" + col + (char) (row - 1);
            case LEFT -> "" + (char) (col - 1) + row;
            case RIGHT -> "" + (char) (col + 1) + row;
            case UPL -> move(Direction.UP, move(Direction.LEFT, position));
            case UPR -> move(Direction.UP, move(Direction.RIGHT, position));
            case DWNL -> move(Direction.DOWN, move(Direction.LEFT, position));
            case DWNR -> move(Direction.DOWN, move(Direction.RIGHT, position));
        };
    }

    private boolean isOnBoard(String position) {
        if (position == null || position.length() < 2)
            return false;
        char col = position.charAt(0);
        char row = position.charAt(1);
        return col >= 'A' && col <= 'H' && row >= '1' && row <= '8';
    }

    private boolean isLandable(String position, Team movingTeam) {
        if (!isOnBoard(position))
            return false;
        Team occupant = board.getPieceAt(position).getTeam();
        return occupant != movingTeam;
    }

    private void addSlidingMoves(ArrayList<String> moves, String from, Direction dir, Team team) {
        String current = move(dir, from);
        while (isOnBoard(current)) {
            Team occupant = board.getPieceAt(current).getTeam();
            if (occupant == team)
                break;
            moves.add(current);
            if (occupant != Team.BOARD)
                break;
            current = move(dir, current);
        }
    }

    // ---------------------------------------------------------------------------
    // RAW moves (no king safety)
    // ---------------------------------------------------------------------------

    public ArrayList<String> calculateLegalMoves(String position) {
        Piece piece = board.getPieceAt(position);
        Team team = piece.getTeam();

        ArrayList<String> legalMoves = new ArrayList<>();

        switch (piece.getType()) {

            case KING -> {
                for (Direction dir : Direction.values()) {
                    String target = move(dir, position);
                    if (isLandable(target, team)) {
                        legalMoves.add(target);
                    }
                }
            }

            case QUEEN -> {
                for (Direction dir : Direction.values()) {
                    addSlidingMoves(legalMoves, position, dir, team);
                }
            }

            case ROOK -> {
                addSlidingMoves(legalMoves, position, Direction.UP, team);
                addSlidingMoves(legalMoves, position, Direction.DOWN, team);
                addSlidingMoves(legalMoves, position, Direction.LEFT, team);
                addSlidingMoves(legalMoves, position, Direction.RIGHT, team);
            }

            case BISHOP -> {
                addSlidingMoves(legalMoves, position, Direction.UPL, team);
                addSlidingMoves(legalMoves, position, Direction.UPR, team);
                addSlidingMoves(legalMoves, position, Direction.DWNL, team);
                addSlidingMoves(legalMoves, position, Direction.DWNR, team);
            }

            case KNIGHT -> {
                char col = position.charAt(0);
                char row = position.charAt(1);

                int[][] offsets = {
                        {-2, -1}, {-2, +1},
                        {+2, -1}, {+2, +1},
                        {-1, -2}, {-1, +2},
                        {+1, -2}, {+1, +2}
                };

                for (int[] o : offsets) {
                    String target = "" + (char)(col + o[0]) + (char)(row + o[1]);
                    if (isLandable(target, team)) {
                        legalMoves.add(target);
                    }
                }
            }

            case PAWN -> {
                if (team == Team.WHITE) {

                    String oneUp = move(Direction.UP, position);
                    if (isOnBoard(oneUp) && board.getPieceAt(oneUp).getTeam() == Team.BOARD) {
                        legalMoves.add(oneUp);

                        if (position.charAt(1) == '2') {
                            String twoUp = move(Direction.UP, oneUp);
                            if (board.getPieceAt(twoUp).getTeam() == Team.BOARD) {
                                legalMoves.add(twoUp);
                            }
                        }
                    }

                    String left = move(Direction.UPL, position);
                    String right = move(Direction.UPR, position);

                    if (isOnBoard(left) && board.getPieceAt(left).getTeam() == Team.BLACK)
                        legalMoves.add(left);

                    if (isOnBoard(right) && board.getPieceAt(right).getTeam() == Team.BLACK)
                        legalMoves.add(right);

                } else {

                    String oneDown = move(Direction.DOWN, position);
                    if (isOnBoard(oneDown) && board.getPieceAt(oneDown).getTeam() == Team.BOARD) {
                        legalMoves.add(oneDown);

                        if (position.charAt(1) == '7') {
                            String twoDown = move(Direction.DOWN, oneDown);
                            if (board.getPieceAt(twoDown).getTeam() == Team.BOARD) {
                                legalMoves.add(twoDown);
                            }
                        }
                    }

                    String left = move(Direction.DWNL, position);
                    String right = move(Direction.DWNR, position);

                    if (isOnBoard(left) && board.getPieceAt(left).getTeam() == Team.WHITE)
                        legalMoves.add(left);

                    if (isOnBoard(right) && board.getPieceAt(right).getTeam() == Team.WHITE)
                        legalMoves.add(right);
                }
            }

            default -> throw new IllegalArgumentException();
        }

        return legalMoves;
    }

    // ---------------------------------------------------------------------------
    // SAFE moves (no self-check)
    // ---------------------------------------------------------------------------

    public ArrayList<String> calculateSafeMoves(String position) {
        ArrayList<String> moves = calculateLegalMoves(position);
        ArrayList<String> safe = new ArrayList<>();

        Piece piece = board.getPieceAt(position);
        Team team = piece.getTeam();

        for (String to : moves) {

            Piece captured = board.getPieceAt(to);

            board.setPieceAt(to, piece);
            board.setPieceAt(position, new Piece(Type.EMPTY, Team.BOARD));

            if (!isInCheck(team)) {
                safe.add(to);
            }

            // undo
            board.setPieceAt(position, piece);
            board.setPieceAt(to, captured);
        }

        return safe;
    }

    // ---------------------------------------------------------------------------
    // CHECK
    // ---------------------------------------------------------------------------

    public boolean isInCheck(Team team) {

        String kingPos = null;

        for (char c = 'A'; c <= 'H'; c++) {
            for (char r = '1'; r <= '8'; r++) {
                String pos = "" + c + r;
                Piece p = board.getPieceAt(pos);
                if (p.getTeam() == team && p.getType() == Type.KING) {
                    kingPos = pos;
                }
            }
        }

        if (kingPos == null) return false;

        Team enemy = (team == Team.WHITE) ? Team.BLACK : Team.WHITE;

        for (char c = 'A'; c <= 'H'; c++) {
            for (char r = '1'; r <= '8'; r++) {
                String pos = "" + c + r;
                if (board.getPieceAt(pos).getTeam() == enemy) {
                    if (calculateLegalMoves(pos).contains(kingPos)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ---------------------------------------------------------------------------
    // CHECKMATE
    // ---------------------------------------------------------------------------

    public boolean isCheckmate(Team team) {

        if (!isInCheck(team)) return false;

        for (char c = 'A'; c <= 'H'; c++) {
            for (char r = '1'; r <= '8'; r++) {

                String from = "" + c + r;
                if (board.getPieceAt(from).getTeam() != team) continue;

                if (!calculateSafeMoves(from).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    // ---------------------------------------------------------------------------
    // MOVE EXECUTION
    // ---------------------------------------------------------------------------

    public void movePiece(String input, Team team) {

        String[] parts = input.toUpperCase().split(">");
        String from = parts[0];
        String to = parts[1];

        if (team != board.getPieceAt(from).getTeam()) {
            throw new IllegalArgumentException("Not your turn");
        }

        ArrayList<String> moves = calculateSafeMoves(from);

        if (!moves.contains(to)) {
            throw new IllegalArgumentException("Illegal move");
        }

        board.setPieceAt(to, board.getPieceAt(from));
        board.setPieceAt(from, new Piece(Type.EMPTY, Team.BOARD));

        moveHistory.add(input);
    }

    // ---------------------------------------------------------------------------
    // MOVE HISTORY ACCESS
    // ---------------------------------------------------------------------------

    public ArrayList<String> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    // ---------------------------------------------------------------------------
    // SAVE GAME
    // ---------------------------------------------------------------------------

    /**
     * Saves the current move history to a text file.
     * Returns the filename so the caller can report it to the user.
     */
    public String saveGame(String winner) throws IOException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "chess_game_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("# Chess Game - Saved " + timestamp);
            writer.newLine();
            writer.write("# Result: " + winner);
            writer.newLine();
            writer.write("# Moves: " + moveHistory.size());
            writer.newLine();
            writer.write("# Format: FROM>TO (one move per line)");
            writer.newLine();
            writer.newLine();

            for (String move : moveHistory) {
                writer.write(move);
                writer.newLine();
            }
        }

        return filename;
    }
}
