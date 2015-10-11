import com.rabbitmq.client.*;

public class Exchange {
    private Connection connection;
    private static ConnectionFactory factory = new ConnectionFactory();
    private Channel channel;
    
    
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 5672;

    private static final String QUEUE_NAME = "nameQueue";
    
    public static void main(String[] args){
        factory = new ConnectionFactory();
    }
}
