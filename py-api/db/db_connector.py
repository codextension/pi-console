import sqlite3
from datetime import datetime
from enum import Enum

class Table(Enum):
    dht11 = 1
    mcp3008 = 2

class DBConnector:

    def __init__(self, db_url):
        self.__db_url=db_url        
        self.__create_db()

    def __execute_sql(self, sql):
        conn = sqlite3.connect(self.__db_url)
        cursor = conn.cursor()
        cursor.execute(sql)
        conn.commit()
        conn.close()
    
    def __query_sql(self, sql):
        conn = sqlite3.connect(self.__db_url)
        cursor = conn.cursor()
        cursor.execute(sql)
        result = cursor.fetchall()
        conn.close()
        return result

    def __create_db(self):
        tables = [
            "create table IF NOT EXISTS dht11 (id INTEGER PRIMARY KEY, temperature REAL NOT NULL, humidity REAL NOT NULL, measured_date timestamp NOT NULL)",
            "create table IF NOT EXISTS mcp3008 (id INTEGER PRIMARY KEY, value_measured REAL NOT NULL, voltage REAL NOT NULL, density REAL NOT NULL, measured_date timestamp NOT NULL)"]
        self.__execute_sql(tables[0])
        self.__execute_sql(tables[1])


    def new_dht11(self, temp, humidity):
        now = datetime.now()
        timestamp = datetime.timestamp(now)
        query = f'insert into dht11(temperature, humidity, measured_date) values ({temp},{humidity},{timestamp})'
        self.__execute_sql(query)

    def new_mcp3008(self, value_measured, voltage, density):
        now = datetime.now()
        timestamp = datetime.timestamp(now)
        query = f"insert into mcp3008(value_measured, voltage, density, measured_date) values ({value_measured},{voltage}, {density}, {timestamp})"
        self.__execute_sql(query)

    def get_all_dht11(self):
        print(self.__query_sql('select * from dht11'))

    def get_all_mcp3008(self):
        print(self.__query_sql('select * from mcp3008'))

    def find_by_measured_date(self,tablename, from_date, to_date):
        query = f'select * from {tablename} where measured_date >= {from_date} and measured_date <= {to_date} order by measured_date desc'
        print(self.__query_sql(query))
        return self.__query_sql(query)


"""
db = DBConnector('/home/ubuntu/db/py_api.db')
db.new_dht11(23, 45)
db.find_by_measured_date(Table.dht11.name,1572896770, 1572896964)
"""