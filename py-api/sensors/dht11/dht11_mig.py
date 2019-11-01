import time
import RPi


class DHT11Result:
    'DHT11 sensor result returned by DHT11.read() method'

    ERR_NO_ERROR = 0
    ERR_MISSING_DATA = 1
    ERR_CRC = 2

    error_code = ERR_NO_ERROR
    temperature = -1
    humidity = -1

    def __init__(self, error_code, temperature, humidity):
        self.error_code = error_code
        self.temperature = temperature
        self.humidity = humidity

    def is_valid(self):
        return self.error_code == DHT11Result.ERR_NO_ERROR


class DHT11:
    'DHT11 sensor reader class for Raspberry'

    __pin = 0

    def __init__(self, pin):
        self.__pin = pin

    def read(self):
        RPi.GPIO.setup(self.__pin, RPi.GPIO.OUT)
        self.__send_and_sleep(RPi.GPIO.HIGH)

        # pull down to low
        self.__send_and_sleep(RPi.GPIO.LOW, 0.018)

        # change to input using pull up
        RPi.GPIO.setup(self.__pin, RPi.GPIO.IN, RPi.GPIO.PUD_UP)

        # collect data into an array
        (data, pull_up_lengths) = self.__collect_input()

        if pull_up_lengths != 40:
            return DHT11Result(DHT11Result.ERR_MISSING_DATA, 0, 0)

        checksum = self.__calculate_checksum(data)
        if data[4] != checksum:
            return DHT11Result(DHT11Result.ERR_CRC, 0, 0)

        humidity = ((data[0] << 8) + float(data[1])) / 10
        if(humidity>100):
            humidity=data[0]

        temperature = (((data[2] & 127) << 8) + float(data[3])) / 10
        if(temperature>125):
            temperature=data[2]
        if((data[2] & 128) !=0):
            temperature = -temperature

        return DHT11Result(DHT11Result.ERR_NO_ERROR, temperature, humidity)

    def __send_and_sleep(self, output, sleep=0):
        RPi.GPIO.output(self.__pin, output)
        time.sleep(sleep)

    def __collect_input(self):
        MAXTIMINGS = 85
        max_counter = 255
        data = [0,0,0,0,0]
        i=0
        j=0
        last_state = RPi.GPIO.HIGH

        while (i<MAXTIMINGS):
            counter = 0
            current = RPi.GPIO.input(self.__pin)
            while(current==last_state):
                counter+=1
                #time.sleep(0.0001)
                if(counter==max_counter):
                    break
            last_state = current
            if(counter==max_counter):
                break

            if(i>=4 and i%2==0):
                data[j/8] = data[j/8] << 1
                if(counter>16):
                    data[j/8] = data[j/8] | 1
                j+=1
            i+=1

        return (data, j)

    def __calculate_checksum(self, the_bytes):
        return the_bytes[0] + the_bytes[1] + the_bytes[2] + the_bytes[3] & 255