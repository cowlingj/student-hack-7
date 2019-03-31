import tensorflow as tf

def model():
    initialV1 = tf.random_uniform(shape=[10], dtype=tf.float16)
    initialV2 = tf.random_uniform(shape=[10], dtype=tf.float16)
    initialV3 = tf.random_uniform(shape=[10], dtype=tf.float16)
    initialV4 = tf.random_uniform(shape=[10], dtype=tf.float16)

    con_in = tf.placeholder(tf.float16, shape=[10], name="con_in")
    rat_in = tf.placeholder(tf.float16, shape=[10], name="rat_in")

    con_m = tf.Variable(initial_value=[initialV1],
                        dtype=tf.float16,
                        expected_shape=[10],
                        validate_shape=True,
                        constraint=lambda x: tf.clip_by_value(x, -1, 1))

    rat_m = tf.Variable(initial_value=[initialV2],
                        dtype=tf.float16,
                        expected_shape=[10],
                        validate_shape=True,
                        constraint=lambda x: tf.clip_by_value(x, -1, 1))

    con_c = tf.Variable(initial_value=[initialV3], dtype=tf.float16)
    rat_c = tf.Variable(initial_value=[initialV4], dtype=tf.float16)

    m_con_c = (con_in * con_m) + con_c
    m_rat_c = (rat_in * rat_m) + rat_c

    tf.clip_by_value(m_con_c * m_rat_c, -1, 1, "result")