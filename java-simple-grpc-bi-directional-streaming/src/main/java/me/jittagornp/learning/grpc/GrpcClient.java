package me.jittagornp.learning.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import me.jittagornp.learning.grpc.grpcservice.UploadServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.UploadRequest;
import me.jittagornp.learning.grpc.grpcservice.UploadResponse;

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
        final UploadServiceGrpc.UploadServiceStub serviceStub = UploadServiceGrpc.newStub(channel);

        final StreamObserver<UploadRequest> serverRequest = serviceStub.upload(new StreamObserver<UploadResponse>() {
            @Override
            public void onNext(final UploadResponse response) {
                System.out.println("Server Response => " + response.getProgress() + "%");
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

        for (int i = 0; i < 100; i++) {

            final UploadRequest request = UploadRequest.newBuilder()
                    .setData(ByteString.copyFromUtf8("" + i))
                    .build();

            serverRequest.onNext(request);
        }

        serverRequest.onCompleted();

        Thread.sleep(3000L);

    }

}
