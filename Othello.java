import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.server.ExportException;

public class Othello {

    enum COLOR {NONE, WHITE, BLACK}

    int SIZE = 8;

    public COLOR[][] spaces;

    public Othello() {
        SIZE = getSizeInput();
        initialize();
        setDefaultMiddle();
    }

    public Othello(int size) {
        SIZE = size;
        initialize();
        setDefaultMiddle();
    }

    private void initialize() {
        spaces = new COLOR[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                spaces[x][y] = COLOR.NONE;
            }
        }
    }

    private void setDefaultMiddle() {
        spaces[SIZE / 2 - 1][SIZE / 2] = COLOR.WHITE;
        spaces[SIZE / 2][SIZE / 2 - 1] = COLOR.WHITE;
        spaces[SIZE / 2 - 1][SIZE / 2 - 1] = COLOR.BLACK;
        spaces[SIZE / 2][SIZE / 2] = COLOR.BLACK;
    }

    private String renderSpace(int x, int y) {
        switch (spaces[x][y]) {
            case BLACK:
                return "B";
            case WHITE:
                return "W";
        }
        return " ";
    }


    public void render() {
        String xlabel = " ";
        String slots = "%d";

        for (int i = 0; i < SIZE; i += 1) {
            xlabel = xlabel + " " + (char) ('a' + i);
        }
        System.out.println(xlabel);
        for (int y = 0; y < SIZE; y += 1) {
            System.out.printf("%d", y + 1);
            for (int i = 0; i < SIZE; i += 1) {
                System.out.printf(" %s", renderSpace(i, y));
            }
            System.out.println();
        }
    }


    public void Oldrender() {
        System.out.print("  a b c d e f g h\n");
        for (int y = 0; y < SIZE; y += 1) {
            System.out.printf("%d %s %s %s %s %s %s %s %s\n",
                    y + 1,
                    renderSpace(0, y),
                    renderSpace(1, y),
                    renderSpace(2, y),
                    renderSpace(3, y),
                    renderSpace(4, y),
                    renderSpace(5, y),
                    renderSpace(6, y),
                    renderSpace(7, y)
            );
        }
    }

    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }

    private int getSizeInput() {
        try {
            System.out.print("The size of the board: ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine().strip();
            return Integer.parseInt(text);

        } catch (Exception e) {
            return getSizeInput();
        }
    }

    private Coordinate getMoveInput() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine();
            return new Coordinate(
                    Math.max(0, Math.min(SIZE - 1, text.charAt(0) - 'a')),
                    Math.max(0, Math.min(SIZE - 1, Character.digit(text.charAt(1), 10) - 1))
            );
        } catch (Exception e) {
            return getMoveInput();
        }
    }

    public void play() {
        while (true) {
            boolean placed = false;
            while (!placed) {
                render();
                System.out.println("YOUR TURN!");
                Coordinate c = getMoveInput();
                placed = placePiece(COLOR.WHITE, c);
            }
            render();
            placed = false;
            while (!placed) {
                Coordinate c = new Coordinate((int) (Math.random() * SIZE), (int) (Math.random() * SIZE));
                placed = placePiece(COLOR.BLACK, c);
            }
            System.out.println("COMPUTER JUST PUT DOWN A PIECE!");
        }
    }

    //TODO winning condition!

    private boolean placePiece(COLOR color, Coordinate c) {
        boolean placed = false;
        if (spaces[c.x][c.y] == COLOR.NONE) {
            int[][] directions = {{-1, 1}, {-1, -1}, {-1, 0}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {0, -1}};
            for (int[] d : directions) {
                int i = d[0];
                int j = d[1];
                int x = c.x;
                int y = c.y;
                while (x + i >= 0 && x + i < spaces.length && y + j >= 0 && y + j < spaces[0].length
                        && (spaces[x + i][y + j] != COLOR.NONE)) {
                    x += i;
                    y += j;
                }
                if (spaces[x][y] == color) {
                    while (x != c.x || y != c.y) {  // it's OR here!!! not AND!!!
                        x -= i;
                        y -= j;
                        spaces[x][y] = color;
                    }
                    placed = true;
                }
            }
        }
        return placed;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        Othello b = new Othello();
        b.play();
    }
}

