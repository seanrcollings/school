import java.util.LinkedList;

public class TaskMRU implements Runnable {
    int[] sequence;
    int maxMemoryFrames;
    int maxPageReference;
    int[] pageFaults;
    LinkedList<Integer> frames;

    public TaskMRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
        this.frames = new LinkedList<Integer>();
    }

    @Override
    public void run() {
        for (int page : this.sequence) {
            if(!this.frames.contains(page)) {
                pageFault();
                if(this.frames.size() == this.maxMemoryFrames) {
                    this.frames.removeLast();
                }

                this.frames.add(page);
            } else {
                this.frames.remove(this.frames.indexOf(page));
                this.frames.add(page);
            }
        }
    }

    private void pageFault() {
        this.pageFaults[maxMemoryFrames] += 1;
    }
}
