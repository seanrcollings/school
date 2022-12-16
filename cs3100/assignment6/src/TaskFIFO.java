import java.util.LinkedList;

public class TaskFIFO implements Runnable {
    int[] sequence;
    int maxMemoryFrames;
    int maxPageReference;
    int[] pageFaults;

    LinkedList<Integer> frames;

    public TaskFIFO(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
        this.frames = new LinkedList<Integer>();
    }

    @Override
    public void run() {
        for (Integer page : this.sequence) {
            if (!this.frames.contains(page)) {
                pageFault();
                if (this.frames.size() == this.maxMemoryFrames) {
                    // Memory is full, we need to remove the first item
                    // FIFO
                    this.frames.removeFirst();
                }

                this.frames.add(page);
            }
        }
    }

    private void pageFault() {
        this.pageFaults[maxMemoryFrames] += 1;
    }
}
