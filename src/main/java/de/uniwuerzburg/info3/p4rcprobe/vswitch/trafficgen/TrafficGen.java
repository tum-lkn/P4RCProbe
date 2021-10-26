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
package de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config.Config;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.runner.SwitchRunner;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.arping.ARPManager;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.arping.ArpPacket;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.arping.TCPPacket;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.ofevent.*;

/**
 * TrafficGenerator Event Based Traffic Generator Simulator
 *
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 *
 */
public class TrafficGen implements Runnable {

    /**
     * Debugger
     */
    private static final Logger logger = LoggerFactory.getLogger(TrafficGen.class);

    /**
     * Inter Arrival Time of two Events
     */
    private int IAT;
    /**
     * List of Switches
     */
    private List<Connection> switches;
    /**
     * List of connected Switchs
     */
    private List<Connection> connectedSwitches;
    /**
     * The EventQueue
     */
    private TreeMap<Date, List<IOFEvent>> eventQueue;
    /**
     * the PayloadGenerator
     */
    private PayLoadGen payloadGen;
    /**
     * Map of switchStarters and their Switches
     */
    private Map<SwitchRunner, List<Connection>> switchstarters;
    /**
     * MasterTcp
     */
    private byte[] tcpsyn;
    /**
     * Count of Packets Generated per Event
     */
    private int countPerEvent;
    /**
     * Use Static Payload or randomly generate?
     */
    private boolean staticPacket;
    /**
     * Threshold for QueueFilling
     */
    private int fillThreshold;
    /**
     * The Config
     */
    private Config config;
    /**
     * The Southbound Protocol
     */
    private String protocol;
    /**
     * End Date of Simulation
     */
    private Date endSim;
    /**
     * Doe ofSwitches have individual Settings?
     */
    private boolean individualSwitchSettings;
    /**
     * Is IAT after distribution?
     */
    private boolean iatDistributed;
    /**
     * IATGenerator
     */
    private IATGen iatGen;
    /**
     * The ArpManager
     */
    private ARPManager arpGen;
    /**
     * The scenario
     */
    private String scenario;
    /**
     * Map of PCAPLoader for ofSwitchs
     */
    private Map<Long, PCAPLoader> pcapLoaderMap;
    /**
     * Map of IATGens for ofSwitchs
     */
    private Map<Long, IATGen> ofSwitchDistriMap;

    /**
     * Standard Constructor, initialized Stuff according to provided Config
     */
    public TrafficGen(Config config) {
        this.config = config;
        this.protocol = config.getSouthboundProtocol();
        this.scenario = config.getTrafficGenConfig().getScenario();
        if (this.scenario.equals("TCP")) {

        }
        this.payloadGen = new PayLoadGen(config);
        this.switches = new ArrayList<>();
        this.connectedSwitches = new ArrayList<>();
        this.switchstarters = new HashMap<SwitchRunner, List<Connection>>();
        this.tcpsyn = this.payloadGen.generateTCPSyn();
        this.staticPacket = config.getTrafficGenConfig().getStaticPayloadFlag();
        this.fillThreshold = config.getTrafficGenConfig().getFillThreshold();
        this.countPerEvent = config.getTrafficGenConfig().getCountPerEvent();
        this.IAT = config.getTrafficGenConfig().getIAT();
        this.individualSwitchSettings = config.getTrafficGenConfig().getSwitchHasIndividualSetting();
        logger.trace("TrafficGen has Been Constructed");
        this.pcapLoaderMap = new HashMap<Long, PCAPLoader>();
        this.ofSwitchDistriMap = new HashMap<Long, IATGen>();
        if (this.config.getTrafficGenConfig().getIatType() == 1) {
            this.iatDistributed = true;
            this.iatGen = new IATGen(config.getTrafficGenConfig().getDistribution(),
                    config.getTrafficGenConfig().getDistributionPara1(),
                    config.getTrafficGenConfig().getDistributionPara2());
        } else {
            this.iatDistributed = false;
        }
    }

    /**
     * Registers OFSwitchStarter to this EventGen, so Events will be generated
     * for this OFSwitchStarter
     *
     * @param switchRunner
     */
    public void registerSwitchThread(SwitchRunner switchRunner) {
        this.switches.addAll(switchRunner.getSwitches());
        this.switchstarters.put(switchRunner, switchRunner.getSwitches());

        for (Connection ofSwitch : switchRunner.getSwitches()) {
            if (!ofSwitch.getPcapFileName().equals("notSet!")) {
                PCAPLoader loader = new PCAPLoader(ofSwitch);
                loader.load();
                this.pcapLoaderMap.put(ofSwitch.getDpid(), loader);
            }
            if (!ofSwitch.getDistribution().equals("none!")) {
                IATGen iatGen = new IATGen(ofSwitch.getDistribution(), ofSwitch.getDistributionPara1(), ofSwitch.getDistributionPara2());
                this.ofSwitchDistriMap.put(ofSwitch.getDpid(), iatGen);
            }
        }
    }

