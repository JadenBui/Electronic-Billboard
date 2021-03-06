package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * The type Control panel handler.
 */
public class ControlPanelHandler extends Thread {
    /**
     * The Ois.
     */
    ObjectInputStream ois;
    /**
     * The Oos.
     */
    ObjectOutputStream oos;
    /**
     * The S.
     */
    Socket s;

    /**
     * Instantiates a new Control panel handler.
     *
     * @param s   the s
     * @param ois the ois
     * @param oos the oos
     */
// Constructor
    public ControlPanelHandler(Socket s, ObjectInputStream ois, ObjectOutputStream oos)
    {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {
        Object received;
        Object toReturn;

        // Ask user what he wants
        try {
            oos.writeObject("[S] Hello, start connecting to CONTROL PANEL");
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                // receive the answer from client
                received = ois.readObject();
                System.out.println(received);

                if (received.equals("Exit")) {
                    System.out.println("[ControlPanel] " + this.s + " sends exit...");
                    System.out.println("[Server] Closing connection to ControlPanel.");
                    this.s.close();
                    //BillboardServer.removeThread(this);
                    System.out.println("Connection closed");
                    break;
                }
                ServerRespond res = new ServerRespond(received, oos, ois);
                res.handle();

            } catch (IOException | ClassNotFoundException | SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        // closing resources
        try {
            System.out.println("Closing stream IO at " + s);
            this.ois.close();this.oos.close();
            System.out.println("Closed ControlPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Viewer End");
    }
}
