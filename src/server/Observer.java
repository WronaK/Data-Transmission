package server;

public interface Observer {
    void notify(String nick, byte[] bytes);
}
