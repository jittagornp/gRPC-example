package me.jittagornp.learning.grpc;

import io.grpc.stub.StreamObserver;
import me.jittagornp.learning.grpc.grpcservice.MathServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.SumRequest;
import me.jittagornp.learning.grpc.grpcservice.SumResponse;

/**
 *
 * @author jitta
 */
public class MathServiceImpl extends MathServiceGrpc.MathServiceImplBase {

    @Override
    public StreamObserver<SumRequest> sum(StreamObserver<SumResponse> responseObserver) {
        return new StreamObserver<SumRequest>() {

            int sum = 0;

            @Override
            public void onNext(final SumRequest request) {
                sum = sum + request.getNumber();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(System.err);
            }

            @Override
            public void onCompleted() {
                
                final SumResponse response = SumResponse.newBuilder()
                        .setResult(sum)
                        .build();
                
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

}
