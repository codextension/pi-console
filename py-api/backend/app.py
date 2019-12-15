from flask import Flask, jsonify
from db.db_connector import DBConnector

app = Flask(__name__)


@app.route('/', methods=['GET'])
def index():
    return jsonify({'version': '1.0'})


@app.route('/temperature/current', methods=['GET'])
def get_dht11():
    db = DBConnector('/home/ubuntu/db/py_api.db')
    latest_data = db.get_latest_dht11()
    return jsonify({'latest_data': latest_data})


'''
 @RequestMapping("/range") // ?from=15.09.2012-10:12&to=15.09.2017-10:12
    public List<Dust> getRange(@RequestParam(name = "from", required = true) Date fromDate, @RequestParam(name = "to", required = true) Date toDate) {
        return dustSensorRepository.findByMeasuredDateBetweenOrderByMeasuredDateDesc(fromDate, toDate);
    }
'''

if __name__ == '__main__':
    app.run(host='0.0.0.0')
