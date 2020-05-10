package me.jittagornp.learning.grpc;

import io.grpc.stub.StreamObserver;
import me.jittagornp.learning.grpc.grpcservice.GreetingServiceGrpc;
import me.jittagornp.learning.grpc.grpcservice.HelloRequest;
import me.jittagornp.learning.grpc.grpcservice.HelloResponse;

/**
 *
 * @author jitta
 */
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void hello(final HelloRequest request, final StreamObserver<HelloResponse> responseObserver) {

        for (int i = 0; i < 5; i++) {
            final String message = "Hello " + request.getName() + " : " + (i + 1);

            final HelloResponse response = HelloResponse.newBuilder()
                    .setMessage(message)
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();

    }

}
