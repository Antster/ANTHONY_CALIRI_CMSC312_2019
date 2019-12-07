package mar;

import java.util.ArrayList;

/**
 *
 * @author Anthony Caliri
 */

enum State { NEW, READY, RUN, WAIT, EXIT }
enum Operation { CALCULATE, IO, YEILD, OUT }

public class PCB implements Comparable < PCB >{
    
    private ArrayList<String> operationList;
    protected ArrayList<Page> pageList;
    
    private String name;
    private int totalRuntime;
    private int memory;
    private State state;
    private Operation curOperation;
    private boolean inCritical;
    private int pid;

    public PCB () {
        this.name = "";
        this.totalRuntime = 0;
        this.memory = 0;
        this.state = null;
        this.curOperation = null;
        operationList = new ArrayList<>();
        pageList = new ArrayList<>();
        this.inCritical = false;
        fillPageList();
    }
    
    public PCB(String name, int totalRuntime, byte memory, State state, Operation curOperation) {
        this.name = name;
        this.totalRuntime = totalRuntime;
        this.memory = memory;
        this.state = state;
        this.curOperation = curOperation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalRuntime(int totalRuntime) {
        this.totalRuntime = totalRuntime;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    public void setCritical(boolean b){
        this.inCritical = b;
    }

    public void setCurOperation(String op) {
        switch(op.toLowerCase()){
            case "calculate":
                this.curOperation = Operation.CALCULATE;
                break;
            case "yeild":
                this.curOperation = Operation.YEILD;
                break;
            case "out":
                this.curOperation = Operation.OUT;
                break;
            case "io":
                this.curOperation = Operation.IO;
                break;
        }
    }
    
    public void addToOpList(String s){
        operationList.add(s);
    }
    
    public String getName() {
        return name;
    }

    public int getTotalRuntime() {
        return totalRuntime;
    }

    public int getMemory() {
        return memory;
    }

    public State getState() {
        return state;
    }
    
    public boolean getCritical(){
        return inCritical;
    }

    public Operation getCurOperation() {
        return curOperation;
    }
    
    public ArrayList<String> getOperationList() {
        return operationList;
    }
    
    public int getNumberOfPages(){
        if(operationList.size() % 2 == 0){
            return operationList.size()/2;
        } else {
            return (operationList.size()/2) + 1;
        }    
    }
    
    public void setPID(int id){
        pid = id;
    }
    
    public int getPID(){
        return pid;
    }
    
    public boolean equals(PCB pcb){
        return pcb.getPID() == this.pid;
    }
    
    public void addPageToList(Page page){
        pageList.add(page);
    }
    
    public void fillPageList(){
        int c = 0;
        while (c < getNumberOfPages()){
            pageList.add(new Page(this));
        }
    }
    
    @Override
    public String toString(){
        return "Process Name: " + getName() + " Total Runtime: " + getTotalRuntime() + " Memory: " + getMemory() 
                + " \nCurrent State: " + getState() + " Current Operation: " + getCurOperation();
    }

    @Override
    public int compareTo(PCB o) {
        Integer x = (int) this.getTotalRuntime();
        Integer y = (int) o.getTotalRuntime();
        return x.compareTo(y);
    }
}
