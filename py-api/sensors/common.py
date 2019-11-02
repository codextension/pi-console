from .dht11.dht_reader import DHT11Reader
import asyncio

class Sensors:

    def __init__(self):
        self.dht_instance = DHT11Reader(18)
        pass

    async def __collect_data(self):
        task = asyncio.create_task(self.dht_instance.poll_data(5))
        print('running further')
        try:
            await task
        except asyncio.CancelledError:
            print("main(): cancel_me is cancelled now")

    def start(self):
        asyncio.run(self.__collect_data())