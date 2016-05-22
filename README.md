# VoiceController


 * Smart Remote Voice Control Sketch with Android
 * Politecnico di Torino
 * APPLICAZIONI MULTIMEDIALI
 * A.A. 2015-2016
 * Copyright 2016
 * Authors: Manetta Jessica & Rollo   Manoj
 * PROGETTO D'ESAME
 * Telecomando universale per comandare i televisori tramite la voce
 * da smartphone Android collegato ad Arduio YÙN
 * Based on examples from the IR remote library at: https://github.com/shirriff/Arduino-IRremote
 * An IR LED must be connected to Arduino PWM pin 3.
 * COMANDI POSSIBILI DA ANDROID
 * Accendi TV
 * Spegni TV
 * Alza il volume
 * Abbassa il volume
 * Silenzio
 * Avanti
 * Indietro

```<language identifier>
//  Arduino code:
```
// Per connessione Wi-Fi
#include <Bridge.h>     //libreria usata per accedere a pin analogici e digitali
#include <BridgeServer.h>
#include <BridgeClient.h>
BridgeServer server;


// Per trasmettere IR LED
#include <IRremote.h>
IRsend irsend;
// --- START PROGRAM --- //

void setup()
{
  pinMode(3, INPUT);
  pinMode(13, OUTPUT);

  Bridge.begin();

  server.listenOnLocalhost();
  server.begin();
}

void loop()
{
  BridgeClient client = server.accept();     //si crea un'istanza client per gestire la connessione

  if (client.connected())
  {

    Serial.println("CLIENT CONNECTED!");
    process(client);

    client.stop();      // Close connection and free resources.
  }
 
  delay(100);     // Poll every 100ms
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void process(BridgeClient client)
{
  Serial.println("Interpreto comando ricevuto: ");
  String command = client.readStringUntil('/');   // legge il comando che noi gli iviamo tramite il computer fino a "arduino.local/arduino/
  Serial.print(command);

  if (command == "accendi")
  {
    Serial.println("\n Accendo TV");

    // Metodo per Samsung
    irsend.sendSamsung(0XE0E040BF,68); 
    
    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0xA90, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF10EF, 32);
    delay(200);
  }
  else if (command.equals("spegni"))
  {
    Serial.println("\n Spengo TV");

     // Metodo per Samsung
    irsend.sendSamsung(0XE0E040BF,68);
    
    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0xA90, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF10EF, 32);
    delay(200);
  }
  else if (command.equals("alza"))
  {
    Serial.println("\n Alzo il Volume");
    for(int i= 0; i<5; i++){
    // Metodo per Samsung
    irsend.sendSamsung(0XE0E0E01F,68);

    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0x490, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF40BF, 32);
    delay(200);
  }
  }
  else if (command.equals("abbassa"))
  {
    Serial.println("\n Abbasso il Volume");
    // Metodo per Samsung
    for(int i= 0; i<5; i++){
    irsend.sendSamsung(0XE0E0D02F,68);
    
    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0xc90, 12);
      delay(40);
    }
    }
    // Metodo per LG
    irsend.sendNEC(0x20DFC03F, 32);
    delay(200);
  }
   else if (command.equals("low"))
  {
    Serial.println("\n Abbasso il Volume");
    // Metodo per Samsung
    for(int i= 0; i<10; i++){
    irsend.sendSamsung(0XE0E0D02F,68);
    
    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0xc90, 12);
      delay(40);
    }
    }
    // Metodo per LG
    irsend.sendNEC(0x20DFC03F, 32);
    delay(200);
  }
  else if (command.equals("muto"))
  {
    Serial.println("\n Setto la TV su muto");

    // Metodo per Samsung
    irsend.sendSamsung(0XE0E0F00F,68);

    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0x290, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF906F, 32);
    delay(200);
  }else if (command.equals("voce"))
  {
    Serial.println("\n Setto la TV su muto");

    // Metodo per Samsung
    irsend.sendSamsung(0XE0E0F00F,68);

    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0x290, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF906F, 32);
    delay(200);
  }
  else if (command.equals("avanti"))
  {
    Serial.println("\n Passo al canale successivo");

    // Metodo per Samsung
    irsend.sendSamsung(0X0E0E048B7,68);

    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0x90, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF00FF, 32);
    delay(200);
  }
  else if (command.equals("indietro"))
  {
    Serial.println("\n Passo al canale precedente");

    // Metodo per Samsung
    irsend.sendSamsung(0XE0E008F7,68);

    // Metodo per Sony
    for (int i = 0; i < 3; i++)
    {
      irsend.sendSony(0x890, 12);
      delay(40);
    }

    // Metodo per LG
    irsend.sendNEC(0x20DF807F, 32);
    delay(200);
  }
  else {
    Serial.println("\n Comando inesistente");
  }

  Serial.println("end loop");
} 
