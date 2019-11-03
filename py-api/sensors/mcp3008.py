import time
import RPi.GPIO
import datetime
import asyncio
import spidev

class MCP3008(object):
    
    def __init__(self, pin):
        RPi.GPIO.setwarnings(True)
        RPi.GPIO.setmode(RPi.GPIO.BCM)

        self.__pin = pin
        self.__spi = spidev.SpiDev()
        self.__spi.open(0,0)
        self.__spi.mode = 0b00
        self.__spi.max_speed_hz = 1000000
        RPi.GPIO.setup(self.__pin, RPi.GPIO.OUT)
        self.__send_and_sleep(RPi.GPIO.HIGH)

    def __send_and_sleep(self, output, sleep=0):
        RPi.GPIO.output(self.__pin, output)
        time.sleep(sleep)

    def __analog_input(self, channel):
        adc = self.__spi.xfer2([1,(8+channel)<<4,0])
        data = ((adc[1]&3) << 8) + adc[2]
        return data

    def read_dust(self):
        # Initialising the PIN
        self.__send_and_sleep(RPi.GPIO.LOW, 0.28)

        # read from MCP3008
        dust_value = self.__analog_input(0)
        dust_voltage = (dust_value * 5) / 1024.0
        dust_density = (dust_voltage * 0.17 - 0.1) * 1000
        print(f'Dust value: {dust_value}, Density: {dust_density}', end='\r')
        time.sleep(0.004)
        # Done, reset the channel
        self.__send_and_sleep(RPi.GPIO.HIGH)
    
    def read_noise(self):
        noise_value = self.__analog_input(6)
        noise_intensity = (noise_value * 5) / 1024.0
        print(f'Noise value: {noise_value}, Intensity: {noise_intensity}', end='\r')


mcp = MCP3008(16)

try:
    while(True):
        mcp.read_noise()
        time.sleep(0.0001)
except KeyboardInterrupt:
    print("Cleanup GPIO connections ...")
    RPi.GPIO.cleanup() 