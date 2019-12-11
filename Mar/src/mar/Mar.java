/*
 * @author Anthony Caliri
 * Description: Main class for the Mar operating system simulator
 */
package mar;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mar {

    public static ArrayList<PCB> processList = new ArrayList<>();
    public static PriorityQueue<PCB> processQueue = new PriorityQueue<>();

    public static MCU mcu = new MCU(50);

    public static MessageHandler msgHandler = new MessageHandler();
    public static WaitingMessageThreadReceive receiveThread = new WaitingMessageThreadReceive(msgHandler);
    public static WaitingMessageThreadSend sendThread = null;

    public static PCB p1 = new PCB();
    public static PCB p2 = new PCB();
    public static PCB p3 = new PCB();
    public static PCB p4 = new PCB();
    public static PCB p5 = new PCB();

    public static Scanner scan = new Scanner(System.in);

    public static int clock;

    private static UIMessager uiMessager = new UIMessager();

    public static void main(String[] args) {
        new Console(uiMessager);
        int processNum = promptForProcessAmount();
        readProgramFiles();

        generateProcesses(processNum);
        setProcessIDs();

        //Uncomment to see ProcessList
//        printProcessList();
        sortProcessList();

        Long before = System.currentTimeMillis(), sTime, rrTime;
        startProcessingSortest(); // PROCESSING
        System.out.println("\nSHORTEST PROCESS FIRST HAS FINISHED!");
        sTime = System.currentTimeMillis() - before;
        System.out.println("TIME TAKEN: " + sTime);

        for (PCB p : processList) {
            p.setRunning(false);
        }
        System.out.println("RESETING FOR NEXT SCHEDULER");

        before = System.currentTimeMillis();
        System.out.println("\nROUND ROBIN HAS STARTED!");
        startProcessingRR(); // PROCESSING
        System.out.println("\nROUND ROBIN HAS FINISHED!");
        rrTime = System.currentTimeMillis() - before;
        System.out.println("TIME TAKEN: " + rrTime);

        compareSchedulers(sTime, rrTime);

        int processToRun;
        while (true) {
            processToRun = promptToRunSingleProcess();
            if (processToRun == 6) {
                break;
            } else {
                startSingleProcess(processToRun);
            }
        }

        System.out.println("Mar shutting down!");
        System.out.println("Please wait...");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    private static void compareSchedulers(double ss, double rr) {
        // If shortest took longer else round robin took longer
        double per = 0;
        if (ss < rr) {
            System.out.println("\nShortest Process First was faster by: " + (rr - ss) + " milliseconds");
        } else {
            System.out.println("\nRound Robin was faster by: " + (ss - rr) + " milliseconds");
        }

        per = (Math.abs(rr - ss) / ((rr + ss) / 2)) * 100;
        System.out.println("This is a " + round(per, 2) + "% improvement");
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Sort by shortest runtime
    private static void sortProcessList() {
        Collections.sort(processList);
    }

    // After sorting processing of the list is done FCFS 
    private static void startProcessingSortest() {

        long sysTime = System.currentTimeMillis();
        ArrayList<Thread> tList = new ArrayList<>();
        for (PCB pcb : processList) {
            Thread t = new Thread(new ProcessRunnable(pcb, sysTime, mcu, msgHandler));
            t.start();
            tList.add(t);
        }

        for (Thread t : tList) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void startSingleProcess(int in) {
        long sysTime = System.currentTimeMillis();
        PCB pcb = null;
        switch (in) {
            case 1:
                pcb = p1;
                break;
            case 2:
                pcb = p2;
                break;
            case 3:
                pcb = p3;
                break;
            case 4:
                pcb = p4;
                break;
            case 5:
                pcb = p5;
                break;
        }

        Thread t = new Thread(new ProcessRunnable(pcb, sysTime, mcu, msgHandler));
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startProcessingRR() {

        ArrayList<Thread> tList = new ArrayList<>();
        long sysTime = System.currentTimeMillis();
        Iterator elm = processQueue.iterator();
        PCB pcb;
        while (elm.hasNext()) {
            pcb = (PCB) elm.next();
            if (!pcb.isRunning()) {
                ProcessRunnable pr = new ProcessRunnable(pcb, sysTime, mcu, msgHandler);
                Thread t = new Thread(pr);
                t.start();
                tList.add(t);
            } else {

            }
            wait50Clock();
            pcb.setHalted(true);
        }

        for (Thread t : tList) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void wait50Clock() {
        int loopClock = 0;
        Long sysTime = System.currentTimeMillis();
        while (loopClock < 50) {
            if (System.currentTimeMillis() - sysTime >= 5) {
                loopClock++;
                sysTime = System.currentTimeMillis();
            }
        }
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
        int msg = 0;
        while (uiMessager.getMessage() == -1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        msg = uiMessager.getMessage();
        uiMessager.setMessage(-1);
        return msg;
    }

    private static int promptToRunSingleProcess() {
        System.out.println("\nRun a single process?");
        System.out.println("\n\t1. Text Processor (PF-1)");
        System.out.println("\t2. Quantum Leap (PF-2)");
        System.out.println("\t3. Prime Process (PF-3)");
        System.out.println("\t4. Dark Haven (PF-4)");
        System.out.println("\t5. Krimzon Fury (PF-5)");
        System.out.println("\nTo exit enter 6");
        System.out.println("To run a process input its number\n");
        int msg = 0;
        boolean keepLooping = true;
        while (keepLooping) {

            switch (uiMessager.getMessage()) {
                case 1:
                    msg = 1;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
                case 2:
                    msg = 2;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
                case 3:
                    msg = 3;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
                case 4:
                    msg = 4;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
                case 5:
                    msg = 5;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
                case 6:
                    msg = 6;
                    uiMessager.setMessage(-1);
                    keepLooping = false;
                    break;
            }
        }
        return msg;
    }

    // To randomize the creation of children I have used a random number 0 to 100 with the mod operator
    private static void generateProcesses(int processNum) {
        Random rand = new Random();
        int processToAdd, numChildren, haveChildren;
        for (int i = 0; i < processNum; i++) {
            processToAdd = rand.nextInt((5 - 1) + 1) + 1;
            numChildren = rand.nextInt((4 - 1) + 1) + 1;
            haveChildren = rand.nextInt((100 - 1) + 1) + 1;
            switch (processToAdd) {
                case 1:
                    processList.add(p1);
                    processQueue.add(p1);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
                    break;
                case 2:
                    processList.add(p2);
                    processQueue.add(p2);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
                    break;
                case 3:
                    processList.add(p3);
                    processQueue.add(p3);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
                    break;
                case 4:
                    processList.add(p4);
                    processQueue.add(p4);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
                    break;
                case 5:
                    processList.add(p5);
                    processQueue.add(p5);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
                    break;
                default:
                    processList.add(p1);
                    processQueue.add(p1);
//                    if (haveChildren % 2 == 0) {
                    processList.get(processList.size() - 1).createChildren(numChildren, generateChildProcess(), processList.get(processList.size() - 1));
//                    }
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
            p1.setPriority(1);
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
            p2.setPriority(2);
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
            p3.setPriority(3);
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
            p4.setPriority(4);
            p4.setState(State.NEW);

            // READ PF-4 -------------------------------------------------------
            fileIn = new Scanner(new File("PF-5.txt"));

            p5.setName(fileIn.nextLine().replace("Name: ", ""));
            line = fileIn.nextLine().replace("Total runtime: ", "");
            tmp = Short.parseShort(line);
            p5.setTotalRuntime(tmp);
            line = fileIn.nextLine().replace("Memory: ", "");
            tmp = Short.parseShort(line);
            p5.setPriority(5);
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

    private static PCB generateChildProcess() {

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
