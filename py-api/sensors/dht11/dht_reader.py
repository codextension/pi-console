import RPi.GPIO as GPIO
from .dht11 import DHT11, DHT11Result
import datetime
import asyncio

class DHT11Reader:

	def __init__(self, pin):
		self.pin=pin
		GPIO.setwarnings(True)
		GPIO.setmode(GPIO.BCM)

	async def poll_data(self, delay_time=60):
		instance = DHT11(self.pin)
		degree_sign= u'\N{DEGREE SIGN}'
		try:
			while True:
				result = instance.read()
				if result.is_valid():
					print("Last valid input: " + str(datetime.datetime.now()))
					print("Temperature: %-3.1f %sC" % (result.temperature,degree_sign))
					print("Humidity: %-3.1f %%" % result.humidity)
				
				await asyncio.sleep(delay_time)
		except KeyboardInterrupt:
			print("Cleanup GPIO connections ...")
			GPIO.cleanup()