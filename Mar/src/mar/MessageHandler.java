/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mar;

/**
 *
 * @author Anthony Caliri
 */
public class MessageHandler {

    private String message;
    
    public MessageHandler(){
        message = "";
    }
    
    public void setMessage(String msg){
        this.message = msg;
    }
    
    public String sendMessage(){
        return this.message;
    }
}
