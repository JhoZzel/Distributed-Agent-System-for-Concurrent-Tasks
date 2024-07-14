require 'socket'

class StreamSocket
  def initialize(socket)
    @socket = socket
  end

  def self.connect(ip, port)
    new(TCPSocket.new(ip, port))
  rescue StandardError => e
    puts "Connection error: #{e.message}"
    exit(1)
  end

  def receive
    @socket.gets
  rescue StandardError => e
    puts "Receive error: #{e.message}"
    exit(1)
  end

  def send(message)
    @socket.puts(message)
  rescue StandardError => e
    puts "Send error: #{e.message}"
    exit(1)
  end

  def close
    @socket.close
  end
end

class Cliente
  def self.run
    begin
      port = 8084
      ip = '10.10.0.231'
      server = StreamSocket.connect(ip, port)
      puts 'Conectado al servidor, escribe tus mensajes:'

      loop do
        # Leer datos de la consola
        print 'Ingresa el número de líneas: '
        n = gets.chomp.to_i
        mssg = ''

        n.times do
          line = gets.chomp
          mssg += line + "\n"
        end

        puts 'Sending data to the server'
        server.send(mssg)

        # Leer datos del servidor
        respuesta = server.receive
        puts 'Respuesta del servidor:'
        puts respuesta
        puts '=== FINISH ===='
      end
    rescue StandardError => e
      puts "Exception: #{e.message}"
    end
  end
end

Cliente.run
