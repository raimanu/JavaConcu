package nc.client;

import nc.ITchat;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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

    public Client(ClientUI clientUI, String hostname, int port, String nickname) {
      this.clientUI = clientUI;
      this.hostname = hostname;
      this.port = port;
      this.nickname = nickname;
      try{
      this.sc = SocketChannel.open();}
      catch(IOException e){

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
        this.sc.write(buffer);
        buffer.clear();
      } catch(Exception e){

      }
    }

    /**
     * Process principal du thread
     * on ecoute
     */
    public void run() {
      try {
        SocketChannel sc = SocketChannel.open();
      } catch(Exception e){

      }
    }

}
