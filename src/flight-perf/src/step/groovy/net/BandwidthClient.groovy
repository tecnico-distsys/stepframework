package step.groovy.net;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;


/**
 *  Client program to estimate bandwidth.
 */

final def DEFAULT_SERVER = "localhost"
final def DEFAULT_DURATION = 10
final def DEFAULT_SAMPLES = 30


// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "BandwidthClient")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "server", required: false, args: 1, "Server name or address, " + DEFAULT_SERVER + " by default")
cli._(longOpt: "port", required: true, args: 1, "Server port")
cli._(longOpt: "duration", required: false, args: 1, "Duration of upload session in seconds, " + DEFAULT_DURATION + " by default")
cli._(longOpt: "samples", required: false, args: 1, "Number of samples to take, " + DEFAULT_SAMPLES + " by default")
cli._(longOpt: "seed", required: false, args: 1, "Random seed")

def options = cli.parse(args)
if (!options) return;
if (options.help) {
    cli.usage(); return;
}

// -----------------------------------------------------------------------------

def server = DEFAULT_SERVER
if (options.server) server = options.server
assert server

def port = options.port as Integer
assert port > 0

def duration = DEFAULT_DURATION;
if (options.duration) duration = options.duration as Integer
assert duration >= 5
assert duration <= 60

def samples = DEFAULT_SAMPLES;
if (options.samples) samples = options.samples as Integer
assert samples >= 30
assert samples <= 100

def seed;
if (options.seed) seed = options.seed as Integer

def random = new Random()
if (seed) random.setSeed(seed);

byte[] buf = new byte[100*1024];

def stats_Bps = new DescriptiveStatistics();
def stats_bitps = new DescriptiveStatistics();

def confidence = 95;

// -----------------------------------------------------------------------------

printf "Estimating upload bandwidth to server %s at port %s%n",
    server, port
printf "by taking %d upload samples of %d seconds each%n",
    samples, duration
println ""

// -----------------------------------------------------------------------------

long seqStartTime, seqStopTime, seqDeltaTime, seqElapsedTime;

seqDeltaTime = duration * 0.50 * 1000
printf "Warm-up... ", seqDeltaTime

seqStartTime = System.currentTimeMillis();
seqStopTime = seqStartTime + seqDeltaTime

while (System.currentTimeMillis() < seqStopTime) {
    random.nextBytes(buf);
}

seqElapsedTime = System.currentTimeMillis() - seqStartTime;
printf "done! %d milliseconds elapsed%n", seqElapsedTime

// -----------------------------------------------------------------------------

for (int i = 0; i < samples; i++) {

    printf "Taking sample %d of %d%n", i+1, samples

    // Create connection socket
    Socket socket = new Socket(server, port);

    // Create output stream to send data to server
    OutputStream socketOutputStream = socket.getOutputStream();
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socketOutputStream);
    DataOutputStream out = new DataOutputStream(bufferedOutputStream);

    long startTime, stopTime, deltaTime, elapsedTime;

    long byteCount = 0;

    deltaTime = duration * 1000
    printf "Upload... ", deltaTime

    startTime = System.currentTimeMillis();
    stopTime = startTime + deltaTime

    while (System.currentTimeMillis() < stopTime) {
        random.nextBytes(buf);
        out.write(buf);
        byteCount += buf.length;
    }
    elapsedTime = System.currentTimeMillis() - startTime;
    printf "done! %d milliseconds elapsed%n", elapsedTime

    // compute data transfer rate for sample

    double elapsed_seconds = (double) (elapsedTime) / 1000.0;

    double rate_Bps = byteCount / elapsed_seconds;
    double rate_KiBps = rate_Bps / 1024;
    double rate_MiBps = rate_KiBps / 1024;

    double rate_bitps = (byteCount * 8) / elapsed_seconds;
    double rate_kbitps = rate_bitps / 1000;
    double rate_Mbitps = rate_kbitps / 1000;

    printf "Transferred %d B in %.0f s (%.3f Mbit/s , %.4f MiB/s)%n",
        byteCount, elapsed_seconds, rate_Mbitps, rate_MiBps
    printf "%n"

    stats_Bps.addValue(rate_Bps);
    stats_bitps.addValue(rate_bitps);

    socket.close();
}

println "------------------------------------------------------------"
println ""
println "Average upload bandwidth:"

def computeConfidenceClosure(samples, mean, std, confidence) {
    // compute confidence interval error

    // lo = mean - c * s / sqrt(n)
    // hi = mean + c * s / sqrt(n)

    // c values are fixed for confidence levels
    final def c_MAP = [ 90:1.64 , 95:1.96, 99:2.58 ];

    double n = samples;
    double sqrt_n = Math.sqrt(n);
    double std_over_sqrt_n = std / sqrt_n;

    double error = c_MAP[confidence] * std_over_sqrt_n;

    return error;
}

double mean_bitps = stats_bitps.getMean();
double std_bitps = stats_bitps.getStandardDeviation();
double error_bitps = computeConfidenceClosure(samples, mean_bitps, std_bitps, confidence);

printf "%.3f+-%.3f Mbit/s with %d percent confidence%n",
    mean_bitps / 1000 / 1000, error_bitps / 1000 / 1000, confidence

double mean_Bps = stats_Bps.getMean();
double std_Bps = stats_Bps.getStandardDeviation();
double error_Bps = computeConfidenceClosure(samples, mean_Bps, std_Bps, confidence);

printf "%.4f+-%.4f MiB/s with %d percent confidence%n",
    mean_Bps / 1024 / 1024, error_Bps / 1024 / 1024, confidence

println ""
