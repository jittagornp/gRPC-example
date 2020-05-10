package me.jittagornp.learning.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import me.jittagornp.learning.grpc.grpcservice.GreetingServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.HelloRequest;
import me.jittagornp.learning.grpc.grpcservice.HelloResponse;

/**
 *
 * @author jitta
 */
public class GreetingClient {

    public static void main(String[] args) throws InterruptedException {

        final String gRPCServerAddress = "localhost:9000";

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(gRPCServerAddress)
                .usePlaintext()
                .build();

        final GreetingServiceGrpc.GreetingServiceStub blockingStub = GreetingServiceGrpc.newStub(channel);

        final HelloRequest request = HelloRequest.newBuilder()
                .setName("jittagornp")
                .build();

        blockingStub.hello(request, new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(final HelloResponse response) {
                System.out.println("server response => " + response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onCompleted() {
                try {
                    System.out.println("Server completed");
                    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        
        Thread.sleep(3000L);

    }

}
