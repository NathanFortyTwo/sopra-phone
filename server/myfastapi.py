import os

os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"

from fastapi import FastAPI
from typing import Annotated
from fastapi import FastAPI, File

import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.preprocessing import image
from tensorflow.keras.applications.imagenet_utils import decode_predictions


# Load a pre-trained model (e.g., MobileNetV2)
# model = tf.keras.applications.MobileNetV2(weights='imagenet')
# model.save("model.h5")
app = FastAPI()
model = keras.models.load_model("model.h5")


# Load an image for classification
def process(filepath):
    img = image.load_img(filepath, target_size=(224, 224))
    img_array = image.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)  # Add batch dimension

    # Preprocess the image for the model
    img_array = tf.keras.applications.mobilenet_v2.preprocess_input(img_array)

    # Get model predictions
    predictions = model.predict(img_array)

    # Decode and print the top-3 predicted classes
    decoded_predictions = decode_predictions(predictions, top=1)[0]
    imagenet_id, label, score = decoded_predictions[0]
    return f"This is a {label} with {score * 100:.2f}% confidence."


@app.post("/")
async def create_file(file: bytes = File(...)):
    with open("reception.png", "wb") as f:
        f.write(file)
    return process("reception.png")
