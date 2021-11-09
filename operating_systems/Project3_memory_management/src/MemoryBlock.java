public class MemoryBlock {
    private String assignedTo;
    private int startingLocation;
    private int size;

    public MemoryBlock(String assignedTo, int startingLocation, int size) {
        this.assignedTo = assignedTo;
        this.startingLocation = startingLocation;
        this.size = size;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public int getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(int startingLocation) {
        this.startingLocation = startingLocation;
    }

    public int getSize() {
        return size;
    }

    public int compareBySizeTo(MemoryBlock other) {
        return Integer.compare(this.size, other.getSize());
    }

    public int compareByStartLocationTo(MemoryBlock other) {
        return Integer.compare(this.startingLocation, other.getStartingLocation());
    }
}
