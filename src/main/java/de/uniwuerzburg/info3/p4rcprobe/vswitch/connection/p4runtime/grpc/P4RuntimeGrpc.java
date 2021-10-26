package de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: p4runtime.proto")
public final class P4RuntimeGrpc {

  private P4RuntimeGrpc() {}

  public static final String SERVICE_NAME = "p4.v1.P4Runtime";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Write",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> getWriteMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> getWriteMethod;
    if ((getWriteMethod = P4RuntimeGrpc.getWriteMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getWriteMethod = P4RuntimeGrpc.getWriteMethod) == null) {
          P4RuntimeGrpc.getWriteMethod = getWriteMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "Write"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("Write"))
                  .build();
          }
        }
     }
     return getWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> getReadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Read",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> getReadMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> getReadMethod;
    if ((getReadMethod = P4RuntimeGrpc.getReadMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getReadMethod = P4RuntimeGrpc.getReadMethod) == null) {
          P4RuntimeGrpc.getReadMethod = getReadMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "Read"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("Read"))
                  .build();
          }
        }
     }
     return getReadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> getSetForwardingPipelineConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetForwardingPipelineConfig",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> getSetForwardingPipelineConfigMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> getSetForwardingPipelineConfigMethod;
    if ((getSetForwardingPipelineConfigMethod = P4RuntimeGrpc.getSetForwardingPipelineConfigMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getSetForwardingPipelineConfigMethod = P4RuntimeGrpc.getSetForwardingPipelineConfigMethod) == null) {
          P4RuntimeGrpc.getSetForwardingPipelineConfigMethod = getSetForwardingPipelineConfigMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "SetForwardingPipelineConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("SetForwardingPipelineConfig"))
                  .build();
          }
        }
     }
     return getSetForwardingPipelineConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> getGetForwardingPipelineConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetForwardingPipelineConfig",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> getGetForwardingPipelineConfigMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> getGetForwardingPipelineConfigMethod;
    if ((getGetForwardingPipelineConfigMethod = P4RuntimeGrpc.getGetForwardingPipelineConfigMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getGetForwardingPipelineConfigMethod = P4RuntimeGrpc.getGetForwardingPipelineConfigMethod) == null) {
          P4RuntimeGrpc.getGetForwardingPipelineConfigMethod = getGetForwardingPipelineConfigMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "GetForwardingPipelineConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("GetForwardingPipelineConfig"))
                  .build();
          }
        }
     }
     return getGetForwardingPipelineConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse> getStreamChannelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamChannel",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse> getStreamChannelMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse> getStreamChannelMethod;
    if ((getStreamChannelMethod = P4RuntimeGrpc.getStreamChannelMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getStreamChannelMethod = P4RuntimeGrpc.getStreamChannelMethod) == null) {
          P4RuntimeGrpc.getStreamChannelMethod = getStreamChannelMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "StreamChannel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("StreamChannel"))
                  .build();
          }
        }
     }
     return getStreamChannelMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> getCapabilitiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Capabilities",
      requestType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest.class,
      responseType = de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest,
      de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> getCapabilitiesMethod() {
    io.grpc.MethodDescriptor<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> getCapabilitiesMethod;
    if ((getCapabilitiesMethod = P4RuntimeGrpc.getCapabilitiesMethod) == null) {
      synchronized (P4RuntimeGrpc.class) {
        if ((getCapabilitiesMethod = P4RuntimeGrpc.getCapabilitiesMethod) == null) {
          P4RuntimeGrpc.getCapabilitiesMethod = getCapabilitiesMethod = 
              io.grpc.MethodDescriptor.<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest, de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "p4.v1.P4Runtime", "Capabilities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new P4RuntimeMethodDescriptorSupplier("Capabilities"))
                  .build();
          }
        }
     }
     return getCapabilitiesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static P4RuntimeStub newStub(io.grpc.Channel channel) {
    return new P4RuntimeStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static P4RuntimeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new P4RuntimeBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static P4RuntimeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new P4RuntimeFutureStub(channel);
  }

  /**
   */
  public static abstract class P4RuntimeImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Update one or more P4 entities on the target.
     * </pre>
     */
    public void write(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWriteMethod(), responseObserver);
    }

    /**
     * <pre>
     * Read one or more P4 entities from the target.
     * </pre>
     */
    public void read(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReadMethod(), responseObserver);
    }

    /**
     * <pre>
     * Sets the P4 forwarding-pipeline config.
     * </pre>
     */
    public void setForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetForwardingPipelineConfigMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets the current P4 forwarding-pipeline config.
     * </pre>
     */
    public void getForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetForwardingPipelineConfigMethod(), responseObserver);
    }

    /**
     * <pre>
     * Represents the bidirectional stream between the controller and the
     * switch (initiated by the controller), and is managed for the following
     * purposes:
     * - connection initiation through master arbitration
     * - indicating switch session liveness: the session is live when switch
     *   sends a positive master arbitration update to the controller, and is
     *   considered dead when either the stream breaks or the switch sends a
     *   negative update for master arbitration
     * - the controller sending/receiving packets to/from the switch
     * - streaming of notifications from the switch
     * </pre>
     */
    public io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest> streamChannel(
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getStreamChannelMethod(), responseObserver);
    }

    /**
     */
    public void capabilities(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCapabilitiesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getWriteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse>(
                  this, METHODID_WRITE)))
          .addMethod(
            getReadMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse>(
                  this, METHODID_READ)))
          .addMethod(
            getSetForwardingPipelineConfigMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse>(
                  this, METHODID_SET_FORWARDING_PIPELINE_CONFIG)))
          .addMethod(
            getGetForwardingPipelineConfigMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse>(
                  this, METHODID_GET_FORWARDING_PIPELINE_CONFIG)))
          .addMethod(
            getStreamChannelMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse>(
                  this, METHODID_STREAM_CHANNEL)))
          .addMethod(
            getCapabilitiesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest,
                de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse>(
                  this, METHODID_CAPABILITIES)))
          .build();
    }
  }

  /**
   */
  public static final class P4RuntimeStub extends io.grpc.stub.AbstractStub<P4RuntimeStub> {
    private P4RuntimeStub(io.grpc.Channel channel) {
      super(channel);
    }

    private P4RuntimeStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected P4RuntimeStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new P4RuntimeStub(channel, callOptions);
    }

    /**
     * <pre>
     * Update one or more P4 entities on the target.
     * </pre>
     */
    public void write(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Read one or more P4 entities from the target.
     * </pre>
     */
    public void read(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Sets the P4 forwarding-pipeline config.
     * </pre>
     */
    public void setForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetForwardingPipelineConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets the current P4 forwarding-pipeline config.
     * </pre>
     */
    public void getForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetForwardingPipelineConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Represents the bidirectional stream between the controller and the
     * switch (initiated by the controller), and is managed for the following
     * purposes:
     * - connection initiation through master arbitration
     * - indicating switch session liveness: the session is live when switch
     *   sends a positive master arbitration update to the controller, and is
     *   considered dead when either the stream breaks or the switch sends a
     *   negative update for master arbitration
     * - the controller sending/receiving packets to/from the switch
     * - streaming of notifications from the switch
     * </pre>
     */
    public io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageRequest> streamChannel(
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getStreamChannelMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void capabilities(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest request,
        io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCapabilitiesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class P4RuntimeBlockingStub extends io.grpc.stub.AbstractStub<P4RuntimeBlockingStub> {
    private P4RuntimeBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private P4RuntimeBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected P4RuntimeBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new P4RuntimeBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Update one or more P4 entities on the target.
     * </pre>
     */
    public de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse write(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest request) {
      return blockingUnaryCall(
          getChannel(), getWriteMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Read one or more P4 entities from the target.
     * </pre>
     */
    public java.util.Iterator<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse> read(
        de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getReadMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Sets the P4 forwarding-pipeline config.
     * </pre>
     */
    public de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse setForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetForwardingPipelineConfigMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets the current P4 forwarding-pipeline config.
     * </pre>
     */
    public de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse getForwardingPipelineConfig(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetForwardingPipelineConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse capabilities(de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest request) {
      return blockingUnaryCall(
          getChannel(), getCapabilitiesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class P4RuntimeFutureStub extends io.grpc.stub.AbstractStub<P4RuntimeFutureStub> {
    private P4RuntimeFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private P4RuntimeFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected P4RuntimeFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new P4RuntimeFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Update one or more P4 entities on the target.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse> write(
        de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Sets the P4 forwarding-pipeline config.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse> setForwardingPipelineConfig(
        de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetForwardingPipelineConfigMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets the current P4 forwarding-pipeline config.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse> getForwardingPipelineConfig(
        de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetForwardingPipelineConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse> capabilities(
        de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCapabilitiesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WRITE = 0;
  private static final int METHODID_READ = 1;
  private static final int METHODID_SET_FORWARDING_PIPELINE_CONFIG = 2;
  private static final int METHODID_GET_FORWARDING_PIPELINE_CONFIG = 3;
  private static final int METHODID_CAPABILITIES = 4;
  private static final int METHODID_STREAM_CHANNEL = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final P4RuntimeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(P4RuntimeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_WRITE:
          serviceImpl.write((de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteRequest) request,
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.WriteResponse>) responseObserver);
          break;
        case METHODID_READ:
          serviceImpl.read((de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadRequest) request,
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.ReadResponse>) responseObserver);
          break;
        case METHODID_SET_FORWARDING_PIPELINE_CONFIG:
          serviceImpl.setForwardingPipelineConfig((de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigRequest) request,
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.SetForwardingPipelineConfigResponse>) responseObserver);
          break;
        case METHODID_GET_FORWARDING_PIPELINE_CONFIG:
          serviceImpl.getForwardingPipelineConfig((de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigRequest) request,
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.GetForwardingPipelineConfigResponse>) responseObserver);
          break;
        case METHODID_CAPABILITIES:
          serviceImpl.capabilities((de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesRequest) request,
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.CapabilitiesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_CHANNEL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamChannel(
              (io.grpc.stub.StreamObserver<de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.StreamMessageResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class P4RuntimeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    P4RuntimeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("P4Runtime");
    }
  }

  private static final class P4RuntimeFileDescriptorSupplier
      extends P4RuntimeBaseDescriptorSupplier {
    P4RuntimeFileDescriptorSupplier() {}
  }

  private static final class P4RuntimeMethodDescriptorSupplier
      extends P4RuntimeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    P4RuntimeMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (P4RuntimeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new P4RuntimeFileDescriptorSupplier())
              .addMethod(getWriteMethod())
              .addMethod(getReadMethod())
              .addMethod(getSetForwardingPipelineConfigMethod())
              .addMethod(getGetForwardingPipelineConfigMethod())
              .addMethod(getStreamChannelMethod())
              .addMethod(getCapabilitiesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
