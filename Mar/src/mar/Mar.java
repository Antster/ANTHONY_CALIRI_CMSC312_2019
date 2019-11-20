/*
 * @author Anthony Caliri
 * Description: Main class for the Mar operating system simulator
 */
package mar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 *
 *
 */
public class Mar {

    public static ArrayList<PCB> processList = new ArrayList<>();

    public static MCU mcu = new MCU(300);

    public static PCB p1 = new PCB();
    public static PCB p2 = new PCB();
    public static PCB p3 = new PCB();
    public static PCB p4 = new PCB();
    public static PCB p5 = new PCB();

    public static Scanner scan = new Scanner(System.in);

    public static int clock;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int processNum = promptForProcessAmount();
        readProgramFiles();

        generateProcesses(processNum);

        //Uncomment to see ProcessList
//        printProcessList();
        sortProcessList();

        int returnVal = startProcessing();

        switch (returnVal) {
            case 0:
                System.out.println("\nProcessing finished normaly");
                break;
            case -1:
                System.out.println("\nError while processing!");
        }

    }

    // Sort by intest runtime
    private static void sortProcessList() {
        Collections.sort(processList);
    }

    // After sorting processing of the list is done FCFS 
    private static int startProcessing() {
        Semaphore semaphore = new Semaphore(1);
        
        int totalRuntime;
        int loopClock;
        long sysTime = System.currentTimeMillis();
        String op;
        for (PCB pcb : processList) {
            totalRuntime = pcb.getTotalRuntime();
            System.out.println(pcb.getName() + " " + totalRuntime);

            while (!MCU.memHasSpace(pcb.getMemory())); // If main memory does not have space for this process, wait until there is space

            addToMainMemory(pcb);

            for (String s : pcb.getOperationList()) {
                if (pcb.getState() == State.READY) {
                    pcb.setState(State.RUN);
                }
                loopClock = 0;
                if (!s.contains("EXE")) {
                    if (s.contains("<")) {
                        try {
                            semaphore.acquire();
                            continue;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (s.contains(">")) {
                        semaphore.release();
                        continue;
                    }
                    op = s.substring(0, 10).trim();

                    pcb.setCurOperation(op);
                    totalRuntime = Short.parseShort(s.replaceAll("[^\\d]", ""));

                    if (pcb.getCurOperation().equals("I/0")) {
                        pcb.setState(State.WAIT);
                    }

                    while (loopClock < totalRuntime) {
                        if (System.currentTimeMillis() - sysTime >= 5) {
                            loopClock++;
                            sysTime = System.currentTimeMillis();
                        }
                    }
                }
            }
            removeFromMainMemory(pcb);
            pcb.setState(State.EXIT);
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

    private static void generateProcesses(int processNum) {
        Random rand = new Random();
        int processToAdd;
        for (int i = 0; i < processNum; i++) {
            processToAdd = rand.nextInt((5 - 1) + 1) + 1;

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
                    break;
                case 5:
                    processList.add(p5);
                    break;
                default:
                    processList.add(p1);
                    break;
            }
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

    private static boolean addToMainMemory(PCB p) {
        if (MCU.memHasSpace(p.getMemory())) {
            MCU.setMemorySpace(MCU.getMemorySpace() - p.getMemory());
            MCU.addToMemList(p);
            p.setState(State.READY);
        } else {
            return false;
        }
        return true;
    }

    private static boolean removeFromMainMemory(PCB p) {
        try {
            MCU.setMemorySpace(MCU.getMemorySpace() + p.getMemory());
            MCU.removeToMemList(p);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
