package me.jittagornp.learning.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import me.jittagornp.learning.grpc.grpcservice.GreetingServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.HelloRequest;
import me.jittagornp.learning.grpc.grpcservice.HelloResponse;

/**
 *
 * @author jitta
 */
public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {
        
        final String gRPCServerAddress = "localhost:9000";

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(gRPCServerAddress)
                .usePlaintext()
                .build();

        System.out.println("Client connect to " + gRPCServerAddress);
        final GreetingServiceGrpc.GreetingServiceBlockingStub serviceBlockingStub = GreetingServiceGrpc.newBlockingStub(channel);

        final HelloRequest request = HelloRequest.newBuilder()
                .setName("jittagornp")
                .build();

        final HelloResponse response = serviceBlockingStub.hello(request);

        System.out.println("Server Response => " + response.getMessage());

        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);

    }

}
