package com.technobiz;

import java.util.Arrays;
import java.util.Hashtable;


public class TTTBoard {
    private int currentPlayer;

    private String[] board = new String[9];

    public static final int CROSS_PLAYER = 1;
    public static final int CIRCLE_PLAYER = 2;

    // game Status
    public static final int GAME_INIT = 1;


    public static final int DRAW = 1;
    public static final int CONTINUE = 2;

//    private final Hashtable<Integer, Integer> playerData = new Hashtable<Integer, Integer>();
//    public void setPlayer(int playerPort) {
//
//    }

    public void startGame(int playerFirst) {
        this.initGame();
        this.currentPlayer = playerFirst;
    }

    public int playerMove(int position) {
        if (!this.board[position].equals(" ")) {
            System.out.println("Error");
            return TTTserver.CODE_INVALID_LAST_MODE;
        }
        this.board[position] = this.inputChar();
        this.changePlayerMode();
        return TTTserver.CODE_NO_ERROR;
    }

    private String inputChar() {
        if (this.currentPlayer == CROSS_PLAYER) {
            return "X";
        } else {
            return "O";
        }
    }

    private void changePlayerMode() {
        if (this.currentPlayer == CROSS_PLAYER) {
            this.currentPlayer = CIRCLE_PLAYER;
        } else {
            this.currentPlayer = CROSS_PLAYER;
        }
    }

    private void initGame() {
        for (int row = 0; row < 9; ++row) {
            this.board[row] = " ";
        }
    }

    public int checkBoard() {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> board[0] + board[1] + board[2];
                case 1 -> board[3] + board[4] + board[5];
                case 2 -> board[6] + board[7] + board[8];
                case 3 -> board[0] + board[3] + board[6];
                case 4 -> board[1] + board[4] + board[7];
                case 5 -> board[2] + board[5] + board[8];
                case 6 -> board[0] + board[4] + board[8];
                case 7 -> board[2] + board[4] + board[6];
                default -> null;
            };
            if (line.equals("XXX")) {
                return CROSS_PLAYER;
            } else if (line.equals("OOO")) {
                return CIRCLE_PLAYER;
            }
        }
        for (int a = 0; a < 9; a++) {
            if (this.board[a].equals(" ")) {
                return CONTINUE;
            }
        }
        return DRAW;
    }

    public String[] getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public static void printBoard(String[] board) {
        System.out.println("/---|---|---\\");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("|-----------|");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("|-----------|");
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("/---|---|---\\");
    }
}
