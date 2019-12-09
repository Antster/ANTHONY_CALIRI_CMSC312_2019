package mar;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anthony Caliri
 */
public class ProcessRunnable implements Runnable {

    private PCB pcb;
    private int totalRuntime, loopClock, numPages;
    private long sysTime;
    private static MCU mcu;
    private static MessageHandler msgHandler;

    public ProcessRunnable(PCB p, long sT, MCU mem, MessageHandler msgH) {
        this.pcb = p;
        this.numPages = this.pcb.getNumberOfPages();
        this.sysTime = sT;
        this.mcu = mem;
        this.msgHandler = msgH;
    }

    @Override
    public void run() {
        pcb.setRunning(true);
        if (pcb.hasChildren()) {
            long sysTime = System.currentTimeMillis();
            for (PCB pcb : pcb.getChildrenList()) {
                (new Thread(new ProcessRunnable(pcb, sysTime, mcu, this.msgHandler))).start();
            }
            // Wait for children to finish before running the rest of the process
            while (!pcb.haveChildrenFinished());

            pcb.setChildrenComplete(true);
        }

        boolean memHasSpaceMsg = false;
        int msgCount = 0;
        Semaphore semaphore = new Semaphore(1);
        String op;
        totalRuntime = pcb.getTotalRuntime();
        if (pcb.isChild()) {
            System.out.println("\tSTARTED CHILD PROCESS --- PARENT: " + pcb.getParent().getName());
        } else {
            if (pcb.hasChildren()) {
                System.out.println("STARTED " + pcb.getName() + " " + totalRuntime + " HAS " + pcb.getChildrenList().size() + " CHILDREN");
            } else {
                System.out.println("STARTED " + pcb.getName() + " " + totalRuntime);
            }

        }

        // If main memory does not have space for this process, wait until there is space
        while (numPages > mcu.getFreePages()) {
            if (!memHasSpaceMsg && msgCount % 50 == 0) {
                System.out.println(pcb.getName() + " WAITING FOR PAGES TO OPEN IN MAIN MEMORY!");
                memHasSpaceMsg = true;
            }
        }

        addToMainMemory(pcb);

        for (String s : pcb.getOperationList()) {
            if (!pcb.isHalted()) {
                if (pcb.getState() == State.READY) {
                    pcb.setState(State.RUN);
                }
                loopClock = 0;
                if (!s.contains("EXE")) {
                    if (s.contains("<")) {
                        try {
//                        System.out.println(pcb.getName() + " BEGIN CRITICAL");
                            semaphore.acquire();
                            continue;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Mar.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (s.contains(">")) {
//                    System.out.println(pcb.getName() + " END CRITICAL");
                        semaphore.release();
                        continue;
                    }
                    op = s.substring(0, 10).trim();

                    pcb.setCurOperation(op);
                    totalRuntime = Short.parseShort(s.replaceAll("[^\\d]", ""));
//                System.out.println(pcb.getName() + " | " + pcb.getCurOperation());
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

        }
        removeFromMainMemory(pcb);
        pcb.setFinished(true);
        pcb.setState(State.EXIT);

        if (pcb.isChild()) {
            System.out.println("\tENDED CHILD PROCESS. ALERTING PARENT: " + pcb.getParent().getName());

        } else {
            System.out.println("ENDED " + pcb.getName());
        }
    }

    private static boolean addToMainMemory(PCB p) {
        if (p.getNumberOfPages() <= mcu.getFreePages()) {
            for (Page page : p.pageList) {
                mcu.addPage(page);
            }
            p.setState(State.READY);
        } else {
            return false;
        }
        return true;
    }

    private static boolean removeFromMainMemory(PCB p) {
        try {
            mcu.removeAllProcessPages(p);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
