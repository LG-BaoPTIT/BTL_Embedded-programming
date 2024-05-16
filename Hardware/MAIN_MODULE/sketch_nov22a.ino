#include <SPI.h>
#include <MFRC522.h>
#include <Servo.h>
#include <ESP8266WiFi.h>
#include <DHTesp.h>
#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
/************* WiFi and MQTT Settings *************/
const char* ssid = "Le Yen t6 2.4GHz";
const char* password = "88888888";
const char* mqtt_server = "d65f1c22056c4ca29d2480c231a74215.s2.eu.hivemq.cloud";
const char* mqtt_username = "bao2002pytn";
const char* mqtt_password = "Bao123123";
const int mqtt_port = 8883;

WiFiClientSecure espClient;
PubSubClient client(espClient);

/************* DHT11 Sensor Settings *************/
#define DHTpin 1  // Set DHT pin as GPIO2
DHTesp dht;

/************* Servo Settings *************/
#define SERVO_PIN 2
Servo myServo;
bool isServoTurned = false;
bool isDoorClosed = false;

/************************Hardware Related Macros************************************/
const int MQ_PIN = A0;             // define which analog input channel you are going to use
int RL_VALUE = 5;                  // define the load resistance on the board, in kilo ohms
float RO_CLEAN_AIR_FACTOR = 9.83;  // RO_CLEAR_AIR_FACTOR = (Sensor resistance in clean air) / RO

/***********************Software Related Macros************************************/
int CALIBARAION_SAMPLE_TIMES = 10;      // define how many samples you are going to take in the calibration phase
int CALIBRATION_SAMPLE_INTERVAL = 500;  // define the time interval (in milliseconds) between each sample in the calibration phase
int READ_SAMPLE_INTERVAL = 10;          // define how many samples you are going to take in normal operation
int READ_SAMPLE_TIMES = 5;              // define the time interval (in milliseconds) between each sample in normal operation

/**********************Application Related Macros**********************************/
#define GAS_LPG 0

/*****************************Globals***********************************************/
float LPGCurve[3] = { 2.3, 0.21, -0.47 };
float Ro = 10;  // Ro is initialized to 10 kilo ohms

/************* Gas Sensor Settings *************/
#define BUZZER_PIN D3
#define GAS_THRESHOLD 500

/************* RFID Settings *************/
#define SS_PIN D8
#define RST_PIN D0

MFRC522 rfid(SS_PIN, RST_PIN);
MFRC522::MIFARE_Key key;
byte nuidPICC[4];
// Mã thẻ cố định muốn kiểm tra
//byte expectedUID[] = { 0xC3, 0xC1, 0xD0, 0x0D };
/************* RFID Settings *************/
#define MAX_VALID_CARDS 5

// Array to store valid UIDs
byte validUIDs[MAX_VALID_CARDS][4] = {
  { 0xC3, 0xC1, 0xD0, 0x0D },  // Add more UIDs as needed
  // {Next UID},
  // ...
};
// Function to check if the scanned card's UID is valid
bool isValidCard(byte* scannedUID) {
  for (int i = 0; i < MAX_VALID_CARDS; i++) {
    if (memcmp(scannedUID, validUIDs[i], sizeof(validUIDs[i])) == 0) {
      return true;  // Valid card found
    }
  }
  return false;  // Scanned card is not in the list of valid cards
}
/************* Common Settings *************/
const int ledPN1 = 5;
unsigned long lastGasCheckTime = 0;
const int gasCheckInterval = 1000;

LiquidCrystal_I2C lcd(0x27, 16, 2);  //Thiết lập địa chỉ và loại LCD
String str2 = "Open with card";

/****** root certificate *********/

static const char* root_ca PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw
TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh
cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4
WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu
ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY
MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc
h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+
0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U
A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW
T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH
B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC
B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv
KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn
OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn
jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw
qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI
rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV
HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq
hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL
ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ
3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK
NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5
ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur
TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC
jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc
oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq
4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA
mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d
emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=
-----END CERTIFICATE-----
)EOF";

