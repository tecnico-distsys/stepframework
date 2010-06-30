package step.groovy.net;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *  Client program to estimate bandwidth.
 */

// command line options --------------------------------------------------------

def cli = new CliBuilder(usage: "BandwidthClient")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.s(longOpt: "server", required: true, args: 1, "Server name or address")
cli.p(longOpt: "port", required: true, args: 1, "Server port")

def options = cli.parse(args)
if (!options) return;
if (options.help) {
    cli.usage(); return;
}

// -----------------------------------------------------------------------------

def server = options.server as String
assert server

def port = options.port as Integer
assert port > 0

def seqLen = 1000
def bufSize = 1024*1024

def random = new Random()

// -----------------------------------------------------------------------------

// Create connection socket
Socket socket = new Socket(server, port);
println "Connection established with server: " + server + " at port: " + port

// Create output stream to send data to server
OutputStream socketOutputStream = socket.getOutputStream();
// RTOutputStream rtOutputStream = new RTOutputStream(socketOutputStream);
BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socketOutputStream);
DataOutputStream out = new DataOutputStream(bufferedOutputStream);

byte[] buf = new byte[bufSize];

long seqStartTime, seqElapsedTime;
long startTime, elapsedTime;

// -----------------------------------------------------------------------------

print "Iterate random sequence, start-up ...     "

seqStartTime = System.nanoTime();

random.setSeed(port);
for (int i=0; i < seqLen; i++) {
    random.nextBytes(buf);
}

seqElapsedTime = System.nanoTime() - seqStartTime;
printf "%.0f milliseconds%n", seqElapsedTime / 1000.0 / 1000.0

// -----------------------------------------------------------------------------

print "Iterate random sequence, steady-state ... "

seqStartTime = System.nanoTime();

random.setSeed(port);
for (int i=0; i < seqLen; i++) {
    random.nextBytes(buf);
}

seqElapsedTime = System.nanoTime() - seqStartTime;
printf "%.0f milliseconds%n", seqElapsedTime / 1000.0 / 1000.0

// -----------------------------------------------------------------------------

print "Upload integer sequence ...               "

startTime = System.nanoTime();

random.setSeed(port);
for (int i=0; i < seqLen; i++) {
    random.nextBytes(buf);
    out.write(buf);
}
elapsedTime = System.nanoTime() - startTime;
printf "%.0f milliseconds%n", elapsedTime / 1000.0 / 1000.0


//
//  Data transfer rate
//
double byteCount = seqLen * bufSize;

double delta_seconds = (double) (elapsedTime-seqElapsedTime) / 1000.0 / 1000.0 / 1000.0;

double rate_Bps = byteCount / delta_seconds;
double rate_KiBps = rate_Bps / 1024;
double rate_MiBps = rate_KiBps / 1024;

double rate_bitps = (byteCount * 8) / delta_seconds;
double rate_kbitps = rate_bitps / 1000;
double rate_Mbitps = rate_kbitps / 1000;

printf "Transferred %.0f B in %.0f s (%.4f MiB/s , %.4f Mbit/s)%n",
    byteCount, delta_seconds, rate_MiBps, rate_Mbitps

// -----------------------------------------------------------------------------

println "Done!"
