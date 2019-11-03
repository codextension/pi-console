import time
import RPi
import datetime
import asyncio


class MCP3008(object):
    
    def __init__(self, pin):
        self.__pin = pin
    