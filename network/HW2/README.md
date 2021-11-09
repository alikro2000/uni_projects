# Network Course - HW2

Practicing socket programming.

## Description

### **How it works**

First, run ```downloader.py``` to download contents of some webpages into ./server_file directory as html files. The urls of webpages is in ```info.json``` (Feel free to modify that list to your liking). The downloaded pages have names like "```www.[domain].[top_domain].html```" accordingly. Note that previously downloaded pages are deleted every time ```download.py``` gets executed.

Then, run ```server.py``` to start a server. Not that it'll respond only to a limited number of client requests (10 by default, but it can be set to another number). For example, the following command starts a server with maximum response count of 20:
```shell
python server.py 20
```

Finally, run ```client.py``` separately to request a webpage from that server. The file address and file name must be defined for the client (The connection port is set to 35413 by default). For example:
```shell
python ./client.py ./server_file www.wikipedia.org.html 35413
```

or, alternatively,

```shell
python .\client.py
Enter page repository path: www.wikipedia.org.html
Enter file name: www.wikipedia.org.html
Enter server port to connect to: 35413
```

### **Notes**

Both the server and client will connect to '127.0.0.1' and port 35413 by default.

The default encoding for saving the downloaded html files and data transfer between server and client was set to utf-8. Threfore, there may be problems with some web-pages.

![client_server_sockets_connection](https://files.realpython.com/media/sockets-tcp-flow.1da426797e37.jpg)

## Tools Used
* Python 3.9.6

## Some useful links
[Socket Programming in Python](https://realpython.com/python-sockets/)