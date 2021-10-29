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
package de.uniwuerzburg.info3.p4rcprobe.vswitch.statistics;

import de.uniwuerzburg.info3.p4rcprobe.vswitch.connection.p4runtime.grpc.P4RuntimeOuterClass;
import org.openflow.protocol.OFMessage;

/**
 * Interface for Statistic Modules, every written Module has to have these Methods.
 * @author Christopher Metter(christopher.metter@informatik.uni-wuerzburg.de)
 *
 */
public interface IStatistics {
	
	/**
	 * Defines the targetFile
	 * @param file outputfile
	 */
	void setReportFile(String file);
	
	/**
	 * Incoming Packet FROM the Controller
	 * @param in OFMessage from Controller
	 */
	void OFpacketIn(OFMessage in);
	
	/**
	 * Outgoing Packet TO the Controller
	 * @param out OFMessage to Controller
	 */
	void OFpacketOut(OFMessage out);

	/**
	 * Incoming Packet FROM the Controller
	 * @param in P4Runtime packet from Controller
	 */
	void P4RuntimePacketIn(byte[] in);

	/**
	 * Outgoing Packet TO the Controller
	 * @param out P4Runtime packet to Controller
	 */
	void P4RuntimePacketOut(byte[] out);

	/**
	 * Incoming FlowRule
	 * @param writeRequest Write RPC
	 */
	void P4RuntimeWriteRPC(P4RuntimeOuterClass.WriteRequest writeRequest);

	/**
	 * Evaluate Modules Statistics and produce results
	 */
	void evaluate();
	
	/**
	 * Do tha reportin'
	 */
	void report();

	/**
	 * Session now Started
	 */
	void start();
	
	/**
	 * Session now over
	 */
	void stop();
}
