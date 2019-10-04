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
public class Clock {
    public static int clock;
    public static int max;
    
    public Clock () {
        clock = 0;
    }
    
    public static void start(){
        long sysTime = System.currentTimeMillis();
        while(1 == 1){
            if(System.currentTimeMillis() - sysTime >= 10){
                clock++;
                sysTime = System.currentTimeMillis();
            }
        }
    }
    
    public static int getClock() {
        return clock;
    }
    
    public static void resetClock(){
        clock = 0;
    }
}
