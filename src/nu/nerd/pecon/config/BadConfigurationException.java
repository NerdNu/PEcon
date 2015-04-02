package nu.nerd.pecon.config;

/**
 * An exception to be thrown when an error occurs in the plugin's
 * configuration.
 */
public class BadConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 4775520909416422403L;

    /**
     * Constructs a new {@code BadConfigurationException} with the given
     * message.
     *
     * @param message the message
     */
    public BadConfigurationException(String message) {
        super(message);
    }

}
