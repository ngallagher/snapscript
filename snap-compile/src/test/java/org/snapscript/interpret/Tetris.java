package org.snapscript.interpret;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris {

    private Game game = new Game();
    private final BufferStrategy strategy;

    private final int BOARD_CORNER_X = 300;
    private final int BOARD_CORNER_Y = 50;

    private final KeyboardInput keyboard = new KeyboardInput();
    private long lastIteration = System.currentTimeMillis();

    private static final int PIECE_WIDTH = 20;


    public Tetris() {
        JFrame container = new JFrame("Tetris");
        Canvas canvas = new Canvas();
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);

        canvas.setBounds(0, 0, 800, 600);
        panel.add(canvas);
        canvas.setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addKeyListener(keyboard);
        canvas.requestFocus();

        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
    }

    void gameLoop() {
        while (true) {
            if (keyboard.newGame()) {
                game = new Game();
                game.startGame();
            }
            if (game.isPlaying()) {

                if (!game.isPaused()) {
                    tetrisLoop();
                }
                if (keyboard.pauseGame()) {
                    game.pauseGame();
                }
                try {
                    Thread.sleep(20);
                } catch (Exception e) { }
            }
            draw();
        }
    }

    void tetrisLoop() {
        if (game.isDropping()) {
            game.moveDown();
        } else if (System.currentTimeMillis() - lastIteration >= game.getIterationDelay()) {
            game.moveDown();
            lastIteration = System.currentTimeMillis();
        }
        if (keyboard.rotate()) {
            game.rotate();
        } else if (keyboard.left()) {
            game.moveLeft();
        } else if (keyboard.right()) {
            game.moveRight();
        } else if (keyboard.drop()) {
            game.drop();
        }
    }

    public void draw() {
        Graphics2D g = getGameGraphics();
        drawEmptyBoard(g);
        drawHelpBox(g);
        drawPiecePreviewBox(g);

        if (game.isPlaying()) {
            drawCells(g);
            drawPiecePreview(g, game.getNextPiece().getType());

            if (game.isPaused()) {
                drawGamePaused(g);
            }
        }

        if (game.isGameOver()) {
            drawCells(g);
            drawGameOver(g);
        }

        drawStatus(g);
        drawPlayTetris(g);

        g.dispose();
        strategy.show();
    }

    private Graphics2D getGameGraphics() {
        return (Graphics2D) strategy.getDrawGraphics();
    }

    private void drawCells(Graphics2D g) {
        BoardCell[][] cells = game.getBoardCells();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                drawBlock(g, BOARD_CORNER_X + i * 20, BOARD_CORNER_Y + (19 - j) * 20, getBoardCellColor(cells[i][j]));
            }
        }
    }

    private void drawEmptyBoard(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
        g.setColor(Color.GRAY);
        g.drawRect(BOARD_CORNER_X - 1, BOARD_CORNER_Y - 1, 10 * PIECE_WIDTH + 2, 20 * PIECE_WIDTH + 2);
    }

    private void drawStatus(Graphics2D g) {
        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        g.setColor(Color.RED);
        g.drawString(getLevel(), 10, 20);
        g.drawString(getLines(), 10, 40);
        g.drawString(getScore(), 20, 80);
    }

    private void drawGameOver(Graphics2D g) {
        Font font = new Font("Dialog", Font.PLAIN, 16);
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("GAME OVER", 350, 550);
    }

    private void drawGamePaused(Graphics2D g) {
        Font font = new Font("Dialog", Font.PLAIN, 16);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("GAME PAUSED", 350, 550);
    }


    private void drawPlayTetris(Graphics2D g) {
        Font font = new Font("Dialog", Font.PLAIN, 16);
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Play TETRIS !", 350, 500);
    }

    private String getLevel() {
        return String.format("Your level: %1s", game.getLevel());
    }

    private String getLines() {
        return String.format("Full lines: %1s", game.getLines());
    }

    private String getScore() {
        return String.format("Score     %1s", game.getTotalScore());
    }

    private void drawPiecePreviewBox(Graphics2D g) {
        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        g.setColor(Color.RED);
        g.drawString("Next:", 50, 420);
    }

    private void drawHelpBox(Graphics2D g) {
        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        g.setColor(Color.RED);
        g.drawString("H E L P", 50, 140);
        g.drawString("F1: Pause Game", 10, 160);
        g.drawString("F2: New Game", 10, 180);
        g.drawString("UP: Rotate", 10, 200);
        g.drawString("ARROWS: Move left/right", 10, 220);
        g.drawString("SPACE: Drop", 10, 240);
    }

    private void drawPiecePreview(Graphics2D g, PieceType type) {
        for (Point p : type.getPoints()) {
           System.err.println("type="+type+" p.x="+p.x+" p.y="+p.y);
           drawBlock(g, 60 + p.x * PIECE_WIDTH, 380 + (3 - p.y) * 20, getPieceColor(type));
        }
    }

    private Color getBoardCellColor(BoardCell boardCell) {
        if (boardCell.isEmpty()) {
            return Color.BLACK;
        }
        System.err.println("zzzzzz");
        return getPieceColor(boardCell.getPieceType());
    }

    private Color getPieceColor(PieceType pieceType) {
        if (pieceType == PieceType.I) {
                return Color.RED;
        }
        if(pieceType == PieceType.J) {
                return Color.GRAY;
        }
        if(pieceType == PieceType.L) {
                return Color.CYAN;
        }
        if(pieceType == PieceType.O) {
                return Color.BLUE;
        }
        if(pieceType == PieceType.S) {
                return Color.GREEN;
        }
        return Color.MAGENTA;
    }

    private void drawBlock(Graphics g, int x, int y, Color color) {
       //System.err.println("x="+x+" y="+y+" color="+color);
        g.setColor(color);
        g.fillRect(x, y, PIECE_WIDTH, PIECE_WIDTH);
        g.drawRect(x, y, PIECE_WIDTH, PIECE_WIDTH);
    }

    public static void main(String[] args) {
        new Tetris().gameLoop();
    }

}
class KeyboardInput implements KeyListener {

