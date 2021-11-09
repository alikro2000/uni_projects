import java.util.ArrayList;
import java.util.List;

public class MemoryBestFit extends Memory {
    /**
     * Constructs an instance of Memory class. Must be overridden by child classes.
     *
     * @param size The size of the memory instance. Cannot be modified.
     */
    public MemoryBestFit(int size) {
        super(size);
    }

    @Override
    public void request(String processID, int processSize) throws Exception {
        super.request(processID, processSize);

        //Try finding the smallest free block of memory that could fit the process
        if (tryBestFitAllocation(processID, processSize)) return;

        //If not found, then run compaction and try again
        super.compactProcesses();
        if (!tryBestFitAllocation(processID, processSize)) {
            //If not found, throw and exception regarding insufficient memory
            throw new Exception("Insufficient memory.");
        }
    }

    private boolean tryBestFitAllocation(String processID, int processSize) {
        //First, find a list of all free blocks of memory & sort it by their sizes
        List<MemoryBlock> freeBlocks = getFreeMemoryBlocks();

        //Then try fitting, but be aware that only a portion of a free block may be enough to hold a process.
        for (MemoryBlock block : freeBlocks) {
            if (block.getSize() >= processSize) {
                this.loadedProcesses.add(new MemoryBlock(processID, block.getStartingLocation(), processSize));
                return true;
            }
        }

        return false;
    }

    private List<MemoryBlock> getFreeMemoryBlocks() {
        List<MemoryBlock> freeBlocks = new ArrayList<MemoryBlock>();

        super.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        int memoryPointer = 0;
        int processIndex = 0;
        MemoryBlock process = this.loadedProcesses.size() > 0 ? this.loadedProcesses.get(0) : null;

        while (memoryPointer < this.getSize()) {
            //If it's the end of it
            if (process == null) {
                if (memoryPointer < this.getSize()) {
                    freeBlocks.add(new MemoryBlock("", memoryPointer, this.getSize() - memoryPointer));
                }
            } else {
                if (memoryPointer < process.getStartingLocation()) {
                    freeBlocks.add(new MemoryBlock("", memoryPointer, process.getStartingLocation() - memoryPointer));
                }
            }

            memoryPointer = process == null ? this.getSize() : process.getStartingLocation() + process.getSize();
            ++processIndex;
            process = this.loadedProcesses.size() > 0 && processIndex < this.loadedProcesses.size()
                    ? this.loadedProcesses.get(processIndex) : null;
        }

        freeBlocks.sort(MemoryBlock::compareBySizeTo);

        return freeBlocks;
    }
}
