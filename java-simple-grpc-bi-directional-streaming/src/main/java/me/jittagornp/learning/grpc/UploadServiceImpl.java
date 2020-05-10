package me.jittagornp.learning.grpc;

import io.grpc.stub.StreamObserver;
import me.jittagornp.learning.grpc.grpcservice.UploadServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.UploadRequest;
import me.jittagornp.learning.grpc.grpcservice.UploadResponse;

/**
 *
 * @author jitta
 */
public class UploadServiceImpl extends UploadServiceGrpc.UploadServiceImplBase {

    @Override
    public StreamObserver<UploadRequest> upload(StreamObserver<UploadResponse> responseObserver) {
        return new StreamObserver<UploadRequest>() {

            int count = 0;

            @Override
            public void onNext(final UploadRequest request) {
                System.out.println("Receive byte => " + request.getData());
                count = count + 1;
                
                final UploadResponse response = UploadResponse.newBuilder()
                        .setProgress(count)
                        .build();
                
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(System.err);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

}
