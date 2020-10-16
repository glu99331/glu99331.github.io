import java.util.*;
import java.io.*;
import java.util.Map.*;
/************************************************************************************
* =================== CS 0449 Recitation: LRU Memory Simulator ==================== *
* --------------------------------------------------------------------------------- *
* Author: Gordon Lu														            *						                           
* Term: Fall 2020, Dr.Petrucci's Systems Software Class		                        *
* ----------------------------------------------------------------------------------*
************************************************************************************/
@SuppressWarnings("unchecked")
public class lru_sim 
{
    static int page_faults_lru = 0;
    // java lru_sim –n <numframes> <tracefile>
    public static void main(String[] args) throws Exception
    {
        /*
        Implement a Page Table for a 32-bit address space, all pages will be 4KB in size.
        Number of frames will be a parameter to the execution of your program.

        Least Recently Used (LRU)– Simulate least recently used, whereby you will track when pages
        were last accessed and evict the least recently used page.

        The program will then run through the memory references of the file and decide the action taken for
        each address (hit, page fault – no eviction, page fault – evict clean, page fault – evict dirty).
        */
        int numframes = 0;
        String tracefile = "";
        if(args.length == 3 && args[0].equals("-n"))
        {
            numframes = Integer.parseInt(args[1]);
            tracefile = args[2]; 
        }
        else
        {
            System.out.println("Error! Please enter the correct command line arguments!");
        }
        //simulate_lru(tracefile, numframes);
        generate_lrusim_results();  
        compare_results();
    }

    /****************************************************************************
    *===============Least Recently Used Page Replacement Algorithm:=============*
    *****************************************************************************
    * Algorithm: Evict the least recently used page.                            *
    * - Throw out page that has been unused for longest time                    *
    * - Must keep a linked list of pages                                        *
    *   a) Most recently used at front, least at rear                           *
    *   b) Update this list every memory reference!                             *
    *     - This can be somewhat slow: hardware has to update a linked list     *
    *     on every reference!                                                   *
    * - Alternatively, keep a counter in each Page Table Entry:                 *
    *   a) Global counter increments with each CPU cycle                        *
    *   b) Copy global counter to PTE counter on a reference to the page        *
    *   c) For page replacement, evict the page with the lowest counter value   *
    ****************************************************************************/
    static void simulate_lru(String tracefile, int numframes) throws Exception
    {
        //Track the total number of page faults encountered:
        int num_page_faults = 0; 
        //Track the total number of memory accesses: check the reference bit of each PTE we want to evict...
        int num_memory_accesses = 0;
        //Track the total number of writes to disk: check the dirty bit of each PTE we want to evict..
        int num_writes_to_disk = 0;
        //Calculate the page table size: first 5 hex digits contain the address, the rest is just the offset
        int page_table_size = (int)Math.pow(2, 20);
        //Create a corresponding TreeMap to represent  the Page Table has:
        //Key-Value pair will be the pageNumbers and the corresponding entry!
        LinkedHashMap<String, PTE> pageTable = new LinkedHashMap<String, PTE>();
        //To read from the trace files:
        BufferedReader br = new BufferedReader(new FileReader(tracefile));
        //Record where the minimum PTE based on memory references is located!
        int global_references = 0; //Global references, each page will receive this updated global reference upon each memory reference!
        //Read from trace file:
        while(br.ready())
        {
        //Increment the number of memory accesses:
        num_memory_accesses++;
        //Extract the current line:
        String line = br.readLine();
        //Split the lines, delimited by spaces:
        String[] pageReference_array = line.split(" ");
        //Now assign each of the parts of the memory reference:
        //Access mode will be a char:
        char accessType = pageReference_array[0].charAt(0);
        String pageNum = pageReference_array[1].substring(0, 7);
        //Fetch the corresponding page table entry from the page number:
        if(pageTable.containsKey(pageNum))
        {
            PTE current = pageTable.get(pageNum);
            //Update reference bit
            current.set_referenced_bit(1);
            global_references++;
            //Set dirty bit depending on the instruction:
            if(accessType == 's') current.set_dirty_bit(1);
            
            //If it's already in our page table:
            //Increment the number of references:
            current.set_num_references(global_references);
            //Put it back into the table:
            pageTable.put(pageNum, current);
        
        }
        else
        {
            //increment page faults
            num_page_faults++;
            //If we don't need to evict a frame:
            if(pageTable.size() < numframes)
            {
                PTE newEntry = new PTE();
                if(accessType == 's') newEntry.set_dirty_bit(1);
                //Update reference bit
                //newEntry.set_referenced_bit(1);
                newEntry.set_valid_bit(1); //We're about to put a page into our pageTable, so change the valid bit!
                //Increment the number of references:
                global_references++;
                newEntry.set_num_references(global_references);
                //Put it back into the table:
                pageTable.put(pageNum, newEntry);
            }
            else
            {
            //Get the PTE we want to insert:
            PTE current = new PTE();
            //Increment the number of references:
            global_references++;
            current.set_num_references(global_references);    
            if(accessType == 's') current.set_dirty_bit(1);
            //We need to evict someone:          
            //Temporary map to sort elements based on the global references with each page!
            //Map<String, PTE> lruMap = lruComparator(pageTable);
            IndexFibonacciMinPQ<Map.Entry<String, PTE>> minMapper = new IndexFibonacciMinPQ<Map.Entry<String,PTE>>(pageTable.size());
            //Have pageTable take on all Key, Value Pairs from this map!
            int counter = 0;
            for(Map.Entry<String, PTE> entry : pageTable.entrySet()){
                minMapper.insert(counter++, entry);
            }
            //pageTable = new LinkedHashMap<String, PTE>(lruMap);
            
            //Evict the page at the end of the map: this will be our LRU page!
            //Map.Entry<String, PTE> eviction_page = (Map.Entry<String, PTE>)pageTable.entrySet().toArray()[pageTable.size() - 1];
            //Get the address associated with this page:
            String page_number_to_evict = minMapper.minKey().getKey();
            //String page_number_to_evict = eviction_page.getKey();
            // PTE page_to_evict = pageTable.get(page_number_to_evict);
            
            //Check if the evicted page has been modified:
            if(pageTable.get(page_number_to_evict).get_dirty_bit() == 1)
            {
                num_writes_to_disk++;
            }
            //Remove the evicted page:
            pageTable.remove(page_number_to_evict);
            //Add the new entry into the linked hash map:
            pageTable.put(pageNum, current);
            
            }
        }
            
        }
        page_faults_lru = num_page_faults;
        //printSummaryStatistics("LRU", numframes, num_memory_accesses, num_page_faults, num_writes_to_disk);
    }

