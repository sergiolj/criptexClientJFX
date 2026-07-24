package model;

import shared.remote.ClientInterface;

import java.io.Serial;
import java.io.Serializable;
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
    private int score = 0;

    /** Parâmetro que cria um identificador único para o jogador (UUID Universally Unique Identifier) */
    private final UUID uuid;
    private final String name;

    public Player(String name) {
         this.uuid = UUID.randomUUID();
         this.name = name;
    }

    public void updateScore(){
        score++;
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
