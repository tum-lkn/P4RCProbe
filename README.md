## P4RCProbe

The source code of a platform-independent and flexible SDN controller analysis tool P4RCProbe, an extension of OFCProbe, is available in this repository. It features 
a scalable and modular architecture that allows a granular and deep analysis of the controllers behaviors and 
characteristics. OpenFlow and P4Runtime controllers can be pushed to their limit and the bottlenecks can be investigated.
The tool allows the emulation of virtual switches to provide detailed statistics about the controller 
behavior.

Video Presentation: https://www.youtube.com/watch?v=B0OUKnEVhDs

## Table of Contents
- [Tutorial](#tut)
  - [Requirements](#req)
  - [Building](#building)
  - [Random IAT Values after Distribution](#radomdistri)
  - [Individual Settings for Switches](#indipcap)
- [Statistics Content](#statcontent)


## <a name="tut"></a>Tutorial

### <a name="req"></a>Requirements:

- Java 7
- OpenFlow or P4Runtime (tested for ONOS) Controller
- Preferably a Linux System (Shell Scripts + Screen Usage in Scripts provided)
- Passwordless SSH-Connection between OF Controller Host and P4RCProbe Host for User openflow
- Optional: SNMP Server on Controller Host (for CPU and RAM Utilization of OF Controller Host)

### <a name="building"></a>Building P4RCProbe

- Download and install Maven
- Make sure that your Maven setup allows new repositories
- Execute `mvn package` to download dependencies and compile OFCProbe.
- Command: `java -jar target/p4rcprobe-*.one-jar.jar demo.ini`
- The result can be found in `target/p4rcprobe-*.one-jar.jar`.

### <a name="randomdistri"></a>Random IAT Values after Distribution

Example for NormalDistribution, mean = 10, stdev=5:

* trafficGenConfig.iatType = 1
* trafficGenConfig.iatDistribution = Normal
* trafficGenConfig.iatDistributionParamter1 = 10
* trafficGenConfig.iatDistributionParamter2 = 5

### <a name="indipcap"></a>Individual Settings for Switches and PCAP File Playback

* Set config.checkForIndividualSwitchSettings = true in config.ini on P4RCProbeHost
* Check ofSwitch.ini and change it
* Allows to change IAT, fillThreshold, start- and stopDelay, PCAP file and Distribution Settings
* If pcapFile Options is set, this specific ofSwitch will only send OF_PACKET_INs with Payloads sequentially taken from that PCAP File (File will be looped)


### <a name="statcontent"></a>Statistics Content

see [STATISTICS_CONTENT](https://github.com/tum-lkn/P4RCProbe/blob/master/STATISTICS_CONTENT.md)
