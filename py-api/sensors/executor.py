from .dht11 import DHT11
import asyncio
import RPi.GPIO

class Sensors:

    def __init__(self):
        self.dht_instance = DHT11(18)
        RPi.GPIO.setwarnings(True)
        RPi.GPIO.setmode(RPi.GPIO.BCM)

    async def __collect_data(self):
        task = asyncio.gather(self.dht_instance.poll(5))
        print('running further')
        try:
            await task
        except asyncio.CancelledError:
            print("main(): cancel_me is cancelled now")

    def start(self):
        asyncio.run(self.__collect_data())