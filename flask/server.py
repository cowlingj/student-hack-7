from flask import Flask
import tensorflow as tf
from random import Random

app = Flask(__name__)

@app.route("/")
def hello():
    # do tf calculation here
    return "Hello World!"

if __name__ == '__main__':

    zeros = tf.zeros(shape=[10], dtype=tf.float16)

    con_in = tf.placeholder(tf.float16, shape=[10])
    rat_in = tf.placeholder(tf.float16, shape=[10])

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

    optimizer = tf.train.GradientDescentOptimizer(0.01)
    loss = tf.losses.mean_squared_error(tf.constant(
        [[0.1, 0.2, 0.3, -0.4, 0.5, -0.3, -.9, -.1, .2, -.1]],
        dtype=tf.float16), result
    )
    train = optimizer.minimize(loss)

    with tf.Session() as s:
        s.run(init)
        print("result: ", s.run((train, loss), feed_dict={
          con_in: [Random().random() for i in range(10)],
          rat_in: [Random().random() for i in range(10)]
        }))

    app.run()