package GUI;

import postfix.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class INFORMATION {
    private BlockingQueue<INFOMessage> messageQueue = new LinkedBlockingQueue<>();
    private List<INTERFACE> observers = new ArrayList<>();
    private static INFORMATION instance;

    private INFORMATION() {
        // 私有构造函数，防止直接实例化
        startMessageProcessor(); // 启动消息处理线程
    }

    public static INFORMATION getInstance() {
        if (instance == null) {
            instance = new INFORMATION();
        }
        return instance;
    }

    public void addObserver(INTERFACE observer) {
        observers.add(observer);
    }

    public void removeObserver(INTERFACE observer) {
        observers.remove(observer);
    }

    private void notifyObservers(INFOMessage message) {
        for (INTERFACE observer : observers) {
            observer.update(message.informationType, message.message);
        }
    }

    public void sendMessage(INFOMessage infoMessage) {
        LogManager.addLog("Message sent: " + infoMessage.message);
        messageQueue.offer(infoMessage); // 将消息添加到队列
    }

    public void sendMessage(INFORMATION_TYPE infoMessage) {
        LogManager.addLog("Message sent: " + infoMessage);
        messageQueue.offer(new INFOMessage(infoMessage, null)); // 将消息添加到队列
    }

    private void startMessageProcessor() {
        // 启动消息处理线程
        Thread messageProcessor = new Thread(() -> {
            while (true) {
                try {
                    INFOMessage message = messageQueue.take(); // 从队列中获取消息（阻塞直到有消息可用）
                    notifyObservers(message); // 处理消息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        messageProcessor.setDaemon(true); // 设置为守护线程，随主线程一同结束
        messageProcessor.start();
    }
}
