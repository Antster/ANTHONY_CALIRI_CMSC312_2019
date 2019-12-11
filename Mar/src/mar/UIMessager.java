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
public class UIMessager {
    int message;
    
    public UIMessager(){
        message = -1;
    }
    
    public void setMessage(int msg){
        message = msg;
    }
    
    public int getMessage(){
        return message;
    }
}
