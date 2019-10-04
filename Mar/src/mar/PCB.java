/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mar;

/**
 *
 * @author tonyt
 */

enum State { NEW, READY, RUN, WAIT, EXIT }
enum Operation { CALCULATE, IO, YEILD, OUT }

public class PCB {
    private String name;
    private short totalRuntime;
    private short memory;
    private State state;
    private Operation curOperation;

    public PCB () {
        this.name = "";
        this.totalRuntime = 0;
        this.memory = 0;
        this.state = null;
        this.curOperation = null;
    }
    
    public PCB(String name, short totalRuntime, byte memory, State state, Operation curOperation) {
        this.name = name;
        this.totalRuntime = totalRuntime;
        this.memory = memory;
        this.state = state;
        this.curOperation = curOperation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalRuntime(short totalRuntime) {
        this.totalRuntime = totalRuntime;
    }

    public void setMemory(byte memory) {
        this.memory = memory;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCurOperation(Operation curOperation) {
        this.curOperation = curOperation;
    }

    public String getName() {
        return name;
    }

    public short getTotalRuntime() {
        return totalRuntime;
    }

    public short getMemory() {
        return memory;
    }

    public State getState() {
        return state;
    }

    public Operation getCurOperation() {
        return curOperation;
    }
    
}
