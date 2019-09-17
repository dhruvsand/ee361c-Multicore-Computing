import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        int [] arr ={0,1,2,1,1,1,2,3,1,1};

        System.out.println("The result is : "+ Frequency.parallelFreq(5,arr, 5));

    }
}
