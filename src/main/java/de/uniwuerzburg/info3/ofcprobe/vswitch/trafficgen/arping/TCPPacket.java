/**
 * 
 */
package de.uniwuerzburg.info3.ofcprobe.vswitch.trafficgen.arping;

import java.util.Arrays;

import de.uniwuerzburg.info3.ofcprobe.util.AddressPositions;
import de.uniwuerzburg.info3.ofcprobe.util.Util;

/**
 * An ARP Packet containing a Payload(the byte[]-code of the ARP) and a Port on which it is coming in to an ofSwitch
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 *
 */
public class TCPPacket {

	private Short port;
	private byte[] payload;

	public TCPPacket(Short port, byte[] payload){
		this.port = port;
		this.payload = payload.clone();
	}
	
	public Short getPort(){
		return this.port;
	}
	
	public byte[] getPayload(){
		return this.payload;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TCPPacket other = (TCPPacket) obj;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}
	
	/**
	 * Prints the IP Dst in the ARP Packet
	 */
	public String IPDSTtoString(){
		return Util.fromIPvAddressBytes(Util.getBytes(this.payload, AddressPositions.ARP_IP_DST, 4));
	}
	
	/**
	 * Prints the IP SRC in the ARP Packet
	 * @return
	 */
	public String IPSRCtoString(){
		return Util.fromIPvAddressBytes(Util.getBytes(this.payload, AddressPositions.ARP_IP_SRC, 4));
	}
}