    //Display Summary Statistics:
    static void printSummaryStatistics(String algorithm, int numFrames, int num_memory_accesses, int num_page_faults, int num_writes_to_disk)
    {
        //Format the Strings:
        String formattedAlgorithm = String.format("||\tAlgorithm: %-24s%s\n", algorithm, "||");
        String formattedFrames = String.format("||\tNumber of frames:\t%-11d%s\n", numFrames, "||");
        String formattedMemoryAccesses = String.format("||\tTotal memory accesses:\t%-11d%s\n", num_memory_accesses, "||");
        String formattedPageFaults = String.format("||\tTotal page faults:\t%-11d%s\n", num_page_faults, "||");
        String formattedWrites = String.format("||\tTotal writes to disk:\t%-11d%s\n", num_writes_to_disk, "||");
        //Now print them out, aesthetically!
        System.out.println("<===========Summary====Statistics===========>");
        System.out.print(formattedAlgorithm);
        System.out.print(formattedFrames);
        System.out.print(formattedMemoryAccesses);
        System.out.print(formattedPageFaults);
        System.out.print(formattedWrites);
        System.out.println("<===========================================>");
    }
    private static void generate_lrusim_results() throws Exception
    {
        //Simulate algorithms for report: performance gets slower by like 10 seconds 
        generate_lrusim_results("gcc.trace");
        generate_lrusim_results("gzip.trace");
        generate_lrusim_results("swim.trace");

    }

    private static void generate_lrusim_results(String traceFile) throws Exception
    { 

        String new_traceFile = traceFile.replace(".trace","");
        PrintWriter pw = new PrintWriter(new_traceFile + "_results.txt");
        // pw.print(opt_results.toString());
        

        StringBuilder lru_results = new StringBuilder();
        simulate_lru(traceFile, 8);
        lru_results.append(page_faults_lru + " ");
        simulate_lru(traceFile, 16);
        lru_results.append(page_faults_lru + " ");
        simulate_lru(traceFile, 32);
        lru_results.append(page_faults_lru + " ");
        simulate_lru(traceFile, 64);
        lru_results.append(page_faults_lru + "\n");

        pw.print(lru_results.toString());
        pw.close();


    }

