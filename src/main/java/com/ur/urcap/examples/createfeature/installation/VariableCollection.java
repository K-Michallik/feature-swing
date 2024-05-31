package com.ur.urcap.examples.createfeature.installation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class VariableCollection {
  public String getVarVal(String command) {
    String serverAddress = "127.0.0.1";
    int port = 29999;
    String fullCommand = "getVariable " + command;
    String response = null;
    System.out.println("Starting socket connection.");
    try {
      Socket sc = new Socket(serverAddress, port);
      if (sc.isConnected()) {
        BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
        PrintWriter out = new PrintWriter(sc.getOutputStream(), true);
        in.readLine();
        out.println(fullCommand);
        response = in.readLine();
        out.close();
      } 
      sc.close();
    } catch (SocketTimeoutException e) {
      response = "Socket timeout: " + e;
      return response;
    } catch (Exception e) {
      response = "Generic error: " + e;
      return response;
    } 
    return response;
  }
}