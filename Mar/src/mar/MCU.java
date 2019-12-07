package mar;

/**
 *
 * @author Anthony Caliri
 */
public class MCU {

    private int freePages;
    
    private Page[] pageArray;
    
    public MCU(int memSpace) {
        this.freePages = memSpace;
        initPageArray(memSpace);
    }
    
    private void initPageArray(int memSpace){
        pageArray = new Page[memSpace];
        
        Page page;
        
        for(int i = 0; i < pageArray.length-1; i++){
            page = new Page(i);
            pageArray[i] = page;
        }
    }
    
    protected int getFreePages() {
        return freePages;
    }
    
    protected void addPage(Page page){
        for(int i = 0; i < pageArray.length-1; i++){
            if(!pageArray[i].getTaken()){
                page.setIndex(i);
                pageArray[i] = page;
                this.freePages--;
            }
        }
    }
    
    protected void removePage(Page page){
        for(int i = 0; i < pageArray.length-1; i++){
            if(pageArray[i].getIndex() == page.getIndex()){
                pageArray[i].setOwner(null);
                pageArray[i].setTaken(false);
                this.freePages++;
            }
        }
    }
    
    protected void removeAllProcessPages(PCB pcb){
        for(Page p : pageArray){
            if(p.getOwner().equals(pcb)){
                removePage(p);
            }
        }
    }
}
