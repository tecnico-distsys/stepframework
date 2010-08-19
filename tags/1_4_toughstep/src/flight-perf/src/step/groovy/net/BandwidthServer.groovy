package step.groovy.net;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *  Server program to help client estimate upload bandwidth.
 *  Accepts connections and reads integers uploaded by client.
 */


// command line options --------------------------------------------------------

def cli = new CliBuilder(usage: "BandwidthServer")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "port", required: true, args: 1, "Server port")

def options = cli.parse(args)
if (!options) return;
if (options.help) {
    cli.usage(); return;
}

// -----------------------------------------------------------------------------

def port = options.port as Integer
assert port > 0

// -----------------------------------------------------------------------------

// Create server socket
ServerSocket serverSocket = new ServerSocket(port);
printf "%s Accepting connections at port: %d%n", new Date(), port

byte[] buf = new byte[100*1024];
printf "Buffer size: %d B%n", buf.length

// -----------------------------------------------------------------------------

// Connection handling loop
boolean stopAccept = false;
while (!stopAccept) {
    try {
        // accept connection
        Socket clientSocket = serverSocket.accept();
        printf "%s Connection established with client: %s at port: %s%n",
            new Date(),
            clientSocket.getInetAddress().getHostAddress(),
            clientSocket.getPort()

        // create input stream to read data from client
        InputStream clientSocketInputStream = clientSocket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocketInputStream);
        DataInputStream dis = new DataInputStream(bufferedInputStream);

        // read until connection is closed
        int nr;
        while ((nr = dis.read(buf)) != -1);

        // close connection
        clientSocket.close();
        printf "%s Closed client socket%n", new Date()

    } catch (SocketException se) {
        printf "%s Caught socket exception: %s%n", new Date(), se
    }

}
// close server socket
serverSocket.close();
printf "%s Closed server socket%n", new Date()
