package q6.Tournament;

import java.util.ArrayList;


public class PIncrement implements Runnable{

    private int increment_by;
    private int pid;
    private static int value;
    private static TournamentLock tournamentLock;

    private PIncrement(int increment_by, int pid){
        this.increment_by = increment_by;
        this.pid = pid;
    }



    public static int parallelIncrement(int c, int numThreads){

        int increment_value = 1200000/numThreads;
        tournamentLock = new TournamentLock(numThreads);

        value =c;
        ArrayList<Thread> threads = new ArrayList<>();
        for(int i=0;i<numThreads;i++){
            if(i==numThreads-1)
                increment_value = increment_value + 1200000-numThreads*increment_value;
            Thread t = new Thread(new q6.Tournament.PIncrement(increment_value, i));//pid is pid 0 to n-1;
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
            tournamentLock.lock(pid);
            value=value+1;
            tournamentLock.unlock(pid);

        }
    }

}

