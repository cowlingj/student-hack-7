import os
import tensorflow as tf
import json

# gotta catch 'em all!

if __name__ == '__main__':

    root = os.path.realpath(os.path.dirname(os.path.dirname(__file__)))
    input_json = json.load(open(os.path.join(root, "resources", "data", "expected.json")))["input"]

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