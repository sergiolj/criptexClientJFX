package service;

import client.Player;

public class PlayerManagerRMI {
    private static PlayerManagerRMI instance;
    private Player player;
    private boolean isConnected = false;

    private PlayerManagerRMI(){}

    public static synchronized PlayerManagerRMI getInstance() {
        if (instance == null) {
            instance = new PlayerManagerRMI();
        }
        return instance;
    }

    public boolean connect(String username, String host, int port) {
        try {
            if (isConnected) {
                disconnect();
            }
            player = new Player(username);
            isConnected = true;
            return true;
        } catch (Exception e) {
            System.err.println("Falha na conexão RMI: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    public void disconnect() {
        try {
            if (player != null) {
                player = null;
            }
            isConnected = false;
        } catch (Exception e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
