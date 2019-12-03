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
    private int totalRuntime, loopClock;
    private long sysTime;
    private static MCU mcu;

    public ProcessRunnable(PCB p, long sT, MCU mem) {
        this.pcb = p;
        this.sysTime = sT;
        this.mcu = mem;
    }

    @Override
    public void run() {
        boolean memHasSpaceMsg = false; int msgCount = 0;
        Semaphore semaphore = new Semaphore(1);
        String op;
        totalRuntime = pcb.getTotalRuntime();
        System.out.println("STARTED " + pcb.getName() + " " + totalRuntime);

        // If main memory does not have space for this process, wait until there is space
        while (!mcu.memHasSpace(pcb.getMemory())){
            if(!memHasSpaceMsg && msgCount % 50 == 0){
                System.out.println(pcb.getName() + " WAITING FOR SPACE IN MAIN MEMORY!");
                memHasSpaceMsg = true;
            }
        }

        addToMainMemory(pcb);

        for (String s : pcb.getOperationList()) {
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
        removeFromMainMemory(pcb);
        pcb.setState(State.EXIT);
        
        System.out.println("ENDED " + pcb.getName());
    }

    private static boolean addToMainMemory(PCB p) {
        if (mcu.memHasSpace(p.getMemory())) {
            mcu.setMemorySpace(mcu.getMemorySpace() - p.getMemory());
            mcu.addToMemList(p);
            p.setState(State.READY);
        } else {
            return false;
        }
        return true;
    }

    private static boolean removeFromMainMemory(PCB p) {
        try {
            mcu.setMemorySpace(mcu.getMemorySpace() + p.getMemory());
            mcu.removeFromMemList(p);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
