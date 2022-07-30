package com.technobiz;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
	
	public static void main(String[] args) {
		TTTBoard board = new TTTBoard();
		board.startGame(TTTBoard.CROSS_PLAYER);
		board.playerMove(0);
		board.playerMove(3);
		board.playerMove(1);
		board.playerMove(2);
		board.playerMove(4);
		board.playerMove(7);
		board.playerMove(6);
		board.playerMove(8);
		board.playerMove(5);
		
		TTTBoard.printBoard(board.getBoard());
		System.out.println(board.checkBoard());
		
		
		try {
			final ServerSocket     serverSocket = new ServerSocket(2000);
			final TicTacToe.Server server       = new TicTacToe.Server(serverSocket);
			
			System.out.println("Server running");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		try {
			TicTacToe.Board board1 = new TicTacToe.Board(3);
			System.out.println("\n" + board1 + "\n");
			
			
			board1.setSymbolTo(0, 0, TicTacToe.Role.CROSS);
			System.out.println("\n" + board1 + "\n");
			
			
			board1.setSymbolTo(0, 1, TicTacToe.Role.CIRCLE);
			System.out.println("\n" + board1 + "\n");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
