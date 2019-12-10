from sensors.dht11 import DHT11
from sensors.mcp3008 import MCP3008
import RPi.GPIO
import time
import threading
from kafka import KafkaProducer
from kafka.errors import KafkaError
import json

class Sensors:

    def __init__(self):
        RPi.GPIO.setwarnings(False)
        RPi.GPIO.setmode(RPi.GPIO.BCM)
        self.dht_instance = DHT11(18)
        self.mcp3008_instance = MCP3008(16)
        self.producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

    def start_temp(self):
        while True:
            dht = self.dht_instance.read_temp()
            self.producer.send('temperature', key=b"temperature", value=json.dumps(dht["temperature"]).encode('utf-8'))
            # print(dht, end='\r')
            time.sleep(2)

    def start_dust(self):
        while True:
            dust = self.mcp3008_instance.read_dust()
            self.producer.send('dust', key=b"density", value=json.dumps(dust["density"]).encode('utf-8'))
            # print(f'Dust: {dust}', end='\r')
            time.sleep(2)

    def start_noise(self):
        while True:
            noise = self.mcp3008_instance.read_noise()
            self.producer.send('noise', key=b"noise", value=json.dumps(noise["value"]).encode('utf-8'))
            # print(f'Noise:{noise}', end='\r')
            time.sleep(0.0001)

    def start(self):
        t_1 = threading.Thread(target=self.start_temp)
        t_2 = threading.Thread(target=self.start_dust)
        t_3 = threading.Thread(target=self.start_noise)
        t_1.start()
        t_2.start()
        t_3.start()

        t_1.join()
        t_2.join()
        t_3.join()
        print('Threads started')


s = Sensors()

s.start()
