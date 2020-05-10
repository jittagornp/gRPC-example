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
public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {

        final String gRPCServerAddress = "localhost:9000";

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(gRPCServerAddress)
                .usePlaintext()
                .build();

        System.out.println("Client connect to " + gRPCServerAddress);
        final GreetingServiceGrpc.GreetingServiceStub serviceStub = GreetingServiceGrpc.newStub(channel);

        final HelloRequest request = HelloRequest.newBuilder()
                .setName("jittagornp")
                .build();

        serviceStub.hello(request, new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(final HelloResponse response) {
                System.out.println("Server Response => " + response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(System.err);
            }

            @Override
            public void onCompleted() {
                try {
                    System.out.println("Server completed");
                    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
        
        
        Thread.sleep(3000L);

    }

}
