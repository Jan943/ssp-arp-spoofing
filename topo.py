from mininet.topo import Topo
from mininet.net import Mininet

class SSPTopo( Topo ):
 "SSP ARP spoofing topology."
 def __init__( self ):
 # Initialize topology
        Topo.__init__( self )
 # Add hosts and switches
        h1 = self.addHost( 'h1' )
        h2 = self.addHost( 'h2' )
        h3 = self.addHost( 'h3' )
        h4 = self.addHost( 'h4' )

        s1 = self.addSwitch( 's1' ) #linking h1 and h2
        s2 = self.addSwitch( 's2' ) #linking h3 and h4
        s3 = self.addSwitch( 's3' ) #linking s1 and s2
 # Add links
        self.addLink( h1, s1 )
        self.addLink( h2, s1 )
        self.addLink( h3, s2 )
        self.addLink( h4, s2 )
        self.addLink( s1, s3 )
        self.addLink( s2, s3 )
topos = { 'ssptopo': ( lambda: SSPTopo() ) }

def main():
        print("Starting")

        print("Starting MiniNet")
        topo = SSPTopo()
        net = Mininet(topo=topo)
        net.start()

        for host in net.hosts:
                host.cmd("iperf -s -p 5001 &")
                host.cmd("./generator.sh &")

        net.interact()

if __name__ == '__main__':
        main()
