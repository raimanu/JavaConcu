package nc.client;

import nc.ITchat;
import nc.server.Server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.net.Socket;
import java.io.*;
import java.net.InetAddress;

/**
 * Client de tchat
 */
public class Client extends Thread implements ITchat {

    ClientUI clientUI;
    String hostname;
    int port;
    String nickname;
    SocketChannel sc;
    Selector selector;

    public Client(ClientUI clientUI, String hostname, int port, String nickname) {
      this.clientUI = clientUI;
      this.hostname = hostname;
      this.port = port;
      this.nickname = nickname;
      try{
      this.sc = SocketChannel.open(new InetSocketAddress(hostname, port));
      sc.configureBlocking(false);
    }
      catch(IOException e){
        System.out.println("Exception throw dans le constructeur de la classe client");
        e.getStackTrace();
      }
    }

    /**
     * Ajoute un message et passe le channel en
     * mode ecriture
     */
    public void addMessage(String message) {
      try {
        
        byte[] result = new String(message).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(result);
        buffer.clear();
        sc.register(selector, SelectionKey.OP_WRITE);
        buffer.put(message.getBytes());
        buffer.flip();
        sc.write(buffer);
        
      } catch(Exception e){
        System.out.println("Exception throw dans la méthode addMessage de la classe client");
        e.getStackTrace();
      }
    }

    /**
     * Process principal du thread
     * on ecoute
     */
    public void run() {
      try {
        selector = Selector.open();
        sc.register(selector, SelectionKey.OP_CONNECT);
        sc.finishConnect();
        
      } catch(Exception e){
        System.out.println("Exception throw dans le run de la classe client");
        e.getStackTrace();
      }
    }

}
