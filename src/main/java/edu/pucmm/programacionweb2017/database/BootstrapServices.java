package edu.pucmm.programacionweb2017.database;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by mt on 04/06/17.
 */
public class BootstrapServices {
    /**
     *
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }
}
