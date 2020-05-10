package me.jittagornp.learning.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import me.jittagornp.learning.grpc.grpcservice.MathServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.SumRequest;
import me.jittagornp.learning.grpc.grpcservice.SumResponse;

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
        final MathServiceGrpc.MathServiceStub serviceStub = MathServiceGrpc.newStub(channel);

        final StreamObserver<SumRequest> serverRequest = serviceStub.sum(new StreamObserver<SumResponse>() {
            @Override
            public void onNext(final SumResponse response) {
                 System.out.println("Server Response => " + response.getResult());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(System.err);
            }

            @Override
            public void onCompleted() {
                try {
                    System.out.println("Client shoutdown channel.");
                    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });

        for(int i=0; i<10; i++){
            
            final SumRequest request = SumRequest.newBuilder()
                .setNumber(i)
                .build();
            
            serverRequest.onNext(request);
        }
        
        serverRequest.onCompleted();
        
        Thread.sleep(3000L);

    }

}
