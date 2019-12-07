package mar;

/**
 *
 * @author Anthony Caliri
 */
public class Page {
    
    private boolean isTaken;
    private PCB owner;
    private int indexInArray;
    
    
    public Page(){
        this.isTaken = false;
        this.owner = null;
        this.indexInArray = -1;
    }
    
    public Page(int indx){
        this.isTaken = false;
        this.owner = null;
        this.indexInArray = indx;
    }
    
    public Page(PCB pcb){
        this.isTaken = false;
        this.owner = pcb;
        this.indexInArray = -1;
    }
    
    protected void setTaken(boolean bool){
        this.isTaken = bool;
    }
    
    protected boolean getTaken(){
        return this.isTaken;
    }
    
    protected void setOwner(PCB own){
        this.owner = own;
    }
    protected PCB getOwner(){
        return owner;
    }
    
    protected void setIndex(int indx){
        this.indexInArray = indx;
    }
    
    protected int getIndex(){
        return this.indexInArray;
    }
    
    
}
