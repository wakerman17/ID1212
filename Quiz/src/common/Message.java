package common;

import java.io.Serializable;

/**
 * A message between a client and the server.
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 4475673338165374707L;
	private final MsgType type;
    private final String stringBody;
    private final GameStateDTO gameStateDTO;

    /**
     * Constructs a new <code>Message</code>, with the specified type and body.
     *
     * @param type The message type.
     * @param body The message {@code String} body.
     */
    public Message(MsgType type, String body) {
        this.type = type;
        this.stringBody = body;
        gameStateDTO = null;
    }
    
    /**
     * Constructs a new <code>Message</code>, with the specified type and body.
     * 
     * @param type The message type.
     * @param gameState The message {@code GameState} body.
     */
    public Message(MsgType type, GameStateDTO gameStateDTO) {
        this.type = type;
        this.stringBody = null;
        this.gameStateDTO = gameStateDTO; 
    }
    
    /**
     * Constructs a new <code>Message</code>, with the specified type.
     * 
     * @param type The message type.
     */
    public Message(MsgType type) {
        this.type = type;
        this.stringBody = null;
        gameStateDTO = null;
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
    public GameStateDTO getGameStateDTO() {
        return gameStateDTO;
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
