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
        ssc.configureBlocking(false);
        InetSocketAddress inetSocket = new InetSocketAddress(ip, port);
        ssc.bind(inetSocket);
        sendLogToUI("Serveur créer");
        } catch(IOException e){
            System.out.println("Ioexception");
            e.printStackTrace();
        }
        int ops = ssc.validOps();
        try{
        SelectionKey keySelec = ssc.register(selector, ops, null);
        sendLogToUI("Fin");
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
        sendLogToUI("Lancement du serveur");
        try{
        while (true) {
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
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String resultat = new String(buffer.array()).trim();
                    sendLogToUI(resultat);
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
