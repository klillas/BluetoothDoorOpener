const int relayPin = 3;
int state = 0;

// the setup function runs once when you press reset or power the board
void setup() {
  pinMode(relayPin, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(relayPin, HIGH);   // turn the LED on (HIGH is the voltage level)
  Serial.begin(9600);
}

// the loop function runs over and over again forever
void loop() {  
  if (Serial.available() > 0)
  {
    state = Serial.read();
  }

  if (state == '0')
  {
    digitalWrite(relayPin, LOW);
    Serial.println("LOW");
  }
  else if (state == '1')
  {
    digitalWrite(relayPin, HIGH);
    Serial.println("HIGH");
  }
  state = -1;
  delay(100);
  //digitalWrite(relayPin, HIGH);   // turn the LED on (HIGH is the voltage level)
  //digitalWrite(LED_BUILTIN, HIGH);
  //delay(1000);                       // wait for a second
  //digitalWrite(relayPin, LOW);    // turn the LED off by making the voltage LOW
  //digitalWrite(LED_BUILTIN, LOW);
  //delay(1000);                       // wait for a second
}
