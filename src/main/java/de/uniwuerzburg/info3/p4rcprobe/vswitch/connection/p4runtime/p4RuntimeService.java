package de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime;

import com.google.protobuf.ByteString;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.Connection;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass;
import io.grpc.stub.StreamObserver;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Our implementation of RouteGuide service.
 *
 * <p>See route_guide.proto for details of the methods.
 */
public class p4RuntimeService extends de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeGrpc.P4RuntimeImplBase {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(p4RuntimeService.class);

    private static de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ForwardingPipelineConfig.Cookie COOKIE_VALUE = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ForwardingPipelineConfig.Cookie.newBuilder()
            .setCookie(-2258856645975481100L)
            .build();

    private final AtomicBoolean shutdown = new AtomicBoolean();

    private StreamObserver<P4RuntimeOuterClass.StreamMessageResponse> responseObserver;

    private Connection p4RuntimeConnection;
//    private int writes = 0;

    public void shutdown() {
        shutdown.set(true);
    }

    public void getConnection(Connection p4RuntimeConnection) {
        this.p4RuntimeConnection = p4RuntimeConnection;
    }

    @Override
    public void write(P4RuntimeOuterClass.WriteRequest request, StreamObserver<P4RuntimeOuterClass.WriteResponse> responseObserver) {

        this.p4RuntimeConnection.receiveP4runtimeFlowRule(request);

//        this.writes++;
//        logger.info("WRITE RPC: {}", this.writes);
//        logger.info("{}", request.getAllFields());
//        logger.info("------------> {}", Util.asString(request.getUpdates(0).getEntity().getTableEntry().getMatch(0).getTernary().getValue()
//                .toStringUtf8().getBytes()));
//        logger.info("------------> {}", Util.asString(request.getUpdates(0).getEntity().getTableEntry().getMatch(0).getLpm().getValue()
//                .toStringUtf8().getBytes()));
//        logger.info("------------> {}", request.getUpdates(0).getEntity().getTableEntry().getAction());
//        logger.info("------------> {}", Util.asString(request.getUpdates(0).getEntity().getTableEntry().getAction().getAction().getParamsList().get(0).getValue()
//                .toStringUtf8().getBytes()));
        responseObserver.onNext(
                P4RuntimeOuterClass.WriteResponse.newBuilder()
                        .clear()
                        .build()
        );

        responseObserver.onCompleted();
    }

    @Override
    public void read(P4RuntimeOuterClass.ReadRequest request, StreamObserver<P4RuntimeOuterClass.ReadResponse> responseObserver) {
        ArrayList<P4RuntimeOuterClass.Entity> filteredEntitiesList = new ArrayList<P4RuntimeOuterClass.Entity>();
//        logger.info("{}", request.getAllFields());
//        responseObserver.onNext(
//                P4RuntimeOuterClass.ReadResponse.newBuilder()
//                        .addAllEntities(filteredEntitiesList)
//                        .build());
        responseObserver.onNext(
                P4RuntimeOuterClass.ReadResponse
                        .getDefaultInstance()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void setForwardingPipelineConfig(P4RuntimeOuterClass.SetForwardingPipelineConfigRequest request,
                                            StreamObserver<P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> responseObserver) {
        COOKIE_VALUE = request.getConfig().getCookie();
    }

    @Override
    public void getForwardingPipelineConfig(P4RuntimeOuterClass.GetForwardingPipelineConfigRequest request,
                                            StreamObserver<P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> responseObserver) {
        responseObserver.onNext(
                P4RuntimeOuterClass.GetForwardingPipelineConfigResponse.newBuilder()
                        .setConfig(
                                P4RuntimeOuterClass.ForwardingPipelineConfig
                                        .newBuilder()
                                        .setCookie(COOKIE_VALUE)
                                        .build())
                        .build());
        responseObserver.onCompleted();
    }

    public void sendP4RuntimePacketIn(byte[] packet, short port) {
//        String payloadString = Util.asString(packet);
//        ByteString payloadbyteString = ByteString.copyFromUtf8(payloadString);

        ByteString payloadbyteString = ByteString.copyFrom(packet);

//        streamChannel(this.responseObserver).onNext(
//                P4RuntimeOuterClass.StreamMessageRequest.newBuilder()
//                        .setOther(Any.newBuilder()
//                                .setTypeUrl("Send Packet")
//                                .setValue(payloadbyteString)
//                                .build())
//                        .build()
//        );

//        Short.toString(port)

//        String portString = Short.toString(port);
//        String paddingString = "00000000";

        String portString = null;
        if(port == 1){
            portString = "\000\001";
        }else if(port == 2){
            portString = "\000\002";
        }else if(port == 3){
            portString = "\000\003";
        }else if(port == 4){
            portString = "\000\004";
        }

//        logger.info("META: {}", portString);

//        ByteString ingressPortbyteString = ByteString.copyFromUtf8("000000001");
//        ByteString ingressPortbyteString = ByteString.copyFromUtf8(paddingString + portString);
        ByteString ingressPortbyteString = ByteString.copyFrom(portString.getBytes());
//        ByteString ingressPortbyteString = ByteString.copyFromUtf8(paddingString);
//        ByteString paddingByteString = ByteString.copyFromUtf8("0000000");
//        ByteString paddingByteString = ByteString.copyFromUtf8("0000000");
        ByteString paddingByteString = ByteString.copyFrom("\377\377".getBytes());
//        ByteString paddingByteString = ByteString.copyFrom()

        P4RuntimeOuterClass.PacketMetadata metadata1 = P4RuntimeOuterClass.PacketMetadata.newBuilder()
                .setMetadataId(1)
                .setValue(ingressPortbyteString)
                .build();
        P4RuntimeOuterClass.PacketMetadata metadata2 = P4RuntimeOuterClass.PacketMetadata.newBuilder()
                .setMetadataId(2)
                .setValue(paddingByteString)
                .build();

        P4RuntimeOuterClass.PacketIn packetIn = P4RuntimeOuterClass.PacketIn
                .newBuilder()
                .addMetadata(metadata1)
                .addMetadata(metadata2)
                .setPayload(payloadbyteString)
                .build();

//        synchronized(this.responseObserver){
            this.responseObserver.onNext(
                    P4RuntimeOuterClass.StreamMessageResponse.newBuilder()
                            .setPacket(packetIn)
                            .build()
            );
//        }

    }

    public void receiveP4RuntimePacketOut(P4RuntimeOuterClass.PacketOut packet) {
        this.p4RuntimeConnection.receiveP4runtimePacket(packet);
    }


    @Override
    public StreamObserver<P4RuntimeOuterClass.StreamMessageRequest>
    streamChannel(StreamObserver<P4RuntimeOuterClass.StreamMessageResponse> responseObserver) {

        this.responseObserver = responseObserver;

        return new StreamObserver<P4RuntimeOuterClass.StreamMessageRequest>() {

            @Override
            public void onNext(P4RuntimeOuterClass.StreamMessageRequest request) {
//                synchronized(this){
                    if (request.getUpdateCase() == P4RuntimeOuterClass.StreamMessageRequest.UpdateCase.valueOf("PACKET")) {
//                        receiveP4RuntimePacketOut(request.getPacket().getPayload().toStringUtf8().getBytes());
                        receiveP4RuntimePacketOut(request.getPacket());
                    }
//                }
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("streamChannel cancelled");
            }

            @Override
            public void onCompleted() {
            }
        };
    }
}