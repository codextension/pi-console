import json


if __name__ == "__main__":
    data = {"name": "elie", "age":39}
    json_str = json.dumps(data["age"]).encode('utf-8')
    print(json_str)