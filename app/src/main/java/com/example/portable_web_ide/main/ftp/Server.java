package com.example.portable_web_ide.main.ftp;

public class Server {

    public String serverName;
    public String hostName;
    public String userName;
    public String password;
    public int port;

    public Server(String serverName,String hostName,String userName,String password,int port)
    {
        this.serverName = serverName;
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        this.port = port;
    }
    public String getServerName()
    {
        return serverName;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

}
