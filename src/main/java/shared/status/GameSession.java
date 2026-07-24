package shared.status;

import client.Player;
import shared.config.Config;
import shared.dto.GuessRequest;
import shared.dto.GuessResponse;
import shared.remote.ServerInterface;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;


/**
 *
 * Deve armazenar as informações do jogo como tentativas e tempo decorrido.
 *
 * @author Bruna Brito Muniz Filgueiras
 * @author Laís de Assis Doria da Silva
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class GameSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static GameSession instance;
    private int attempt = 0;
    private static Instant startTime;
    private Player player;
    private boolean isConnected = false;
    private ServerInterface proxy;


    public static synchronized GameSession getInstance() throws RemoteException {
        if (instance == null) {
            instance = new GameSession();
            startTime = Instant.now();
        }
        return instance;
    }

    public GuessResponse verifyGuess(String guess) throws RemoteException {
        if (noAttemptsLeft()) {
            System.out.println(this.player.getName() + " | " + startTime);
            System.out.println("Você não possui mais tentativas.");
            return null;
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
                    getAttempt()
            );

            System.out.println("Tentativa registrada! " + attempt);
            if (responseWithAttempt.isWordMatch()) {
                player.updateScore();
            }

            return responseWithAttempt;
        }
        return null;
    }

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

    private GameSession(){}

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


    public Player getPlayer() {
        return player;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
