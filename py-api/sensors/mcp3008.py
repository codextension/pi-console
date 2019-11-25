import time
import RPi.GPIO
import datetime
import spidev


class MCP3008:

    def __init__(self, pin):
        self.__pin = pin
        self.__spi = spidev.SpiDev()
        self.__spi.open(0, 0)
        self.__spi.mode = 0b00
        self.__spi.max_speed_hz = 1000000
        RPi.GPIO.setup(self.__pin, RPi.GPIO.OUT)
        self.__send_and_sleep(RPi.GPIO.HIGH)

    def __send_and_sleep(self, output, sleep=0):
        RPi.GPIO.output(self.__pin, output)
        time.sleep(sleep)

    def __analog_input(self, channel):
        adc = self.__spi.xfer2([1, (8+channel) << 4, 0])
        data = ((adc[1] & 3) << 8) + adc[2]
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

    def read_dust(self):
        try:
            (dust_value, dust_voltage, dust_density) = self.__read_dust()
            return {'value':dust_value, 'voltage':dust_voltage, 'density':dust_density}
        except KeyboardInterrupt:
            RPi.GPIO.cleanup()
            return None

    def read_noise(self):
        try:
            (noise_value, noise_intensity) = self.__read_noise()
            return {'value': noise_value, 'intensity': noise_intensity}
        except KeyboardInterrupt:
            RPi.GPIO.cleanup()
            return None


'''
    def get_dust(self):
        try:
            while True:
                (dust_value, dust_voltage, dust_density) = self.__read_dust()
                yield (dust_value, dust_voltage, dust_density)
                time.sleep(self.__delay_time)
        except KeyboardInterrupt:
            print("Cleanup GPIO connections ...")
            RPi.GPIO.cleanup()          
            yield None

RPi.GPIO.setmode(RPi.GPIO.BCM)
mcp = MCP3008(16, delay_time=0.001)

generator = mcp.get_dust()
try:
    while(True):
        print(next(generator), end='\r')
        time.sleep(0.002)
except StopIteration:
    print('Nothing to fetch anymore')
    pass
'''
