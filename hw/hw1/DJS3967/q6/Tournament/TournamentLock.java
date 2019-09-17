package q6.Tournament;

import java.util.concurrent.atomic.AtomicInteger;

public class TournamentLock implements Lock {
    private static AtomicInteger[] Level;
    private static AtomicInteger[] Turn;
    private  int numThreads;

    public TournamentLock(int numThreads){
        // your implementation goes here.
        this.numThreads = numThreads;
        Level = new AtomicInteger[numThreads+1];
        Turn = new AtomicInteger[numThreads+1];
        for (int i=0;i<Level.length;i++) {
            Level[i]=new AtomicInteger(0);
            Turn[i]=new AtomicInteger(0);
            //Level[i].set(0);
        }
    }

    @Override
    public void lock(int pid) {
        int nodeID= numThreads/2 + pid/2;
        int k = (int) Math.ceil(Math.log(numThreads));
        int turn = pid%2;
        for(int i = 1; i <= k; i++)
        {
            Level[pid].set(i);
            Turn[nodeID].set(turn);

            int base_process= nodeID;
            while (base_process<numThreads/2)
                base_process=base_process*2;
            base_process = base_process-numThreads/2;

            for(int j=base_process;j<base_process+Math.pow(2,i);j++) {
                while (Turn[nodeID].get() == turn && Level[j].get() > i) {
                }
            }

            nodeID /= 2;
            turn = nodeID%2;
        }


    }

    @Override
    public void unlock(int pid) {
        Level[pid].set(0);//Sayign we dont want this

    }
}
