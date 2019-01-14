package android.william.hangman.common;

import java.io.Serializable;

/**
 * A message between a chat client and the chat server.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 4475673338165374707L;
    private final MsgType type;
    private final String stringBody;
    private final GameStateDTO gameState;

    /**
     * Constructs a new <code>Message</code>, with the specified type and body.
     *
     * @param type The message type.
     * @param body The message {@code String} body.
     */
    public Message(MsgType type, String body) {
        this.type = type;
        this.stringBody = body;
        gameState = null;
    }

    /**
     * Constructs a new <code>Message</code>, with the specified type and body.
     *
     * @param type The message type.
     * @param gameState The message {@code GameState} body.
     */
    public Message(MsgType type, GameStateDTO gameState) {
        this.type = type;
        this.stringBody = null;
        this.gameState = gameState;
    }

    /**
     * Constructs a new <code>Message</code>, with the specified type.
     *
     * @param type The message type.
     */
    public Message(MsgType type) {
        this.type = type;
        this.stringBody = null;
        gameState = null;
    }

    /**
     * @return The message {@code String} body
     */
    public String getBody() {
        return stringBody;
    }
    /**
     *
     * @return The message's {@code GameState} body
     */
    public GameStateDTO getGameState() {
        return gameState;
    }

    /**
     * @return The message type
     */
    public MsgType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", body=" + stringBody + '}';
    }
}
