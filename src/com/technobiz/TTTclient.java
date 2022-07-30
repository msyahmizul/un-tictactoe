package com.technobiz;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class TTTclient {
    private Socket socket;
    private int playerType;

    public TTTclient(int port) {
        if (port == 7001) {
            this.playerType = TTTBoard.CROSS_PLAYER;
        } else {
            this.playerType = TTTBoard.CIRCLE_PLAYER;
        }
        System.out.println("Tic Tac Toe Game");
        try {
            this.socket = new Socket();
            this.socket.bind(new InetSocketAddress(port));
            this.socket.connect(new InetSocketAddress("localhost", TTTserver.SERVER_PORT));
            System.out.println("Connection Success");
        } catch (IOException e) {
            System.out.println("Error Bind Port");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TTTclient client = new TTTclient(Integer.parseInt(args[0]));
        System.out.println("Start");
        client.start();

    }

    public void start() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            int code = Integer.parseInt(reader.readLine());
            if (code == TTTserver.CODE_NO_ERROR) {
                if (playerType == TTTBoard.CROSS_PLAYER) {
                    System.out.println("You are Player 'X'");
                } else {
                    System.out.println("You are Player 'O'");
                }
                this.waitingInput();
                this.cleanUp();
            } else {
                System.out.println("Server Not Ready Shutting down");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanUp() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitingInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("Waiting Other Player");
        System.out.println("Press q to quit");
        System.out.println("Press c to continue");
        System.out.print("Input: ");
        String input = in.nextLine();
        if (input.equals("c")) {
            this.waitingStage();
        }
    }

    public void waitingStage() {
        System.out.println("Waiting Other Player");
        do {
            this.sendWaiting();
        } while (this.isWaitingReply());
        System.out.println("Other Player Ready");

    }

    private void sendWaiting() {
        try {
            OutputStream output = this.socket.getOutputStream();
//            System.out.println("Send out reply");
            PrintWriter writer = new PrintWriter(output, true);
            if (this.playerType == TTTBoard.CROSS_PLAYER) {
                writer.println("1R");
            } else {
                writer.println("2R");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isWaitingReply() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String code_status = reader.readLine();
            if (this.playerType == TTTBoard.CROSS_PLAYER) {
                return code_status.equals("2W");
            } else {
                return code_status.equals("1W");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public void ping_pong() {
//        while (true) {
//            try {
//                Thread.sleep(2000);
//                Socket send_socket = new Socket("localhost", TTTserver.SERVER_PORT);
//                OutputStream output = send_socket.getOutputStream();
//                PrintWriter writer = new PrintWriter(output, true);
//                writer.println("ping 1");
//                send_socket.close();
//                Socket socket = this.serverSocket.accept();
//                InputStream input = socket.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                String line = reader.readLine();
//                System.out.println(line);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }


//    public static void runClient(String[] args) {
//        String hostname = "127.0.0.1";
//        int server_port = 2000;
//        try (Socket socket = new Socket(hostname, server_port)) {
//            OutputStream output = socket.getOutputStream();
//            PrintWriter writer = new PrintWriter(output, true);
//
//            writer.println("UwU");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
