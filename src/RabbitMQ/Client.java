package RabbitMQ;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import RabbitMQ.User;
import java.util.HashSet;
import java.util.Set;

public class Client {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8080;
    
    private static Channel channel;
    private static Connection connection;
    private static ConnectionFactory factory = new ConnectionFactory();
    private static Consumer consumer;
    private static String queue;
    private static User user;
    private static NameGenerator generator;
    
    private final static String EXCHANGE = "log";
    
    public Client() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();        
        channel = connection.createChannel();
        user = new User();
        
        channel.exchangeDeclare(EXCHANGE, "direct");
        queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE, "BROADCAST");
        
        consumer = new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
            String message = new String(body,"UTF-8");
            System.out.println(message);
          }
        };
        channel.basicConsume(queue, consumer);
    }
    
    public static void main(String [] args) throws IOException, TimeoutException{
        generator = new NameGenerator();
        Client client = new Client();
        System.out.println("Mulai dengan mengetikkan /NICK <username>");
        boolean exit = false;
        Scanner cli = new Scanner(System.in);

        while(!exit){   
            String userInput = cli.nextLine();
            
            //String userAction = userInput.substring(0,userInput.indexOf(' '));
            String[] arrayInput = userInput.split(" ");
            String userAction = arrayInput[0];
            String userMessage = "";
            if(arrayInput.length>1)
            {
                userMessage = arrayInput[1];
            }
            System.out.println("Action : "+userAction+"\n"+"Message : "+userMessage);
            if(userAction.equals("/NICK")){
                if(userMessage.equals(userInput)){
                  //tidak ada input <username> -> random
                  user.setUsername(generator.randomIdentifier());
                } else {
                  //ada input
                  user.setUsername(userMessage.trim());
                }
            } else if (!user.isEmpty()) {
                if(userAction.equals("/JOIN") && !userMessage.equals("")){
                    //JOIN Channel
                    client.handleJoin(userMessage);
                }
                else if(userAction.equals("/LEAVE") && !userMessage.equals("")){
                    //LEAVE Channel
                    client.handleLeave(userMessage);
                }
                else if(userAction.equals("/EXIT")){
                    //EXIT Application
                    exit = true;
                }
                else if(userAction.charAt(0) == '@'){
                    //SEND message to a channel
                    client.handleSend(userMessage,userAction.substring(1));
                }
                else{ //send broadcast message
                    client.handleBroadcast(userInput);
                }
                
            }
        }
        channel.close();
        connection.close();
    }
    
    public void handleJoin(String chnl) throws IOException{
        channel.queueBind(queue, EXCHANGE, chnl);
    }
    
    public void handleLeave(String chnl) throws IOException{
        channel.queueUnbind(queue, EXCHANGE, chnl);
    }
    
    public void handleSend(String message, String chnl) throws IOException{
        String msg = "[" + chnl + "] [" + user.getUsername() + "] : " + message;
        channel.basicPublish(EXCHANGE, chnl, null, msg.getBytes());
    }
    
    public void handleBroadcast(String message) throws IOException{
        String msg = "[" +  user.getUsername() + "] : " + message;
        channel.basicPublish(EXCHANGE, "BROADCAST", null, msg.getBytes());
    }
    
    public static class NameGenerator{        
        // For generating name, using : http://stackoverflow.com/questions/5025651/java-randomly-generate-distinct-names
        // class variable
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

        final java.util.Random rand = new java.util.Random();

        // consider using a Map<String,Boolean> to say whether the identifier is being used or not 
        final Set<String> identifiers = new HashSet<String>();

        public String randomIdentifier() {
            StringBuilder builder = new StringBuilder();
            while(builder.toString().length() == 0) {
                int length = rand.nextInt(5)+5;
                for(int i = 0; i < length; i++)
                    builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
                if(identifiers.contains(builder.toString())) 
                    builder = new StringBuilder();
            }
            return builder.toString();
      }
    }
}
