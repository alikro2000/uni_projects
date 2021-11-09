import java.util.ArrayList;
import java.util.List;

public abstract class Memory {
    /**
     * The size of the memory. Cannot be modified.
     */
    private final int size;
    /**
     * The processes currently loaded on the memory.
     */
    protected List<MemoryBlock> loadedProcesses;
    /**
     * The number of times the memory compacted its processes.
     */
    private int compactionCount;

    /**
     * Constructs an instance of Memory class. Must be overridden by child classes.
     *
     * @param size The size of the memory instance. Cannot be modified.
     */
    public Memory(int size) {
        this.size = size;
        this.loadedProcesses = new ArrayList<MemoryBlock>();
        this.compactionCount = 0;
    }

    /**
     * The size of the memory.
     *
     * @return An integer respectively.
     */
    public int getSize() {
        return size;
    }

    /**
     * The processes currently loaded on the memory.
     *
     * @return A list of processes respectively.
     */
    public List<MemoryBlock> getLoadedProcesses() {
        return loadedProcesses;
    }

    /**
     * The number of times the memory compacted its processes.
     *
     * @return An integer respectively.
     */
    public int getCompactionCount() {
        return compactionCount;
    }

    /**
     * Allocates memory to a process.
     *
     * @param processID   ID of the process to be added to memory.
     * @param processSize size of the process to be added to memory.
     * @throws Exception If there isn't enough memory.
     */
    public void request(String processID, int processSize) throws Exception {
        //First check if it already exists
        if (getTotalAllocatedSize() + processSize > this.size) {
            throw new Exception("Insufficient Memory!");
        }

        //The rest must be overridden by child classes.
        //Allocation must start from 0.
    }

    /**
     * Removes a process from the memory by its processID accordingly.
     *
     * @param processID The id of the process to be removed from the memory.
     */
    public void release(String processID) {
        for (int i = 0; i < loadedProcesses.size(); ++i) {
            if (loadedProcesses.get(i).getAssignedTo().equals(processID)) {
                loadedProcesses.remove(i);
                break;
            }
        }
    }

    /**
     * Eliminates all free space between processes on the memory by moving all processes to
     * the starting side of the memory, leaving only one block of free memory.
     */
    public void compactProcesses() {
        this.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);
        int firstFreeLocation = 0;

        for (MemoryBlock p : this.loadedProcesses) {
            if (firstFreeLocation < p.getStartingLocation()) {
                p.setStartingLocation(firstFreeLocation);
            }
            firstFreeLocation += p.getSize();
        }

        ++this.compactionCount;
    }

    /**
     * Total amount of memory that is currently allocated to other processes.
     *
     * @return An integer respectively.
     */
    public int getTotalAllocatedSize() {
        return loadedProcesses.stream().mapToInt(MemoryBlock::getSize).sum();
    }

    /**
     * The number of external fragmentation blocks on the memory.
     *
     * @return An integer respectively.
     */
    public int getExternalFragmentationCount() {
        int fragmentationCount = 0;
        int memoryPointer = 0;

        this.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        for (MemoryBlock p : this.loadedProcesses) {
            if (memoryPointer < p.getStartingLocation()) {
                ++fragmentationCount;
            }
            memoryPointer = p.getStartingLocation() + p.getSize();
        }

//        fragmentationCount += memoryPointer < this.size ? 1 : 0;

        return fragmentationCount;
    }

    /**
     * The total size of all external fragmentation blocks on the memory.
     *
     * @return An integer respectively.
     */
    public int getTotalExternalFragmentationSize() {
        int fragmentationSize = 0;
        int memoryPointer = 0;

        this.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        for (MemoryBlock p : this.loadedProcesses) {
            if (memoryPointer < p.getStartingLocation()) {
                fragmentationSize += p.getStartingLocation() - memoryPointer;
            }

            memoryPointer = p.getStartingLocation() + p.getSize();
        }

//        fragmentationSize += this.size - memoryPointer;

        return fragmentationSize;
    }

    /**
     * A string containing the memory's status indicating an order of free spaces and processes in which the memory is
     * distributed.
     *
     * @return A string respectively.
     */
    public String getMemoryStatus() {
        StringBuilder result = new StringBuilder("");

        this.loadedProcesses.sort(MemoryBlock::compareByStartLocationTo);

        int memoryPointer = 0;

        for (MemoryBlock p : this.loadedProcesses) {
            if (memoryPointer < p.getStartingLocation()) {
                result.append(String.format("%d, ", p.getStartingLocation() - memoryPointer));
            }
            result.append(String.format("%s, ", p.getAssignedTo()));
            memoryPointer = p.getStartingLocation() + p.getSize();
        }

        result.append(memoryPointer < this.size ? this.size - memoryPointer : "");

        return result.toString();
    }
}
