import java.util.concurrent.*;

public class Frequency implements Callable<Integer> {
    private int lo;
    private int hi;
    private int[] arr;
    private int to_find;
    private static int SEQUENTIAL_CUTOFF;
    private int ans=0;
    private Frequency(int lo, int hi, int[] arr, int to_find){
        this.lo=lo;
        this.hi=hi;
        this.arr =arr;
        this.to_find=to_find;
    }

    public static int parallelFreq(int x, int[] A, int numThreads) throws ExecutionException, InterruptedException {
        // your implementation goes here.
        SEQUENTIAL_CUTOFF = (int) Math.ceil((double)A.length/numThreads);

        Frequency task = new Frequency(0, A.length,A, x);
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(task);
        executorService.shutdown();
        return future.get().intValue();
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
            ExecutorService executorService= Executors.newSingleThreadExecutor();

            Future<Integer> future1 = executorService.submit(left);
            Future<Integer> future2 = executorService.submit(right);

            ans = future1.get().intValue() + future2.get().intValue();
            executorService.shutdown();
        }

        return ans;
    }
}
