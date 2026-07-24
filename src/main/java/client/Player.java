package client;

import service.PlayerManagerRMI;
import shared.status.GameSession;
import shared.config.Config;
import shared.dto.GuessRequest;
import shared.dto.GuessResponse;
import shared.remote.ClientInterface;
import shared.remote.ServerInterface;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

/**
 *
 * Classe com as regras de negócio para a parte do jogador.
 *
 *
 * @author Bruna Brito Muniz Filgueiras
 * @author Laís de Assis Doria da Silva
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class Player implements Serializable, ClientInterface {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ServerInterface proxy;
    private int score = 0;
    private final GameSession gameSession;

    /** Parâmetro que cria um identificador único para o jogador (UUID Universally Unique Identifier) */
    private final UUID uuid;
    private final String name;

     public Player() throws RemoteException {
         this("Anonymous");
    }

    public Player(String name) throws RemoteException {
        try {
            this.uuid = UUID.randomUUID();
            this.name = name;
            Registry registry = LocateRegistry.getRegistry(Config.IP_ADDRESS, Config.SERVER_PORT);
            this.proxy = (ServerInterface) registry.lookup(Config.SERVER_NAME);

            this.proxy.loginUser(this);
            this.gameSession = new GameSession();

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @throws RemoteException
     */

    public void disconnect(){
        if(proxy != null){
            try{
                proxy.logoutUser(this);
                /* Não foi implementado CallBack. Player não extende UnicastRemoteObject e não abre portas de acesso para
                 o servidor enviar ativamente atualizações, logo não há objeto para ser fechado (unexport).
                UnicastRemoteObject.unexportObject(this, true);
                */
            }catch (RemoteException e){
                System.out.println("Erro ao tentar desconectar:  " + e.getMessage());
            }
        }
    }


    public GuessResponse verifyGuess(String guess) throws RemoteException {
        if (!gameSession.hasAttempts()) {
            System.out.println("Você não possui mais tentativas.");
            return null;
        }
        System.out.println("Existem tentativas");

        GuessRequest request = new GuessRequest(this.uuid, guess);
        GuessResponse response = this.proxy.verifyGuess(request);
        if (response != null) {
            gameSession.registerAttempt();

            GuessResponse responseWithAttempt = new GuessResponse(
                    response.getStatus(),
                    response.getGuess(),
                    response.isWordMatch(),
                    gameSession.getAttempt()
            );

            if (responseWithAttempt.isWordMatch()) {
                score++;
            }

            return responseWithAttempt;
        }
        return null;
    }

    public GameSession getGameSession() {
        return gameSession;
    }


    public int getScore() {
        return score;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public UUID getUuid() {
        return uuid;
    }

}
