package Token;

public class TokenCommand extends Token{
    private final String commandName;
    private final String[] features;
    public TokenCommand(String commandName, String... strings) {
        super(TokenClass.TK_COMMAND);
        this.commandName = commandName;
        this.features = strings;
    }
    public String[] getFeatures(){return features;}
    private String getCommandName(){return commandName;}
}
