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
        InetSocketAddress inetSocket = new InetSocketAddress(ip, port);
        ssc.bind(inetSocket);
        ssc.configureBlocking(false);
        } catch(IOException e){
        System.out.println("Exception entrée/sortie");
        }
        int ops = ssc.validOps();
        try{
        SelectionKey keySelec = ssc.register(selector, ops, null);
        } catch(ClosedChannelException e){
            System.out.println("ClosedChannelException exception");
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
        try{
        while (true) {
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();
            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterateur = keys.iterator();
            while (iterateur.hasNext()) {
                SelectionKey key = iterateur.next();
                // Tests whether this key's channel is ready to accept a new socket connection
                if (key.isAcceptable()) {
                    SocketChannel client = ssc.accept();
                    // Adjusts this channel's blocking mode to false
                    client.configureBlocking(false);
                    // Operation-set bit for read operations
                    client.register(selector, SelectionKey.OP_READ);
                    // Tests whether this key's channel is ready for reading
                } else if (key.isReadable()) {
                    
                    SocketChannel client = (SocketChannel) key.channel();
                    
                    // ByteBuffer: A byte buffer.
                    // This class defines six categories of operations upon byte buffers:
                    // Absolute and relative get and put methods that read and write single bytes;
                    // Absolute and relative bulk get methods that transfer contiguous sequences of bytes from this buffer into an array;
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String resultat = new String(buffer.array()).trim();
                    if (resultat.equals("Server.com")) {
                        client.close();
                    }
                }
                iterateur.remove();
            }
        }
    } catch(IOException e){
        System.out.println("Ioexception");
    }
    }
		
	// TODO A completer
}
