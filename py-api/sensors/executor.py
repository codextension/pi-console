from .dht11 import DHT11
from .mcp3008 import MCP3008
from db.db_connector import DBConnector
import asyncio
import RPi.GPIO

class Sensors:

    def __init__(self):
        RPi.GPIO.setwarnings(False)
        RPi.GPIO.setmode(RPi.GPIO.BCM)
        
        self.__db = DBConnector('/home/ubuntu/db/py_api.db')
        self.dht_instance = DHT11(18,self.__db)
        self.mcp3008_instance = MCP3008(16,self.__db)

    async def __collect_data(self):
        task = asyncio.gather(self.dht_instance.read_temp(5), self.mcp3008_instance.read_dust(), self.mcp3008_instance.read_noise())
        print('running further')
        try:
            await task
        except asyncio.CancelledError:
            print("main(): cancel_me is cancelled now")

    def start(self):
        asyncio.run(self.__collect_data())