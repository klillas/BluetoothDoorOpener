#include <SoftwareSerial.h>

enum DoorState
{
  Open,
  Closed,
  Opening,
  Closing
};

const int bluetoothTxPin = 2;
const int bluetoothRxPin = 3;
const int bluetoothEnPin = 10;
const int relayPin = 5;
const int millisToChangeDoorPosition = 30000;
int state = 0;
bool ATModeActive = false;
DoorState doorState = Closed;
unsigned long millisWhenLastActionStarted = millis();

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
  if (doorState == Opening || doorState == Closing)
  {
    unsigned long currentMillis = millis();
    if ((unsigned long)(currentMillis - millisWhenLastActionStarted) >= millisToChangeDoorPosition)
    {
      if (doorState == Opening)
      {
        doorState = Open;
      }
      else if (doorState == Closing)
      {
        doorState = Closed;
      }
    }
  }
  
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

  if (state == '0')
  {
    CloseGarage();
  }
  else if (state == '1')
  {
    OpenGarage();
  }
  state = -1;
  delay(500);
}

void OpenGarage()
{
  switch (doorState)
  {
    case Open:
    case Opening:
    {
      break; 
    }
    case Closed:
    {
      SendRelayPulse();
      doorState = Opening;
      millisWhenLastActionStarted = millis();
      break;
    }
    case Closing:
    {
      SendRelayPulse();
      SendRelayPulse();
      doorState = Opening;
      // Because we are "in the middle" of closing the door, we can reduce the time needed to open it again
      unsigned long millisClosing = millis() - millisWhenLastActionStarted;
      millisWhenLastActionStarted = millis() - (millisToChangeDoorPosition - millisClosing);
      break;      
    }
  }
}

void CloseGarage()
{
  switch (doorState)
  {
    case Closed:
    case Closing:
    {
      break; 
    }
    case Open:
    {
      SendRelayPulse();
      doorState = Opening;
      millisWhenLastActionStarted = millis();
      break;
    }
    case Opening:
    {
      SendRelayPulse();
      SendRelayPulse();
      doorState = Closing;
      // Because we are "in the middle" of opening the door, we can reduce the time needed to open it again
      unsigned long millisOpening = millis() - millisWhenLastActionStarted;
      millisWhenLastActionStarted = millis() - (millisToChangeDoorPosition - millisOpening);
      break;      
    }
  }
}

void SendRelayPulse()
{
    BTSerial.println("Activating relay");
    Serial.println("Activating relay");
    
    digitalWrite(relayPin, LOW);
    BTSerial.println("LOW");
    Serial.println("LOW");

    delay(1000);

    digitalWrite(relayPin, HIGH);
    BTSerial.println("HIGH");
    Serial.println("HIGH");

    delay(1000);
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
