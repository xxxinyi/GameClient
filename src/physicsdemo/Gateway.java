/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author user
 */
public class Gateway implements Game.GameConstants {
    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    
   public Gateway(){
        try{
            Socket socket = new Socket("localhost", 8000);
            outputToServer = new PrintWriter(socket.getOutputStream());
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException ex){
            ex.printStackTrace();
        }
   }
   
   
   public void sendHandle(String handle){
       outputToServer.println(SEND_HANDLE);
       outputToServer.println(handle);
       outputToServer.flush();
   }
   
   public void sendControl(String handle, int c){
       outputToServer.println(SEND_CONTROL);
       outputToServer.println(handle);
       outputToServer.println(c);
       outputToServer.flush();
   }
   
   public int getControl(){
       outputToServer.println(GET_CONTROL);
       outputToServer.flush();
       int control = 0;
       try{
           control = Integer.parseInt(inputFromServer.readLine());
       }catch(IOException ex){
           ex.printStackTrace();
       }
       return control;
   }
   
   public int getEvolve(){
       outputToServer.println(GET_EVOLVE);
       outputToServer.flush();
       int e = 0;
       try{
           e = Integer.parseInt(inputFromServer.readLine());
       }catch(IOException ex){
           ex.printStackTrace();
       }
       return e;
   }
}
