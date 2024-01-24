package pl.edu.agh.kt;

import java.util.Collection;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.kt.HostsInfo.HostInfo;
import pl.edu.agh.kt.PacketExtractor.IPInfo;

public class SdnLabListener implements IFloodlightModule, IOFMessageListener {

	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	
	public HostsInfo hostsInfo = new HostsInfo();
	public AntiARPSpoof antiARPSpoof = new AntiARPSpoof();

	@Override
	public String getName() {
		return SdnLabListener.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg,
			FloodlightContext cntx) {
		
		if (msg.getType() == OFType.PACKET_IN){
			
			OFPacketIn pin = (OFPacketIn) msg;

			PacketExtractor extractor = new PacketExtractor(cntx, msg);
			
			
			if (extractor.isIPv6() || antiARPSpoof.isSpoof(extractor, hostsInfo)) {
				return Command.STOP;
			}
			
			logger.info("************* NEW PACKET IN *************" + " " + sw.getId() + " on port " + pin.getInPort());
			
			DatapathId switchId = sw.getId();
			
			/*
			 * Po prostu routing
			 */
			
			if (switchId.equals(DatapathId.of("00:00:00:00:00:00:00:03"))){
				if (pin.getInPort() == OFPort.of(1))
					Flows.forwardPacket(sw, pin, OFPort.of(2));
				else if (pin.getInPort() == OFPort.of(2))
					Flows.forwardPacket(sw, pin, OFPort.of(1));
			} else {
				
				IPInfo ipInfo = extractor.extractIPInfo();

				if (ipInfo == null) {
					logger.info("!!!!!!!!!!!!! IPInfo null");
					return Command.STOP;
				}
				
				if (ipInfo.dstIp.equals(IPv4Address.of("255.255.255.255"))){
					/*if (pin.getInPort() != OFPort.of(1))
						Flows.forwardPacket(sw, pin, OFPort.of(1));
					if (pin.getInPort() != OFPort.of(2))
						Flows.forwardPacket(sw, pin, OFPort.of(2));
					if (pin.getInPort() != OFPort.of(3))
						Flows.forwardPacket(sw, pin, OFPort.of(3));
					if (pin.getInPort() != OFPort.of(4))
						Flows.forwardPacket(sw, pin, OFPort.of(4));*/
					return Command.STOP;
				}
				
				HostInfo hi = hostsInfo.getByHostIp(ipInfo.dstIp);
				
				if (hi == null){
					logger.info("!!!!!!!!!!!!! HostInfo null");
					return Command.STOP;
				}
				
				if (hi.switchDatapathId.equals(switchId)){
					Flows.forwardPacket(sw, pin, hi.switchPort);
				} else {
					Flows.forwardPacket(sw, pin, OFPort.of(3));
				}
					
				
			}
			
		}

		return Command.STOP;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		logger = LoggerFactory.getLogger(SdnLabListener.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		logger.info("******************* START **************************");

	}

}
