package RabbitMQ;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<String> channels;
    private List<String> messageLog;

    public List<String> getMessageLog() {
        return messageLog;
    }

    public void setMessageLog(List<String> _messageLog) {
        this.messageLog = _messageLog;
    }

    public User(String _username) {
        this.username = _username;
        this.channels = new ArrayList<String>();
        this.messageLog = new ArrayList<String>();
    }
    
    public User() {
        this.username = "";
        this.channels = new ArrayList<String>();        
        this.messageLog = new ArrayList<String>();
        this.channels.add("BROADCAST");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> _channels) {
        this.channels = _channels;
    }

    public void joinChannel(String channel) {
        this.channels.add(channel);
    }
    
    public void leaveChannel (String channel)
    {
        this.channels.remove(this.channels.indexOf(channel));
    }
    public boolean isEmpty()
    {
        return this.username.isEmpty();
    }
    public void addMessage(String message)
    {
        System.out.println("User = "+message);
        //channelname - nickname - content
        messageLog.add(message);
    }
    public String getAllMessage()
    {
        StringBuilder result = new StringBuilder();
        while (!messageLog.isEmpty())
        {
            result.append(messageLog.remove(0)).append("\n");
        }
        return result.toString();
    }
}