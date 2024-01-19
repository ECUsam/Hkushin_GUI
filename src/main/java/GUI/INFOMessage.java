package GUI;

public class INFOMessage {
    public INFORMATION_TYPE informationType;
    public Object message;
    public INFOMessage(INFORMATION_TYPE informationType, Object message){
        this.informationType = informationType;
        this.message = message;
    }
}
