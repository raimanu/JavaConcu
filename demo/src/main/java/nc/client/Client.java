package nc.client;

import nc.ITchat;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.net.Socket;
import java.io.*;
import java.net.InetAddress;

/**
 * Client de tchat
 */
public class Client extends Thread implements ITchat {

    ClientUI clientUI = new ClientUI();
    String hostname;
    int port;
    String nickname;

    public Client(ClientUI clientUI, String hostname, int port, String nickname) {
      this.clientUI = clientUI;
      this.hostname = hostname;
      this.port = port;
      this.nickname = nickname;
    }

    /**
     * Ajoute un message et passe le channel en
     * mode ecriture
     */
    public void addMessage(String message) {
      try {
        Socket s = new Socket(InetAddress.getLocalHost(), 1111);
        PrintWriter p = new PrintWriter(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        message = br.readLine();
        br.close();
        p.close();
        s.close();
      } catch(Exception e){

      }
    }

    /**
     * Process principal du thread
     * on ecoute
     */
    public void run() {
      try {
        SocketChannel sc = new SocketChannel(InetAddress.getLocalHost(), 1111);
      } catch(Exception e){

      }
    }

}
