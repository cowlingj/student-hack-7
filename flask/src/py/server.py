import json
import os
from flask import Flask, request

import tensorflow as tf
import src.py.model as model_init

app = Flask(__name__)

model = None

root = os.path.realpath(os.path.dirname(os.path.dirname(__file__)))
json_url = os.path.join(root, "resources", "data", "data.json")

@app.route("/", methods = ["POST"])
def hello():
    # do tf calculation here

    body = request.json
    print(body)

    session = tf.Session()
    model_init.model()
    tf.train.Saver().restore(session, str(os.path.join(root, "resources", "data", "model.ckpt")))

    g = tf.get_default_graph()

    result = g.get_tensor_by_name("result:0")
    con_in = g.get_tensor_by_name("con_in:0")
    rat_in = g.get_tensor_by_name("rat_in:0")

    res = session.run(result, feed_dict={
        con_in: body["confidence"],
        rat_in: body["ratings"]
    })

    return json.dumps({ "recommendation": res[0].tolist() })

if __name__ == '__main__':

    app.run()
