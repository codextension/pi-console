import RPi.GPIO as GPIO
import dht11
import time
import datetime

# initialize GPIO
GPIO.setwarnings(True)
GPIO.setmode(GPIO.BCM)

instance = dht11.DHT11(pin=18)
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