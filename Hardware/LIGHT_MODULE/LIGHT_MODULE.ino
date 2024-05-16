#include <SPI.h>
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFiClientSecure.h>

/************* WiFi and MQTT Settings *************/
const char* ssid = "Le Yen t6 2.4GHz";
const char* password = "88888888";
const char* mqtt_server = "d65f1c22056c4ca29d2480c231a74215.s2.eu.hivemq.cloud";
const char* mqtt_username = "luvkll2";
const char* mqtt_password = "Bao123123";
const int mqtt_port = 8883;

WiFiClientSecure espClient;
PubSubClient client(espClient);

/************* Common Settings *************/
const int lightPN_001 = 16;  // Đèn cho phòng ngủ 1
const int lightPK_001 = 4;   // Đèn cho phòng khách 1
const int lightPB_001 = 5;   // Đèn cho phòng bếp 1

const int buttonPN_001 = 2;   // Nút điều khiển cho phòng ngủ 1
const int buttonPK_001 = 1;  // Nút điều khiển cho phòng khách 1
const int buttonPB_001 = 0;  // Nút điều khiển cho phòng bếp 1bếp 1
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
      client.subscribe("SMH-001/light_state/PN_001");
      client.subscribe("SMH-001/light_state/PK_001");
      client.subscribe("SMH-001/light_state/PB_001");


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
  if (strcmp(topic, "SMH-001/light_state/PN_001") == 0) {
    // Xử lý khi nhận được thông điệp cho phòng ngủ 1
    if (incomingMessage.equals("ON")) {
      digitalWrite(lightPN_001, HIGH);
    } else {
      digitalWrite(lightPN_001, LOW);
    }
  }
  if (strcmp(topic, "SMH-001/light_state/PK_001") == 0) {
    // Xử lý khi nhận được thông điệp cho phòng khách 1
    if (incomingMessage.equals("ON")) {
      digitalWrite(lightPK_001, HIGH);
    } else {
      digitalWrite(lightPK_001, LOW);
    }
  }
  if (strcmp(topic, "SMH-001/light_state/PB_001") == 0) {
    // Xử lý khi nhận được thông điệp cho phòng bếp 1
    if (incomingMessage.equals("ON")) {
      digitalWrite(lightPB_001, HIGH);
    } else {
      digitalWrite(lightPB_001, LOW);
    }
  }
}
void publishMessage(const char* topic, String payload, boolean retained) {
  if (client.publish(topic, payload.c_str(), true))
    Serial.println("Message publised [" + String(topic) + "]: " + payload);
}



void setup() {
  Serial.begin(9600);
  pinMode(buttonPN_001, INPUT_PULLUP);
  pinMode(buttonPK_001, INPUT_PULLUP);
  pinMode(buttonPB_001, INPUT_PULLUP);

  pinMode(lightPN_001, OUTPUT);
  pinMode(lightPK_001, OUTPUT);
  pinMode(lightPB_001, OUTPUT);
  

  while (!Serial) delay(1);
  setup_wifi();

#ifdef ESP8266
  espClient.setInsecure();
#else
  espClient.setCACert(root_ca);
#endif

  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  SPI.begin();
}
void publishLightState(const char* lightId, bool isOn) {
  DynamicJsonDocument lightState(128);
  lightState["home_id"] = "SMH-001";
  lightState["light_id"] = lightId;
  lightState["status"] = isOn ? "ON" : "OFF";
  char lightStateMessage[128];
  serializeJson(lightState, lightStateMessage);
  publishMessage("light_data", lightStateMessage, true);
}


void loop() {
  if (!client.connected()) reconnect();
  client.loop();
  // // Đọc trạng thái  nút
  int buttonState1 = digitalRead(buttonPN_001);
  int buttonState2 = digitalRead(buttonPK_001);
  int buttonState3 = digitalRead(buttonPB_001);





  // Xử lý trạng thái của nút 1
  static int previousButtonState1 = 1;
  if (buttonState1 == 0 && previousButtonState1 == 1) {
    digitalWrite(lightPN_001, !digitalRead(lightPN_001));  // Đảo ngược trạng thái của đèn 1
    publishLightState("PN_001", digitalRead(lightPN_001));
  }
  previousButtonState1 = buttonState1;

  // Xử lý trạng thái của nút 2
  static int previousButtonState2 = 1;
  if (buttonState2 == 0 && previousButtonState2 == 1) {
    digitalWrite(lightPK_001, !digitalRead(lightPK_001));  // Đảo ngược trạng thái của đèn 2
    publishLightState("PK_001", digitalRead(lightPK_001));

  }
  previousButtonState2 = buttonState2;

  // Xử lý trạng thái của nút 3
  static int previousButtonState3 = 1;
  if (buttonState3 == 0 && previousButtonState3 == 1) {
    digitalWrite(lightPB_001, !digitalRead(lightPB_001));  // Đảo ngược trạng thái của đèn 3
    publishLightState("PB_001", digitalRead(lightPB_001));

  }
  previousButtonState3 = buttonState3;
}
