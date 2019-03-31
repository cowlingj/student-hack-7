from flask import Flask
import tensorflow as tf
from random import Random
import json

app = Flask(__name__)


@app.route("/")
def hello():
    # do tf calculation here
    return "Hello World!"


if __name__ == '__main__':

    input = json.load(open("expected.json"))["input"]

    zeros = tf.ones(shape=[10], dtype=tf.float16)

    con_in = tf.placeholder(tf.float16, shape=[10])
    rat_in = tf.placeholder(tf.float16, shape=[10])
    expected_out = tf.placeholder(tf.float16, shape=[10])

    con_m = tf.Variable(initial_value=[zeros],
                        dtype=tf.float16,
                        expected_shape=[10],
                        validate_shape=True,
                        constraint=lambda x: tf.clip_by_value(x, -1, 1))

    rat_m = tf.Variable(initial_value=[zeros],
                        dtype=tf.float16,
                        expected_shape=[10],
                        validate_shape=True,
                        constraint=lambda x: tf.clip_by_value(x, -1, 1))

    con_c = tf.Variable(initial_value=[zeros], dtype=tf.float16)
    rat_c = tf.Variable(initial_value=[zeros], dtype=tf.float16)

    m_con_c = (con_in * con_m) + con_c
    m_rat_c = (rat_in * rat_m) + rat_c

    result = tf.clip_by_value(m_con_c * m_rat_c, -1, 1)

    init = tf.global_variables_initializer()

    optimizer = tf.train.GradientDescentOptimizer(0.001)
    loss = tf.losses.mean_squared_error([expected_out], result
    )
    train = optimizer.minimize(loss)

    with tf.Session() as s:
        for i in range(len(input) * 10000):
            input_data = input[i % len(input)]
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

    # writer = tf.summary.FileWriter('.')
    # writer.add_graph(tf.get_default_graph())
    # writer.flush()

    app.run()