    /**
     * Initialises the EventQueue.
     */
    private void initQueue() {
        this.eventQueue = new TreeMap<>();
        eventQueue.clear();

        Date now = new Date();
        Date startSim = new Date(now.getTime() + 2 * config.getStartDelay());

        // Schedule End of Simulation: Now + 2*StartDelay + SimTime + SafetyBuffer
        this.endSim = new Date(startSim.getTime() + config.getSimTime() + config.getStopDelay());
        generateNext(this.endSim, EventType.GENERATION_END);

        for (Connection ofSwitch : this.switches) {
            long connectDelay = ofSwitch.getConDelay();
            IOFEvent connectEvent = generateEvent(ofSwitch, EventType.OFSWITCH_CONNECT_EVENT);
            Date connectDate = new Date(now.getTime() + connectDelay);

            queueEvent(connectDate, connectEvent);

            long disconnectDelay = ofSwitch.getStopDelay();
            if (disconnectDelay != 0) {
                IOFEvent disconnectEvent = generateEvent(ofSwitch, EventType.OFSWITCH_DISCONNECT_EVENT);
                Date disconnectDate = new Date(startSim.getTime() + disconnectDelay);
                if (disconnectDate.before(this.endSim)) {
                    queueEvent(disconnectDate, disconnectEvent);
                }
            }
        }

        if (this.config.getTrafficGenConfig().getArpFlag()) {
            // (trafficGenConfig.arpEnabled = true)
            this.arpGen = new ARPManager(config, this.switches);
            this.payloadGen = null;
        }

    }

    /**
     * Queue an Event into the Eventqueue
     *
     * @param targetDate targetDate
     * @param event the Event
     */
    private void queueEvent(Date targetDate, IOFEvent event) {
        List<IOFEvent> eventList = eventQueue.get(targetDate);
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        eventList.add(event);
        eventQueue.put(targetDate, eventList);
        logger.trace("Event has been Queued: Date: {} - Event: {}", targetDate.toString(), event.toString());
    }

    /**
     * Generate a new Event and add it to the (existing) List of this Date
     *
     * @param targetDate Date when this event shall be executed
     */
    private void generateNext(Date targetDate, EventType eventType) {

        List<IOFEvent> eventList = eventQueue.get(targetDate);
        if (eventList == null) {
            eventList = new ArrayList<IOFEvent>();
        }
        for (int i = 0; i < this.countPerEvent; i++) {
            for (Connection con : this.connectedSwitches) {
                eventList.add(generateEvent(con, eventType));
            }
        }
        eventQueue.put(targetDate, eventList);
        logger.trace("{} PacketIns have been queued", eventList.size());
    }

    /**
     * Generate a new Event and add it to the (existing) List of this Date
     *
     * @param now Date when this event shall be executed
     */
    private void generateIndividualNext(Date now, Connection ofSwitch, EventType eventType) {

        for (int i = 0; i < this.countPerEvent; i++) {
            long nextTime = now.getTime() + getIAT(ofSwitch);
            Date target = new Date(nextTime);

            List<IOFEvent> eventList = eventQueue.get(target);
            if (eventList == null) {
                eventList = new ArrayList<>();
            }

            eventList.add(generateEvent(ofSwitch, eventType));
            eventQueue.put(target, eventList);
            logger.trace("{} PacketIns have been queued for ofSwitch{}", eventList.size(), ofSwitch.getDpid());
        }

    }

    /**
     * Actually generates the Event, currently only PacketIn supported
     *
     * @param ofSwitch the ofSwitch for this Event
     * @return the generated Event
     */
    private IOFEvent generateEvent(Connection ofSwitch, EventType eventType) {
        IOFEvent output = null;
        switch (eventType) {
            case GENERATION_END:
                output = new GenerationEndEvent(ofSwitch);
                break;
            case OFSWITCH_CONCHECK_EVENT:
                output = new OFSwitchConCheckEvent(ofSwitch);
                break;
            case OFSWITCH_CONNECT_EVENT:
                output = new OFSwitchConnectEvent(ofSwitch);
                break;
            case OFSWITCH_DISCONNECT_EVENT:
                output = new OFSwitchDisconnectEvent(ofSwitch);
                break;
            case OFSWITCH_QUEUESWITCH_EVENT:
                output = new OFSwitchQueueSwitchEvent(ofSwitch);
                break;
            case ARP_EVENT:
                output = new ARPEvent(ofSwitch);
                break;
            case TCP_AFTER_ARP:
                output = new TCPafterARP(ofSwitch);
                break;
            case PACKET_IN_EVENT:
                output = new PacketInEvent(ofSwitch);
                break;
            default:
                break;
        }
        return output;
    }

