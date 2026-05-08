package gui;

import models.Board;
import models.Piece;
import models.Piece.Team;
import models.Piece.Type;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayBoard extends JPanel {

    // -------------------------------------------------------------------------
    // Callback interface
    // -------------------------------------------------------------------------

    public interface MoveListener {
        /** Called when the user completes a from→to selection. */
        void onMove(String from, String to);
    }

    // -------------------------------------------------------------------------
    // Colours – refined wood-tone palette
    // -------------------------------------------------------------------------

    private static final Color LIGHT_SQUARE   = new Color(0xF0D9B5);
    private static final Color DARK_SQUARE    = new Color(0xB58863);
    private static final Color SELECTED_CLR   = new Color(0xF6, 0xF6, 0x69, 200);
    private static final Color MOVE_DOT_CLR   = new Color(0x20, 0x20, 0x20, 80);
    private static final Color CAPTURE_RIM    = new Color(0x20, 0x20, 0x20, 100);
    private static final Color CHECK_CLR      = new Color(0xE8, 0x27, 0x27, 160);
    private static final Color COORD_LIGHT    = new Color(0xB58863);
    private static final Color COORD_DARK     = new Color(0xF0D9B5);
    private static final Color LAST_MOVE_CLR  = new Color(202, 210, 106, 180);
    private static final Color PANEL_BG       = new Color(0x312E2B);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final Board board;
    private MoveListener moveListener;

    private String selectedSquare   = null;   // e.g. "E2"
    private List<String> legalMoves = new ArrayList<>();
    private String lastFrom         = null;
    private String lastTo           = null;
    private String inCheckSquare    = null;   // king square to tint red

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DisplayBoard(Board board) {
        this.board = board;
        setBackground(PANEL_BG);
        setPreferredSize(new Dimension(560, 560));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void setMoveListener(MoveListener listener) {
        this.moveListener = listener;
    }

    public void setLegalMoves(List<String> moves) {
        this.legalMoves = moves == null ? new ArrayList<>() : new ArrayList<>(moves);
        repaint();
    }

    public void setLastMove(String from, String to) {
        this.lastFrom = from;
        this.lastTo   = to;
    }

    public void setInCheck(String kingSquare) {
        this.inCheckSquare = kingSquare;
        repaint();
    }

    public void clearSelection() {
        selectedSquare = null;
        legalMoves.clear();
        repaint();
    }

    public void refresh() {
        repaint();
    }

    // -------------------------------------------------------------------------
    // Click handling
    // -------------------------------------------------------------------------

    private void handleClick(int px, int py) {
        int sq = squareSize();
        int boardOff = boardOffset();

        int col = (px - boardOff) / sq;
        int row = (py - boardOff) / sq;

        if (col < 0 || col > 7 || row < 0 || row > 7) return;

        // Convert pixel row/col → chess notation (row 0 = rank 8)
        char fileChar = (char) ('A' + col);
        int  rank     = 8 - row;
        String clicked = "" + fileChar + rank;

        if (selectedSquare == null) {
            // First click — select a piece
            Piece p = board.getPieceAt(clicked);
            if (p.getType() != Type.EMPTY) {
                selectedSquare = clicked;
                repaint();
                if (moveListener != null) {
                    moveListener.onMove(clicked, null);   // signal "selected"
                }
            }
        } else {
            if (clicked.equals(selectedSquare)) {
                // Deselect
                clearSelection();
            } else {
                // Second click — attempt move
                String from = selectedSquare;
                selectedSquare = null;
                legalMoves.clear();
                repaint();
                if (moveListener != null) {
                    moveListener.onMove(from, clicked);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Painting
    // -------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);

        int sq   = squareSize();
        int off  = boardOffset();

        drawBoardFrame(g2, off, sq);
        drawSquares(g2, off, sq);
        drawCoordinates(g2, off, sq);
        drawPieces(g2, off, sq);
    }

    private void drawBoardFrame(Graphics2D g2, int off, int sq) {
        // Outer wooden border
        int total = sq * 8 + off * 2;
        g2.setColor(new Color(0x5C4033));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
    }

    private void drawSquares(Graphics2D g2, int off, int sq) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = off + col * sq;
                int y = off + row * sq;

                boolean isLight = (row + col) % 2 == 0;
                Color base = isLight ? LIGHT_SQUARE : DARK_SQUARE;
                g2.setColor(base);
                g2.fillRect(x, y, sq, sq);

                // Last-move highlight
                String sqName = squareName(col, row);
                if (sqName.equals(lastFrom) || sqName.equals(lastTo)) {
                    g2.setColor(LAST_MOVE_CLR);
                    g2.fillRect(x, y, sq, sq);
                }

                // Selected highlight
                if (sqName.equals(selectedSquare)) {
                    g2.setColor(SELECTED_CLR);
                    g2.fillRect(x, y, sq, sq);
                }

                // Check highlight
                if (sqName.equals(inCheckSquare)) {
                    drawCheckGlow(g2, x, y, sq);
                }
            }
        }

        // Legal-move dots / capture rings (drawn after squares so they're on top)
        for (String target : legalMoves) {
            int[] cr = squareCoords(target);
            if (cr == null) continue;
            int tx = off + cr[0] * sq;
            int ty = off + cr[1] * sq;

            Piece occupant = board.getPieceAt(target);
            if (occupant.getType() == Type.EMPTY) {
                // Small centre dot
                int dotR = sq / 6;
                g2.setColor(MOVE_DOT_CLR);
                g2.fillOval(tx + sq/2 - dotR, ty + sq/2 - dotR, dotR*2, dotR*2);
            } else {
                // Ring around capture square
                int rim = 4;
                g2.setColor(CAPTURE_RIM);
                g2.setStroke(new BasicStroke(rim));
                g2.drawOval(tx + rim, ty + rim, sq - rim*2, sq - rim*2);
                g2.setStroke(new BasicStroke(1));
            }
        }
    }

    private void drawCheckGlow(Graphics2D g2, int x, int y, int sq) {
        // Radial gradient: red in centre fading to transparent
        float cx = x + sq / 2f;
        float cy = y + sq / 2f;
        float r  = sq / 2f;
        RadialGradientPaint rg = new RadialGradientPaint(
                cx, cy, r,
                new float[]{0f, 1f},
                new Color[]{CHECK_CLR, new Color(0, 0, 0, 0)}
        );
        g2.setPaint(rg);
        g2.fillRect(x, y, sq, sq);
    }

    private void drawCoordinates(Graphics2D g2, int off, int sq) {
        Font coordFont = new Font("SansSerif", Font.BOLD, Math.max(9, sq / 6));
        g2.setFont(coordFont);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < 8; i++) {
            // Rank numbers (left side, inside squares)
            int rank = 8 - i;
            boolean lightRow = i % 2 == 0;   // A8 (row0,col0) is light → rank label is dark colour
            g2.setColor(lightRow ? COORD_DARK : COORD_LIGHT);
            String rankStr = String.valueOf(rank);
            g2.drawString(rankStr, off + 3, off + i * sq + fm.getAscent() + 2);

            // File letters (bottom of squares)
            char file = (char) ('a' + i);
            boolean lightCol = i % 2 == 0;
            // bottom square of column i: row=7, so colour depends on (7+i)%2
            g2.setColor(((7 + i) % 2 == 0) ? COORD_DARK : COORD_LIGHT);
            String fileStr = String.valueOf(file);
            int fw = fm.stringWidth(fileStr);
            g2.drawString(fileStr,
                    off + i * sq + sq - fw - 3,
                    off + 8 * sq - 3);
        }
    }

    private void drawPieces(Graphics2D g2, int off, int sq) {
        // Use a large font for Unicode chess symbols
        Font pieceFont = new Font("Segoe UI Symbol", Font.PLAIN, (int)(sq * 0.72));
        // Fallback chain
        if (!pieceFont.getFamily().equals("Segoe UI Symbol")) {
            pieceFont = new Font("DejaVu Sans", Font.PLAIN, (int)(sq * 0.72));
        }
        g2.setFont(pieceFont);
        FontMetrics fm = g2.getFontMetrics();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board.getPieceAtIndex(row, col);
                if (p.getType() == Type.EMPTY) continue;

                String symbol = pieceSymbol(p);
                int sx = off + col * sq + (sq - fm.stringWidth(symbol)) / 2;
                int sy = off + row * sq + (sq - fm.getHeight()) / 2 + fm.getAscent();

                // Shadow / outline for contrast
                g2.setColor(new Color(0, 0, 0, 90));
                g2.drawString(symbol, sx + 1, sy + 1);

                // Piece colour
                if (p.getTeam() == Team.WHITE) {
                    g2.setColor(new Color(0xFFFAF0));
                } else {
                    g2.setColor(new Color(0x1A1A1A));
                }
                g2.drawString(symbol, sx, sy);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private int squareSize() {
        return Math.min(getWidth(), getHeight()) / 10;  // leaves room for border
    }

    private int boardOffset() {
        int sq = squareSize();
        return (Math.min(getWidth(), getHeight()) - sq * 8) / 2;
    }

    private String squareName(int col, int row) {
        return "" + (char)('A' + col) + (8 - row);
    }

    /** Returns [col, row] for a chess square name, or null if invalid. */
    private int[] squareCoords(String name) {
        if (name == null || name.length() < 2) return null;
        int col = name.charAt(0) - 'A';
        int row = 8 - (name.charAt(1) - '0');
        if (col < 0 || col > 7 || row < 0 || row > 7) return null;
        return new int[]{col, row};
    }

    private String pieceSymbol(Piece p) {
        return switch (p.getType()) {
            case KING   -> p.getTeam() == Team.WHITE ? "♔" : "♚";
            case QUEEN  -> p.getTeam() == Team.WHITE ? "♕" : "♛";
            case ROOK   -> p.getTeam() == Team.WHITE ? "♖" : "♜";
            case BISHOP -> p.getTeam() == Team.WHITE ? "♗" : "♝";
            case KNIGHT -> p.getTeam() == Team.WHITE ? "♘" : "♞";
            case PAWN   -> p.getTeam() == Team.WHITE ? "♙" : "♟";
            default     -> "";
        };
    }
}
