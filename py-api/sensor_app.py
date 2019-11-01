from sensors.dht11.dht_reader import DHT11Reader

dht_instance = DHT11Reader()

dht_instance.poll_data()