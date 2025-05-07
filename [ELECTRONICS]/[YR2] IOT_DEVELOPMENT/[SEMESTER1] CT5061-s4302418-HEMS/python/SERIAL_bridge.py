import serial 
import socket

arduino_port = "COM9"
baud_rate = 9600
server_ip = "192.168.0.24"
server_port = 6377

arduino = serial.Serial(arduino_port, baud_rate)

server = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server.bind((server_ip, server_port))
server.listen(1)

while True:
    client, addr = server.accept()
    print(f"Connection from {addr}")
    try:
        while True:
            data = client.recv(1024)
            if data:
                arduino.write(data)

            if arduino.in_waiting:
                data = arduino.read(arduino.in_waiting)
                client.sendall(data)
    except:
        client.close()