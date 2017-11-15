import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
public class Pi{

    public static long inCircle = 0;

    public static void generate_point(Object lock){
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double x = rng.nextDouble(1);
        double y = rng.nextDouble(1);

        double result = Math.pow(x, 2)+Math.pow(y, 2);
        if (result < 1){
            synchronized(lock){
                inCircle += 1;
            }
        }
    }

    public static void main(String[] args){
        int numThreads = 0;
        long numItr = 0;

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


        final int numT = numThreads;
        final long numI = numItr;

        Thread[] threads = new Thread[numThreads];

        final Object ref = new Object();


        for (int i = 0; i < numThreads; i++){
            threads[i] = new Thread(() -> {
                                        for (int itr = 0; itr < numI/numT; itr++){
                                            ThreadLocalRandom rng = ThreadLocalRandom.current();
                                            double x = rng.nextDouble(1);
                                            double y = rng.nextDouble(1);

                                            double result = Math.pow(x, 2)+Math.pow(y, 2);
                                            if (result < 1){
                                                synchronized(ref){
                                                    inCircle += 1;
                                                }
                                            }
                                        }
                                   });
        }

        try {
           // Start each individual thread to calculate.
           for (Thread t : threads) t.start();
           for (Thread t : threads) t.join();

           System.out.println("Pi is: "+(inCircle/(double)numI)*4);
	   } catch (InterruptedException iex) { }


    }
}
