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
import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.P4RuntimeConnection;
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
public class P4RuntimeSwitchRunner implements SwitchRunner {

    /**
     * The debug instance for this object
     */
    private static final Logger logger = LoggerFactory.getLogger(P4RuntimeSwitchRunner.class);
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
    private int p4runtime_version;
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
     * Creates a new P4RuntimeSwitchRunner with the Parameters of the provided
     * config-object.
     *
     * @param config the config-object
     */
    public P4RuntimeSwitchRunner(Config config, Main main) {
        this.main = main;
        //initialize Lists
        this.switches = new ArrayList<Connection>();
        this.sessionEnded = false;
        this.selectorInitialized = false;

        this.config = config;
        RunnerConfig runConfig = config.getRunnerConfig();

        this.startdpid = runConfig.getStartDpid();
        this.countswitches = runConfig.getCountswitches();

        this.p4runtime_version = config.getProtocolVersion();

        // Here the OpenFlowJ Library Version is chosen
        // Only supports OpenFlow Version 1.0 right now!
        if (this.p4runtime_version == 1) {
            init();
        }
    }

    @Override
    public boolean isReady() {
        Date now = new Date();
        boolean allConnected = true;
        for (Connection ofSwitch : this.switches) {
            if (!ofSwitch.isServerConnected()) {
                allConnected = false;
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
            Connection ofcon = new P4RuntimeConnection(this, dpid, this.config);

            // add ofSwitch to our Collection
            this.switches.add(ofcon);

        }

        logger.info("All {} Switches have been initiated!", this.countswitches);
        logger.info("Controller-Address: {}", this.config.getSwitchConfig().getContAddress().toString());
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
            } catch (ClosedSelectorException e) {
                logger.error("Selector has been closed while operating!");
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

    @Override
    public void alrdyOpenFlowed(Connection ofSwitch) {
    }

    @Override
    public void initOFSwitchConnections(Connection ofSwitch) throws IOException, InterruptedException {
            ofSwitch.startServer();
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
        result = prime * result + p4runtime_version;
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
        P4RuntimeSwitchRunner other = (P4RuntimeSwitchRunner) obj;
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
        if (p4runtime_version != other.p4runtime_version) {
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