/************* Helper Functions *************/
void setup_wifi() {
  delay(10);
  Serial.print("\nConnecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  randomSeed(micros());
  Serial.println("\nWiFi connected\nIP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);

    if (client.connect(clientId.c_str(), mqtt_username, mqtt_password)) {
      Serial.println("connected");

      client.subscribe("SMH-001/door_state/MAIN_DOOR");


    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  String incomingMessage = "";
  for (int i = 0; i < length; i++) {
    incomingMessage += (char)payload[i];
  }

  Serial.println("Thông điệp đã đến [" + String(topic) + "] " + incomingMessage);

  // So sánh chủ đề nhận được với các định dạng dự kiến
  if (strcmp(topic, "NodeMCU/PN1/light_state") == 0) {
    // Xử lý khi nhận được thông điệp từ phòng ngủ PN1
    if (incomingMessage.equals("1")) {
      digitalWrite(ledPN1, HIGH);  // Thay thế 'ledPN1' bằng tên chính xác của đèn trong phòng ngủ PN1
    } else {
      digitalWrite(ledPN1, LOW);
    }
  }
  // Xử lý cho chủ đề điều khiển cửa
  if (strcmp(topic, "SMH-001/door_state/MAIN_DOOR") == 0) {

    if (incomingMessage.equals("Opened")) {
      if (isDoorClosed) {
        activateBuzzer();
        lcd.setCursor(0, 1);
        lcd.print("                ");
        lcd.setCursor(4, 1);
        lcd.print("Is open!");
        delay(1000);
        isServoTurned = !isServoTurned;
        myServo.write(180);  // Mở cửa khi có yêu cầu và cửa đang đóng

        isDoorClosed = false;  // Cập nhật trạng thái cửa là đã mở
        lcd.setCursor(0, 1);
        lcd.print("                ");
        //scrollMessage(1, str2, 250, 16);
        lcd.setCursor(0, 1);
        lcd.print("Close with card");
      } else {
        activateBuzzer();
        Serial.println("Cửa đang mở hoặc đã được mở bởi yêu cầu trước đó");
      }
    } else if (incomingMessage.equals("Closed")) {
      if (!isDoorClosed) {

        isServoTurned = !isServoTurned;
        myServo.write(0);  // Đóng cửa khi có yêu cầu và cửa đang mở
        activateBuzzer();
        isDoorClosed = true;  // Cập nhật trạng thái cửa là đã đóng
        lcd.setCursor(0, 1);
        lcd.print("                ");
        lcd.setCursor(3, 1);
        lcd.print("Closed!");
        delay(1000);

        lcd.setCursor(0, 1);
        lcd.print("                ");
        //scrollMessage(1, str2, 250, 16);
        lcd.setCursor(1, 1);
        lcd.print("Open with card");
      } else {
        activateBuzzer();
        Serial.println("Cửa đang đóng hoặc đã được đóng bởi yêu cầu trước đó");
      }
    } else {
      Serial.println("Lệnh không hợp lệ cho cửa");
    }
  }
}

void publishMessage(const char* topic, String payload, boolean retained) {
  if (client.publish(topic, payload.c_str(), true))
    Serial.println("Message publised [" + String(topic) + "]: " + payload);
}

void activateFireAlarm() {
  // Giai điệu cảnh báo cháy
  int fireMelody[] = { 262, 294, 330, 349, 392, 440, 523, 587 };

  // Thời lượng tương ứng với mỗi nốt nhạc (milliseconds)
  int noteDurations[] = { 200, 200, 200, 200, 200, 200, 200, 200 };

  // Chạy qua mỗi nốt nhạc trong giai điệu
  for (int i = 0; i < 8; i++) {
    int noteDuration = noteDurations[i];
    tone(BUZZER_PIN, fireMelody[i], noteDuration);

    // Đợi một khoảng thời gian trước khi chuyển sang nốt nhạc tiếp theo
    delay(noteDuration + 50);

    // Tắt còi
    noTone(BUZZER_PIN);
  }
}


void activateBuzzer() {
  tone(BUZZER_PIN, 2000);
  delay(500);
  noTone(BUZZER_PIN);
}
void activateBuzzer2() {
  tone(BUZZER_PIN, 262);
  delay(500);
  noTone(BUZZER_PIN);
}
void scrollMessage(int row, String message, int delayTime, int totalColumns) {
  for (int i = 0; i < totalColumns; i++) {
    message = " " + message;
  }
  message = message + " ";
  for (int position = 0; position < message.length(); position++) {
    lcd.setCursor(0, row);
    lcd.print(message.substring(position, position - totalColumns));
    delay(delayTime);
  }
}

void setup() {
  pinMode(BUZZER_PIN, OUTPUT);
  digitalWrite(BUZZER_PIN, LOW);

  dht.setup(DHTpin, DHTesp::DHT11);
  pinMode(ledPN1, OUTPUT);
  Serial.begin(9600);

  Wire.begin(D2, D1);             //Thiết lập chân kết nối I2C (SDA,SCL);
  lcd.init();                     //Khởi tạo LCD
  lcd.clear();                    //Xóa màn hình
  lcd.backlight();                //Bật đèn nền
  lcd.setCursor(0, 0);            //Đặt vị trí muốn hiển thị ô thứ 1 trên dòng 1
  lcd.write(0);                   //Ghi byte 0 ra vị trí ô thứ 1 trên dòng 1
  lcd.setCursor(0, 0);            //Đặt vị trí ở ô thứ 3 trên dòng 1
  lcd.print("---Smart home---");  //Ghi đoạn text "Welcom to"
  lcd.setCursor(1, 1);            //Đặt vị trí ở ô thứ 1 trên dòng 2
  //scrollMessage(1, str2, 250, 16);
  lcd.print(str2);
  while (!Serial) delay(1);
  setup_wifi();
  delay(1000);
  Ro = MQCalibration(MQ_PIN);

#ifdef ESP8266
  espClient.setInsecure();
#else
  espClient.setCACert(root_ca);
#endif

  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  SPI.begin();
  rfid.PCD_Init();
  Serial.println();
  Serial.print(F("Reader: "));
  rfid.PCD_DumpVersionToSerial();

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }
  Serial.println();
  Serial.println(F("This code scans the MIFARE Classic NUID."));
  Serial.print(F("Using the following key: "));
  printHex(key.keyByte, MFRC522::MF_KEY_SIZE);

  myServo.attach(SERVO_PIN);
}
void printHex(byte* buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
  Serial.println();
}
void printDec(byte* buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
  Serial.println();
}

unsigned long lastDHTReadTime = 0;
const int dhtReadInterval = 5000;  // Đọc dữ liệu DHT mỗi 5 giây
bool gasAlertSent = false;

void publishDoorEvent(bool isOpen, String cardID) {
  DynamicJsonDocument doorEvent(128);
  doorEvent["home_id"] = "SMH-001";
  doorEvent["door_id"] = "MAIN_DOOR";
  doorEvent["status"] = isOpen ? "Opened" : "Closed";
  doorEvent["card_id"] = cardID;
  char doorEventMessage[128];
  serializeJson(doorEvent, doorEventMessage);
  publishMessage("door_data", doorEventMessage, true);
}

void loop() {
  if (!client.connected()) reconnect();
  client.loop();

  unsigned long loopMillis = millis();  // Thay đổi tên biến thành loopMillis

  if (loopMillis - lastDHTReadTime >= dhtReadInterval) {
    lastDHTReadTime = loopMillis;

    float humidity = dht.getHumidity();
    float temperature = dht.getTemperature();

    DynamicJsonDocument doc(1024);
    doc["home_id"] = "SMH-001";
    doc["dhtid"] = "DHT11_S";
    doc["humidity"] = humidity;
    doc["temperature"] = temperature;

    char mqtt_message[128];
    serializeJson(doc, mqtt_message);
    publishMessage("DHT11_data", mqtt_message, true);
  }

  unsigned long currentMillis = millis();

  if (currentMillis - lastGasCheckTime >= gasCheckInterval) {
    lastGasCheckTime = currentMillis;

    int gasValue = analogRead(A0);
    long iPPM_LPG = 0;

    iPPM_LPG = MQGetGasPercentage(MQRead(MQ_PIN) / Ro, GAS_LPG);
    Serial.print("LPG Concentration: ");
    Serial.print(iPPM_LPG);
    Serial.println(" ppm");
    if (iPPM_LPG > 0) {
      activateFireAlarm();

      // Gửi thông báo lên cloud nếu chưa gửi trước đó
      if (!gasAlertSent) {
        DynamicJsonDocument gasAlert(1024);
        gasAlert["home_id"] = "SMH-001";
        gasAlert["gas_sensor_id"] = "GAS_SENSOR_S";
        gasAlert["value"] = iPPM_LPG;
        gasAlert["gasStatus"] = "1";
        char gasAlertMessage[128];
        serializeJson(gasAlert, gasAlertMessage);

        publishMessage("gas_data", gasAlertMessage, true);

        // Đánh dấu là đã gửi thông báo để tránh gửi liên tục
        gasAlertSent = true;
      } else {
        // Reset trạng thái đã gửi khi giá trị gas quay về bình thường
        DynamicJsonDocument gasData(128);
        gasData["home_id"] = "SMH-001";
        gasData["gas_sensor_id"] = "GAS_SENSOR_S";
        gasData["value"] = iPPM_LPG;
        gasData["gasStatus"] = "0";
        char gasMessage[128];
        serializeJson(gasData, gasMessage);

        publishMessage("gas_data", gasMessage, true);
      }

    } else {
      // Reset trạng thái đã gửi khi giá trị gas quay về bình thường
      DynamicJsonDocument gasData(128);
      gasData["home_id"] = "SMH-001";
      gasData["gas_sensor_id"] = "GAS_SENSOR_S";
      gasData["value"] = iPPM_LPG;
      gasData["gasStatus"] = "0";
      char gasMessage[128];
      serializeJson(gasData, gasMessage);

      publishMessage("gas_data", gasMessage, true);
      gasAlertSent = false;
    }
  }

  if (!rfid.PICC_IsNewCardPresent())
    return;

  if (!rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI && piccType != MFRC522::PICC_TYPE_MIFARE_1K && piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  // Kiểm tra xem UID của thẻ có khớp với mã thẻ cố định không
  if (isValidCard(rfid.uid.uidByte)) {
    Serial.println(F("Correct card detected."));
    activateBuzzer();
    lcd.setCursor(0, 1);
    lcd.print("                ");
    lcd.setCursor(2, 1);
    lcd.print("Valid card!");
    delay(1000);

    String cardID = "";
    for (byte i = 0; i < 4; i++) {
      cardID += String(rfid.uid.uidByte[i], HEX);
    }
    // Chuyển trạng thái của servo
    isServoTurned = !isServoTurned;

    // Điều khiển servo dựa trên trạng thái của isServoTurned
    if (isServoTurned) {
      myServo.write(180);
      isDoorClosed = false;
      publishDoorEvent(true, cardID);  // Góc 180 độ
    } else {
      myServo.write(0);
      isDoorClosed = true;
      publishDoorEvent(false, cardID);  // Góc 0 độ
      lcd.setCursor(0, 1);
      lcd.print("                ");
      lcd.setCursor(3, 1);
      lcd.print("Closed!");
      delay(1000);
    }
  } else {
    activateBuzzer2();
    Serial.println(F("Incorrect card detected."));
    lcd.setCursor(0, 1);
    lcd.print("                ");
    lcd.setCursor(1, 1);
    lcd.print("Invalid card!");
    delay(1000);
  }
  if (isDoorClosed) {
    str2 = "Open with card ";
    lcd.setCursor(0, 1);
    lcd.print("                ");
    //scrollMessage(1, str2, 250, 16);
    lcd.setCursor(1, 1);
    lcd.print(str2);

  } else {
    str2 = "Close with card";
    lcd.setCursor(0, 1);
    lcd.print("                ");
    lcd.setCursor(0, 1);
    lcd.print(str2);
  }


  // Lưu trữ UID cho lần quét tiếp theo
  memcpy(nuidPICC, rfid.uid.uidByte, sizeof(nuidPICC));

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
  delay(500);
}

float MQResistanceCalculation(int raw_adc) {
  return (((float)RL_VALUE * (1023 - raw_adc) / raw_adc));
}

/***************************** MQCalibration ****************************************
Input:   mq_pin - analog channel
Output:  Ro of the sensor
Remarks: This function assumes that the sensor is in clean air. It uses
         MQResistanceCalculation to calculate the sensor resistance in clean air
         and then divides it by RO_CLEAN_AIR_FACTOR. RO_CLEAN_AIR_FACTOR is about
         10, which differs slightly between different sensors.
************************************************************************************/
float MQCalibration(int mq_pin) {
  int i;
  float val = 0;

  for (i = 0; i < CALIBARAION_SAMPLE_TIMES; i++) {
    val += MQResistanceCalculation(analogRead(mq_pin));
    delay(CALIBRATION_SAMPLE_INTERVAL);
  }
  val = val / CALIBARAION_SAMPLE_TIMES;
  val = val / RO_CLEAN_AIR_FACTOR;

  return val;
}

/*****************************  MQRead *********************************************
Input:   mq_pin - analog channel
Output:  Rs of the sensor
Remarks: This function uses MQResistanceCalculation to calculate the sensor resistance (Rs).
         The Rs changes as the sensor is in the different concentrations of the target
         gas. The sample times and the time interval between samples could be configured
         by changing the definition of the macros.
************************************************************************************/
float MQRead(int mq_pin) {
  int i;
  float rs = 0;

  for (i = 0; i < READ_SAMPLE_TIMES; i++) {
    rs += MQResistanceCalculation(analogRead(mq_pin));
    delay(READ_SAMPLE_INTERVAL);
  }

  rs = rs / READ_SAMPLE_TIMES;

  return rs;
}

/*****************************  MQGetGasPercentage **********************************
Input:   rs_ro_ratio - Rs divided by Ro
         gas_id      - target gas type
Output:  ppm of the target gas
Remarks: This function passes different curves to the MQGetPercentage function which
         calculates the ppm (parts per million) of the target gas.
************************************************************************************/
long MQGetGasPercentage(float rs_ro_ratio, int gas_id) {
  if (gas_id == GAS_LPG) {
    return MQGetPercentage(rs_ro_ratio, LPGCurve);
  }

  return 0;
}

/*****************************  MQGetPercentage **********************************
Input:   rs_ro_ratio - Rs divided by Ro
         pcurve      - pointer to the curve of the target gas
Output:  ppm of the target gas
Remarks: By using the slope and a point of the line. The x(logarithmic value of ppm)
         of the line could be derived if y(rs_ro_ratio) is provided. As it is a
         logarithmic coordinate, power of 10 is used to convert the result to non-logarithmic
         value.
************************************************************************************/
long MQGetPercentage(float rs_ro_ratio, float* pcurve) {
  return (pow(10, ((log(rs_ro_ratio) - pcurve[1]) / pcurve[2]) + pcurve[0]));
}
