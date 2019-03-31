import os
import tensorflow as tf
import json

# gotta catch 'em all!
from src.py.model import model

if __name__ == '__main__':

    root = os.path.realpath(os.path.dirname(os.path.dirname(__file__)))
    input_json = json.load(open(os.path.join(root, "resources", "data", "expected.json")))["input"]

    model()

    g = tf.Graph()

    result = g.get_tensor_by_name("result")
    con_in = g.get_tensor_by_name("con_in")
    rat_in = g.get_tensor_by_name("rat_in")

    expected_out = tf.placeholder(tf.float16, shape=[10])

    init = tf.global_variables_initializer()

    optimizer = tf.train.GradientDescentOptimizer(0.001)
    loss = tf.losses.mean_squared_error([expected_out],
                                        )
    train = optimizer.minimize(loss)

    with tf.Session() as s:
        for i in range(len(input_json) * 1):
            input_data = input_json[i % len(input_json)]
            s.run(init)
            _, run_result, ex_out, run_loss = s.run(
                (train, result, expected_out, loss),
                feed_dict={
                    con_in: input_data["confidence"],
                    rat_in: input_data["review"],
                    expected_out: list(map(
                        lambda cr,: cr[0] * cr[1],
                        zip(input_data["confidence"], input_data["review"])
                    ))
                }
            )
            print("loss: ", run_loss)

        tf.train.Saver().save(s, os.path.join(root, "resources", "data", "model.ckpt"))

        writer = tf.summary.FileWriter(os.path.join(root, "resources", "data"))
        writer.add_graph(tf.get_default_graph())
        writer.flush()