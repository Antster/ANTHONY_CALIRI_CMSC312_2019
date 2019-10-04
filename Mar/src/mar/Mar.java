/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anthony Caliri
 */
public class Mar {

    public static ArrayList<PCB> processList = new ArrayList<>();
    
    public static PCB p1 = new PCB();
    public static PCB p2 = new PCB();
    public static PCB p3 = new PCB();
    public static PCB p4 = new PCB();
    
    
    public static Scanner scan = new Scanner(System.in); 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("How many processes would you like to generate?");
        int processNum = scan.nextInt();
        readProgramFiles();
        
        Random rand = new Random();
        int processToAdd;
        for(int i = 0; i < processNum; i++){
            processToAdd = rand.nextInt((4 - 1) + 1) + 1;
    
            switch(processToAdd){
                case 1:
                    processList.add(p1);
                    break;
                case 2:
                    processList.add(p2);
                    break;
                case 3:
                    processList.add(p3);
                    break;
                case 4:
                    processList.add(p4);
            }
        }
        
        for(PCB pcb : processList){
            System.out.println(pcb.toString());
        }
    }
    
    
    private static void readProgramFiles() {
        try {
            // READ PROCESS FILE 1
            Scanner fileIn = new Scanner(new File("PF-1.txt"));
            String line;
            short tmp;
          
            p1.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p1.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p1.setMemory(tmp);
            
            ArrayList<String> operationList = new ArrayList<>();
            
            while(fileIn.hasNextLine()){
                line = fileIn.nextLine();
                operationList.add(line);
            }
            
            p1.setOperationList(operationList);
            
            // READ PROCESS FILE 2
            fileIn = new Scanner(new File("PF-2.txt"));
            
            
            p2.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p2.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p2.setMemory(tmp);
            
            operationList = new ArrayList<>();
            
            while(fileIn.hasNextLine()){
                line = fileIn.nextLine();
                operationList.add(line);
            }
            
            p2.setOperationList(operationList);
            
            // READ PROCESS FILE 3
            fileIn = new Scanner(new File("PF-3.txt"));
            
            
            p3.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p3.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p3.setMemory(tmp);
            
            operationList = new ArrayList<>();
            
            while(fileIn.hasNextLine()){
                line = fileIn.nextLine();
                operationList.add(line);
            }
            
            p3.setOperationList(operationList);
            
            // READ PROCESS FILE 4
            fileIn = new Scanner(new File("PF-4.txt"));
            
            
            p4.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p4.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p4.setMemory(tmp);
            
            operationList = new ArrayList<>();
            
            while(fileIn.hasNextLine()){
                line = fileIn.nextLine();
                operationList.add(line);
            }
            
            p4.setOperationList(operationList);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
