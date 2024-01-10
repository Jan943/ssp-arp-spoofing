#!/bin/bash

# Lista adresów IP hostów
HOSTS=("10.0.0.1" "10.0.0.2" "10.0.0.3" "10.0.0.4")
SERWER_PORT=5001

while true; do
    for ((i=0; i<${#HOSTS[@]}; ++i)); do
        # Losowy indeks hosta docelowego
        index=$((RANDOM % ${#HOSTS[@]}))
        # Sprawdzenie, czy indeks nie wskazuje na ten sam host
        while [ $index -eq $i ]; do
            index=$((RANDOM % ${#HOSTS[@]}))
        done

        # Wysłanie danych z hosta o indeksie 'i' do hosta o losowym indeksie 'index' na losowym porcie "SERWER_PORT"
        iperf -c ${HOSTS[$index]} -p $SERWER_PORT -t 10 -i 5 -P 5 &
        sleep 2s         # Dodanie opóźnienia 2 sekund między testami
    done
    # Oczekiwanie na zakończenie wszystkich testów
    wait
done
