import os
from flask import Flask
import tensorflow as tf

app = Flask(__name__)

model = None
session = tf.Session()

root = os.path.realpath(os.path.dirname(os.path.dirname(__file__)))
json_url = os.path.join(root, "resources", "data", "data.json")

@app.route("/")
def hello():
    # do tf calculation here
    if session is not None:
        # session.run()
        pass
    return "Hello World!"

if __name__ == '__main__':
    # tf.train.Saver().restore(session, str(os.path.join(root, "resources", "data", "model.ckpt")))
    app.run()
