# Sterownik Floodlight jako reaktywny firewall przeciwdziałający atakowi typu ARP spoofing

# Topologia
Topologia składa się z trzech przełączników OpenFlow Open vSwitch (S1, S2 i S3). Przełączniki S1 i S2 są przełącznikami krawędziowymi (edge), a S3 to przełącznik pośredni (core). Przełączniki te są połączone w topologii drzewa. Topologia posiada również 4 hosty (H1, H2, H3 i H4), trzy z nich (H1, H2 i H3) to normalni użytkownicy końcowi (end user), a czwarty (H4) działa jako atakujący (attacker). W tej topologii będzie znajdować się również pojedynczy kontroler OpenFlow. Wszystko zostało stworzone w emulatorze Mininet.

![ssp_arp_topo](https://github.com/Jan943/ssp-arp-spoofing/assets/46823541/f2ac8693-2e20-471a-aa29-7a183860b9b3)

# Uruchomienie:
Uruchomienie topologii (oraz automatyczne włączenie generatorów ruchu) następuje po wydaniu polecenia:

$ floodlight > sudo python topo.py 

Działanie generatorów ruchu można zweryfikować za pomocą komendy:

$ mininet > h1 jobs

# Generowanie ruchu:
## iperf
Do generowania ruchu w sieci zostanie wykorzystane narzędzie  iperf, umożliwające symulowanie intensywnego ruchu między serwerem a klientami w celu analizy i pomiaru parametrów sieciowych. 

Parametry uruchomieniowe narzędzia iperf:

-s - uruchamia iperf jako serwer (przyjmujący połączenia przychodzące),  
-c $adresIP - uruchamia iperf jako klienta, który łączy się z serwerem o wskazanym adresie IP,  
-p $nr_portu - określa numer portu, na którym iperf nasłuchuje / do którego klient się podłącza  
-t $czas - określa czas trwania testu (podawany w sekundach)  
-i $interwał - określa interwał, w którym iperf będzie wyświetlał wyniki (podawany w sekundach)  
-u - użycie protokołu UDP zamiast TCP  

Zaawansowane parametry uruchomieniowe narzędzia iperf:

-P $liczba_wątków - określa liczbę równoczesnych połączeń,  
-L $zakres_portow - określa zakres losowych portów do wyboru (od:do)  
-B $adres_IP - określa adres IP interfejsu używanego do nawiązywania połączeń.  

 W celu automatyzacji generowania dużej ilości losowego ruchu między hostami w topologii minine zostanie wykorzystany skrypt wykorzystujący narzędzie iperf. Będzie uruchomiony na jednym z hostów w ww. topologii, tak aby każdy z tych hostów jednocześnie wysyłał dane do innego losowo wybranego hosta na losowym porcie symulując zróżnicowany ruch sieciowy. Skrypt będzie uruchomiony na jednym z hostów w tej topologii. 

## arpspoof

Do generowania ataku typu ARP spoofing wykorzystany zostanie narzedzie arpspoof pakietu dsniff. Dzięki temu przez nieprawdziwe rozgłoszenia ARP powoduje, ze komputer (ofiara) wysyła do intruza wszystkie pakiety, zamiast do zadanego IP. Działa to tak, ze jest podmieniany adres fizyczny z prawdziwego na adres intruza dla pewnego konkretnego IP.

arpspoof [-i interface] [-c own|host|both] [-t target] [-r] host

Parametry uruchomieniowe narzędzia arpspoof:

-i $interfejs - określa interfejs, na którym ma działać arpspoof,  
-c własny|hosta|oba - określa, czy atak ma obejmować tylko własny ruch (own), ruch hosta (host), czy oba (both),  
-t $cel - określa cel ataku,  
-r - odwraca kierunek ataku (opcjonalne),  
host - określa host, który ma być podszywany.

Przykładowe użycie arpspoof:

$ mininet > h1 arpspoof -i h1-eth0 -t 10.0.0.2 10.0.0.3

Przykładowy wynik arpspoof:

$ f2:9d:47:78:6b:76 ba:59:2b:25:13:9 0806 42: arp reply 10.0.0.3 is-at 72:4e:b0:f:1d:d6

# Literatura:
1.Ahmed M Abdelsalam, Ashraf El-Sisi i Vamshi Reddy,"Mitigating ARP Spoofing Attacks in Software-Defined Networks", ICCTA 2015
https://www.researchgate.net/publication/299369116_Mitigating_ARP_Spoofing_Attacks_in_Software-Defined_Networks

2.Sergey Morzhov, Igor V. Alekseev i Mikhail Nikitinskiy, "Firewall application for Floodlight SDN controller", SIBCON 2016
https://www.researchgate.net/publication/304021770_Firewall_application_for_Floodlight_SDN_controller

3.Saritakumar N., Anusuya K. V. i Ajitha S., "Detection and Mitigation of ARP Poisoning Attack in Software Defined Network", ICCAP 2021
https://eudl.eu/pdf/10.4108/eai.7-12-2021.2314502

4.Ghadeer Darwesh, Alisa Vorobeva i Viktoria Korzhuk, "An efficient mechanism to detect and mitigate an ARP spoofing attack in software-defined networks", 2021
https://www.researchgate.net/publication/353532250_An_efficient_mechanism_to_detect_and_mitigate_an_ARP_spoofing_attack_in_software-defined_networks
