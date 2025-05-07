// USB Serial Mode & Speed
#define REMOTEXY_MODE__HARDSERIAL
#define REMOTEXY_SERIAL Serial
#define REMOTEXY_SERIAL_SPEED 9600

//Libraries for LCD, Temperature/Humidity Sensor & RemoteXY Remote Access are here: 
#include "DHT.h"
#include "LiquidCrystal.h"
#include "RemoteXY.h"

// Pin for the sensor is defined 
#define DHTPIN 13
// The sensor type is specified
#define DHTTYPE DHT11   

// The instance of the sensor (object) is created
DHT dht(DHTPIN, DHTTYPE);


// RemoteXY GUI configuration 
#pragma pack(push, 1)  
uint8_t RemoteXY_CONF[] =  // 91 bytes
  { 255,0,0,8,0,84,0,19,0,0,0,72,69,77,83,32,82,101,109,111,
  116,101,32,65,99,99,101,115,115,0,24,1,106,200,1,1,4,0,67,8,
  66,40,10,205,16,26,3,67,59,66,40,10,205,16,26,3,129,12,57,28,
  6,64,97,72,85,77,73,68,73,84,89,0,129,58,57,42,6,64,97,84,
  69,77,80,69,82,65,84,85,82,69,0 };
  
// this structure defines all the variables and events of your control interface 
struct {

    // output variables
  float Humidity;
  float Temperature;

    // other variable
  uint8_t connect_flag;  // =1 if wire connected, else =0

} RemoteXY;   
#pragma pack(pop)


// The pins for the LED lights and the buzzer
const int ledGreenPin = 8;
const int ledRedPin = 9;
const int buzzer = 6;


// Initialise HEMS settings on components & remote access 
void setup() 
{
  RemoteXY_Init(); 

  Serial.begin(9600);
  Serial.println(F("\n Initialing to set up readings....."));

  pinMode(ledGreenPin, OUTPUT);
  pinMode(ledRedPin, OUTPUT);
  pinMode(buzzer, OUTPUT);
  dht.begin();

} 



void loop() 
{
  // Handler on reading the sensor data 
  RemoteXY_Handler();

  RemoteXY.Humidity = dht.readHumidity();
  RemoteXY.Temperature = dht.readTemperature();


  // Variables to collect and hold temperature and humidity data 
  delay(2000);
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  // If no readings are taken, this is the error message 
  if (isnan(h) || isnan(t)) 
  {
    Serial.println(F("Failed to gather readings from DHT11 Sensor!"));
    return;
  }
  
  // Selection statements to trigger LEDS and buzzer when humidity/temperature falls within these conditions 
  else if(h > 60 || h < 40)
  { 
    digitalWrite(ledGreenPin, HIGH);
    delay(1000);
    digitalWrite(ledGreenPin, LOW);
    digitalWrite(buzzer, HIGH);
    tone(buzzer, 1000, 10);
  }

  else if(t > 30 || t < 18 )
  { 
    digitalWrite(ledRedPin, HIGH);
    delay(1000);
    digitalWrite(ledRedPin, LOW);
    digitalWrite(buzzer, HIGH);
    tone(buzzer, 1000, 5);
  }

  else if((t > 30 || t < 18) && (h > 60 || h < 40))
  { 
     digitalWrite(ledGreenPin, HIGH);
     delay(1000);
     digitalWrite(ledGreenPin, LOW);
     digitalWrite(ledRedPin, HIGH);
     delay(1000);
     digitalWrite(ledRedPin, LOW);
     tone(buzzer, 2000, 5);
  }

  // Prints the readings on the Serial Monitor 
  Serial.print(F("Humidity: "));
  Serial.print(h);
  Serial.print(F("%  Temperature: "));
  Serial.print(t);
  Serial.print(F("°C "));
  Serial.print("\n");

  // Instantiate the lcd obejct + displays the readings on the LCD
  LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
  lcd.begin(16, 2);
  lcd.clear();
  lcd.println("Humidity:"); lcd.print(h);
  lcd.setCursor(0, 1);
  lcd.println("Temp:"); lcd.print(t);
 
}