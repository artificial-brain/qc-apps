import streamlit as st
import requests
import json
import pickle
from models.data import Data
import re
from PIL import Image


URL = "http://localhost:8000/openQuantum"
filename = 'openpickle.pk'

# Use the full page instead of a narrow central column
st.set_page_config(layout="wide")
col1, col2 = st.beta_columns(2)


@st.cache
def createDataObj():
    with open(filename, 'wb') as fi:
        pickle.dump(Data(), fi)


createDataObj()

m = col1.markdown("""
<style>
div.stButton > button:first-child {
    background-color: rgb(64, 147, 99);
    color: white;
}
</style>""", unsafe_allow_html=True)

command = col1.text_input('Enter Command')
submit_button = col1.button('Submit')
col1.write('#### Powered by [quantumcat](https://github.com/artificial-brain/quantumcat)')
col1.write('')
image = Image.open('assets/circle-particles.gif')

if submit_button:
    loading = col1.image('https://github.com/artificial-brain/quantumcat/blob/assets/quantumcat/screen.gif?raw=true')
    if re.search("^Create a ", command):
        data = Data()
    else:
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
        loading.empty()
        response_json = json.loads(response.text)

        col1.write('Circuit diagram for ' + response_json['circuit_name'])
        col1.image(response_json['filename'])

        data.num_qubits = response_json['num_qubits']
        data.circuit_name = response_json['circuit_name']
        data.operations = response_json['operations']

        if data.operations != '':
            col2.write('#quantumcat code')
            col2.write('from quantumcat.circuit import QCircuit')
            col2.write('circuit = QCircuit(' + str(data.num_qubits) + ')')
            for op in data.operations:
                operation = next(iter(op.items()))
                i = 0
                gate_qubits = ''
                for elem in operation[1]:
                    if i == 0:
                        gate_qubits = (str(elem))
                    else:
                        gate_qubits = gate_qubits + ',' + (str(elem))
                    i = i + 1

                col2.write(f'circuit.{operation[0]}({gate_qubits})')
            if re.search("^Execute ", command):
                col2.write('circuit.measure_all()')
                on = re.findall(r'on\s*([^.]+|\S+)', command)[0]

                if on.lower() == 'ibm':
                    col2.write('counts = circuit.execute(provider=providers.IBM_PROVIDER)')
                elif on == 'google':
                    col2.write('counts = circuit.execute(provider=providers.GOOGLE_PROVIDER)')
                elif on == 'aws':
                    col2.write('counts = circuit.execute(provider=providers.AMAZON_PROVIDER)')

                col2.write('circuit.histogram(counts)')

        else:
            col2.write('#quantumcat code')
            col2.write('from quantumcat.circuit import QCircuit')
            col2.write('circuit = QCircuit(' + str(data.num_qubits) + ')')

        with open(filename, 'wb') as fi:
            pickle.dump(data, fi)
