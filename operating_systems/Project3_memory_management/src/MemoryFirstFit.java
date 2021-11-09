public class MemoryFirstFit extends Memory {
    public MemoryFirstFit(int size) {
        super(size);
    }

    @Override
    public void request(String processID, int processSize) throws Exception {
        //First check if there is sufficient memory left.
        super.request(processID, processSize);

        //See if you can allocate memory to the process.
        super.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        if (tryFirstFitAllocation(processID, processSize)) {
            return;
        }

        //If not, then compress the already loaded processes & try again.
        super.compactProcesses();

        if (!tryFirstFitAllocation(processID, processSize)) {
            throw new Exception("Insufficient memory.");
        }
    }

    private boolean tryFirstFitAllocation(String processID, int processSize) {
        int memoryPointer = 0;
        int processIndex = 0;
        MemoryBlock process = this.loadedProcesses.size() > 0 ? this.loadedProcesses.get(0) : null;

        while (memoryPointer < this.getSize()) {
            //If you can add it, add it, otherwise, move to the point after the process
            if (process == null) {
                if (memoryPointer + processSize <= this.getSize()) {
                    this.loadedProcesses.add(new MemoryBlock(processID, memoryPointer, processSize));
                    return true;
                }
            } else {
                if (memoryPointer + processSize - 1 <= process.getStartingLocation()) {
                    this.loadedProcesses.add(new MemoryBlock(processID, memoryPointer, processSize));
                    return true;
                }
            }

            memoryPointer = process == null ? this.getSize() : process.getStartingLocation() + process.getSize();
            ++processIndex;
            process = this.loadedProcesses.size() > 0 && processIndex < this.loadedProcesses.size()
                    ? this.loadedProcesses.get(processIndex) : null;
        }

        return false;
    }
}
