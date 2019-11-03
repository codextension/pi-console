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
        self.__send_and_sleep(RPi.GPIO.HIGH, 0.05)

    def __send_and_sleep(self, output, sleep):
        RPi.GPIO.output(self.__pin, output)
        time.sleep(sleep)

    def __analog_input(self, channel):
        adc = self.__spi.xfer2([1,(8+channel)<<4,0])
        data = ((adc[1]&3) << 8) + adc[2]
        return data

    def read(self):
        # Initialising the PIN
        self.__send_and_sleep(RPi.GPIO.LOW, 0.3)
        # read from MCP3008
        dust_value = self.__analog_input(0)
        dust_voltage = (dust_value * 5) / 1024.0
        dust_density = (dust_voltage * 0.17 - 0.1) * 1000

        noise_value = self.__analog_input(6)
        noise_intensity = (noise_value * 5) / 1024.0
        # do math
        print(f'Dust value: {dust_density}')
        print(f'Noise value: {noise_intensity}')
        # Done, reset the channel
        time.sleep(0.004)
        RPi.GPIO.output(self.__pin, RPi.GPIO.HIGH)


mcp = MCP3008(16)

try:
    while(True):
        mcp.read()
        time.sleep(2)
except KeyboardInterrupt:
    print("Cleanup GPIO connections ...")
    RPi.GPIO.cleanup() 