import java.util.LinkedList;

class SchedulerSRTF extends SchedulerBase implements Scheduler {
    Platform platform;
    LinkedList<Process> readyProcesses = new LinkedList<Process>();

    public SchedulerSRTF(Platform platform) {
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

        if (cpu.isBurstComplete()) {
            platform.log("Process " + cpu.getName() + " burst complete");
            if (!cpu.isExecutionComplete()) {
                readyProcesses.add(cpu);
                contextSwitches += 2;
                toSchedule = nextProcess();
            }
        }

        if (cpu.isExecutionComplete()) {
            contextSwitches += 2;
            platform.log("Process " + cpu.getName() + " execution complete");
            toSchedule = nextProcess();
        } else {
            Process smallest = getSmallest();
            if (getMin(cpu, smallest) != cpu) {
                platform.log("Process " + cpu.getName() + " preeempted");
                contextSwitches += 2;
                readyProcesses.add(cpu);
                toSchedule = nextProcess();
            }
        }

        return toSchedule;
    }


    private Process nextProcess() {
        Process next = getSmallest();
        if (next != null) {
            readyProcesses.remove(next);
            platform.log("Scheduled: " + next.getName());
        }
        return next;
    }

    private Process getSmallest() {
        if (readyProcesses.size() > 0) {
            Process smallest =  readyProcesses.getFirst();
            for (Process p : readyProcesses) {
                smallest = getMin(smallest, p);
            }
            return smallest;
        }
        return null;
    }

    private Process getMin(Process p1, Process p2) {
        if (p1 == null && p2 == null) { return null; }
        if (p1 == null) { return p2; }
        if (p2 == null) { return p1; }
        if (remainingTime(p1) <= remainingTime(p2)) {
            return p1;
        }

        return p2;
    }


    private int remainingTime(Process p) {
        return p.getTotalTime() - p.getElapsedTotal();
    }
}