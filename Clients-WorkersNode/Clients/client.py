import socket

class Cliente:
    def __init__(self):
        self.PUERTO = 8084
        self.ip = "10.10.0.231"
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.connect((self.ip, self.PUERTO))
        print("Conectado al servidor, escribe tus mensajes:")

    def run(self):
        console_input = input
        while True:
            # Reading data from the console
            mssg = []
            n = int(console_input("Ingrese el número de elementos: "))
            mssg.append(str(n) + "\n")
            for i in range(n):
                line = console_input("Ingrese elemento {}: ".format(i+1))
                mssg.append(line + "\n")
            k = int(console_input("Ingrese el número de elementos adicionales: "))
            mssg.append(str(k) + "\n")
            for i in range(k):
                line = console_input("Ingrese elemento adicional {}: ".format(i+1))
                mssg.append(line + "\n")
            
            mssg_str = "".join(mssg)
            print("Enviando datos al servidor...")
            self.server_socket.sendall(mssg_str.encode())

            # Reading data from the server
            response = self.server_socket.recv(1024).decode()
            print("Respuesta del servidor:")
            print(response)
            print("=== FINISH ====")

if __name__ == "__main__":
    cliente = Cliente()
    cliente.run()
