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

/**
 *
 *
 */
public class Mar {

    public static ArrayList<PCB> processList = new ArrayList<>();

    public static List<PCB> MainMemory = new ArrayList<>();

    public static PCB p1 = new PCB();
    public static PCB p2 = new PCB();
    public static PCB p3 = new PCB();
    public static PCB p4 = new PCB();

    public static Scanner scan = new Scanner(System.in);

    public static int clock;
    public static short memorySpace = 300;

    public static boolean isCriticalTaken = false;

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
                System.out.println("Processes finished normaly");
                break;
            case -1:
                System.out.println("Error while processing!");
        }

    }

    // Sort by shortest runtime
    private static void sortProcessList() {
        Collections.sort(processList);
    }

    // After sorting processing of the list is done FCFS 
    private static int startProcessing() {
        short totalRuntime;
        int loopClock;
        long sysTime = System.currentTimeMillis();
        String op;
        for (PCB pcb : processList) {
            totalRuntime = pcb.getTotalRuntime();
            System.out.println(pcb.getName() + " " + totalRuntime);

            while (!memHasSpace(pcb.getMemory())); // If main memory does not have space for this process, wait until there is space

            addToMainMemory(pcb);

            for (String s : pcb.getOperationList()) {
                if (pcb.getState() == State.READY) {
                    pcb.setState(State.RUN);
                }
                loopClock = 0;
                if (!s.contains("EXE")) {
                    if (s.contains("<")) {
                        while (isCriticalTaken) {
                        } // If another process is in critical section wait until it calls release() which will end this loop
                        acquire();
                        continue;
                    } else if (s.contains(">")) {
                        release();
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
            // READ PF-1 -------------------------------------------------------
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

    private static void acquire() {
        isCriticalTaken = true;
    }

    private static void release() {
        isCriticalTaken = false;
    }

    private static boolean memHasSpace(short in) {
        return (memorySpace - in) >= 0;
    }

    private static boolean addToMainMemory(PCB p) {
        if (memHasSpace(p.getMemory())) {
            memorySpace -= p.getMemory();
            MainMemory.add(p);
            p.setState(State.READY);
        } else {
            return false;
        }
        return true;
    }

    private static boolean removeFromMainMemory(PCB p) {
        try {
            memorySpace += p.getMemory();
            MainMemory.remove(p);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
