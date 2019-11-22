from .dht11 import DHT11
from .mcp3008 import MCP3008
import RPi.GPIO
import time

class Sensors:

    def __init__(self):
        RPi.GPIO.setwarnings(False)
        RPi.GPIO.setmode(RPi.GPIO.BCM)
        
        #self.__db = DBConnector('/home/ubuntu/db/py_api.db')
        self.dht_instance = DHT11(18, delay_time=1)
        self.mcp3008_instance = MCP3008(16, delay_time=0.001)

    def start_temp(self):
        dht = self.dht_instance.read_temp()
        try:
            while(True):
                print(next(dht), end='\r')
                time.sleep(1.1)
        except StopIteration:
            pass
    
    def start_dust(self):
        dust = self.mcp3008_instance.read_dust()
        try:
            while(True):
                print(next(dust), end='\r')
                time.sleep(0.001)
        except StopIteration:
            pass        

    def start_noise(self):
        noise = self.mcp3008_instance.read_noise()
        try:
            while(True):
                print(next(noise), end='\r')
                time.sleep(self.mcp3008_instance.NOISE_FREQUENCY)
        except StopIteration:
            pass 