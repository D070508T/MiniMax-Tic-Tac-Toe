import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String playAgain;

        System.out.println("""
                 ---------- <<< WELCOME TO MINIMAX TIC-TAC-TOE >>> ----------
                
                You are 'O'
                Minimax is 'X'
                You start!
                """);
        do {
            totalCalculations = 0;
            char[][] board = {{'1', '2', '3'}, {'4', '5', '6'}, {'7', '8', '9'}};

            displayBoard(board);
            String input;
            while (notGameOver(board)) {
                do {
                    System.out.print("Choose a spot to place: ");
                    input = scanner.nextLine();
                } while (getNumberInput(board, input) == 0);
                makeMove(1, board, getNumberInput(board, input));
                if (notGameOver(board)) {
                    int[] ai = getBestMove(board);
                    board[ai[0]][ai[1]] = 'X';
                }
                displayBoard(board);
            }
            if (getWinner(board) == 'X') {
                System.out.println("YOU LOSE! MINIMAX BEAT YOU");
            } else if (getWinner(board) == 'O') {
                System.out.println("YOU WIN!");
            } else {
                System.out.println("TIE");
            }

            System.out.println("Total calculations made: " + totalCalculations);
            System.out.print("Would you like to play again? (y/n): ");
            playAgain = scanner.nextLine();
        } while (playAgain.equalsIgnoreCase("Y"));
    }

    public static boolean notGameOver(char[][] board) {
        return !isTie(board) && getWinner(board) == '_';
    }

    public static void resetMove(char[][] board, int[] move) {
        int digit;
        if (move[0] == 0) {
            digit = move[1] + 1;
        } else if (move[0] == 1) {
            digit = move[1] + 4;
        } else {
            digit = move[1] + 7;
        }

        board[move[0]][move[1]] = Character.forDigit(digit, 10);
    }

    public static int miniMaxCalls;
    public static int totalCalculations;
    public static int minimax(char[][] board, boolean isMaximizing) {
        miniMaxCalls++;
        totalCalculations++;
        if (getWinner(board) == 'X') {
            return 1;
        } else if (getWinner(board) == 'O') {
            return -1;
        } else if (isTie(board)) {
            return 0;
        }

        ArrayList<int[]> moves = getAvailableMoves(board);
        int bestScore;

        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int[] move : moves) {
                board[move[0]][move[1]] = 'X';
                int score = minimax(board, false);

                resetMove(board, move);

                bestScore = Math.max(bestScore, score);
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int[] move : moves) {
                board[move[0]][move[1]] = 'O';
                int score = minimax(board, true);

                resetMove(board, move);

                bestScore = Math.min(bestScore, score);
            }
        }

        return bestScore;
    }

    public static int[] getBestMove(char[][] board) {
        miniMaxCalls = 0;
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : getAvailableMoves(board)) {
            board[move[0]][move[1]] = 'X';
            int score = minimax(board, false);

            resetMove(board, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        System.out.println("Calculations made: " + miniMaxCalls);
        return bestMove;
    }

    public static ArrayList<int[]> getAvailableMoves(char[][] board) {
        ArrayList<int[]> list = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 'O' && board[i][j] != 'X') {
                    list.add(new int[]{i, j});
                }
            }
        }

        return list;
    }

    public static void makeMove(int plr, char[][] board, int num) {
        if (plr == 1) {
            board[convertToIndexes(num)[0]][convertToIndexes(num)[1]] = 'O';
        } else {
            board[convertToIndexes(num)[0]][convertToIndexes(num)[1]] = 'X';
        }
    }

    public static int[] convertToIndexes(int num) {
        int[] index;
        if (num <= 3) {
            index = new int[]{0, num - 1};
        } else {
            if (num <= 6) {
                index = new int[]{1, num - 4};
            } else {
                index = new int[]{2, num - 7};
            }
        }

        return index;
    }

    public static int getNumberInput(char[][] board, String input) {
        if (input.length() != 1 ||
                (int) input.charAt(0) < 49 ||
                (int) input.charAt(0) > 57 ||
                getBoardIndex(board, (int) input.charAt(0) - 48) != input.charAt(0)) {
            System.out.println("ERROR OCCURRED. TRY AGAIN");
            return 0;
        }

        return (int) input.charAt(0) - 48;
    }

    public static char getBoardIndex(char[][] board, int num) {
        return board[convertToIndexes(num)[0]][convertToIndexes(num)[1]];
    }

    public static void displayBoard(char[][] board) {
        System.out.print("""
                \n     |     |
                """);
        System.out.println("  " + board[0][0] + "  |  " + board[0][1] + "  |  " + board[0][2]);
        System.out.print("""
                     |     |
                -----+-----+-----
                     |     |
                """);
        System.out.println("  " + board[1][0] + "  |  " + board[1][1] + "  |  " + board[1][2]);
        System.out.print("""
                     |     |
                -----+-----+-----
                     |     |
                """);
        System.out.println("  " + board[2][0] + "  |  " + board[2][1] + "  |  " + board[2][2]);
        System.out.println("     |     |");
    }

    public static boolean isTie(char[][] board) {
        for (char[] i : board) {
            for (char c : i) {
                if (c != 'X' && c != 'O') {
                    return false;
                }
            }
        }

        return getWinner(board) == '_';
    }

    public static char getWinner(char[][] board) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0]; // Row win
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i]; // Column win
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]; // Diagonal win
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]; // Diagonal win
        }
        return '_'; // No winner
    }
}