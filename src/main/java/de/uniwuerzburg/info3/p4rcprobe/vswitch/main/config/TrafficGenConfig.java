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
package de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.ipgen.IPGeneratorType;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.macgen.MACGeneratorType;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.portgen.PortGeneratorType;

/**
 * Configuration of a p4rcprobe.vswitch.trafficgen.TrafficGen Module
 *
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 *
 */
public class TrafficGenConfig {

    /**
     * The time between two RefillEvents
     */
    private int iat;
    /**
     * Use a static Payload?
     */
    private boolean staticPayload;
    /**
     * Fill every queue up to this threshold
     */
    private int threshold;
    /**
     * Number of Payloads generated per Event
     */
    private int countPerEvent;
    private String scenario;
    private boolean switchHasSettings;
    private boolean arpEnabled;
    private MACGeneratorType macGenType;
    private IPGeneratorType ipGenType;
    private PortGeneratorType portGenType;
    private int iatType;
    private String distriType;
    private Double distriPara1;
    private Double distriPara2;
    private boolean onlyTopoPayloads;
    boolean getOnlyOneHostPerSwitch;

    /**
     * Constructor with default values.
     */
    public TrafficGenConfig() {
        this.iat = 100;
        this.staticPayload = false;
        this.threshold = 100;
        this.countPerEvent = 1;
        this.scenario = "TCPSYN";
        this.switchHasSettings = false;
        this.arpEnabled = false;
        this.macGenType = MACGeneratorType.SERIAL;
        this.ipGenType = IPGeneratorType.SERIAL;
        this.portGenType = PortGeneratorType.SERIAL;
        this.iatType = 0;
        this.distriType = "none";
        this.distriPara1 = 0.0;
        this.distriPara2 = 0.0;
        this.onlyTopoPayloads = false;
        this.getOnlyOneHostPerSwitch = false;
    }

    /**
     * Set the time between two refill-events
     *
     * @param iat the time between two refill-events
     */
    public void setIAT(int iat) {
        this.iat = iat;
    }

    /**
     * Get the time between two refill-events
     *
     * @return the time between two refill-events
     */
    public int getIAT() {
        return this.iat;
    }

    /**
     * Set Send static Payload
     *
     * @return true -> always queueing the same (once generated) Payload
     */
    public boolean getStaticPayloadFlag() {
        return this.staticPayload;
    }

    /**
     * Get Send static Payload?
     *
     * @param statFlag true -> always queueing the same (once generated) Payload
     */
    public void setStaticPayloadFlag(boolean statFlag) {
        this.staticPayload = statFlag;
    }

    /**
     * Sets: Fill every queue up to this threshold
     *
     * @param thres Fill every queue up to this threshold
     */
    public void setFillThreshold(int thres) {
        this.threshold = thres;
    }

    /**
     * Gets: Fill every queue up to this threshold
     *
     * @return Fill every queue up to this threshold
     */
    public int getFillThreshold() {
        return this.threshold;
    }

    /**
     * Sets: Number of Payloads generated per Event
     *
     * @param cnt Number of Payloads generated per Event
     */
    public void setCountPerEvent(int cnt) {
        this.countPerEvent = cnt;
    }

    /**
     * Gets: Number of Payloads generated per Event
     *
     * @return Number of Payloads generated per Event
     */
    public int getCountPerEvent() {
        return this.countPerEvent;
    }

    /**
     * Gets the Scenario
     *
     * @return
     */
    public String getScenario() {
        return this.scenario;
    }

    /**
     * Sets the Scenario
     *
     * @param scenario the String
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * Sets whether switches have individual IAT/FillThreshold Settings
     *
     * @param flag the Flag
     */
    public void setSwitchHasIndividualSetting(boolean flag) {
        this.switchHasSettings = flag;
    }

    /**
     * Gets whether switches have individual IAT/FillThreshold Settings
     *
     * @return
     */
    public boolean getSwitchHasIndividualSetting() {
        return this.switchHasSettings;
    }

    /**
     * Sets arp enabled flag
     *
     * @param flag
     */
    public void setArpEnabled(boolean flag) {
        this.arpEnabled = flag;
    }

    /**
     * Gets ARP enabled flag
     *
     * @return
     */
    public boolean getArpFlag() {
        return this.arpEnabled;
    }

    /**
     * Gets MACGeneratorType
     *
     * @return the MACGeneratorType
     */
    public MACGeneratorType getMacGenType() {
        return this.macGenType;
    }

    /**
     * Gets the IPGeneratorType
     *
     * @return the IPGeneratorType
     */
    public IPGeneratorType getIPGenType() {
        return this.ipGenType;
    }

    /**
     * Gets the PortGeneratorType
     *
     * @return the PortGeneratorType
     */
    public PortGeneratorType getPortGenType() {
        return this.portGenType;
    }

    /**
     * Sets the MACGeneratorType
     *
     * @param type the MACGeneratorType
     */
    public void setMACGeneratorType(MACGeneratorType type) {
        if (type != null) {
            this.macGenType = type;
        }
    }

    /**
     * Sets the IPGeneratorType
     *
     * @param type the IPGeneratorType
     */
    public void setIPGeneratorType(IPGeneratorType type) {
        if (type != null) {
            this.ipGenType = type;
        }
    }

    /**
     * Sets the PortGeneratorType
     *
     * @param type the PortGeneratorType
     */
    public void setPortGeneratorType(PortGeneratorType type) {
        if (type != null) {
            this.portGenType = type;
        }
    }

    /**
     * String representation
     *
     * @return
     */
    @Override
    public String toString() {
        double targetPackets = (1000.0 / this.iat) * this.threshold * this.countPerEvent;
        String output = "TrafficGenConfig: Scenario=" + this.scenario + "; Arping=" + this.arpEnabled + "; fillThreshold=" + this.threshold
                + "; IAT=" + this.iat + "; countPerEvent=" + this.countPerEvent + "; TargetGeneratedPackets=" + targetPackets
                + "; Static Payload=" + this.staticPayload + "; individualSwitchSettings=" + this.switchHasSettings
                + "; MACGenType=" + this.macGenType + "; IPGenType=" + this.ipGenType + "; PortGenType=" + this.portGenType;
        return output;
    }

    public void setIatType(int type) {
        this.iatType = type;
    }

    public void setDistribution(String distri) {
        this.distriType = distri;
    }

    public void setDistributionPara1(Double para1) {
        this.distriPara1 = para1;
    }

    public void setDistributionPara2(Double para2) {
        this.distriPara2 = para2;
    }

    public int getIatType() {
        return this.iatType;
    }

    public String getDistribution() {
        return this.distriType;
    }

    public double getDistributionPara1() {
        return this.distriPara1;
    }

    public double getDistributionPara2() {
        return this.distriPara2;
    }

    public void setOnlyTopoPayloads(boolean flag) {
        this.onlyTopoPayloads = flag;
    }

    public boolean getOnlyTopoPayloads() {
        return this.onlyTopoPayloads;
    }

    public void setOnlyOneHostPerSwitch(boolean flag) {
        this.getOnlyOneHostPerSwitch = flag;
    }

    boolean getOnlyOneHostPerSwitch() {
        return this.getOnlyOneHostPerSwitch;
    }
}
