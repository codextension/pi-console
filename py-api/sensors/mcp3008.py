import time
import RPi.GPIO
import datetime
import asyncio
import spidev
from db.db_connector import DBConnector

class MCP3008:
    
    def __init__(self, pin, db, delay_time=1):
        self.__pin = pin
        self.__db = db
        self.__delay_time=delay_time
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

    def __read_dust(self):
        # Initialising the PIN
        self.__send_and_sleep(RPi.GPIO.LOW, 0.28)

        # read from MCP3008
        dust_value = self.__analog_input(0)
        dust_voltage = (dust_value * 5) / 1024.0
        dust_density = (dust_voltage * 0.17 - 0.1) * 1000
        #print(f'Dust value: {dust_value}, Density: {dust_density}', end='\r')
        time.sleep(0.004)
        # Done, reset the channel
        self.__send_and_sleep(RPi.GPIO.HIGH)
        return (dust_value, dust_voltage, dust_density)
    
    def __read_noise(self):
        noise_value = self.__analog_input(6)
        noise_intensity = (noise_value * 5) / 1024.0
        # print(f'Noise value: {noise_value}, Intensity: {noise_intensity}', end='\r')
        return (noise_value, noise_intensity)

    async def read_dust(self):
        previous_value = None
        try:
            while True:
                (dust_value, dust_voltage, dust_density) = self.__read_dust()
                if(dust_value is not None and dust_density > 0):
                    if (previous_value is None or (previous_value != dust_value and abs(previous_value-dust_value)>5)):
                        self.__db.new_mcp3008(dust_value, dust_voltage, dust_density)
                        # print(f'Dust value: {dust_value}, Density: {dust_density}', end='\r')
                        previous_value = dust_value

                await asyncio.sleep(self.__delay_time)
        except KeyboardInterrupt:
            print("Cleanup GPIO connections ...")
            RPi.GPIO.cleanup()         

    async def read_noise(self):
        try:
            while True:
                noise_res = self.__read_noise()
                await asyncio.sleep(0.0001)
        except KeyboardInterrupt:
            print("Cleanup GPIO connections ...")
            RPi.GPIO.cleanup()   


'''
RPi.GPIO.setwarnings(False)
RPi.GPIO.setmode(RPi.GPIO.BCM)
mcp = MCP3008(16)

try:
    while(True):
        mcp.read_dust()
        time.sleep(0.001)
except KeyboardInterrupt:
    print("Cleanup GPIO connections ...")
    RPi.GPIO.cleanup()
'''