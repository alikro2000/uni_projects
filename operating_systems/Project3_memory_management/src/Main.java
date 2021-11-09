import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        runMemorySimulation();
      
        //Built-in tests
//        Test1();
//        Test2();
//        Test3();
    }

    private static void runMemorySimulation() {
        String line;
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter default memory size: ");
        int memorySize = Integer.parseInt(sc.nextLine());

        //Initiate 3 distinct memory managers each with a unique algorithm
        Memory[] memories = {
                new MemoryFirstFit(memorySize), //0: First fit memory
                new MemoryBestFit(memorySize),  //1: Best fit memory
                new MemoryNextFit(memorySize),  //2: Next fit memory
        };

        //Read instructions from terminal and process them, then pass them to each of the managers.
        while (!(line = sc.nextLine()).isEmpty()) {
            String[] words = line.trim().split(" ");

            try {
                for (Memory m : memories) {
                    if (words[0].equals("REQUEST")) {
                        m.request(words[1], Integer.parseInt(words[2]));
                    } else if (words[0].equals("RELEASE")) {
                        m.release(words[1]);
                    } else {
                        //Command not defined.
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //When done, acquire each memory manager's status as a String and print it to the terminal...
        StringBuilder fileBuffer = new StringBuilder("");

        for (Memory m : memories) {
            String str = getOverallMemoryStatus(m);
            System.out.println(str);
            fileBuffer.append(str).append("\n");
        }

        //And then save them to a file, 'output.txt'.
//        File file = new File("./output.txt");
        try {
//            file.createNewFile();
            FileWriter writer = new FileWriter("./output.txt");
            writer.write(fileBuffer.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void Test1() {
        System.out.println("Test 1");

        Memory[] memories = {
                new MemoryFirstFit(70), //0: First fit memory
                new MemoryBestFit(70),  //1: Best fit memory
                new MemoryNextFit(70),  //2: Next fit memory
        };

        for (Memory m : memories) {
            try {

                m.request("A", 23);
                m.request("B", 30);
                m.release("A");
                m.request("C", 17);

            } catch (Exception e) {
                e.printStackTrace();
            }

            printMemoryStatus(m);
        }
    }

    private static void Test2() {
        System.out.println("Test 2");

        Memory[] memories = {
                new MemoryFirstFit(23), //0: First fit memory
                new MemoryBestFit(23),  //1: Best fit memory
                new MemoryNextFit(23),  //2: Next fit memory
        };

        // P5, 4, P3, P4, 2

        for (Memory m : memories) {

            try {

                m.request("P1", 8);
                m.request("P2", 7);
                m.release("P1");
                m.request("P3", 10);
                m.release("P2");
                m.request("P4", 4);
                m.request("P5", 3);

            } catch (Exception e) {
                e.printStackTrace();
            }

            printMemoryStatus(m);
        }
    }

    private static void Test3() {
        System.out.println("Test 2");

        Memory[] memories = {
                new MemoryFirstFit(85), //0: First fit memory
                new MemoryBestFit(85),  //1: Best fit memory
                new MemoryNextFit(85),  //2: Next fit memory
        };

        // P5, 4, P3, P4, 2

        for (Memory m : memories) {

            try {

                m.request("CODE1", 70);
                m.request("CODE2", 8);
                m.release("CODE1");
                m.request("CODE3", 75);
                m.release("CODE2");
                m.request("CODE4", 9);
                m.request("CODE5", 1);

            } catch (Exception e) {
                e.printStackTrace();
            }

            printMemoryStatus(m);
        }
    }

    static void printMemoryStatus(Memory memory) {
//        System.out.println(memory.getTotalExternalFragmentationSize());
        System.out.println(getOverallMemoryStatus(memory));
    }

    private static String getOverallMemoryStatus(Memory memory) {
        StringBuilder result = new StringBuilder("");

        String memoryType =
                memory instanceof MemoryFirstFit ? "First Fit" :
                        memory instanceof MemoryBestFit ? "Best Fit" :
                                memory instanceof MemoryNextFit ? "Next Fit" : "NOT DEFINED!";

        result.append(String.format("*** %s ***\n", memoryType));
        result.append(String.format("Compression No: %d\n", memory.getCompactionCount()));
        result.append(String.format("Memory Status: %s\n", memory.getMemoryStatus()));
        result.append(String.format("External Fragmentation blocks count: %d\n", memory.getExternalFragmentationCount()));

        float avg = (float) memory.getTotalExternalFragmentationSize() / memory.getExternalFragmentationCount();
        result.append(String.format("External Fragmentation average size: %f\n", avg));

        return result.toString();
    }
}