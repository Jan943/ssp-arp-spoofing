package pl.edu.agh.kt;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.TCP;
import net.floodlightcontroller.packet.UDP;

public class PacketExtractor {
	private static final Logger logger = LoggerFactory
			.getLogger(PacketExtractor.class);
	private FloodlightContext cntx;
	protected IFloodlightProviderService floodlightProvider;
	private Ethernet eth;
	private IPv4 ipv4;
	private ARP arp;
	private TCP tcp;
	private UDP udp;
	private OFMessage msg;

	public PacketExtractor(FloodlightContext cntx, OFMessage msg) {
		this.cntx = cntx;
		this.msg = msg;
		
		// Pobieranie ramki Ethernet z kontekstu
		eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	}
	
	public boolean isIPv6() {
		if (eth.getEtherType() == EthType.IPv6)
			return true;
		return false;
	}
	
	public boolean isARP() {
		if (eth.getEtherType() == EthType.ARP)
			return true;
		return false;
	}
	
	public MacAddress getMACAddress(){
		return eth.getSourceMACAddress();
	}

	public IPInfo extractIPInfo() {

		// Wyświetlanie informacji o ramce Ethernet
		logger.info("Frame: src mac {}", eth.getSourceMACAddress());
		logger.info("Frame: dst mac {}", eth.getDestinationMACAddress());
		logger.info("Frame: ether_type {}", eth.getEtherType());

		// Sprawdzenie typu ether_type
		if (eth.getEtherType() == EthType.ARP) {
			// Jeśli to ramka ARP, wyodrębnij informacje
			arp = (ARP) eth.getPayload();
			logger.info("ARP: src ip {}", arp.getSenderProtocolAddress());
			logger.info("ARP: dst ip {}", arp.getTargetProtocolAddress());
			logger.info("ARP: op code {}", arp.getOpCode());
			
			return new IPInfo(arp.getSenderProtocolAddress(), arp.getTargetProtocolAddress());
			
		} else if (eth.getEtherType() == EthType.IPv4) {
			// Jeśli to ramka IPv4, wyodrębnij informacje
			ipv4 = (IPv4) eth.getPayload();
			logger.info("IPv4: src ip {}", ipv4.getSourceAddress());
			logger.info("IPv4: dst ip {}", ipv4.getDestinationAddress());
			logger.info("IPv4: protocol {}", ipv4.getProtocol());
			return new IPInfo(ipv4.getSourceAddress(), ipv4.getDestinationAddress());
			
			/*
			if (ipv4.getProtocol() == IpProtocol.TCP) {
				// Jeśli to ramka TCP, wyodrębnij informacje
				TCP tcp = (TCP) ipv4.getPayload();
				logger.info("TCP: src port {}", tcp.getSourcePort());
				logger.info("TCP: dst port {}", tcp.getDestinationPort());
			} else if (ipv4.getProtocol() == IpProtocol.UDP) {
				// Jeśli to ramka UDP, wyodrębnij informacje
				UDP udp = (UDP) ipv4.getPayload();
				logger.info("UDP: src port {}", udp.getSourcePort());
				logger.info("UDP: dst port {}", udp.getDestinationPort());
			}
			*/
		}
		
		return null;
	}
	
	class IPInfo {
		
		public IPInfo(IPv4Address srcIp, IPv4Address dstIp) {
			this.srcIp = srcIp;
			this.dstIp = dstIp;
		}
		
		public IPv4Address srcIp;
		public IPv4Address dstIp;
	}

}
