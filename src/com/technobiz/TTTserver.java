package com.technobiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TTTserver implements Runnable {
    // STATUS_CODE
    public static final int CODE_NO_ERROR = 0;
    public static final int CODE_INVALID_LAST_MODE = 1;
    public static final int CODE_GAME_FORFIET = 2;

    private volatile int totalPlayer = 0;

    private TTTBoard boardClass = new TTTBoard();

    public static int SERVER_PORT = 2000;
    private Socket socket;

    public TTTserver(Socket socket) {
        this.socket = socket;

    }

    private void newConnection() {
        this.sendReply("0");
        this.isPlayerReady();
//        if (this.totalPlayer == 0) {
//
//        }
//        if (this.totalPlayer == 1) {
//            this.boardClass.startGame(TTTBoard.CROSS_PLAYER);
//            this.waitOtherPlayer(socket);
//        }
    }

    private void isPlayerReady() {
        try {
            if (this.waitReply().charAt(1) == 'R') {
                synchronized (this) {
                    this.totalPlayer += 1;
                }
                if (this.totalPlayer == 1) {
                    this.waitOtherPlayer();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendReply(String data) {
        try {
            OutputStream output = this.socket.getOutputStream();
//            System.out.println("Send Reply: " + data);
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String waitReply() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        return reader.readLine();
    }

    private void sendWaitCommand() {
        this.sendReply("2W");

    }

    private void waitOtherPlayer() {
        while (true) {
            try {
                this.sendReply("2W");
                this.waitReply();
                if (this.totalPlayer == 2) {
                    System.out.println("Player Ready");
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGame() {

    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Server Is Run");
                this.newConnection();
                this.socket.close();
            } catch (IOException e) {
                System.out.println("Unknown Error");
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ssock = new ServerSocket(SERVER_PORT);
        System.out.println("Server UP");
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Client Connected");
            new Thread(new TTTserver(sock)).start();
        }

    }
}

//    @Override
//    public void run() {
//        super.run();
//    }
//    public static void runServer(String[] args) {
//        // Check if args supply for the port number
////        if (args.length < 1) return;
//        int port = Integer.parseInt("2000");
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("Listening on port : " + port);
//            while (true) {
//                Socket socket = serverSocket.accept();
//                //System.out.println("New Client Connected");
//                InputStream input = socket.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                String line = reader.readLine();
//                System.out.println(line);
//                socket.close();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//
//        }
//    }
