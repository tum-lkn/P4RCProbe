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
package de.uniwuerzburg.info3.p4rcprobe.vswitch.runner;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.Connection;
import org.openflow.protocol.OFHello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.openflow.OFConnection1_zero;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.Main;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config.Config;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config.RunnerConfig;

/**
 * This Class reasambles a Thread who has controll over a defined count of
 * ofSwitch objects.
 *
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 */
public class OFSwitchRunner implements SwitchRunner {

    /**
     * The debug instance for this object
     */
    private static final Logger logger = LoggerFactory.getLogger(OFSwitchRunner.class);
    /**
     * List of 'connected' ofSwitches
     */
    private List<Connection> switches;
    /**
     * the DPID of the first ofSwitch
     */
    private int startdpid;
    /**
     * the Amount of ofSwitches that have to be initiated
     */
    private int countswitches;

    /**
     * The Selector for the connectionhandling
     */
    private Selector selector;
    /**
     * the OpenFlow Protocol Version used in this Run
     */
    private int openflow_version;
    /**
     * When has this Instance been Initiated?
     */
    private Date initialized;
    /**
     * The Configuration for this Session
     */
    private Config config;
    /**
     * Has the BenchingSession already ended?
     */
    private boolean sessionEnded;
    /**
     * Already one ofSwitch connected ?
     */
    private boolean selectorInitialized;
    /**
     * The corresponding starting Thread
     */
    private Main main;

    /**
     * Creates a new OFSwitchRunner with the Parameters of the provided
     * config-object.
     *
     * @param config the config-object
     */
    public OFSwitchRunner(Config config, Main main) {
        this.main = main;
        //initialize Lists
        this.switches = new ArrayList<Connection>();
        this.sessionEnded = false;
        this.selectorInitialized = false;

        this.config = config;
        RunnerConfig runConfig = config.getRunnerConfig();

        this.startdpid = runConfig.getStartDpid();
        this.countswitches = runConfig.getCountswitches();

        this.openflow_version = config.getProtocolVersion();

        // Here the OpenFlowJ Library Version is chosen
        // Only supports OpenFlow Version 1.0 right now!
        if (this.openflow_version == 1) {
            init();
        }
    }

    /**
     * Is switch connected?
     *
     * @param switchNo SwitchNo 1 -> index 0!
     * @return true-> this switch is Connected, false-> this switch is not
     * connected!
     */
    public boolean isConnected(int switchNo) {
        if (switchNo - 1 < this.switches.size()) {
            return this.switches.get(switchNo - 1).getChannel().isConnected();
        } else {
            return false;
        }
    }

