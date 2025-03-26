import java.util.ArrayList;
import java.util.concurrent.Semaphore; // Importing the Semaphore package to the file.

/* ASSIGNMENT1 CLASS */
class Assignment1 {

    // Simulation Initialisation.
    private static int NUM_MACHINES = 50; // Number of Machines in the system that issue print requests.
    private static int NUM_PRINTERS = 5; // Number of Printers in the system that print requests.
    private static int SIMULATION_TIME = 30;
    private static int MAX_PRINTER_SLEEP = 3;
    private static int MAX_MACHINE_SLEEP = 5;
    private static boolean sim_active = true;
    private static Semaphore machine_Semaphore; // The 5 available slots for printing documents in the machine.
    private static Semaphore printer_Semaphore; // The amount of jobs being executed.

    // Create an empty list of print requests.
    printList list = new printList();

    public void startSimulation() {

        // ArrayList to keep for Machine and Printer threads.
        ArrayList<Thread> mThreads = new ArrayList<Thread>();
        ArrayList<Thread> pThreads = new ArrayList<Thread>();

        // Creates the Machine and Printer Semaphores.
        machine_Semaphore = new Semaphore(5);
        printer_Semaphore = new Semaphore(0); // Currently 0 as there are no jobs being executed as of the moment.

        // Create and start the Machine and Printer threads.
        // Write code here:
        // Creates Machine and Printer threads, adds them to their corresponding array lists and starts them.
        for (int i = 0; i < NUM_MACHINES; i++) {
            Thread machine = new machineThread(i);
            mThreads.add(machine);
            machine.start();
        }

        for (int i = 0; i < NUM_PRINTERS; i++) {
            Thread printer = new printerThread(i);
            pThreads.add(printer);
            printer.start();
        }

        // Let the simulation run for some time.
        sleep(SIMULATION_TIME);

        // Finish simulation.
        sim_active = false;

        // Wait until all Printer threads finish by using the join function.
        // Write code here:
        for (Thread machine : mThreads) {
            try {
                machine.join();
                // To ensure that the Machine threads joined successfully.
                //System.out.println("Machine thread successful.");
            } catch (InterruptedException e) {
                System.out.println("Machine thread interrupted.");
            }
        }

        for (Thread printer : pThreads) {
            try {
                printer.join();
                // To ensure that the Printer threads joined successfully.
                //System.out.println("Printer thread successful.");
            } catch (InterruptedException e) {
                System.out.println("Printer thread interrupted.");
            }
        }
    }

    /* PRINTER CLASS */
    public class printerThread extends Thread {
        private int printerID;

        public printerThread(int id) {
            printerID = id;
        }

        public void run() {
            while (sim_active) {
                // Using a try and catch statement to catch any errors that might happen while the printer class is running.
                try {
                    // Waits for a permit to print the document and acquires the lock.
                    printer_Semaphore.acquire();

                    // Simulate printer taking some time to print the document.
                    printerSleep();
                    // Grab the request at the head of the queue and print it.
                    // Write code here:
                    printList.queuePrint(list, printerID);

                    // Releases a permit from the Machine Semaphores indicating that there is a printer available.
                    machine_Semaphore.release();
                } catch (InterruptedException ex) {
                    System.out.print("System error: " + ex);
                }
            }
        }

        public void printerSleep() {
            int sleepSeconds = 1 + (int) (Math.random() * MAX_PRINTER_SLEEP);

            // sleep(sleepSeconds*1000);
            
            try {
                sleep(sleepSeconds * 1000);
            } catch (InterruptedException ex) {
                System.out.println("Sleep Interrupted");
            }
        }

        public void printDox(int printerID) {
            System.out.println("Printer ID:" + printerID + " : now available");
            // Print from the queue.
            list.queuePrint(list, printerID);
        }

    }

    /* MACHINE CLASS */
    public class machineThread extends Thread {
        private int machineID;

        public machineThread(int id) {
            machineID = id;
        }

        public void run() {
            while (sim_active) {
                // Using a try and catch statement to catch any errors that might happen while the machine class is running.
                try {
                    // Waits for a permit to print the document and acquires a lock so the process is uninterrupted.
                    machine_Semaphore.acquire();

                    // Machine sleeps for a random amount of time.
                    machineSleep();
                    // Machine wakes up and sends a print request.
                    // Write code here:
                    printRequest(machineID);

                    // Releases a permit from the Printer Semaphores letting it know the document is finished printing.
                    printer_Semaphore.release();
                } catch (InterruptedException ex) {
                    System.out.print("System error: " + ex);
                }
            }
        }

        // Machine sleeps for a random amount of time.
        public void machineSleep() {
            int sleepSeconds = 1 + (int) (Math.random() * MAX_MACHINE_SLEEP);

            try {
                sleep(sleepSeconds * 1000);
            } catch (InterruptedException ex) {
                System.out.println("Sleep Interrupted");
            }
        }

        public void printRequest(int id) {
            System.out.println("Machine " + id + ". Sent a print request");
            // Build a print document.
            printDoc doc = new printDoc("My name is machine " + id, id);
            // Insert it in print queue.
            list = list.queueInsert(list, doc);
        }
    }

    private static void sleep(int s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException ex) {
            System.out.println("Sleep Interrupted");
        }
    }
}