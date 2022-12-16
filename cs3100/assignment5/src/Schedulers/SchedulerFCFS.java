import java.util.LinkedList;

public class SchedulerFCFS extends SchedulerBase implements Scheduler {
    Platform platform;
    LinkedList<Process> readyProcesses = new LinkedList<Process>();

    public SchedulerFCFS(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyProcesses.add(p);
    }

    @Override
    public Process update(Process cpu) {
        Process toSchedule = cpu;

        if (cpu == null) {
            return nextProcess();
        }

        if (cpu.isExecutionComplete()) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " execution complete");

            toSchedule = nextProcess();
        } else if (cpu.isBurstComplete()) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " burst complete");

            readyProcesses.add(cpu);
            toSchedule = nextProcess();
        }

        return toSchedule;
    }

    private Process nextProcess() {
        if (readyProcesses.size() > 0) {
            Process next =  readyProcesses.removeFirst();
            platform.log("Scheduled: " + next.getName());
            return next;
        }
        return null;
    }
}
