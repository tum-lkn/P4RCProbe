package de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime;

import com.google.common.util.concurrent.UncaughtExceptionHandlers;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto) service.
 */
public class GrpcServer {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GrpcServer.class);

    private final Server server;
    private static ManagedChannel channel;
//    private final NettyServerBuilder remoteServer;
    private final int port;
    private final p4RuntimeService p4RuntimeService;

    /**
     * Create a RouteGuide server using serverBuilder as a base and features as data.
     */
    public GrpcServer(int port, p4RuntimeService p4RuntimeService){
        this.port = port;
        this.p4RuntimeService = p4RuntimeService;

        //----------------------------------

        //----------------------------------

//        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port).maxInboundMessageSize(10000000);
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);

        //----------------
//        SocketAddress remoteAddress = new InetSocketAddress("10.152.13.203", 9090);
//        NettyServerBuilder remoteServerBuilder = NettyServerBuilder.forAddress(remoteAddress);
//        this.server = remoteServerBuilder
//                .addService(p4RuntimeService)
//                .build();
        //----------------

//        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(1000);
//        this.server = serverBuilder.executor(threadPoolExecutor).addService(p4RuntimeService)
//                .build();
//        this.server = serverBuilder.maxInboundMessageSize(256 * 1024 * 1024 * 7).addService(p4RuntimeService).maxInboundMessageSize(256 << 20)

        this.server = serverBuilder.addService(p4RuntimeService)
                .build();

//        this.server = NettyServerBuilder.forPort(port)
//                .addService(p4RuntimeService)
//                .addService(ProtoReflectionService.newInstance())
              // //  .maxConcurrentCallsPerConnection(10000000)
               // // .maxInboundMessageSize(256 * 1024 * 1024 * 7)
//                .build();

//        ThreadFactory tf = new DefaultThreadFactory("server-elg-", true);
//        // On Linux it can, possibly, be improved by using
//        // io.netty.channel.epoll.EpollEventLoopGroup
//        // io.netty.channel.epoll.EpollServerSocketChannel
//        final EventLoopGroup boss = new NioEventLoopGroup(1, tf);
////        EpollEventLoopGroup group = new EpollEventLoopGroup();
//        final EventLoopGroup worker = new NioEventLoopGroup(0, tf);
//        final Class<? extends ServerChannel> channelType = NioServerSocketChannel.class;
////        final Class<? extends ServerChannel> channelType = EpollServerSocketChannel.class;
////        final Class<? extends ServerChannel> channelType = EpollServerDomainSocketChannel.class;
//        this.server = NettyServerBuilder
//                .forPort(port)
//                .bossEventLoopGroup(boss)
//                .workerEventLoopGroup(worker)
//                .channelType(channelType)
//                .addService(ServerInterceptors.intercept(p4RuntimeService))
//                .flowControlWindow(NettyChannelBuilder.DEFAULT_FLOW_CONTROL_WINDOW)
//                .executor(getAsyncExecutor())
//                .build();
////		builder.directExecutor();
////        builder.executor(getAsyncExecutor());

    }

    private static Executor getAsyncExecutor() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                new ForkJoinPool.ForkJoinWorkerThreadFactory() {
                    final AtomicInteger num = new AtomicInteger();

                    @Override
                    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                        ForkJoinWorkerThread thread =
                                ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                        thread.setDaemon(true);
                        thread.setName("grpc-server-app-" + "-" + num.getAndIncrement());
                        return thread;
                    }
                }, UncaughtExceptionHandlers.systemExit(), true);
    }

//    @PreDestroy
//    public void destroy() {
//        threadPoolExecutor.shutdown();
//    }

    public Server getServer() {
        return this.server;
    }

    /**
     * Start serving requests.
     */
    public void start() throws IOException, InterruptedException {
        this.server.start();
        logger.info("Grpc server is starting at port: {}", this.port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    GrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });

//        this.server.awaitTermination();
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() throws InterruptedException {
        if (this.server != null) {
            logger.warn("Server on port: {} is stopping", this.port);
//            this.server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            this.server.shutdown();
        }
    }

}

//}