    private static void compare_results() throws Exception
    {
        //gcc.trace, swim.trace, gzip.trace
        
        String actual_trace = "gcc_results_actual.txt";
        String sim_trace = "gcc_results.txt";
        long[] actual;
        long[] sim;
        if((new File(actual_trace).exists() && !(new File(actual_trace).isDirectory())) && (new File(sim_trace).exists() && !(new File(sim_trace).isDirectory())))
        {
            actual = fileAsArray(actual_trace);
            sim = fileAsArray(sim_trace);
    
            if(Arrays.equals(actual, sim))
            {
                System.out.println("PASSED gcc.trace");
            }
        }
        else
        {
            System.out.println("SIMULATION NOT RUN YET");
        }
        

        actual_trace = "gzip_results_actual.txt";
        sim_trace = "gzip_results.txt";
        if((new File(actual_trace).exists() && !(new File(actual_trace).isDirectory())) && (new File(sim_trace).exists() && !(new File(sim_trace).isDirectory())))
        {

            actual = fileAsArray(actual_trace);
            sim = fileAsArray(sim_trace);

            if(Arrays.equals(actual, sim))
            {
                System.out.println("PASSED gzip.trace");
            }
        }
        else
        {
            System.out.println("SIMULATION NOT RUN YET");
        }
        actual_trace = "swim_results_actual.txt";
        sim_trace = "swim_results.txt";
        if((new File(actual_trace).exists() && !(new File(actual_trace).isDirectory())) && (new File(sim_trace).exists() && !(new File(sim_trace).isDirectory())))
        {

            actual = fileAsArray(actual_trace);
            sim = fileAsArray(sim_trace);

            if(Arrays.equals(actual, sim))
            {
                System.out.println("PASSED swim.trace");
            }
        }
        else
        {
            System.out.println("SIMULATION NOT RUN YET");
        }
    }
    private static long[] fileAsArray(String traceFile) throws Exception
    {
        ArrayList<Long> res = new ArrayList<Long>();
        BufferedReader br = new BufferedReader(new FileReader(traceFile));
        while(br.ready())
        {
            String line = br.readLine();
            String[] splittr = line.trim().split("\\s+");
            for(int i = 0; i < splittr.length; i++)
            {
                res.add(Long.parseLong(splittr[i]));
            }
        }
        return toPrimitive(res.toArray());
    }
    private static long[] toPrimitive(Object[] arr)
    {
        long[] primitives = new long[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            Long tmp = (Long)arr[i];
            primitives[i] = tmp.longValue();
        }

        return primitives;
    }
}


//Object to represent a single page table entry (PTE)
class PTE implements Comparable<PTE>
{
    //Each entry in the page table contains...
    //Valid bit: set if this logical page number has a corresponding physical frame in memory
    // - If not valid, remainder of PTE is irrelevant
    int valid_bit; 
    //Page frame number: page in physical memory
    int page_frame_number;
    //Referenced bit: set if data on the page has been accessed
    int referenced_bit;
    //Dirty (modified) bit: set if data on the page has been modified
    int dirty_bit;
    //Protection information
    int protection_bits;
    //Add another field for LRU: Keep counter in each page table entry
    //This counter will be incremented each time the PTE is referenced!
    int num_memory_references;
    //Default Constructor: When we create a PTE, initialize to default values:
    public PTE()
    {
        valid_bit = 0; //0 means that this is page number does not have a corresponding frame in memory
        referenced_bit = 0; //0 means not accessed yet
        dirty_bit = 0; //0 means not modified yet
        page_frame_number = -1; //0 actual indicates some value, but -1 doesn't...
        protection_bits = -1; //0 actual indicates some value, but -1 doesn't...
        num_memory_references = 0; //0 indicates that this memory has not been referenced yet
        
    }
    //Override the compareTo function from the Comparable interface:
    @Override
    public int compareTo(PTE other)
    {
        return (this.get_memory_references() - other.get_memory_references());
    }
    //Accessors:
    public int get_valid_bit()
    {
        return valid_bit;
    }
    public int get_page_frame_number()
    {
        return page_frame_number;
    }
    public int get_referenced_bit()
    {
        return referenced_bit;
    }
    public int get_dirty_bit()
    {
        return dirty_bit;
    }
    public int get_protection_bits()
    {
        return protection_bits;
    }
    public int get_memory_references()
    {
        return num_memory_references;
    }
    //Mutators
    public void set_valid_bit(int valid_bit)
    {
        this.valid_bit = valid_bit;
    }
    public void set_page_frame_number(int page_frame_number)
    {
        this.page_frame_number = page_frame_number;
    }
    public void set_referenced_bit(int referenced_bit)
    { 
        this.referenced_bit = referenced_bit;
    }
    public void set_dirty_bit(int dirty_bit)
    {
        this.dirty_bit = dirty_bit;
    }
    public void set_protection_bits(int protection_bits)
    {
        this.protection_bits = protection_bits;
    }
    public void set_num_references(int num_memory_references)
    {
        this.num_memory_references = num_memory_references;
    }
    public void printContents()
    {
        System.out.println("-------------Page Contents---------");
        System.out.println("Valid Bit: " + valid_bit);
        System.out.println("Referenced Bit: " + referenced_bit);
        System.out.println("Dirty Bit: " + dirty_bit);
        System.out.println("Page Frame Number: " + page_frame_number);
        System.out.println("Memory References: " + num_memory_references);
        System.out.println("-----------------------------------");
    }
}
