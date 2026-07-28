package rmi.network;

import model.Player;
import shared.config.Config;
import shared.dto.GuessRequest;
import shared.dto.GuessResponse;
import shared.remote.Ranking;
import shared.remote.ServerInterface;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


/**
 *
 * Deve armazenar as informações do jogo como tentativas e tempo decorrido além de gerenciar a conexão e comunicações
 * entre o servidor e cliente.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 2.0
 */
public class GameSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static GameSession instance;
    private int attempt = 0;
    private Player player;
    private boolean isConnected = false;
    private boolean wordMatch = false;
    private ServerInterface proxy;


    public static synchronized GameSession getInstance() throws RemoteException {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    private GameSession(){}

    public void connect(Player player) throws RemoteException {
        try {
            if (isConnected) {
                disconnect();
            }

            this.player = player;
            try {
                Registry registry = LocateRegistry.getRegistry(Config.IP_ADDRESS, Config.SERVER_PORT);
                this.proxy = (ServerInterface) registry.lookup(Config.SERVER_NAME);
                this.proxy.loginUser(player);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
            isConnected = true;
        } catch (RemoteException e) {
            isConnected = false;
            throw new RemoteException(e.getMessage());
        }
    }

    public void disconnect() throws RemoteException {
        try {
            if (player != null) {
                proxy.logoutUser(this.player);
                instance = null;
            }
            isConnected = false;
        } catch (Exception e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public GuessResponse verifyGuess(String guess) throws RemoteException {
        if (noAttemptsLeft()) {
            System.out.println(this.player.getName());
            System.out.println("Você não possui mais tentativas.");
            return new GuessResponse(null, null, false, attempt);
        }
        System.out.println("Tentativas registradas " + attempt);

        GuessRequest request = new GuessRequest(player.getUuid(), guess);
        GuessResponse response = this.proxy.verifyGuess(request);
        if (response != null) {
            registerAttempt();

            GuessResponse responseWithAttempt = new GuessResponse(
                    response.getStatus(),
                    response.getGuess(),
                    response.isWordMatch(),
                    attempt
            );

            System.out.println("Tentativa registrada! " + attempt);
            if (responseWithAttempt.isWordMatch()) {
                player.updateScore();
                wordMatch = true;
            }

            return responseWithAttempt;
        }
        return null;
    }

    public void registerAttempt() {
        attempt++;
    }

    public int getAttempt() {
        return attempt;
    }

    public boolean noAttemptsLeft() {
        int maxAttempts = 6;
        return attempt >= maxAttempts;
    }

    public List<Ranking> getRanking() throws RemoteException {
        return proxy.getRanking();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isWordMatch(){
        return wordMatch;
    }
}
