/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tonyt
 */
public class MCU {

    private static int memorySpace;
    
    private static List<PCB> memoryList = new ArrayList<>();
    
    public MCU(int memSpace) {
        this.memorySpace = memSpace;
    }
    
    protected static int getMemorySpace() {
        return memorySpace;
    }
    
    protected static void setMemorySpace(int in){
        memorySpace = in;
    }
    
    protected static void addToMemList(PCB p){
        memoryList.add(p);
    }
    protected static void removeFromMemList(PCB p){
        memoryList.remove(p);
    }
    
    protected static boolean memHasSpace(int in) {
        return (memorySpace - in) >= 0;
    }
}
