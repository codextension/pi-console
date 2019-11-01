import RPi.GPIO as GPIO
import time
from .dht11 import DHT11, DHT11Result
import datetime

class DHT11Reader:

	def __init__(self):
		GPIO.setwarnings(True)
		GPIO.setmode(GPIO.BCM)

	def poll_data(self):
		instance = DHT11(pin=18)
		degree_sign= u'\N{DEGREE SIGN}'
		try:
			while True:
				result = instance.read()
				if result.is_valid():
					print("Last valid input: " + str(datetime.datetime.now()))
					print("Temperature: %-3.1f %sC" % (result.temperature,degree_sign))
					print("Humidity: %-3.1f %%" % result.humidity)
				
				time.sleep(60)
		except KeyboardInterrupt:
			print("Cleanup GPIO connections ...")
			GPIO.cleanup()