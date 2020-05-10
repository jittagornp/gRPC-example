package me.jittagornp.learning.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jitta
 */
public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        int port = 9000;
        
        final Server server = ServerBuilder.forPort(port)
                .addService(new GreetingServiceImpl()) //register gRPC service
                .build()
                .start();
        
        System.out.println("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                } finally {
                    System.err.println("*** server shut down");
                }
            }
        });
        
        server.awaitTermination();
    }
}
