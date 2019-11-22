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
        self.mcp3008_instance = MCP3008(16)

    def start(self):
        dht = self.dht_instance.read_temp()
        try:
            while(True):
                print(next(dht), end='\r')
                time.sleep(1.1)
        except StopIteration:
            pass