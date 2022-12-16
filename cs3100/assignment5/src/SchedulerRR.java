import java.util.ArrayList;

class SchedulerRR extends SchedulerBase implements Scheduler {
    Platform platform;
    int timeQuantum;
    ArrayList<Process> readyProcesses = new ArrayList<Process>();

    public SchedulerRR(Platform platform, int timeQuantum) {
        this.platform = platform;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyProcesses.add(p);
    }

    @Override
    public Process update(Process cpu) {
        if (cpu == null) {
            return nextProcess();
        }

        Process toSchedule = cpu;

        if (cpu.isExecutionComplete()) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " execution complete");
            toSchedule = nextProcess();
        } else if (cpu.getElapsedTotal() % timeQuantum == 0) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " time quantum complete");
            readyProcesses.add(cpu);
            toSchedule = nextProcess();
        }

        return toSchedule;
    }

    private Process nextProcess() {
        if (readyProcesses.size() > 0) {
            Process next =  readyProcesses.remove(0);
            platform.log("Scheduled: " + next.getName());
            return next;
        }
        return null;
    }
}