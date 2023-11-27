package Token;
import Constants.Constants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//我为什么要写这个？
public class TokenPool {
    private static final int POOL_SIZE = Constants.POOL_SIZE;
    private static final BlockingQueue<Token> pool = new ArrayBlockingQueue<>(POOL_SIZE);

    static {
        for (int i=0; i<POOL_SIZE; i++){
            pool.offer(new Token(TokenClass.TK_UNKONWN));
        }
    }

    public static Token getToken(){
        Token token = null;
        try{
            token = pool.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return token;
    }
    public static void releaseToken(Token token) {
        try {
            pool.put(token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
