from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import RemoteController

class SSPTopo( Topo ):
 "SSP ARP spoofing topology."
 def __init__( self ):
 # Initialize topology
        Topo.__init__( self )
 # Add hosts and switches
        h1 = self.addHost( 'h1',mac="52:e5:0a:4b:c0:52" )
        h2 = self.addHost( 'h2',mac="82:60:eb:d9:67:2d" )
        h3 = self.addHost( 'h3',mac="8e:50:d3:f7:cd:d2" )
        h4 = self.addHost( 'h4',mac="aa:96:d4:47:dd:c4" )

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
        net.addController(RemoteController('Controller',ip='127.0.0.1',port=6653))
        net.start()

        for host in net.hosts:
                host.cmd("iperf -s -p 5001 &")
                host.cmd("./generator.sh &")

        net.interact()

if __name__ == '__main__':
        main()
