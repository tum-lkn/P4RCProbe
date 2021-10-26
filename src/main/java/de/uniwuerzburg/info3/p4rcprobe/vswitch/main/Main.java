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
package de.uniwuerzburg.info3.p4rcprobe.vswitch.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.Connection;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config.SwitchConfig;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.runner.OFSwitchRunner;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.runner.P4RuntimeSwitchRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.main.config.Config;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.runner.SwitchRunner;
import de.uniwuerzburg.info3.p4rcprobe.vswitch.trafficgen.TrafficGen;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * This Class is actually executed first when starting p4rcprobe.Vswitch from the
 * cmd line. It loads up a provided configuration file an instantiates the
 * VirtualOFSwitch Objects. After that the benching begins. In the whole Javadoc
 * 'ofSwitch' is an instance of the 'Connection' interface, so atm an
 * instance of 'OFConnection1_zero'
 *
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 * @version 1.0.3-SNAPSHOT
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private List<SwitchRunner> switchThreads;

    public void startStuff(String[] args) throws IOException, InterruptedException {
        this.switchThreads = new ArrayList<>();

        if (args.length < 1 || args.length > 2) {
            logger.error("Error! Need at least one argument! May have 2 Arguments Maximum:");
            logger.error("1.Argument: ConfigFile; 2.Argument(optional): SwitchCount");
            throw new IllegalArgumentException();
        }

        String configFile = new String();
        if (args.length > 0) {
            configFile = args[0];
        }
        int switchCount = -1;
        if (args.length == 2) {
            switchCount = Integer.parseInt(args[1]);
        }
        Config config = new Config(configFile, switchCount);
        switchCount = config.getCountSwitches();

        int ThreadCount = config.getThreadCount();
        int switchesPerThread = (int) (switchCount / ThreadCount);
        int rest = switchCount % ThreadCount;

        config.getSwitchConfig().setSession(switchCount);
        config.getRunnerConfig().setCountSwitches(switchesPerThread);

        List<SwitchRunner> switchRunners = new ArrayList<>();
        List<Thread> switchThreads = new ArrayList<>();
        Map<SwitchRunner, Thread> switchThreadMap = new HashMap<>();
        TrafficGen trafficGen = new TrafficGen(config);

        int startDpid = config.getStartDpid();
        int initializedSwitches = 0;

        if (switchCount < ThreadCount) {
            ThreadCount = switchCount;
        }

        for (int i = 0; i < ThreadCount; i++) {
            if (i < rest) {
                config.getRunnerConfig().setCountSwitches(switchesPerThread + 1);
                config.getRunnerConfig().setStartDpid(initializedSwitches + startDpid);
                initializedSwitches += (switchesPerThread + 1);

            } else {
                config.getRunnerConfig().setCountSwitches(switchesPerThread);
                config.getRunnerConfig().setStartDpid(initializedSwitches + startDpid);
                initializedSwitches += switchesPerThread;
            }

            SwitchRunner ofswitch;
            if (config.getSouthboundProtocol().equals("openflow")) {
                ofswitch = new OFSwitchRunner(config, this);
            } else {
                ofswitch = new P4RuntimeSwitchRunner(config, this);
            }

            Thread switchThread = new Thread(ofswitch, "switchThread#" + i);

            trafficGen.registerSwitchThread(ofswitch);
            switchThread.start();

            switchRunners.add(ofswitch);
            switchThreads.add(switchThread);
            this.switchThreads.add(ofswitch);
            switchThreadMap.put(ofswitch, switchThread);
        }

        if (config.getSouthboundProtocol().equals("p4runtime")) {


            JSONObject deviceObj = new JSONObject();

            for (int i = 1; i < switchCount + 1; i++) {
                JSONObject basicObj = new JSONObject();

                int switchPort =  config.getFirstP4runtimeSwitchPort() + i;

                basicObj.put("driver", "bmv2");
                basicObj.put("managementAddress", "grpc://" + config.getP4runtimeSwitchesAddress() + ":" + switchPort + "?device_id=" + i);
                basicObj.put("pipeconf", "p4-tutorial-pipeconf");

                JSONObject portsObj = new JSONObject();
                SwitchConfig switchConfig = config.getSwitchConfig();
                int portCount = switchConfig.getPortCountperSwitch();

                for (int j = 1; j < portCount + 1; j++) {
                    JSONObject portDetailsObj = new JSONObject();
                    portDetailsObj.put("name", "s" + i + "-eth" + j);
                    portDetailsObj.put("speed", 10000);
                    portDetailsObj.put("enabled", true);
                    portDetailsObj.put("number", j);
                    portDetailsObj.put("removed", false);
                    portDetailsObj.put("type", "copper");
                    portsObj.put(Integer.toString(j), portDetailsObj);
                }

                JSONObject p4runtimeObj = new JSONObject();
                p4runtimeObj.put("ip", config.getP4runtimeSwitchesAddress());
                p4runtimeObj.put("port", config.getFirstP4runtimeSwitchPort() + i);
                p4runtimeObj.put("deviceKeyId", "p4runtime:device:bmv2:s" + i);
                p4runtimeObj.put("deviceId", i);

                JSONObject generalProviderObj = new JSONObject();
                generalProviderObj.put("p4runtime", p4runtimeObj);

                JSONObject deviceDetailsObj = new JSONObject();
                deviceDetailsObj.put("generalprovider", generalProviderObj);
                deviceDetailsObj.put("ports", portsObj);
                deviceDetailsObj.put("basic", basicObj);

                deviceObj.put("device:bmv2:s" + i, deviceDetailsObj);
            }
            JSONObject devicesObj = new JSONObject();
            devicesObj.put("devices", deviceObj);

            Writer file = null;

            try {

                // Constructs a FileWriter given a file name, using the platform's default charset
                file = new FileWriter("toOnos.json");
                file.write(devicesObj.toJSONString());
                logger.info("Successfully Copied JSON Object to File...");
//                logger.info("\nJSON Object: " + obj);

            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                try {
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("FILE FLUSH OR CLOSE FAILED");
                    e.printStackTrace();
                }
            }


            runCommand("curl -X POST -H content-type:application/json http://" + config.getControllerAddress() + ":8181/onos/v1/network/configuration -d @toOnos.json --user onos:rocks");
        }


        Thread trafficThread = new Thread(trafficGen, "TrafficGen");

        // Start benching
        trafficThread.start();
        double targetPackets = (1000.0 / config.getTrafficGenConfig().getIAT()) * config.getTrafficGenConfig().getFillThreshold() * config.getTrafficGenConfig().getCountPerEvent();
        try {
            Thread.sleep(2 * config.getStartDelay());
            logger.info("Benching now started! - Target Packets Generated per connected ofSwitch per Second: {}", targetPackets);
            logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Thread.sleep(config.getSimTime() + config.getStopDelay());
            logger.info("{} sec gone!", config.getSimTime() / 1000);
        } catch (InterruptedException e) {
            // Auto-generated catch block
            logger.debug(e.getLocalizedMessage());
        }

        // Benching done, now stop TrafficGen
        trafficThread.interrupt();

        for (SwitchRunner SwitchRunner : switchRunners) {
            SwitchRunner.endSession();
        }

        long WAITTIME = config.getStopDelay();
        int threadsStopped = 0;
        Date benchinEnd = new Date();
        Date now = new Date();
        while (threadsStopped < ThreadCount || now.getTime() - benchinEnd.getTime() < 10000) {
            now = new Date();
            for (SwitchRunner SwitchRunner : switchRunners) {
                if (now.getTime() - SwitchRunner.lastPacketInTime() > WAITTIME) {
                    threadsStopped++;
                }
            }
        }

        // Stop all SwitchThreads
        for (Thread t : switchThreads) {
            t.interrupt();
        }

        logger.info("Interrupted, now ending");

        // Now end session, evaluate and do the reporting
        for (SwitchRunner SwitchRunner : switchRunners) {
            if (config.getSouthboundProtocol().equals("openflow")) {
                SwitchRunner.getSelector().wakeup();
            }
            SwitchRunner.evaluate();
            SwitchRunner.report();
        }

        logger.info("Session ended!");
        logger.info("~~~~~~~~~~~~~~");
        System.out.println();
        System.exit(0);
    }

    public static void runCommand(String cmd) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);

        OutputStream os = pr.getOutputStream();

        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            logger.info(line);
        }
    }

    /**
     * Main Method
     *
     * @param args atm: 1.Argument: Configfile; 2.Argument: SwitchCount;
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        main.startStuff(args);
    }

    /**
     * Returns the Connection for a given DPID
     *
     * @param dpid the DPID
     * @return the corresponding Connection
     */
    public Connection getIOFConByDpid(long dpid) {
        Iterator<SwitchRunner> iter = this.switchThreads.iterator();
        while (iter.hasNext()) {
            SwitchRunner runner = iter.next();
            Connection ofSwitch = runner.getSwitch(dpid);
            if (ofSwitch != null) {
                return ofSwitch;
            }
        }
        return null;
    }

}
