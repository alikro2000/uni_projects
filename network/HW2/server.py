from base64 import decode, encode
from genericpath import isdir
import os
import socket
import sys


HOST = '127.0.0.1'
PORT = 35413
BUFFER_SIZE = 131072 * 1
MAX_RSP_COUNT = 10 if len(sys.argv) < 2 else int(sys.argv[1])
rsp_count = 0

# Checks whether a file exists or not
#
# Returns true if the file at path, exists and false if it doesn't
def check_file_exists(path):
    return os.path.exists(path)

# Returns the contents of a file
#
# Throws an IOException if the file doesn't exist.
def load_file_contents(path):
    if not check_file_exists(path):
        raise 'IOException: File at [{path}] does not exist!'.format(path=path)

    contents = None
    with open(path, 'r', encoding='utf-8') as f:
        contents = f.read()
    return contents

# Create a socket to listen for client requests
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    # The listening must continue for a limited number of connections.
    while(rsp_count <= MAX_RSP_COUNT):
        s.listen()
        conn, addr = s.accept()
        with conn:
            print('Connected by [{addr}]'.format(addr = addr))
            while True:
                data = conn.recv(BUFFER_SIZE)
                rsp_count += 1
                if not data:
                    break
                # The client requested for a webpage. Check if that page exists or not and send the client a proper status code.
                requested_file_path = data.decode('utf-8')
                does_file_exist = check_file_exists(requested_file_path)
                rsp = '200 ok' if does_file_exist else '404 Not Found'
                conn.sendall(bytes(rsp, encoding='utf-8'))
                # Finally, if only the file existed (code 200), send the contents of the requested file to the client.
                if (does_file_exist):
                    contents = load_file_contents(requested_file_path)
                    conn.sendall(bytes(contents, encoding='utf-8'))
