import gui.DisplayBoard;
import game.MoveManager;
import models.Board;
import models.Piece;
import models.Piece.Team;
import models.Piece.Type;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameRunner {

    // -------------------------------------------------------------------------
    // Layout constants
    // -------------------------------------------------------------------------

    private static final Color BG_DARK   = new Color(0x1E1B18);
    private static final Color BG_PANEL  = new Color(0x2A2522);
    private static final Color ACCENT    = new Color(0xC8A96E);
    private static final Color TEXT_MAIN = new Color(0xE8DCC8);
    private static final Color TEXT_DIM  = new Color(0x8A7A6A);
    private static final Color WHITE_CLR = new Color(0xFFFAF0);
    private static final Color BLACK_CLR = new Color(0x1A1A1A);
    private static final Font  MONO      = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font  TITLE_F   = new Font("Serif", Font.BOLD, 22);
    private static final Font  INFO_F    = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font  SMALL_F   = new Font("SansSerif", Font.PLAIN, 12);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final Board       board;
    private final MoveManager moveManager;
    private final DisplayBoard boardPanel;

    private Team    currentTurn = Team.WHITE;
    private boolean gameOver    = false;
    private String  selectedFrom = null;   // square currently selected

    // UI components updated during play
    private JLabel  statusLabel;
    private JLabel  turnLabel;
    private JTextArea moveLogArea;
    private JButton resignBtn;
    private JButton saveBtn;
    private JButton newGameBtn;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new GameRunner().show();
        });
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public GameRunner() {
        board       = new Board();
        moveManager = new MoveManager(board);
        boardPanel  = new DisplayBoard(board);

        // Wire board clicks
        boardPanel.setMoveListener(this::handleBoardClick);
    }

    // -------------------------------------------------------------------------
    // Window
    // -------------------------------------------------------------------------

    public void show() {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(720, 580));

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        // Title bar
        root.add(buildTitleBar(), BorderLayout.NORTH);

        // Centre: board
        JPanel boardWrap = new JPanel(new GridBagLayout());
        boardWrap.setBackground(BG_DARK);
        boardWrap.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 12));
        boardPanel.setPreferredSize(new Dimension(520, 520));
        boardWrap.add(boardPanel);
        root.add(boardWrap, BorderLayout.CENTER);

        // Right sidebar
        root.add(buildSidebar(), BorderLayout.EAST);

        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateStatus();
    }

    // -------------------------------------------------------------------------
    // UI builders
    // -------------------------------------------------------------------------

    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0x14120F));
        bar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JLabel title = new JLabel("♟  Chess");
        title.setFont(TITLE_F);
        title.setForeground(ACCENT);

        turnLabel = new JLabel("White to move");
        turnLabel.setFont(INFO_F);
        turnLabel.setForeground(TEXT_MAIN);

        bar.add(title,     BorderLayout.WEST);
        bar.add(turnLabel, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(BG_PANEL);
        side.setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 16));
        side.setPreferredSize(new Dimension(200, 0));

        // Status box
        statusLabel = new JLabel("<html>Click a piece<br>to begin</html>");
        statusLabel.setFont(SMALL_F);
        statusLabel.setForeground(TEXT_DIM);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(statusLabel);
        side.add(Box.createVerticalStrut(16));

        // Divider
        side.add(divider());
        side.add(Box.createVerticalStrut(12));

        // Move log header
        JLabel logHeader = new JLabel("MOVE LOG");
        logHeader.setFont(new Font("SansSerif", Font.BOLD, 10));
        logHeader.setForeground(ACCENT);
        logHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logHeader);
        side.add(Box.createVerticalStrut(6));

        // Move log text area
        moveLogArea = new JTextArea();
        moveLogArea.setEditable(false);
        moveLogArea.setFont(MONO);
        moveLogArea.setBackground(new Color(0x1A1714));
        moveLogArea.setForeground(TEXT_MAIN);
        moveLogArea.setCaretColor(ACCENT);
        moveLogArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(moveLogArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        scroll.setPreferredSize(new Dimension(170, 240));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x3A3530)));
        scroll.setBackground(new Color(0x1A1714));
        side.add(scroll);
        side.add(Box.createVerticalStrut(16));

        side.add(divider());
        side.add(Box.createVerticalStrut(14));

        // Buttons
        resignBtn  = sideButton("Resign");
        saveBtn    = sideButton("Save Game");
        newGameBtn = sideButton("New Game");

        resignBtn.addActionListener(e -> handleResign());
        saveBtn.addActionListener(e   -> handleSave());
        newGameBtn.addActionListener(e -> handleNewGame());

        for (JButton btn : new JButton[]{resignBtn, saveBtn, newGameBtn}) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            side.add(btn);
            side.add(Box.createVerticalStrut(8));
        }

        side.add(Box.createVerticalGlue());
        return side;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x3A3530));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JButton sideButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(SMALL_F);
        btn.setForeground(TEXT_MAIN);
        btn.setBackground(new Color(0x3A3530));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x5A5040), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0x4A4038));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0x3A3530));
            }
        });
        return btn;
    }

    // -------------------------------------------------------------------------
    // Board interaction
    // -------------------------------------------------------------------------

    private void handleBoardClick(String from, String to) {
        if (gameOver) return;

        if (to == null) {
            // Selection — validate it belongs to current player
            Piece p = board.getPieceAt(from);
            if (p.getTeam() != currentTurn) {
                clearSelection();
                setStatus("That's not your piece.");
                return;
            }
            selectedFrom = from;

            // Ask MoveManager for legal moves and pass them to the board
            ArrayList<String> moves = moveManager.calculateSafeMoves(from);
            boardPanel.setLegalMoves(moves);
            boardPanel.repaint();

            String pieceName = friendlyName(p);
            setStatus(pieceName + " at " + from + "\n" + moves.size() + " legal move" + (moves.size() == 1 ? "" : "s"));
        } else {
            // Move attempt
            if (selectedFrom == null) return;
            attemptMove(selectedFrom, to);
            selectedFrom = null;
        }
    }

    private void attemptMove(String from, String to) {
        try {
            moveManager.movePiece(from + ">" + to, currentTurn);

            // Success — update visuals
            boardPanel.setLastMove(from, to);
            boardPanel.clearSelection();
            appendMoveLog(from, to);

            // Flip turn
            currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;

            // Check / checkmate detection
            boolean inCheck    = moveManager.isInCheck(currentTurn);
            boolean inCheckmate = moveManager.isCheckmate(currentTurn);

            if (inCheckmate) {
                Team winner = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
                boardPanel.setInCheck(findKingSquare(currentTurn));
                boardPanel.refresh();
                gameOver = true;
                String winnerName = winner == Team.WHITE ? "White" : "Black";
                setStatus("✓ Checkmate!\n" + winnerName + " wins!");
                turnLabel.setText("Game over – " + winnerName + " wins");
                turnLabel.setForeground(ACCENT);
                JOptionPane.showMessageDialog(boardPanel.getTopLevelAncestor(),
                        winnerName + " wins by checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (inCheck) {
                boardPanel.setInCheck(findKingSquare(currentTurn));
                boardPanel.refresh();
                updateTurnLabel();
                setStatus("⚠ " + (currentTurn == Team.WHITE ? "White" : "Black") + " is in check!");
            } else {
                boardPanel.setInCheck(null);
                boardPanel.refresh();
                updateStatus();
            }

        } catch (IllegalArgumentException ex) {
            clearSelection();
            setStatus("Illegal move.\nTry again.");
        }
    }

    // -------------------------------------------------------------------------
    // Button handlers
    // -------------------------------------------------------------------------

    private void handleResign() {
        if (gameOver) return;
        Team loser  = currentTurn;
        Team winner = (loser == Team.WHITE) ? Team.BLACK : Team.WHITE;
        gameOver = true;
        String winnerName = winner == Team.WHITE ? "White" : "Black";
        setStatus(winnerName + " wins\nby resignation.");
        turnLabel.setText("Game over – " + winnerName + " wins");
        turnLabel.setForeground(ACCENT);
        boardPanel.clearSelection();
    }

    private void handleSave() {
        String winner = gameOver ? turnLabel.getText() : "Game in progress";
        try {
            String filename = moveManager.saveGame(winner);
            JOptionPane.showMessageDialog(boardPanel.getTopLevelAncestor(),
                    "Game saved to:\n" + filename, "Game Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(boardPanel.getTopLevelAncestor(),
                    "Could not save: " + ex.getMessage(), "Save Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleNewGame() {
        int confirm = JOptionPane.showConfirmDialog(
                boardPanel.getTopLevelAncestor(),
                "Start a new game? Current game will be lost.",
                "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Restart – just relaunch (simplest approach that keeps it clean)
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(boardPanel);
        topFrame.dispose();
        new GameRunner().show();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void updateStatus() {
        updateTurnLabel();
        String player = currentTurn == Team.WHITE ? "White" : "Black";
        setStatus(player + " to move.\nClick a piece.");
    }

    private void updateTurnLabel() {
        String player = currentTurn == Team.WHITE ? "White" : "Black";
        turnLabel.setText(player + " to move");
        turnLabel.setForeground(currentTurn == Team.WHITE
                ? new Color(0xFFFAF0) : new Color(0xAAAAAA));
    }

    private void setStatus(String text) {
        statusLabel.setText("<html>" + text.replace("\n", "<br>") + "</html>");
    }

    private void clearSelection() {
        selectedFrom = null;
        boardPanel.clearSelection();
    }

    private void appendMoveLog(String from, String to) {
        ArrayList<String> history = moveManager.getMoveHistory();
        int moveNum = (history.size() + 1) / 2;
        boolean isWhite = history.size() % 2 == 1; // white just moved

        String entry;
        if (isWhite) {
            entry = moveNum + ". " + from + "→" + to;
        } else {
            // append to last line
            entry = "  " + from + "→" + to;
        }

        String current = moveLogArea.getText();
        if (isWhite) {
            moveLogArea.setText(current.isEmpty()
                    ? entry
                    : current + "\n" + entry);
        } else {
            moveLogArea.setText(current + "  " + from + "→" + to);
        }

        // Auto-scroll to bottom
        moveLogArea.setCaretPosition(moveLogArea.getDocument().getLength());
    }

    private String findKingSquare(Team team) {
        for (char c = 'A'; c <= 'H'; c++) {
            for (char r = '1'; r <= '8'; r++) {
                String pos = "" + c + r;
                Piece p = board.getPieceAt(pos);
                if (p.getTeam() == team && p.getType() == Type.KING) {
                    return pos;
                }
            }
        }
        return null;
    }

    private String friendlyName(Piece p) {
        return switch (p.getType()) {
            case KING   -> "King";
            case QUEEN  -> "Queen";
            case ROOK   -> "Rook";
            case BISHOP -> "Bishop";
            case KNIGHT -> "Knight";
            case PAWN   -> "Pawn";
            default     -> "Piece";
        };
    }
}
