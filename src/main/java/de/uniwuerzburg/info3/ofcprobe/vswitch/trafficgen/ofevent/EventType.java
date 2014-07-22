package de.uniwuerzburg.info3.ofcprobe.vswitch.trafficgen.ofevent;

public enum EventType {
	// Resembles OFPacketIn Message
	PACKET_IN_EVENT,
	OFSWITCH_CONNECT_EVENT,
	OFSWITCH_CONCHECK_EVENT,
	OFSWITCH_QUEUESWITCH_EVENT,
	OFSWITCH_DISCONNECT_EVENT,
	GENERATION_END,
	ARP_EVENT,
	TCP_AFTER_ARP

}