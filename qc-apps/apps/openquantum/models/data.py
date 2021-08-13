class Data(object):

    def __init__(self):
        self.__num_qubits = ''
        self.__circuit_name = ''
        self.__operations = ''

    @property
    def num_qubits(self):
        return self.__num_qubits

    @num_qubits.setter
    def num_qubits(self, val):
        self.__num_qubits = val

    @property
    def circuit_name(self):
        return self.__circuit_name

    @circuit_name.setter
    def circuit_name(self, val):
        self.__circuit_name = val

    @property
    def operations(self):
        return self.__operations

    @operations.setter
    def operations(self, val):
        self.__operations = val

