#include <SoftwareSerial.h>

const int bluetoothTxPin = 2;
const int bluetoothRxPin = 3;
const int bluetoothEnPin = 10;
const int relayPin = 5;
int state = 0;
bool ATModeActive = false;

SoftwareSerial BTSerial(bluetoothRxPin, bluetoothTxPin); // RX, TX

// the setup function runs once when you press reset or power the board
void setup() {
  pinMode(bluetoothEnPin, OUTPUT);
  digitalWrite(bluetoothEnPin, LOW); 
  pinMode(relayPin, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(relayPin, HIGH);
  Serial.begin(9600);
  BTSerial.begin(9600);
  Serial.println("Starting up");
  Serial.println("Write A to enter AT mode, bluetooth module must be manually set to AT mode for this to work");
}

// the loop function runs over and over again forever
void loop() {
  if (ATModeActive)
  {
    ATMode();
    delay(300);
    return;
  }
  
  while (Serial.available() > 0)
  {
    if (Serial.read() == 'A')
    {
      Serial.println("AT mode active, restart to exit to normal mode");
      digitalWrite(bluetoothEnPin, HIGH); 
      ATModeActive = true;
      BTSerial.begin(38400);
      BTSerial.write("AT\r\n");
    }
  }
  
  if (BTSerial.available() > 0)
  {
    state = BTSerial.read();
  }

  if (state == '0' || state == '1')
  {
    BTSerial.println("Activating relay");
    Serial.println("Activating relay");
    
    digitalWrite(relayPin, LOW);
    BTSerial.println("LOW");
    Serial.println("LOW");

    delay(2000);

    digitalWrite(relayPin, HIGH);
    BTSerial.println("HIGH");
    Serial.println("HIGH");
  }
  state = -1;
  delay(500);
  //digitalWrite(relayPin, HIGH);   // turn the LED on (HIGH is the voltage level)
  //digitalWrite(LED_BUILTIN, HIGH);
  //delay(1000);                       // wait for a second
  //digitalWrite(relayPin, LOW);    // turn the LED off by making the voltage LOW
  //digitalWrite(LED_BUILTIN, LOW);
  //delay(1000);                       // wait for a second
}

void ATMode()
{
    while (BTSerial.available())
    {
      Serial.write(BTSerial.read());
    }
    
    while (Serial.available())
    {
      char c = Serial.read();
      Serial.write(c);
      BTSerial.write(c);

      if (!Serial.available())
      {
        //BTSerial.write("\r\n");
      }
    }
}
