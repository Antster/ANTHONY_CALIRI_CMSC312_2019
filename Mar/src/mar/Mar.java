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

    public static int clock;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int processNum = promptForProcessAmount();

        readProgramFiles();
        
        printProcessList();
        generateProcesses(processNum);

        int returnVal = startProcessing();

        switch (returnVal) {
            case 0:
                System.out.println("Processes finished normaly");
                break;
            case -1:
                System.out.println("Error while processing!");
        }

    }

    private static int startProcessing() {
        // DOING FIRST COME FIRST SERVE FOR PART 1
        short totalRuntime;
        int loopClock;
        long sysTime = System.currentTimeMillis();
        String op;
        for (PCB pcb : processList) {
            totalRuntime = pcb.getTotalRuntime();
            System.out.println(pcb.getName() + " " + totalRuntime);

            for (String s : pcb.getOperationList()) {
                
                loopClock = 0;
                if (!s.contains("EXE")) {
                    op = s.substring(0, 10).trim();
                    pcb.setCurOperation(op);
                    totalRuntime = Short.parseShort(s.replaceAll("[^\\d]", ""));

                    while (loopClock < totalRuntime) {
                        if (System.currentTimeMillis() - sysTime >= 10) {
                            loopClock++;
                            sysTime = System.currentTimeMillis();
                        }
                    }
                }
            }

        }
        return 0;
    }

    private static void dispatcher() {
        /* 
            CANT I DO THIS BY USING MY setState() METHOD ON EACH INDIVUAL PROCESS AS THE
            PROCESS NEEDS TO CHANGE STATES?
         */
    }

    private static int promptForProcessAmount() {
        System.out.println("How many processes would you like to generate?");
        return scan.nextInt();
    }

    private static void generateProcesses(int processNum) {
        Random rand = new Random();
        int processToAdd;
        for (int i = 0; i < processNum; i++) {
            processToAdd = rand.nextInt((4 - 1) + 1) + 1;

            switch (processToAdd) {
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
    }

    private static void readProgramFiles() {
        try {
            readPF1();
            readPF2();
            readPF3();
            readPF4();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void readPF1() throws FileNotFoundException{
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

            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                p1.addToOpList(line);
            }
    }
    
    private static void readPF2() throws FileNotFoundException {
        
        Scanner fileIn = new Scanner(new File("PF-2.txt"));
        String line;
        short tmp;
        
            p2.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p2.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p2.setMemory(tmp);

            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                p2.addToOpList(line);
            }
    }
    
    private static void readPF3() throws FileNotFoundException {
        Scanner fileIn = new Scanner(new File("PF-3.txt"));
        String line;
        short tmp;
        
            p3.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p3.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p3.setMemory(tmp);

            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                p3.addToOpList(line);
            }
    }
    
    private static void readPF4() throws FileNotFoundException {
        Scanner fileIn = new Scanner(new File("PF-4.txt"));
        String line;
        short tmp;
        
            p4.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p4.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p4.setMemory(tmp);

            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                p4.addToOpList(line);
            }
    }

    private static void printProcessList() {
        for (PCB pcb : processList) {
            System.out.println(pcb.toString());
            for (String s : pcb.getOperationList()){
                System.out.println("\t" + s);
            }
        }
    }
}
