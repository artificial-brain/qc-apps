import streamlit as st
import requests
import json
import pickle
from models.data import Data
import re
import time
from time import sleep
import sys


URL = "https://api.quantumcat.io/openQuantum"
filename = 'openpickle.pk'

# Use the full page instead of a narrow central column
quantumcat_logo_url = "https://raw.githubusercontent.com/artificial-brain/quantumcat/" \
                     "assets/quantumcat/logo/quantum_cat_logo.jpg"

# Set page title and favicon.
st.set_page_config(
    page_title="openquantum", page_icon=quantumcat_logo_url, layout="wide"
)

col1, col2 = st.beta_columns([2, 1])

st.sidebar.markdown('__Installation__')
st.sidebar.code('$ pip install quantumcat')
st.sidebar.write('Few command examples:')
st.sidebar.code('Create a circuit of 2 qubits')
st.sidebar.code('Apply h_gate to 0')
st.sidebar.code('Apply cx_gate to 0, 1')
st.sidebar.code('Execute on Qiskit')
st.sidebar.code('Execute on Cirq')
st.sidebar.code('Execute on Braket')



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

def header_code():
    code_header_string = '#quantumcat code'
    import_string = 'from quantumcat.circuit import QCircuit'
    circuit_string = 'circuit = QCircuit(' + str(data.num_qubits) + ')'

    with col2:
        code_header_text = st.empty()
        import_text = st.empty()
        circuit_text = st.empty()

    all_words = ' '
    time.sleep(0.5)
    for letter in code_header_string:
        sleep(0.03)  # In seconds
        all_words = all_words + letter
        with col2:
            code_header_text.write(all_words)

    all_words = ' '
    sleep(0.5)
    for letter in import_string:
        sleep(0.03)  # In seconds
        all_words = all_words + letter
        with col2:
            import_text.write(all_words)

    all_words = ' '
    sleep(0.5)
    for letter in circuit_string:
        sleep(0.03)  # In seconds
        all_words = all_words + letter
        with col2:
            circuit_text.write(all_words)


def body_code(code_string):
    with col2:
        code_body_text = st.empty()

    all_words = ' '
    for letter in code_string:
        sleep(0.03)  # In seconds
        all_words = all_words + letter
        with col2:
            code_body_text.write(all_words)


if submit_button:
    loading = col1.image('https://github.com/artificial-brain/quantumcat/blob/assets/quantumcat/opengreen.gif?raw=true',
                         width=200)
    if re.search("^Create a ", command, re.IGNORECASE):
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

        col1.write('Random circuit name ' + response_json['circuit_name'])
        col1.image(response_json['filename'])

        data.num_qubits = response_json['num_qubits']
        data.circuit_name = response_json['circuit_name']
        data.operations = response_json['operations']

        with open(filename, 'wb') as fi:
            pickle.dump(data, fi)

        if data.operations != '':
            header_code()
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
                code_string = f'circuit.{operation[0]}({gate_qubits})'
                body_code(code_string)

            if re.search("^Execute ", command, re.IGNORECASE):
                col2.write('circuit.measure_all()')
                sleep(0.5)
                on = re.findall(r'on\s*([^.]+|\S+)', command)[0]
                if on.lower() == 'qiskit':
                    code_string = 'counts = circuit.execute(provider=providers.IBM_PROVIDER)'
                elif on.lower() == 'cirq':
                    code_string = 'counts = circuit.execute(provider=providers.GOOGLE_PROVIDER)'
                elif on.lower() == 'braket':
                    code_string = 'counts = circuit.execute(provider=providers.AMAZON_PROVIDER)'
                body_code(code_string)
                code_string = 'circuit.histogram(counts)'
                body_code(code_string)
        else:
            header_code()

