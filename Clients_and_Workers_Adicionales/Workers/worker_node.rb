require 'socket'
require 'thread'

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

class WorkerBeat
  def initialize(server)
    @server = server
  end

  def start
    Thread.new do
      loop do
        begin
          sb = @server.receive
          puts sb
          puts 'Sending the heartbeat to the server....'
          @server.send("HeartBeat from the worker node!!\n")
          sleep 1
        rescue StandardError => e
          puts "Heartbeat error: #{e.message}"
          break
        end
      end
      puts 'WorkerBeat finalizado.'
    end
  end
end

class WorkerNode
  def self.count_a(s)
    s.count('a')
  end

  def self.run
    begin
      port = 8084
      ip = '10.10.0.231'
      server = StreamSocket.connect(ip, port)
      puts 'Connected to the leader node...'
      server_beat = StreamSocket.connect(ip, port)
      puts 'Creating the heartbeat channel to the server...'
      puts 'Starting the heartbeats...'

      WorkerBeat.new(server_beat).start

      loop do
        puts 'Waiting for data...'
        msg = server.receive
        puts 'Received Data:'
        puts msg
        puts ''

        ans = ''
        msg.each_line do |line|
          next if line.strip.empty?

          puts "line: #{line}"
          line.split.each do |word|
            cnt = count_a(word)
            ans += "#{cnt}\n"
          end
        end

        puts 'Processed Data:'
        puts ans
        server.send(ans)
      end
    rescue StandardError => e
      puts "Exception: #{e.message}"
    end
  end
end

WorkerNode.run
