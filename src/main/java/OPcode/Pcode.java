package OPcode;

public class Pcode {
    public OpType opType;
    public int codeLine;
    public int level;
    public String operator;
    public Pcode feature_p;
    public String[] features;

    public Pcode(){

    }
}
enum OpType{
    command,
    feature,
    logic
}