/*
 * @author Anthony Caliri
 * Description: Main class for the Mar operating system simulator
 */
package mar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class Mar {

    public static ArrayList<PCB> processList = new ArrayList<>();

    public static MCU mcu = new MCU(1000);

    public static PCB p1 = new PCB();
    public static PCB p2 = new PCB();
    public static PCB p3 = new PCB();
    public static PCB p4 = new PCB();
    public static PCB p5 = new PCB();

    public static Scanner scan = new Scanner(System.in);

    public static int clock;

    public static void main(String[] args) {
        int processNum = promptForProcessAmount();
        readProgramFiles();

        generateProcesses(processNum);
        setProcessIDs();

        //Uncomment to see ProcessList
//        printProcessList();
        sortProcessList();

        int returnVal = startProcessing();

//        switch (returnVal) {
//            case 0:
//                System.out.println("\nProcessing finished normaly");
//                break;
//            case -1:
//                System.out.println("\nError while processing!");
//        }
    }

    // Sort by shortest runtime
    private static void sortProcessList() {
        Collections.sort(processList);
    }

    // After sorting processing of the list is done FCFS 
    private static int startProcessing() {
        long sysTime = System.currentTimeMillis();
        for (PCB pcb : processList) {
            (new Thread(new ProcessRunnable(pcb, sysTime, mcu))).start();
        }
        return 0;
    }

    private static void printProcessState(PCB pcb) {
        System.out.println("Process State: " + pcb.getState());

        System.out.println("Current Operation: " + pcb.getCurOperation());
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

    // To randomize the creation of children I have used a random number 0 to 100 with the mod operator
    private static void generateProcesses(int processNum) {
        Random rand = new Random();
        int processToAdd, numChildren, haveChildren;
        for (int i = 0; i < processNum; i++) {
            processToAdd = rand.nextInt((5 - 1) + 1) + 1;
            numChildren = rand.nextInt((3 - 1) + 1) + 1;
            haveChildren = rand.nextInt((100 - 1) + 1) + 1;
            switch (processToAdd) {
                case 1:
                    processList.add(p1);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
                case 2:
                    processList.add(p2);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
                case 3:
                    processList.add(p3);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
                case 4:
                    processList.add(p4);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
                case 5:
                    processList.add(p5);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
                default:
                    processList.add(p1);
                    if(haveChildren % 2 == 0){
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
                    }
                    break;
            }
        }
    }

    private static void setProcessIDs() {
        int pid = 1;
        for (PCB p : processList) {
            p.setPID(pid++);
        }
    }

    private static void readProgramFiles() {
        try {
            // READ PF-1 -------------------------------------------------------
            Scanner fileIn = new Scanner(new File("PF-1.txt"));
            String line;
            int tmp;

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
            p1.setState(State.NEW);

            // READ PF-2 -------------------------------------------------------
            fileIn = new Scanner(new File("PF-2.txt"));

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
            p2.setState(State.NEW);

            // READ PF-3 -------------------------------------------------------
            fileIn = new Scanner(new File("PF-3.txt"));

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
            p3.setState(State.NEW);

            // READ PF-4 -------------------------------------------------------
            fileIn = new Scanner(new File("PF-4.txt"));

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
            p4.setState(State.NEW);

            // READ PF-4 -------------------------------------------------------
            fileIn = new Scanner(new File("PF-5.txt"));

            p5.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p5.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p5.setMemory(tmp);

            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                p5.addToOpList(line);
            }
            p5.setState(State.NEW);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static PCB generateChildProcess(){

        try {
            PCB pC = new PCB();
            // READ PF-C -------------------------------------------------------
            Scanner fileIn = new Scanner(new File("PF-C.txt"));
            
            pC.setToChild();
            pC.setName(fileIn.nextLine().replace("Name: ", ""));
            String line = fileIn.nextLine().replace("Total runtime: ", "");
            Short tmp = Short.parseShort(line);
            pC.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            pC.setMemory(tmp);
            
            fileIn.nextLine();
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                pC.addToOpList(line);
            }
            pC.setState(State.NEW);
            
            return pC;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void printProcessList() {
        System.out.println("ProcessList size: " + processList.size());

        for (PCB pcb : processList) {
            System.out.println(pcb.toString());
        }
    }

    private static void printProcessList(boolean verbose) {
        System.out.println("ProcessList size: " + processList.size());

        for (PCB pcb : processList) {
            System.out.println(pcb.toString());
            if (verbose) {
                for (String s : pcb.getOperationList()) {
                    System.out.println("\t" + s);
                }
            }
        }
    }
}
