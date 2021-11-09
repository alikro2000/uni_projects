public class MemoryNextFit extends Memory {
    private int lastAllocationIndex;

    /**
     * Constructs an instance of Memory class. Must be overridden by child classes.
     *
     * @param size The size of the memory instance. Cannot be modified.
     */
    public MemoryNextFit(int size) {
        super(size);
        this.lastAllocationIndex = 0;
    }

    @Override
    public void request(String processID, int processSize) throws Exception {
        super.request(processID, processSize);

        super.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        //Set the pointer to last index and try to fit the process until the pointer reaches the end
        if (tryNextFitAllocation(processID, processSize)) {
            return;
        }

        //If the space wasn't allocated to the process, then compact the processes and try again
        this.compactProcesses();

        //If not successful again, then throw an exception for insufficient memory
        if (!tryNextFitAllocation(processID, processSize)) {
            throw new Exception("Insufficient memory.");
        }
    }

    private boolean tryNextFitAllocation(String processID, int processSize) {
        int memoryPointer = this.lastAllocationIndex;
        int processIndex = 0;
        MemoryBlock process = this.loadedProcesses.size() > 0 ? this.loadedProcesses.get(0) : null;

        boolean flagHasCircled = false;

        while ((!flagHasCircled && memoryPointer < this.getSize()) ||
                (flagHasCircled && memoryPointer < this.lastAllocationIndex)) {
            if (process == null) {
                if (memoryPointer + processSize <= this.getSize()) {
                    this.loadedProcesses.add(new MemoryBlock(processID, memoryPointer, processSize));
                    lastAllocationIndex = memoryPointer;
                    return true;
                }
            } else {
                if (memoryPointer + processSize <= process.getStartingLocation()) {
                    this.loadedProcesses.add(new MemoryBlock(processID, memoryPointer, processSize));
                    lastAllocationIndex = memoryPointer;
                    return true;
                }
            }

            memoryPointer = process != null ? process.getStartingLocation() + process.getSize() : this.getSize();
            if (memoryPointer >= this.getSize() && !flagHasCircled) {
                memoryPointer = 0;
                flagHasCircled = true;
                processIndex = 0;
            } else {
                ++processIndex;
            }
            process = this.loadedProcesses.size() > 0 && processIndex < this.loadedProcesses.size()
                    ? this.loadedProcesses.get(processIndex) : null;
        }

        return false;
    }

    @Override
    public void compactProcesses() {
        super.compactProcesses();
        lastAllocationIndex = super.getTotalAllocatedSize();
    }
}
