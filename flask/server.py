from flask import Flask
app = Flask(__name__)

@app.route("/")
def hello():
    # do tf calculation here
    return "Hello World!"

if __name__ == '__main__':
    app.run()