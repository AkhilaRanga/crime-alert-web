package com.crimealert.models;

public class SmtpConfig {
	 private String host;
     private int port;

     public SmtpConfig(String host, int port) {
         this.host = host;
         this.port = port;
     }

     public String getHost() {
         return host;
     }

     public int getPort() {
         return port;
     }

}
