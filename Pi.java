import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.*;
import java.util.*;
public class Pi{

    public static AtomicLong inCircle = new AtomicLong(0);

    public static void main(String[] args){
        int numThreads = 0;
        long numItr = 0;


        //parse command line
        try{
            numThreads = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println("The first argument is the number of threads.");
            System.exit(-1);
        }

        try{
            numItr = Long.parseLong(args[1]);
        }catch(Exception e){
            System.out.println("The second argument is the total number of iterations.");
            System.exit(-1);
        }

        //convert input into final for stabby lambda
        final int numT = numThreads;
        final long numI = numItr;

        Thread[] threads = new Thread[numThreads];

        //deprecated with use of AtomicLong
        //final Object ref = new Object();


        for (int i = 0; i < numThreads; i++){
            threads[i] = new Thread(() -> {
                                        //each thread contributes to a portion of iterations
                                        for (int itr = 0; itr < numI/numT; itr++){
                                            ThreadLocalRandom rng = ThreadLocalRandom.current();
                                            double x = rng.nextDouble(1);
                                            double y = rng.nextDouble(1);

                                            double result = Math.pow(x, 2)+Math.pow(y, 2);
                                            if (result < 1){ //point is inside the circle
                                                //synchronized(ref){ //deprecated because atomic
                                                inCircle.getAndIncrement(); //atomic increment
                                                //}
                                            }
                                        }
                                   });
        }

        try {
           // Start each individual threads.
           for (Thread t : threads) t.start();

           // wait for each thread to finish before report
           for (Thread t : threads) t.join();

           // report
           long inC = inCircle.get(); //conversion from AtomicLong to normal
           System.out.println("Total\t= "+numI);
           System.out.println("Inside\t= "+inC);
           System.out.println("Ratio\t= "+(inC/(double)numI));
           System.out.println("Pi is: "+(inC/(double)numI)*4);
	   } catch (InterruptedException iex) { }


    }
}
