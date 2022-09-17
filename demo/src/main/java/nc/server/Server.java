package nc.server;



import javafx.application.Platform;
import nc.ITchat;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Processus serveur qui ecoute les connexion entrantes,
 * les messages entrant et les rediffuse au clients connectes
 *
 * @author mathieu.fabre
 */
public class Server extends Thread implements ITchat {

    /**
     * Interface graphique du serveur
     */
    private ServerUI serverUI;

	// TODO A completer
    private String ip;
    private int port;
    private ServerSocketChannel ssc;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private StringBuffer  message = new StringBuffer();

    /**
     * Constructeur
     * Lien avec l interface Graphique
     * Creation du selecteur et du socket serveur
     *
     * @param serverUI
     */
    public Server(ServerUI serverUI, String ip, int port) {

		// TODO A completer
        this.ip = ip;
        this.port = port;
        this.serverUI = serverUI;

        try{
        this.selector = Selector.open();
        this.ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetSocketAddress inetSocket = new InetSocketAddress(ip, port);
        ssc.bind(inetSocket);
        sendLogToUI("Serveur créer");
        } catch(IOException e){
            System.out.println("Exception throw dans le constructeur de la classe Server");
            e.printStackTrace();
        }

    }

    /**
     * Envoi un message de log a l'IHM
     */
    public void sendLogToUI(String message) {
        Platform.runLater(() -> serverUI.log(message));
    }

    /**
     * Process principal du server
     */
    public void run(){

        // TODO A completer
        sendLogToUI("Lancement du serveur");
        try{
        while (this.serverUI.isRunning()) {
            try{
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                } catch(ClosedChannelException e){
                    System.out.println("ClosedChannelException exception");
                    e.getStackTrace();
                }
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterateur = keys.iterator();
            while (iterateur.hasNext()) {
                SelectionKey key = iterateur.next();
                if (key.isAcceptable()) {
                    SocketChannel client = ssc.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    buffer.clear();
                    client.read(buffer);
                    buffer.flip();
                    while(buffer.hasRemaining()) {
                        char c = (char) buffer.get();
                        if (c == '\r' || c == '\n') break;
                             message.append(c);
                }
                sendLogToUI("Message de " +  message);
            }
            message.setLength(0);
            iterateur.remove();
            }
        }
        sendLogToUI("Fermeture du serveur");
        ssc.close();
    } catch(IOException e){
        System.out.println("Exception throw dans la méthode run de la classe Server");
        e.getStackTrace();
    }
    } 
		
	// TODO A completer
}
