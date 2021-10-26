/*
 * Copyright 2021 khaled.sherkawi.
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

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.Connection;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.Main;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.List;

public interface SwitchRunner extends Runnable {

    /**
     * All Switches ready for input?
     *
     * @return true if All Switches Ready
     */
    boolean isReady();

    /**
     * Init Switches
     */
    void init();

    /**
     * Connection handling happens here.
     */
    @Override
    void run();

    /**
     * Get Switches of this SwitchRunner
     *
     * @return List containing ofSwitches of this switchRunner
     */
    List<Connection> getSwitches();

    /**
     * Send to all connected ofSwitches the evaluate command
     */
    void evaluate();

    /**
     * Send all connected ofSwitches the report command
     */
    void report();

    /**
     * Set flag in all switches so that packets comming in/going out are now
     * processed by the Statistic modules.
     */
    void startBenching();

    /**
     * Closes Connection between OpenFlowController and vSwitch
     */
    void endSession();

    /**
     * The Selector for this Thread.
     *
     * @return the selector
     */
    Selector getSelector();

    /**
     * Last Time a Packet needed for Benching from a controller came in
     *
     * @return time in millis as long
     */
    long lastPacketInTime();

    /**
     * Has provided ofSwitch had OpenFlow Messages? Needed e.g. for NOX: after
     * successfull TCP Session, NOX does NOTHING, so each 'switch' has to send a
     * OFHello for Handshake with Floodlight sending a OFHello at any time
     * follows an immediate Disconnect from the Controller.
     *
     * @param ofSwitch
     */
    void alrdyOpenFlowed(Connection ofSwitch);

    /**
     * Connects Provided ofSwitch and then adds it to the Selector
     *
     */
    void initOFSwitchConnections(Connection ofSwitch) throws IOException, InterruptedException;

    /**
     * Gets the Main Method
     *
     * @return
     */
    Main getMain();

    /**
     * Gets you the Switch with DPID=dpid
     *
     * @param dpid the DPID
     * @return the Switch
     */
    Connection getSwitch(long dpid);

}
