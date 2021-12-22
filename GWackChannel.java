import java.util.*;
import java.net.*;
import java.io.*;


public class GWackChannel{

    ServerSocket serverSock;
    LinkedList<ClientHandler> users = new LinkedList<ClientHandler>();
    

    public GWackChannel(int port){

        try{
            serverSock = new ServerSocket(port);
        }catch(Exception e){
            System.err.println("Cannot establish server socket");
            System.exit(1);
        }
        
    }

    public void serve(){
        
        while(true){
            try{
                
                //accept incoming connection
                Socket clientSock = serverSock.accept();
                System.out.println("New connection: "+clientSock.getRemoteSocketAddress());
                
                ClientHandler newUser = new ClientHandler(clientSock);
                users.add(newUser);

                //start the thread
                newUser.start();

                //continue looping
            }catch(Exception e){} //exit serve if exception
        }
    }

    private class ClientHandler extends Thread{
        Socket sock;
        PrintWriter pw = null;
        BufferedReader in = null;
        
        public ClientHandler(Socket sock){
            this.sock=sock;
        }

        public void run(){
            
            try{
                pw = new PrintWriter(sock.getOutputStream());
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                if(in.readLine().equals("NAME")){
                    this.setName(in.readLine());
                }
                else{
                    return;
                }
                for(ClientHandler user: users){
                    user.pw.println("START_CLIENT_LIST");
                    for (ClientHandler client: users){
                        user.pw.println(client.getName());
                    }
                    user.pw.println("END_CLIENT_LIST");
                    user.pw.flush();
                }
                
                //read and echo back forever!
                while(true){
                    String msg = in.readLine();
                    if(msg == null || !this.isAlive()) break; //read null, remote closed
                    for (ClientHandler user: users){
                        user.pw.println("[" + this.getName() + "] " + msg);
                        user.pw.flush();
                    }
                }
                

                //close the connections
                
                
                
            }catch(Exception e){
                try {
                    pw.close();
                    in.close();
                    sock.close();
                } catch (Exception e2) {}
                
            }

            //note the loss of the connection
            System.out.println("Connection lost: "+sock.getRemoteSocketAddress());

            for (ClientHandler client: users){
                client.pw.println("START_CLIENT_LIST");
                for (ClientHandler user: users){
                    if (user.getName().equals(this.getName())){
                        users.remove(user);
                    }
                    else{
                        client.pw.println(user.getName());
                    }
                }
                client.pw.println("END_CLIENT_LIST");
                client.pw.flush();
            }
            
            try {
                pw.close();
                in.close();
                sock.close();
            } catch (Exception e) {
                System.err.println("Cannot Disconnect");
            }
                
        }

    }

    public static void main(String args[]){
        GWackChannel server = new GWackChannel(Integer.parseInt(args[0]));
        server.serve();
    }
       
    
}