    /**
     * Calculates time Thread has to sleep between two Events
     *
     * @param d1 Date1
     * @param d2 Date2
     * @return sleepingTime (always >= 0)
     */
    private long sleepingTime(Date d1, Date d2) {
        long sleeptime = d2.getTime() - d1.getTime();
        if (sleeptime < 0) {
            return 0;
        } else {
            return sleeptime;
        }
    }

    /**
     * Run Method.
     */
    @Override
    public void run() {
        initQueue();
        Set<SwitchRunner> starters = new LinkedHashSet<>();
        while (!Thread.interrupted()) {
            Date now = new Date();

            // Get first Entry
            Entry<Date, List<IOFEvent>> currentEventEntry = this.eventQueue.pollFirstEntry();

            if (currentEventEntry != null) {
                // Sleep till next Key/Event
                try {
                    long stuff = sleepingTime(now, currentEventEntry.getKey());
                    Thread.sleep(stuff);
                } catch (InterruptedException e) {
                    logger.error(e.getLocalizedMessage());
                    Thread.currentThread().interrupt();

                }

                // List of events that have to be processed
                List<IOFEvent> nextEventList = (List<IOFEvent>) currentEventEntry.getValue();
                Date eventTime = currentEventEntry.getKey();

                // Generate new PacketIn for every 'connected' ofSwitch
                for (IOFEvent event : nextEventList) {
                    logger.trace("Processing Event: {}", event.toString());
                    SwitchRunner ofRunner = event.getCon().getRunner();
                    Connection ofSwitch = event.getCon();

                    switch (event.getType()) {
                        case OFSWITCH_CONNECT_EVENT:
                            // Connect Channel
                            try {
                                ofRunner.initOFSwitchConnections(event.getCon());
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Schedule con_check_event
                            IOFEvent concheck = generateEvent(event.getCon(), EventType.OFSWITCH_CONCHECK_EVENT);
                            queueEvent(new Date(eventTime.getTime() + config.getStartDelay()), concheck);
                            break;
                        case OFSWITCH_CONCHECK_EVENT:
                            // wenn alles okay --> schedule ofswitch_queueswitch_event
                            // + schedule ofswitch_disconnect_event mittels runtime - stopDelay
                            ofRunner.alrdyOpenFlowed(event.getCon());

                            // Schedule queueSwitching Event
                            IOFEvent queueSwitch = generateEvent(event.getCon(), EventType.OFSWITCH_QUEUESWITCH_EVENT);
                            queueEvent(new Date(eventTime.getTime() + config.getStartDelay() + event.getCon().getStartDelay()), queueSwitch);

                            break;
                        case OFSWITCH_QUEUESWITCH_EVENT:
                            // add ofSwitch to this.connectedSwitches
                            this.connectedSwitches.add(event.getCon());
                            event.getCon().startSession();

                            if (this.individualSwitchSettings && !this.config.getTrafficGenConfig().getOnlyTopoPayloads()) {
                                IOFEvent packet_in = generateEvent(event.getCon(), EventType.PACKET_IN_EVENT);
                                queueEvent(eventTime, packet_in);
                            }
                            if (this.config.getTrafficGenConfig().getArpFlag()) {
                                IOFEvent arping = generateEvent(event.getCon(), EventType.ARP_EVENT);
                                queueEvent(new Date(eventTime.getTime() + 1000), arping);
                            }

                            break;
                        case OFSWITCH_DISCONNECT_EVENT:
                            // remove ofSwitch from this.connectedSwitches
                            this.connectedSwitches.remove(event.getCon());
                            event.getCon().stopSession();

                            break;
                        case ARP_EVENT:
                            // Get ARPs for this ofSwitch
                            List<ArpPacket> arps = this.arpGen.getArpsForIOFConnection(ofSwitch);

                            // and Queue them on the ofSwitch
                            for (ArpPacket arp : arps) {
                                Short port = arp.getPort();
                                byte[] payload = arp.getPayload();
                                ofSwitch.queuePacketIn(payload, port, true);
                                logger.trace("Queued ARP to Switch({})", ofSwitch.toString());
                            }
                            // Schedule TCP Syn for this host after the ARPing
                            IOFEvent tcpAfterArp = generateEvent(ofSwitch, EventType.TCP_AFTER_ARP);
                            queueEvent(new Date(eventTime.getTime() + 2500), tcpAfterArp);

                            break;
                        case TCP_AFTER_ARP:
                            event.getCon().packetInQueueLength();
                            List<TCPPacket> tcpSyns = this.arpGen.getTCPSynsForIOFConnection(ofSwitch);
                            // and Queue them on the ofSwitch
                            for (TCPPacket tcpSyn : tcpSyns) {
                                Short port = tcpSyn.getPort();
                                byte[] payload = tcpSyn.getPayload();
                                ofSwitch.queuePacketIn(payload, port, true);
                                logger.trace("Queued TCPSYN after ARP to ofSwitch({})", ofSwitch.toString());
                            }
                            // Only Payloads from Topology 'Devices'
                            if (this.config.getTrafficGenConfig().getOnlyTopoPayloads()) {
                                IOFEvent tcpAfterTcps = generateEvent(ofSwitch, EventType.TCP_AFTER_ARP);
                                queueEvent(new Date(eventTime.getTime() + getIAT(ofSwitch)), tcpAfterTcps);
                            }

                            break;
                        case PACKET_IN_EVENT:
                            if (ofRunner != null) {

                                starters.add(ofRunner);
                                // Calculate number of packets to queue in ofSwitch
                                int packetsQueued = event.getCon().packetInQueueLength();
                                // by subtracting threshold - filling(now)
                                int diff = this.fillThreshold - packetsQueued;
                                if (this.individualSwitchSettings) {
                                    diff = event.getCon().getFillThreshold() - packetsQueued;
                                }
                                if (diff < 0) {
                                    diff = 0;
                                }
                                // Create a List of Payloads
                                List<byte[]> payloads = new ArrayList<>(diff);
                                // Check wether this ofSwitch will get PCAP payloads
                                if (this.pcapLoaderMap.containsKey(ofSwitch.getDpid())) {
                                    logger.trace("Taking payload from pcap for {}", ofSwitch.toString());
                                    // Get em
                                    PCAPLoader pcapL = this.pcapLoaderMap.get(ofSwitch.getDpid());
                                    for (int i = 0; i < diff; i++) {
                                        // Add em
                                        payloads.add(pcapL.nextPayload());
                                    }
                                } else {
                                    for (int i = 0; i < diff; i++) {
                                        if (this.staticPacket) {
                                            // Get static
                                            payloads.add(this.tcpsyn);
                                        } else {
                                            // or generated Payload
                                            payloads.add(this.payloadGen.generateTCPSyn());
                                        }
                                    }
                                }
                                // and add the List to the ofSwitch's queue
                                event.getCon().queuePacketInS(payloads);

                                if (this.individualSwitchSettings) {
                                    generateIndividualNext(eventTime, event.getCon(), EventType.PACKET_IN_EVENT);
                                }
                            }
                            break;
                        case GENERATION_END:
                            event.getCon().stopSession();
                            this.connectedSwitches.remove(event.getCon());
                            break;
                        default:
                            break;

                    }
                }

                // now wake up all Selectors
                if(this.protocol.equals("openflow")){
                    for (SwitchRunner r : starters) {
                        r.getSelector().wakeup();
                    }
                }
                starters.clear();

                // Generate new Date
                now = currentEventEntry.getKey();

                if (now.before(this.endSim)) {
                    if (!this.individualSwitchSettings && !this.config.getTrafficGenConfig().getOnlyTopoPayloads()) {
                        int iat = getIAT(null);
                        Date next = new Date(now.getTime() + iat);

                        //Generate new Events for new Date
                        generateNext(next, EventType.PACKET_IN_EVENT);
                    }
                }
            }

        }
    }

    /**
     * Inter Arrival Time getter
     *
     * @param ofSwitch the ofSwitch
     * @return the IAT
     */
    private int getIAT(Connection ofSwitch) {
        int iat = 0;
        if (!this.individualSwitchSettings) {
            // Every ofSwitch gets Same stuff
            iat = this.IAT;
            if (this.iatDistributed) {
                iat = this.iatGen.nextIAT();
            }
        } else {
            // Every ofSwitch has individual IAT
            if (this.iatDistributed) {
                if (this.ofSwitchDistriMap.containsKey(ofSwitch.getDpid())) {
                    IATGen iatGen = this.ofSwitchDistriMap.get(ofSwitch.getDpid());
                    iat = iatGen.nextIAT();
                } else {
                    // get (standard) random genrated IAT
                    iat = this.iatGen.nextIAT();
                }
            } else {
                if (ofSwitch != null) {
                    // get IAT set by ofSwitch.ini File
                    iat = ofSwitch.getIAT();
                } else {
                    // Fallback
                    iat = this.IAT;
                }
            }
        }

        return iat;
    }

}
