# Network Course - HW1

Observing data transfer between 2 devices connected to a network.

## Tools Used

* php 7.4.10
* Wireshark
* Fing

### **Description**

Connect the host device (laptop) and the client device (phone) to the same router. Start the a server on the host device using the following command:

```shell
php -S 192.168.x.xxx:80
```

```192.168.x.xxx``` must be replaced with the the default gateway your host device has on the router (find it by running ```ipconfig``` in the Terminal).

Then, use your phone's browser to access the webpage (192.168.x.xxx:80).

Finally, use wireshark to trace the packets transferred between the host and client devices. Applying the following filter to wireshark helps to focus only on packets transferred on port 80.
```
tcp port 80
```

### **Notes**

Alternatively, you can use software like link [Fing](https://play.google.com/store/apps/details?id=com.overlook.android.fing&hl=en&gl=US) to find information about the devices connected to your router, including their IP addresses.

## Some useful links

[php built-in server](https://www.php.net/manual/en/features.commandline.webserver.php)