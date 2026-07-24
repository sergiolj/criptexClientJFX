package service;

import client.Player;

import java.rmi.RemoteException;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
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

    public boolean connect(String username, String host, int port) throws RemoteException{
        try {
            if (isConnected) {
                disconnect();
            }
            player = new Player(username);
            isConnected = true;
            return true;
        } catch (RemoteException e) {
            isConnected = false;
            throw new RemoteException(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (player != null) {
                player.disconnect();
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
