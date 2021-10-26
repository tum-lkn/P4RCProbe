/* 
 * Copyright 2016 christopher.metter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniwuerzburg.info3.p4rcprobe.vswitch.connection;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.List;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.runner.SwitchRunner;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPhysicalPort;

/**
 * Interface for all ofSwitch Implementations. This is the current Set of
 * required Methods per Implementation. Basically Handles the OFMessages for a
 * TCPSocket
 *
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 *
 */
public interface Connection {

    /**
     * Sends OFMessage to Controller.
     *
     * @param out packet in
     */
    void sendOFPacket(OFMessage out);

    /**
     * Sends P4Runtime packet to Controller.
     *
     * @param out packet in
     * @param port port in
     */
    void sendP4runtimePacket(byte[] out, short port);

    /**
     * Receives P4Runtime packet from Controller.
     *
     * @param packet packet out
     */
    void receiveP4runtimePacket(P4RuntimeOuterClass.PacketOut packet);

    /**
     * Receives P4Runtime Flow Rule.
     *
     * @param writeRequest packet out
     */
    void receiveP4runtimeFlowRule(P4RuntimeOuterClass.WriteRequest writeRequest);

    /**
     * Receives OFMessage from controller.
     */
    void receive();

    /**
     * Gets OFVersion
     *
     * @return byte array 0x01 -> Version 1.0
     */
    byte getProtocolVersion();

    /**
     * Sets the SocketChannel
     *
     * @param chan the SocketChannel
     * @throws IOException can be thrown ;P
     */
    void setChannel(SocketChannel chan) throws IOException;

    /**
     * Gets the SocketChannel
     *
     * @return theSocketChannel of this ofSwitch
     */
    SocketChannel getChannel();

    /**
     * Checks is server is running
     *
     * @return Boolean
     */
    boolean isServerConnected();

    /**
     * Start gRPC server
     *
     *
     */
    void startServer() throws IOException, InterruptedException;

    /**
     * Stop gRPC server
     *
     *
     */
    void stopServer() throws InterruptedException;

    /**
     * Returns String Representation
     *
     * @return String Representation
     */
    @Override
    String toString();

    /**
     * getNextFreeBufferId.
     *
     * @return next Free Buffer Id of this ofSwitch
     */
    int getNextFreeBufferId();

    /**
     * Start Evaluation.
     */
    void evaluate();

    /**
     * Start Reporting.
     */
    void report();

    /**
     * Set Flag to Benching. All future Packets will now be processed by the
     * Statistics MOdules
     */
    void startSession();

    /**
     * Stop Session. Wait for x Seconds (configurable), when in this interval no
     * new packet comes in, the channel will be closed
     */
    void stopSession();

    /**
     * queue a new Packet for the PacketInMsgQueue
     *
     * @param payload a complete TCP/UDP/... Packet as byte[]
     * @param port the incoming Port of this
     * @param safeFlag safe this payload?
     */
    void queuePacketIn(byte[] payload, short port, boolean safeFlag);

    /**
     * queue multiple Packets (see #queuePacketIn(byte[] payload, int port)).
     *
     * @param payloadList
     */
    void queuePacketInS(Collection<byte[]> payloadList);

    /**
     * capsulate packets queue in OFPacketIn Messages and send them to
     * Controller.
     */
    void sendPacketIn();

    /**
     * Check if packetQueue of this ofSwitch Object is empty
     *
     * @return true-> packets queued, false -> queue is empty
     */
    boolean hasPacketInQueued();

    /**
     * Returns the PacketInQueeLength.
     *
     * @return packetInQueue.length()
     */
    int packetInQueueLength();

    /**
     * When has last OFPacketIn arrived.
     *
     * @return the time since last OFPacketIn
     */
    long lastPacketInTime();

    /**
     * Gets the Thread for this ofSwitch.
     *
     * @return the Thread/Object running this ofSwitch
     */
    SwitchRunner getRunner();

    /**
     * Has this ofSwitch already communicated in OF-Protocol?
     *
     * @return yes or no ;)
     */
    boolean hadOFComm();

    /**
     * Returns the connection establishment time Delay between initialization of
     * the Switch
     *
     * @return Time after Time in Millis!
     */
    long getConDelay();

    /**
     * Returns the Stop Time Delay of the Switch 0 = immediately, x = 0+x
     *
     * @return Time after Time in Millis!
     */
    long getStopDelay();

    /**
     * Returns the Stop Time Delay of the Switch 0 = immediately, x = 0+x
     *
     * @return
     */
    long getStartDelay();

    /**
     * The DPID
     *
     * @return dpid as long
     */
    long getDpid();

    /**
     * Get the Switches ports
     *
     * @return the ports
     */
    List<OFPhysicalPort> getPorts();

    /**
     * Get the IAT of generated Packet_ins
     *
     * @return
     */
    int getIAT();

    /**
     * Get the FillThreshold for this ofSwitch
     *
     * @return the fillthreshold
     */
    int getFillThreshold();

    /**
     * Get the PcapFileName. If not set, String is "notSet!"
     *
     * @return the PcapFileName
     */
    String getPcapFileName();

    /**
     * Get the Set Distribution for this ofSwitch
     *
     * @return the Distribution; "none" when not set
     */
    String getDistribution();

    /**
     * Get Distribution Parameter1
     *
     * @return
     */
    double getDistributionPara1();

    /**
     * Get Distiribution Parameter2
     *
     * @return
     */
    double getDistributionPara2();

}
