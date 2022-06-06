

import java.util.ArrayList;
import java.util.Random;

public class QuickSort {
    private static  int[] input;
    private static int[] output;
    static int hoareIterations;
    static int lomutoIterations;
    public static void main(String[] args){
        solve();
    }

    public static void solve() {
        // int random = Math.random()*(2);
        Random r = new Random();

        int random = r.nextInt(131072-65536);
        System.out.println("size = "+random);
        int[] input = new int[random];
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0;i<random;i++) arrayList.add(i);
        System.out.println("Arraylist generated");
        long temp_time = System.currentTimeMillis();
        for(int i=0;i<random;i++) {
            int temp = arrayList.get(new Random().nextInt(arrayList.size()));
            input[i] = temp;
            arrayList.remove(Integer.valueOf(temp));
        }
        System.out.println("Randomized array");
        setInput(input);

        //Lomuto

        long time = System.nanoTime();
        sortUsingLomutoPartition(0,input.length-1);
        System.out.println((System.nanoTime()-time)+"ns for "+lomutoIterations+" iterations for Lomuto");

        //Hoare

        setInput(input);
        time = System.nanoTime();
        sortUsingHoarePartition(0,input.length-1);
        System.out.println((System.nanoTime()-time)+"ns for "+hoareIterations+" iterations for Hoare");

        //Random Lomuto

        setInput(input);
        time = System.nanoTime();
        randomizedSortUsingLomutoPartition(0,input.length-1);
        System.out.println((System.nanoTime()-time)+"ns for "+lomutoIterations+" iterations for Randomized Lomuto");

        //Random Hoare

        setInput(input);
        time = System.nanoTime();
        randomizedSortUsingHoarePartition(0,input.length-1);
        System.out.println((System.nanoTime()-time)+"ns for "+hoareIterations+" iterations for Randomized Hoare");
    }

    public static void setInput(int[] inputt) {
        input = inputt;
        output = new int[inputt.length+1];
        System.arraycopy(inputt,0,output,0,inputt.length);
        output[inputt.length] = Integer.MAX_VALUE;
        hoareIterations = 0;
        lomutoIterations = 0;
    }

    public static void sortUsingHoarePartition(int low, int high) {
        if(low<high) {
            int part = hoarePartition(low, high);
            sortUsingHoarePartition(low, part);
            sortUsingHoarePartition(part + 1, high);
        }
    }

    public static void sortUsingLomutoPartition(int low, int high) {
        if(low<high) {
            int part = lomutoPartition(low, high);
            sortUsingLomutoPartition(low, part-1);
            sortUsingLomutoPartition(part + 1, high);
        }
    }

    public static void randomizedSortUsingHoarePartition(int low, int high) {
        if(low<high) {
            int part = randomHoarePartition(low, high);
            randomizedSortUsingHoarePartition(low, part);
            randomizedSortUsingHoarePartition(part + 1, high);
        }
    }

    public static void randomizedSortUsingLomutoPartition(int low, int high) {
        if(low<high) {
            int part = randomLomutoPartition(low, high);
            randomizedSortUsingLomutoPartition(low, part);
            randomizedSortUsingLomutoPartition(part + 1, high);
        }
    }

    private static int hoarePartition(int low, int high) {
        int pivot = output[low];
        int i = low;
        int j = high;
        while(i<j) {
            while(output[i]<=pivot) {
                i++;
                hoareIterations++;
            }
            while(output[j]>pivot){
                j--;
                hoareIterations++;
            }
            if(i<j) {
                int temp = output[i];
                output[i] = output[j];
                output[j] = temp;
            }
        }
        int temp = output[j];
        output[j] = output[low];
        output[low] = temp;
        hoareIterations++;
        return j;
    }

    private static int randomHoarePartition(int low, int high) {
        int random = new Random().nextInt(high+1-low)+low;
        int pivot = output[random];
        int temp = output[random];
        output[random] = output[low];
        output[low] = temp;
        int i = low;
        int j = high;
        while(i<j) {
            while(output[i]<=pivot) {
                i++;
                hoareIterations++;
            }
            while(output[j]>pivot){
                j--;
                hoareIterations++;
            }
            if(i<j) {
                temp = output[i];
                output[i] = output[j];
                output[j] = temp;
            }
        }
        temp = output[j];
        output[j] = output[low];
        output[low] = temp;
        hoareIterations++;
        return j;
    }

    private static int lomutoPartition(int low, int high) {
        int pivot = output[high];
        int i = low;
        for(int j=low;j<high;j++) {
            lomutoIterations++;
            if(output[j]<=pivot) {
                int temp = output[j];
                output[j] = output[i];
                output[i] = temp;
                lomutoIterations++;
                i++;
            }
        }
        int temp = output[i];
        output[i] = output[high];
        output[high] = temp;
        lomutoIterations++;
        return i;
    }

    private static int randomLomutoPartition(int low, int high) {
        int random = new Random().nextInt(high+1-low)+low;
        int pivot = output[random];
        int temp = output[random];
        output[random] = output[high];
        output[high] = temp;
        int i = low;
        for(int j=low;j<high;j++) {
            lomutoIterations++;
            if(output[j]<=pivot) {
                temp = output[j];
                output[j] = output[i];
                output[i] = temp;
                lomutoIterations++;
                i++;
            }
        }
        temp = output[i];
        output[i] = output[high];
        output[high] = temp;
        lomutoIterations++;
        return i;
    }

    public void displayOutput() {
        for(int i : output) System.out.print(i+" ");
    }
}
