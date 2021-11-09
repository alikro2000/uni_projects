from base64 import encode
import sys
import socket


HOST = '127.0.0.1'
PORT = 35413
BUFFER_SIZE = 131072 * 16

#Get data from console
data = sys.argv
page_path = data[1] if len(data) > 1 else input('Enter page repository path: ')
page_name = data[2] if len(data) > 2 else input('Enter file name: ')
server_port = data[3] if len(data) > 3 else input('Enter server port to connect to: ')

#Get data from server
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    data = '{path}/{file}'.format(path = page_path, file = page_name)
    s.sendall(bytes(data, encoding='utf-8'))
    data = s.recv(BUFFER_SIZE)
    print('status code: {code}'.format(code=data.decode(encoding='utf-8')))
    if (data == b'200 ok'):
        data = s.recv(BUFFER_SIZE)
        print('Page Contents:\n', data.decode('utf-8'))
