import streamlit as st
import requests
import json
import pickle
from models.data import Data

URL = "http://localhost:8000/openQuantum"
filename = 'openpickle.pk'


@st.cache
def createDataObj():
    with open(filename, 'wb') as fi:
        pickle.dump(Data(), fi)


createDataObj()

m = st.markdown("""
<style>
div.stButton > button:first-child {
    background-color: rgb(64, 147, 99);
    color: white;
}
</style>""", unsafe_allow_html=True)

command = st.text_input('Enter Command')
if st.button('Submit'):
    with open(filename, 'rb') as fi:
        data = pickle.load(fi)
    request_json = {
        "command": command,
        "num_qubits": data.num_qubits,
        "circuit_name": data.circuit_name,
        "operations": data.operations
    }

    response = requests.post(url=URL, data=json.dumps(request_json))
    if response.status_code == 200:
        response_json = json.loads(response.text)
        st.write('Circuit Name is ' + response_json['circuit_name'])
        st.image(response_json['filename'])

        data.num_qubits = response_json['num_qubits']
        data.circuit_name = response_json['circuit_name']
        data.operations = response_json['operations']

        with open(filename, 'wb') as fi:
            pickle.dump(data, fi)