    private final Map<Integer, Boolean> currentStates = new ConcurrentHashMap<Integer, Boolean>();

    public KeyboardInput() {
        currentStates.put(KeyEvent.VK_LEFT, Boolean.FALSE);
        currentStates.put(KeyEvent.VK_RIGHT, Boolean.FALSE);
        currentStates.put(KeyEvent.VK_UP, Boolean.FALSE);
        currentStates.put(KeyEvent.VK_SPACE, Boolean.FALSE);
        currentStates.put(KeyEvent.VK_F2, Boolean.FALSE);
        currentStates.put(KeyEvent.VK_F1, Boolean.FALSE);
    }

    public boolean left() {
        return keyDown(KeyEvent.VK_LEFT);
    }

    public boolean right() {
        return keyDown(KeyEvent.VK_RIGHT);
    }

    public boolean drop() {
        return keyDown(KeyEvent.VK_SPACE);
    }

    public boolean rotate() {
        return keyDown(KeyEvent.VK_UP);
    }

    public boolean pauseGame() {
        return keyDown(KeyEvent.VK_F1);
    }

    public boolean newGame() {
        return keyDown(KeyEvent.VK_F2);
    }

    private boolean keyDown(int keyCode) {
        return currentStates.put(keyCode, Boolean.FALSE);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (currentStates.containsKey(keyEvent.getKeyCode())) {
            currentStates.put(keyEvent.getKeyCode(), Boolean.TRUE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
enum PieceType {
    
    O(0, p(-1, 0), p(0, 0),  p(-1, -1), p(0, -1)),
    I(2, p(-2, 0), p(-1, 0), p(0, 0),   p(1, 0)),
    S(2, p(0, 0),  p(1, 0),  p(-1, -1), p(0, -1)),
    Z(2, p(-1, 0), p(0, 0),  p(0, -1),  p(1, -1)),
    L(4, p(-1, 0), p(0, 0),  p(1, 0),   p(-1, -1)),
    J(4, p(-1, 0), p(0, 0),  p(1, 0),   p(1, -1)),
    T(4, p(-1, 0), p(0, 0),  p(1, 0),   p(0, -1));

    private static final Random random = new Random();
    private final int maxOrientations;
    private final Point points[];

    PieceType(int maxOrientations, Point... points) {
        this.maxOrientations = maxOrientations;
        this.points = points;
    }

    public static PieceType getRandomPiece() {
        return PieceType.values()[random.nextInt(PieceType.values().length)];
    }

    public Point[] getPoints() {
        return points;
    }

    public int getMaxOrientations() {
        return maxOrientations;
    }

    private static Point p(int x, int y) {
        return new Point(x, y);
    }
}
class Piece {

    private final Point points[];
    private final PieceType type;
    private final boolean initialOrientation;

    private Piece(PieceType pieceType, Point[] points, boolean initial) {
        initialOrientation = initial;
        this.points = points;
        this.type = pieceType;
    }

    public static Piece getRandomPiece() {
        PieceType pieceType = PieceType.getRandomPiece();
        return new Piece(pieceType, pieceType.getPoints(), true);
    }

    public static Piece getPiece(PieceType pieceType) {
        return new Piece(pieceType, pieceType.getPoints(), true);
    }

    public PieceType getType() {
        return type;
    }

    public Point[] getPoints() {
        return points;
    }

    public Piece rotate() {
        if (type.getMaxOrientations() == 0) {
            return this;
        } else if (type.getMaxOrientations() == 2) {
            if (initialOrientation) {
                return new Piece(type, rotateRight(points), false);
            } else {
                return new Piece(type, rotateLeft(points), true);
            }
        }
        return new Piece(type, rotateRight(points), true);
    }

    private Point[] rotateLeft(Point toRotate[]) {
        return rotate(toRotate, 1, -1);
    }

    private Point[] rotateRight(Point toRotate[]) {
        return rotate(toRotate, -1, 1);
    }

    private Point[] rotate(Point toRotate[], int x, int y) {
        Point rotated[] = new Point[4];

        for (int i = 0; i < 4; i++) {
            int temp = toRotate[i].x;
            rotated[i] = new Point(x * toRotate[i].y, y * temp);
        }

        return rotated;
    }

}
class Game {

    private final Board board;
    private Piece nextPiece;

    private boolean playing = false;
    private boolean paused = false;
    private boolean dropping = false;
    private boolean gameOver = false;

    private int freeFallIterations;
    private int totalScore;

    public Game() {
        board = new Board();
    }

    public BoardCell[][] getBoardCells() {
        return board.getBoardWithPiece();
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    public long getIterationDelay() {
        return (long) (((11 - getLevel()) * 0.05) * 1000);
    }

    public int getScore() {
        return ((21 + (3 * getLevel())) - freeFallIterations);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getLines() {
        return board.getFullLines();
    }

    public int getLevel() {
        if ((board.getFullLines() >= 1) && (board.getFullLines() <= 90)) {
            return 1 + ((board.getFullLines() - 1) / 10);
        } else if (board.getFullLines() >= 91) {
            return 10;
        } else {
            return 1;
        }
    }

    public void startGame() {
        paused = false;
        dropping = false;
        nextPiece = Piece.getRandomPiece();
        board.setCurrentPiece(Piece.getRandomPiece());
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void pauseGame() {
        paused = !paused;
    }

    public void moveLeft() {
        board.moveLeft();
    }

    public void moveRight() {
        board.moveRight();
    }

    public void moveDown() {
        if (!board.canCurrentPieceMoveDown()) {

            if (freeFallIterations == 0) {
                playing = false;
                gameOver = true;
            } else {
                dropping = false;
                board.setCurrentPiece(nextPiece);
                nextPiece = Piece.getRandomPiece();
                totalScore += getScore();
                freeFallIterations = 0;
            }
        } else {
            board.moveDown();
            freeFallIterations++;
        }
    }

    public void rotate() {
        board.rotate();
    }

    public void drop() {
        dropping = true;
    }

    public boolean isDropping() {
        return dropping;
    }

}
class BoardCell {

    private final PieceType pieceType;

    private BoardCell() {
        pieceType = null;
    }

    private BoardCell(PieceType type) {
        pieceType = type;
    }

    public boolean isEmpty() {
        return pieceType == null;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public static BoardCell getCell(PieceType pieceType) {
        return new BoardCell(pieceType);
    }

    public static BoardCell[] getEmptyArray(int size) {
        BoardCell[] cells = new BoardCell[size];
        Arrays.fill(cells, new BoardCell());
        return cells;
    }

}
/**
 * 20 [ ][ ][ ][X][X][X][X][ ][ ][ ]
 * 19 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 18 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 17 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 16 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 15 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 14 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 13 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 12 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 11 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 10 [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 9  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 8  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 7  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 6  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 5  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 4  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 3  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 2  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 * 1  [ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]
 *     1  2  3  4  5  6  7  8  9 10
 */
class Board {

    private static final int DROP_X = 5;
    private static final int DROP_Y = 19;

    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;

    private Point pieceCenter = new Point(DROP_X, DROP_Y);

    private Piece currentPiece;

    private BoardCell[][] board = new BoardCell[WIDTH][HEIGHT];

    private int fullLines = 0;

    public Board() {
        board = createEmptyBoard();
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getFullLines() {
        return fullLines;
    }

    public BoardCell getBoardAt(int x, int y) {
        return board[x][y];
    }

    private boolean isRowFull(int y) {
        for (int x = 0; x < WIDTH; x++) {
            if (getBoardAt(x, y).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void removeFullRows() {
        BoardCell[][] boardX = createEmptyBoard();

        int full = 0;
        for (int y = 0; y < HEIGHT; y++) {
            if (isRowFull(y)) {
                full++;
            } else {
                copyRow(boardX, y, y-full);
            }
        }

        fullLines += full;
        board = boardX;
    }

    private void copyRow(BoardCell[][] to, int y, int toy) {
        for (int x = 0; x < WIDTH; x++) {
            to[x][toy] = board[x][y];
        }
    }

    private BoardCell[][] createEmptyBoard() {
        BoardCell[][] boardX = new BoardCell[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            boardX[x] = BoardCell.getEmptyArray(HEIGHT);
        }
        return boardX;
    }

    public void rotate() {
        Piece rot = currentPiece.rotate();
        if (fit(rot.getPoints(), 0, 0)) {

            currentPiece = rot;
        }
    }

    public void moveLeft() {
        if (fit(currentPiece.getPoints(), -1, 0)) {
            mv( -1, 0);
        }
    }

    public void moveRight() {
        if (fit(currentPiece.getPoints(), 1, 0)) {
            mv(1, 0);
        }
    }

    public boolean canCurrentPieceMoveDown() {
        return fit(currentPiece.getPoints(), 0, -1);
    }

    public void moveDown() {
        if (canCurrentPieceMoveDown()) {
            mv(0, -1);
            removeFullRows();
        }
    }

    public boolean fit(Point[] points, int moveX, int moveY) {
        for (Point point : points) {
            int x = pieceCenter.x + point.x + moveX;
            int y = pieceCenter.y + point.y + moveY;

            if (x < 0 || x >= getWidth() || y >= getHeight() || y < 0) {
                return false;
            }

            if (!board[x][y].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public BoardCell[][] getBoardWithPiece() {
        BoardCell[][] dest = new BoardCell[WIDTH][HEIGHT];

        for (int y = 0; y < WIDTH; y++) {
            System.arraycopy(board[y], 0, dest[y], 0, board[0].length);
        }

        // add piece
        for (Point point : currentPiece.getPoints()) {
            int x = point.x + pieceCenter.x;
            int y = point.y + pieceCenter.y;
            dest[x][y] = BoardCell.getCell(currentPiece.getType());
        }

        return dest;
    }

    private void addPieceToBoard() {
        for (Point point : currentPiece.getPoints()) {
            int x = pieceCenter.x + point.x;
            int y = pieceCenter.y + point.y;
            board[x][y] = BoardCell.getCell(currentPiece.getType());
        }
    }

    private void mv(int moveX, int moveY) {
        pieceCenter = new Point(pieceCenter.x + moveX, pieceCenter.y + moveY);
    }

    public void setCurrentPiece(Piece piece) {
        if (currentPiece != null) {
            addPieceToBoard();
        }
        currentPiece = piece;
        resetPieceCenter();
    }

    private void resetPieceCenter() {
        pieceCenter.x = DROP_X;
        pieceCenter.y = DROP_Y;
    }

}