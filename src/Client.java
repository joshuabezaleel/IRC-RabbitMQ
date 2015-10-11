import com.rabbitmq.client.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 5672;
    
    private static Connection connection;
    private static ConnectionFactory factory = new ConnectionFactory();
    
    public static void main(String [] args){
        try {
            perform(DEFAULT_HOST,DEFAULT_PORT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void perform(String host, int port) throws Exception {
        System.out.println("Mulai dengan mengetilkan /NICK <username>");
        boolean exit = false;
        Scanner cli = new Scanner(System.in);
        final StringBuilder userName = new StringBuilder();
        
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        connection = factory.newConnection();
        
        Timer timer = new Timer();
        TimerTask asyncTask;
        
        asyncTask = new TimerTask() {
            @Override
            public void run() {
                String usernm = userName.toString();
                try{
                    if(!usernm.isEmpty()){
                        handleNick(usernm);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
        timer.schedule(asyncTask, 0, 1000);
        
        while(!exit){
            String userInput = cli.nextLine();
            
            String userAction = userInput.substring(0,userInput.indexOf(' '));
            String userMessage = userInput.substring(userInput.indexOf(' ')+1);
            System.out.println("Action : "+userAction+"\n"+"Message : "+userMessage);
            if(userAction.equals("/NICK")){
                if(userMessage.equals(userInput)){
                  //tidak ada input <username>  
                } else {
                  //ada input
                  
                }
            } else if (userName != null) {
                if(userAction.equals("/JOIN") && !userMessage.equals("")){
                    //JOIN Channel
                    handleJoin(userName.toString(),userMessage);
                }
                else if(userAction.equals("/LEAVE") && !userMessage.equals("")){
                    //LEAVE Channel
                    handleLeave(userName.toString(),userMessage);
                }
                else if(userAction.equals("/EXIT")){
                    //EXIT Application
                    handleExit(userName.toString());
                    exit = true;
                }
                else if(userAction.charAt(0) == '@'){
                    //SEND message to a channel
                    handleSend(userName.toString(),userMessage,userAction.substring(1));
                }
                else{ //send broadcast message
                    handleBroadcast(userName.toString(),userInput);
                }
            }
        }
        timer.cancel();
        timer.purge();
        cli.close();
        
    }
    
    
    private static void handleNick(String userName){

    }
    
    private static void handleJoin(String userName, String channel){
        
    }
    
    private static void handleLeave(String userName, String channel){
        
    }
    
    private static void handleExit(String userName){
        
    }
    
    private static void handleSend(String userName, String message, String channel){
        
    }
    
    private static void handleBroadcast(String userName, String message){
        
    }

}
