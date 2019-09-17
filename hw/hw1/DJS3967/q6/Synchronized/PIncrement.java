package q6.Synchronized;

import java.util.ArrayList;

public class PIncrement implements Runnable{

    private int increment_by;
    private static int value;

    private PIncrement(int increment_by){
        this.increment_by = increment_by;
    }



    public static int parallelIncrement(int c, int numThreads){

        int increment_value = 1200000/numThreads;

        value =c;
        ArrayList<Thread> threads = new ArrayList<>();
        for(int i=0;i<numThreads;i++){
            if(i==numThreads-1)
                increment_value = increment_value + 1200000-numThreads*increment_value;
            Thread t = new Thread(new q6.Synchronized.PIncrement(increment_value));
            threads.add(t);
            t.start();
        }
        for(int i=0; i<numThreads;i++) {
            try {
                threads.get(i).join();
            } catch (Exception e) {
            }
        }
        return value;
    }

    @Override
    public void run() {
        for (int i = 0; i < increment_by; i++) {
//            atomicValue.getAndIncrement();
            increment();
        }
    }
    private static synchronized void increment(){
        value =value+1;
    }
}
