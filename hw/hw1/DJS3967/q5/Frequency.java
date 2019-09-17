package q5;

import java.util.concurrent.*;

public class Frequency implements Callable<Integer> {
    private int lo;
    private int hi;
    private int[] arr;
    private int to_find;
    private static int SEQUENTIAL_CUTOFF;
    private int ans=0;
    public static ExecutorService threadPool = Executors. newCachedThreadPool ( ) ;
    private Frequency(int lo, int hi, int[] arr, int to_find){
        this.lo=lo;
        this.hi=hi;
        this.arr =arr;
        this.to_find=to_find;
    }

    public static int parallelFreq(int x, int[] A, int numThreads) {
        // your implementation goes here.
        if(A.length==1){
            if(x==A[0])
                return 1;
            else
                return 0;
        }
        else
            SEQUENTIAL_CUTOFF = (int) Math.ceil((double)A.length/numThreads);

        Frequency task = new Frequency(0, A.length,A, x);
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(task);



//        task.threadPool.shutdown();
        executorService.shutdown();
        int ans=0;
        try {
            ans = future.get().intValue();
        }
        catch (Exception e){

        }
        return ans;
    }

    @Override
    public Integer call() throws Exception {
        if(hi-lo<SEQUENTIAL_CUTOFF){
            for(int i=lo;i<hi;i++){
                if(arr[i]==to_find)
                    ans+=1;
            }
        }
        else {
            Frequency left = new Frequency(lo,(hi+lo)/2, arr,to_find);
            Frequency right= new Frequency((hi+lo)/2,hi,arr,to_find);
//            ExecutorService executorService= Executors.newSingleThreadExecutor();

            Future<Integer> future1 = threadPool.submit(left);
            Future<Integer> future2 = threadPool.submit(right);

            ans = future1.get().intValue() + future2.get().intValue();
//            executorService.shutdown();
        }

        return ans;
    }
}