    @Override
    public boolean isReady() {
        Date now = new Date();
        boolean allConnected = true;
        for (Connection ofSwitch : this.switches) {
            if (ofSwitch.getChannel() != null) {
                if (!ofSwitch.getChannel().isConnected()) {
                    allConnected = false;
                }
            }
        }

        if (!allConnected || now.getTime() - this.initialized.getTime() < 10000) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void init() {
        this.initialized = new Date();

        // For loop that instantiates the ofSwitch Objects
        for (int i = 0; i < this.countswitches; i++) {
            int dpid = i + this.startdpid;

            // Method names should speak for whats happening
            this.config.getSwitchConfig().setDpid(dpid);
            Connection ofcon = new OFConnection1_zero(this, this.config);

            // add ofSwitch to our Collection
            this.switches.add(ofcon);

        }

        logger.info("All {} Switches have been initiated!", this.countswitches);
        logger.info("Controller-Address: {}", this.config.getSwitchConfig().getContAddress().toString());
    }

    /**
     * Configure this Channel.
     *
     * @param chan the Channel to Configure
     * @throws IOException is processed by calling Method
     */
    private SocketChannel configureChannel(SocketChannel chan) throws IOException {
        chan.configureBlocking(false);
        if (this.config.getSwitchConfig().disableNagle()) {
            // Disable Nagle's Alogrithm
            logger.trace("Nagle Algorithm Disabled!");
            chan.socket().setTcpNoDelay(true);
        }
        return chan;
    }

    /**
     * Initializes the Selector
     *
     * @return the initialized Selector
     * @throws IOException handled by calling Method
     */
    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        return socketSelector;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // check if handshakes done (10sek after initialization)
                if (isReady()) {
                    // and then start sending the generated and queued packets
                    queueProcessing();
                }

                // the actual connection handling
                if (this.selector != null) {
                    this.selector.selectNow();
                    Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                    // Process all keys
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        selectedKeys.remove();

                        // Check whether key is a valid one
                        if (!key.isValid()) {
                            continue;
                        }

                        // Try to process key
                        try {
                            processKey(key);
                        } catch (ConnectException e) {
                            logger.error("Cannot connect to Controller! Is it started yet?");
                            logger.error("Exiting ...");
                            System.exit(1);
                        } catch (IOException e) {
                            logger.error("{}", e);
                        }
                    }
                }
            } catch (ClosedSelectorException e) {
                logger.error("Selector has been closed while operating!");
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }

    }

    /**
     * Process PacketInQueue of every ofSwitch
     */
    private void queueProcessing() {
        boolean packetOutQueueEmpty = false;
        while (!packetOutQueueEmpty) {
            if (this.sessionEnded) {
                break;
            }
            packetOutQueueEmpty = true;

            for (Connection ofSwitch : this.switches) {
                if (!outQueue(ofSwitch)) {
                    packetOutQueueEmpty = false;
                }
            }
            for (Connection ofSwitch : this.switches) {
                ofSwitch.receive();
            }
        }
    }

    /**
     * Process outGoing Queue of a ofSwitch
     *
     * @param ofSwitch will be processed
     * @return flag
     */
    private boolean outQueue(Connection ofSwitch) {
        boolean packetOutQueueEmpty;
        if (ofSwitch.hasPacketInQueued()) {

            packetOutQueueEmpty = false;
            ofSwitch.sendPacketIn();
        } else {
            packetOutQueueEmpty = true;
        }
        return packetOutQueueEmpty;
    }

    /**
     * KeyProcessor.
     *
     * @param key SelectionKey
     * @throws IOException
     */
    private void processKey(SelectionKey key) throws IOException {
        try {
            //Get the Channel of this Key
            SocketChannel chan = (SocketChannel) key.channel();

            if (key.isReadable() && chan.isOpen() && key.isValid()) {
                // Get the Connection/the Switch for this Channel
                Connection toRead = (Connection) key.attachment();
                if (toRead != null) {
                    // Process Incoming Message
                    toRead.receive();
                }
            }

            if (key.isWritable() && chan.isOpen() && key.isValid()) {
                //write queuedMsgs(this.packetQueues) to channel
                write(chan, key);
            }

            if (key.isConnectable()) {
                // connect selected Channel
                connect(chan, key);
            }
        } catch (CancelledKeyException e) {
            logger.error("Selector has been closed while operating!");
        }
    }

    /**
     * Method which writes queued Payloads from queuePacketInEvent(..) to the
     * Socket of the corresponding ofSwitch
     *
     * @param chan the channel (and with it connected: the ofSwitch)
     * @param key SelectionKey
     */
    private void write(SocketChannel chan, SelectionKey key) {

    }

    /**
     * Finish Connection for SocketChannel
     *
     * @param chan
     * @param key
     * @throws IOException
     */
    private void connect(SocketChannel chan, SelectionKey key) throws IOException {
        if (chan.finishConnect()) {
            key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        }

    }

    @Override
    public List<Connection> getSwitches() {
        return this.switches;
    }

    @Override
    public void evaluate() {
        for (Connection ofswitch : this.switches) {
            ofswitch.evaluate();
        }
    }

    @Override
    public void report() {
        for (Connection ofswitch : this.switches) {
            ofswitch.report();
        }
    }

    @Override
    public void startBenching() {
        for (Connection ofswitch : this.switches) {
            ofswitch.startSession();
        }
    }

    @Override
    public void endSession() {
        this.sessionEnded = true;
        for (Connection ofswitch : this.switches) {
            ofswitch.stopSession();
        }
    }

    @Override
    public Selector getSelector() {
        return selector;
    }

    @Override
    public long lastPacketInTime() {
        long lastPacketInTime = new Date().getTime();

        for (Connection ofswitch : this.switches) {
            if (lastPacketInTime > ofswitch.lastPacketInTime()) {
                lastPacketInTime = ofswitch.lastPacketInTime();
            }
        }
        return lastPacketInTime;
    }

    /**
     * Have all instantiated ofSwitches had OpenFlow Messages? Needed e.g. for
     * NOX: after successfull TCP Session, NOX does NOTHING, so each 'switch'
     * has to send a OFHello for Handshake with Floodlight sending a OFHello at
     * any time follows an immediate Disconnect from the Controller.
     */
    public void alrdyOpenFlowed() {
        for (Connection ofSwitch : this.switches) {
            alrdyOpenFlowed(ofSwitch);

        }
    }

    @Override
    public void alrdyOpenFlowed(Connection ofSwitch) {
        if (!ofSwitch.hadOFComm()) {
            ofSwitch.sendOFPacket(new OFHello());
        }
    }

    @Override
    public void initOFSwitchConnections(Connection ofSwitch) {
        if (!this.selectorInitialized) {
            try {
                this.selector = initSelector();
                this.selectorInitialized = true;
            } catch (IOException e) {
                logger.error("Selectorinit Failed: {}", e);
            }
        }

        // Create a new non-blocking socket channel
        SocketChannel chan;
        try {
            chan = SocketChannel.open();
            chan = configureChannel(chan);
            // Set this Channel to an OfSwitch -> all Communication of this ofSwitch will happen over this channel
            ofSwitch.setChannel(chan);

            // Connect Socket;
            chan.connect(this.config.getSwitchConfig().getContAddress());

            // Register the socket channel, indicating an interest in
            // accepting new connections and attaching the ofSwitch object
            chan.register(this.selector, SelectionKey.OP_CONNECT, ofSwitch);
        } catch (IOException e) {
            // Auto-generated catch block
            logger.error("ofSwitch Connect Failed: {}", e);
        }
    }

    @Override
    public Main getMain() {
        return this.main;
    }

    @Override
    public Connection getSwitch(long dpid) {
        Iterator<Connection> iter = this.switches.iterator();
        while (iter.hasNext()) {
            Connection ofSwitch = iter.next();
            if (ofSwitch.getDpid() == dpid) {
                return ofSwitch;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + countswitches;
        result = prime * result
                + ((initialized == null) ? 0 : initialized.hashCode());
        result = prime * result + ((main == null) ? 0 : main.hashCode());
        result = prime * result + openflow_version;
        result = prime * result + (selectorInitialized ? 1231 : 1237);
        result = prime * result + (sessionEnded ? 1231 : 1237);
        result = prime * result + startdpid;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OFSwitchRunner other = (OFSwitchRunner) obj;
        if (countswitches != other.countswitches) {
            return false;
        }
        if (initialized == null) {
            if (other.initialized != null) {
                return false;
            }
        } else if (!initialized.equals(other.initialized)) {
            return false;
        }
        if (main == null) {
            if (other.main != null) {
                return false;
            }
        } else if (!main.equals(other.main)) {
            return false;
        }
        if (openflow_version != other.openflow_version) {
            return false;
        }
        if (selectorInitialized != other.selectorInitialized) {
            return false;
        }
        if (sessionEnded != other.sessionEnded) {
            return false;
        }
        if (startdpid != other.startdpid) {
            return false;
        }
        return true;
    }

}
