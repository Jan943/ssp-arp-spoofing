# Sterownik Floodlight jako reaktywny firewall przeciwdziałający atakowi typu ARP spoofing

# Topologia
Topologia składa się z trzech przełączników OpenFlow Open vSwitch (S1, S2 i S3). Przełączniki S1 i S2 są przełącznikami krawędziowymi (edge), a S3 to przełącznik pośredni (core). Przełączniki te są połączone w topologii drzewa. Topologia posiada również 4 hosty (H1, H2, H3 i H4), trzy z nich (H1, H2 i H3) to normalni użytkownicy końcowi (end user), a czwarty (H4) działa jako atakujący (attacker). W tej topologii będzie znajdować się również pojedynczy kontroler OpenFlow. Wszystko zostało stworzone w emulatorze Mininet.

# Literatura:
1.Ahmed M Abdelsalam, Ashraf El-Sisi i Vamshi Reddy,"Mitigating ARP Spoofing Attacks in Software-Defined Networks", ICCTA 2015
https://www.researchgate.net/publication/299369116_Mitigating_ARP_Spoofing_Attacks_in_Software-Defined_Networks

2.Sergey Morzhov, Igor V. Alekseev i Mikhail Nikitinskiy, "Firewall application for Floodlight SDN controller", SIBCON 2016
https://www.researchgate.net/publication/304021770_Firewall_application_for_Floodlight_SDN_controller

3.Saritakumar N., Anusuya K. V. i Ajitha S., "Detection and Mitigation of ARP Poisoning Attack in Software Defined Network", ICCAP 2021
https://eudl.eu/pdf/10.4108/eai.7-12-2021.2314502

4.Ghadeer Darwesh, Alisa Vorobeva i Viktoria Korzhuk, "An efficient mechanism to detect and mitigate an ARP spoofing attack in software-defined networks", 2021
https://www.researchgate.net/publication/353532250_An_efficient_mechanism_to_detect_and_mitigate_an_ARP_spoofing_attack_in_software-defined_networks
