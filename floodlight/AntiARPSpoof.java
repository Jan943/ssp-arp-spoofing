package pl.edu.agh.kt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.projectfloodlight.openflow.types.IPv4Address;
import org.restlet.engine.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.kt.HostsInfo.HostInfo;
import pl.edu.agh.kt.PacketExtractor.IPInfo;

public class AntiARPSpoof {
	
	private static final Logger logger = LoggerFactory.getLogger(Flows.class);
	
	public static int thresholdPerSecond = 1000;
	public static boolean enabled = true;
	
	private int counterSecond = -1;
	private int counterValue;
	
	public boolean isSpoof(PacketExtractor extractor, HostsInfo hostsInfo) {
		
		if (!enabled)
			return false;
		
		if (!extractor.isARP())
			return false;
		
		//logger.info("ARP spoof check!");
		
		IPInfo ipInfo = extractor.extractIPInfo();
		
		HostInfo hi = hostsInfo.getByHostIp(ipInfo.srcIp);
		if (counterSecond != new Date().getSeconds()){
			counterSecond = new Date().getSeconds();
			counterValue = 0;
		}
		
		if (counterValue > thresholdPerSecond){
			logger.info("ARP MITIGATION");
			return true;
		}
		
		if(!hi.hostMacAddress.equals(extractor.getMACAddress())){
			logger.info("SPOOF DETECTED! " + hi.hostMacAddress + " =/= " + extractor.getMACAddress());
			counterValue++;
			return true;
		}
		
		return false;
	}

}
