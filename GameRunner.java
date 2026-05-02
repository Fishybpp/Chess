import game.DisplayBoard;
import game.MoveManager;
import models.Board;
import models.Piece.Team;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GameRunner {

    // -------------------------------------------------------------------------
    // ENTRY POINT — main menu
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CHESS ===");
            System.out.println("1. New Game");
            System.out.println("2. Replay Saved Game");
            System.out.println("3. Quit");
            System.out.print("Choose: ");

            String choice = scan.nextLine().trim();

            switch (choice) {
                case "1" -> runGame(scan);
                case "2" -> replayGame(scan);
                case "3" -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println("Enter 1, 2, or 3.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // PLAY a new game
    // -------------------------------------------------------------------------

    public static void runGame(Scanner scan) {
        Board board = new Board();
        MoveManager mover = new MoveManager(board);
        DisplayBoard display = new DisplayBoard(board);

        System.out.println("\nGame started! Commands: FROM>TO  |  save  |  quit");

        Team current = Team.WHITE;

        while (true) {
            display.showBoard();
            System.out.println(current + "'s turn (e.g. E2>E4):");
            String input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                promptSave(mover, scan, "Game abandoned");
                return;
            }

            if (input.equalsIgnoreCase("save")) {
                save(mover, "In progress");
                continue;
            }

            try {
                mover.movePiece(input, current);
            } catch (Exception e) {
                System.out.println("  Invalid: " + e.getMessage());
                continue;
            }

            // check win condition
            Team other = (current == Team.WHITE) ? Team.BLACK : Team.WHITE;
            if (!board.hasKing(other)) {
                display.showBoard();
                String result = current + " wins!";
                System.out.println(result);
                promptSave(mover, scan, result);
                return;
            }

            current = other;
        }
    }

    // -------------------------------------------------------------------------
    // SAVE helpers
    // -------------------------------------------------------------------------

    private static void save(MoveManager mover, String result) {
        try {
            String filename = mover.saveGame(result);
            System.out.println("  Game saved to: " + filename);
        } catch (IOException e) {
            System.out.println("  Save failed: " + e.getMessage());
        }
    }

    private static void promptSave(MoveManager mover, Scanner scan, String result) {
        System.out.print("Save game? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            save(mover, result);
        }
    }

    // -------------------------------------------------------------------------
    // REPLAY a saved game from file
    // -------------------------------------------------------------------------

    public static void replayGame(Scanner scan) {
        System.out.print("Enter filename to replay: ");
        String filename = scan.nextLine().trim();

        // Load moves from file — skip comment lines starting with #
        ArrayList<String> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                moves.add(line);
            }
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
            return;
        }

        if (moves.isEmpty()) {
            System.out.println("No moves found in file.");
            return;
        }

        System.out.println("Loaded " + moves.size() + " moves. Press Enter to step, 'q' to quit.");

        Board board = new Board();
        MoveManager mover = new MoveManager(board);
        DisplayBoard display = new DisplayBoard(board);

        Team current = Team.WHITE;

        for (int i = 0; i < moves.size(); i++) {
            String moveStr = moves.get(i);
            display.showBoard();
            System.out.println("Move " + (i + 1) + "/" + moves.size() + " — " + current + ": " + moveStr);

            try {
                mover.movePiece(moveStr, current);
            } catch (Exception e) {
                System.out.println("  Replay error at move " + (i + 1) + ": " + e.getMessage());
                return;
            }

            System.out.print("[Enter] next  |  q = quit replay: ");
            String input = scan.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Replay stopped.");
                return;
            }

            current = (current == Team.WHITE) ? Team.BLACK : Team.WHITE;
        }

        display.showBoard();
        System.out.println("Replay complete — " + moves.size() + " moves played.");
    }
}
