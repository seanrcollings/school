import java.util.PriorityQueue;
import java.util.Comparator;

public class SchedulerPriority extends SchedulerBase implements Scheduler {
    Platform platform;
    PriorityQueue<Process> pq = new PriorityQueue<Process>(1, new Compare());

    public SchedulerPriority(Platform platform) {
        this.platform = platform;
    }


    @Override
    public void notifyNewProcess(Process p) {
        pq.add(p);
    }



    @Override
    public Process update(Process cpu) {
        Process toSchedule = cpu;

        if (cpu == null) {
            return nextProcess();
        }

        if (cpu.isBurstComplete()) {
            platform.log("Process " + cpu.getName() + " burst complete");
            if (!cpu.isExecutionComplete()) {
                pq.add(cpu);
                contextSwitches += 2;
                toSchedule = nextProcess();
            }
        }

        if (cpu.isExecutionComplete()) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " execution complete");
            toSchedule = nextProcess();
        }

        return toSchedule;
    }

    private Process nextProcess() {
        if (pq.size() > 0) {
            Process next =  pq.remove();
            platform.log("Scheduled: " + next.getName());
            return next;
        }
        return null;
    }



    private static class Compare implements Comparator<Process> {

        @Override
        public int compare(Process p1, Process p2) {
            if (p1.getPriority() >= p2.getPriority()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
