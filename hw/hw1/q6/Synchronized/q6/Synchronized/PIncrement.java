package q6.Synchronized;

import java.util.ArrayList;
import java.util.List;

public class PIncrement implements Runnable{
    static int count;


    public static int parallelIncrement(int c, int numThreads) throws InterruptedException {

        // your implementation goes here.
        List<Thread> threads= new ArrayList<>();

        for(int i=0;i<numThreads;i++){
            PIncrement t = new PIncrement();
            Thread thread = new Thread(t);
            thread.start();
            threads.add(thread);
        }
        for(int i=0; i<threads.size();i++){
            threads.get(i).join();
            Thread cur= threads.get(i);
        }



        return 0;
    }

    @Override
    public void run() {

    }
    public void add(int value){

        synchronized(this){
            this.count += value;
        }
    }
}
