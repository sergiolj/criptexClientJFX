package client;

import util.Word;
import shared.status.GameSession;
import shared.config.Config;
import shared.dto.GuessRequestDTO;
import shared.dto.GuessResponseDTO;
import shared.remote.ClientInterface;
import shared.remote.ServerInterface;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

            this.proxy.registerUser(this);
            this.gameSession = new GameSession();

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param guess
     * @throws RemoteException
     */

    public GuessResponseDTO verifyGuess(String guess) throws RemoteException {
        guess = Word.removeAccent(guess);

        if (!gameSession.hasAttempts()) {
            System.out.println("Você não possui mais tentativas.");
            return null;
        }
        System.out.println("Existem tentativas");

        if (!Word.validate(guess)) {
            System.out.println("Palpite inválido. A palavra precisa ter exatamente 5 letras.");
            return null;
        }
        System.out.println("Palpite aceito");

        GuessRequestDTO request = new GuessRequestDTO(this.uuid, guess);
        GuessResponseDTO response = this.proxy.verifyGuess(request);
        gameSession.increaseAttempt();

        GuessResponseDTO responseWithAttempt = new GuessResponseDTO(
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

    public GameSession getGameSession() {
        return gameSession;
    }

    public int getAttempts() {
        return gameSession.getAttempt();
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
