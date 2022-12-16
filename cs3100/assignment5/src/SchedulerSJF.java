import java.util.LinkedList;

class SchedulerSJF extends SchedulerBase implements Scheduler {
    Platform platform;
    LinkedList<Process> readyProcesses = new LinkedList<Process>();

    public SchedulerSJF(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyProcesses.add(p);
    }

    @Override
    public Process update(Process cpu) {
        if (cpu == null) {
            return scheduleSmallest();
        }

        if (cpu.isBurstComplete()) {
            platform.log("Process " + cpu.getName() + " burst complete");
        }

        if (cpu.isExecutionComplete()) {
            platform.log("Process " + cpu.getName() + " execution complete");
            contextSwitches += 2;
            return scheduleSmallest();
        }

        return cpu;
    }

    private Process scheduleSmallest() {
        Process next = getSmallestProcess();
        readyProcesses.remove(next);
        if (next != null ) {
            platform.log("Scheduled: " + next.getName());
        }
        return next;
    }

    private Process getSmallestProcess() {
        if (readyProcesses.size() == 0) { return null; }

        Process smallest = readyProcesses.getFirst();
        for (Process p : readyProcesses) {
            if (p.getTotalTime() < smallest.getTotalTime()) {
                smallest = p;
            }
        }
        return smallest;
    }
}
