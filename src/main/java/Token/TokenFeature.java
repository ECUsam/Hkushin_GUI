package Token;

public class TokenFeature extends Token{
    public int num_feature;
    public String[] strings_feature;
    public TokenFeature(int num_feature) {
        super(TokenClass.Tk_feature);
        this.num_feature = num_feature;
    }
    public TokenFeature(String... strings){
        super(TokenClass.Tk_feature);
        strings_feature = strings;
    }

}
