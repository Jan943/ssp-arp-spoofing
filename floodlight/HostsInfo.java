package pl.edu.agh.kt;

import java.util.ArrayList;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;

public class HostsInfo {

	public ArrayList<HostInfo> hostsInfo = new ArrayList<HostInfo>();
	
	public HostsInfo() {		
		addHostInfo("H1", "00:00:00:00:00:00:00:01", 1, "10.0.0.1", "52:e5:0a:4b:c0:52");
		addHostInfo("H2", "00:00:00:00:00:00:00:01", 2, "10.0.0.2", "82:60:eb:d9:67:2d");
		addHostInfo("H3", "00:00:00:00:00:00:00:02", 1, "10.0.0.3", "8e:50:d3:f7:cd:d2");
		addHostInfo("H4", "00:00:00:00:00:00:00:02", 2, "10.0.0.4", "aa:96:d4:47:dd:c4");
	}
	
	public void addHostInfo(String hostname, String switchDatapathId, int switchPort, String hostIp, String hostMacAddress){
		hostsInfo.add(new HostInfo(hostname, DatapathId.of(switchDatapathId), OFPort.of(switchPort), IPv4Address.of(hostIp), MacAddress.of(hostMacAddress)));
	}
	
	public void addHostInfo(String hostname, DatapathId switchDatapathId, OFPort switchPort, IPv4Address hostIp, MacAddress hostMacAddress){
		hostsInfo.add(new HostInfo(hostname, switchDatapathId, switchPort, hostIp, hostMacAddress));
	}
	
	public HostInfo getByDatapathId(DatapathId dpid) {
		HostInfo hostInfo = null;
		
		for (HostInfo hi : hostsInfo){
			if (hi.switchDatapathId.getLong() == dpid.getLong()) {
				hostInfo = hi;
				break;
			}
		}
		
		return hostInfo;
	}
	
	public HostInfo getByHostIp(IPv4Address hostIp) {
		HostInfo hostInfo = null;
		
		for (HostInfo hi : hostsInfo){
			if (hi.hostIp.equals(hostIp)) {
				hostInfo = hi;
				break;
			}
		}
		
		return hostInfo;
	}
	
	public class HostInfo {
		
		public HostInfo(String name, DatapathId switchDatapathId, OFPort switchPort, IPv4Address hostIp, MacAddress hostMacAddress){
			this.name = name;
			this.switchDatapathId = switchDatapathId;
			this.switchPort = switchPort;
			this.hostIp = hostIp;
			this.hostMacAddress = hostMacAddress;
		}
		
		public String name;
		public DatapathId switchDatapathId;
		public OFPort switchPort;
		public IPv4Address hostIp;
		public MacAddress hostMacAddress;
		
		@Override
		public String toString(){
			return "[name="+name + " | switchDatapathId=" + switchDatapathId + "  | switchPort=" + switchPort + " | hostIp=" + hostIp + "]";
		}
	}
	
}
