package com.technobiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class TicTacToe {
	
	public static class Server {
		
		private final ExecutorService       executor = Executors.newSingleThreadScheduledExecutor();
		private final BlockingQueue<Socket> queue    = new LinkedBlockingQueue<>();
		
		private final Board             board   = new Board(3);
		private final ArrayList<Player> players = new ArrayList<>();
		
		
		private final ServerSocket socket;
		
		public Server(ServerSocket socket) {
			this.socket = socket;
			
			executor.execute(()->{
				while (true) {
					try {
						final Socket clientSocket = socket.accept();
						System.out.println("Client Connected");
						queue.add(clientSocket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			while (true) {
				try {
					final Socket clientSocket = queue.take();
					final Client client       = new Client(clientSocket);
					if (players.size() > 2) {
						System.out.println("Reached maximum client, rejecting");
						client.streamWrite("Reached maximum client, sorry");
						clientSocket.close();
					} else {
						final Role role = getAvailableRole();
						if (role == null) {
							System.out.println("Roles unavailable");
							client.streamWrite("Sorry, there is no roles left for you");
							clientSocket.close();
						} else {
							client.streamWrite("Hello, what is your name?");
							final String username = client.streamRead();
							final Player player   = new Player(client, username, role);
							
							players.add(player);
							onPlayerJoined();
						}
						
						
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private Role getAvailableRole() {
			Role[]          symbols = new Role[]{Role.CIRCLE, Role.CROSS};
			ArrayList<Role> roles   = new ArrayList<>();
			Collections.addAll(roles, symbols);
			for (Player player : players) {
				roles.remove(player.role);
			}
			
			return roles.isEmpty() ? null : roles.get(new Random().nextInt(roles.size()));
		}
		
		private void onPlayerJoined() {
			if (players.size() < 2) {
				for (Player player : players) {
					try {
						player.tellWait();
					} catch (IOException e) {
						System.out.println("Player is not responding");
					}
				}
			} else if (players.size() == 2) {
				for (Player player : players) {
					try {
						player.tellReady();
					} catch (IOException e) {
						System.out.println("Player is not responding");
					}
				}
			} else {
				final Player player = players.remove(players.size() - 1);
				try {
					player.streamWrite("Sorry, the server already have other players");
					player.quit();
				} catch (IOException e) {
					System.out.println("Player is not responding");
				}
				onPlayerJoined();
			}
		}
		
	}
	
	
	
	public static class Client {
		
		protected final Socket socket;
		
		public Client(Socket socket) {
			this.socket = socket;
		}
		
		public void streamWrite(String text) throws IOException {
			final OutputStream       outputStream = socket.getOutputStream();
			final OutputStreamWriter writer       = new OutputStreamWriter(outputStream);
			writer.write(text);
			writer.close();
		}
		
		public String streamRead() throws IOException {
			final InputStream       inputStream    = socket.getInputStream();
			final InputStreamReader reader         = new InputStreamReader(inputStream);
			final BufferedReader    bufferedReader = new BufferedReader(reader);
			final String            text           = bufferedReader.readLine();
			
			bufferedReader.close();
			return text;
		}
		
	}
	
	public static class Player
		extends Client {
		
		
		private final String username;
		private final Role   role;
		
		public Player(Client client, String username, Role role) {
			super(client.socket);
			
			this.username = username;
			this.role     = role;
		}
		
		
		
		public void tellWait() throws IOException {
			streamWrite("2W");
		}
		
		public void tellReady() throws IOException {
		
		}
		
		public void tellStart() throws IOException {
		
		}
		
		public void quit() throws IOException {
			socket.close();
		}
		
	}
	
	
	public static class Board {
		
		
		public final int width;
		public final int height;
		
		
		private final Role[] length;
		
		public Board(int widthHeight) {
			
			width = height = widthHeight;
			
			length = new Role[width * height];
			Arrays.fill(length, Role.EMPTY);
		}
		
		public int indexOfSymbol(int x, int y) {
			int positionX = Math.min(x, width - 1);
			int positionY = Math.min(y, height - 1);
			
			return positionX + (positionY * (height));
		}
		
		public void setSymbolTo(int x, int y, Role role) {
			role = role != null ? role : Role.EMPTY;
			
			int indexSymbol = indexOfSymbol(x, y);
			
			length[indexSymbol] = role;
		}
		
		@Override
		public String toString() {
			
			return new StringBuilder()
				
				// Outer Boarder
				.append("/---|---|---\\").append("\n")
				
				// Row index 0
				.append("| ")
				.append(length[0].toString()).append(" | ")
				.append(length[1].toString()).append(" | ")
				.append(length[2].toString()).append(" |")
				
				// Separator
				.append("\n").append("|-----------|").append("\n")
				
				// Row index 1
				.append("| ")
				.append(length[3].toString()).append(" | ")
				.append(length[4].toString()).append(" | ")
				.append(length[5].toString()).append(" |")
				
				// Separator
				.append("\n").append("|-----------|").append("\n")
				
				// Row index 2
				.append("| ")
				.append(length[6].toString()).append(" | ")
				.append(length[7].toString()).append(" | ")
				.append(length[8].toString()).append(" |")
				
				// Outer Boarder
				.append("\n").append("\\---|---|---/")
				
				.toString();
		}
		
	}
	
	public static class Role {
		
		
		public static final Role EMPTY  = new Role(" ");
		public static final Role CROSS  = new Role("X");
		public static final Role CIRCLE = new Role("O");
		
		private final String name;
		
		private Role(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
	}
	
	
}
