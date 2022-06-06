
import java.util.ArrayList;
import java.util.Scanner;

public class FordFulkersonAlgorithm {
    static int[][] matrix;
    static int source;
    static int destination;
    public static void main(String[]args){
        RunAlgo();
    }
    public static void RunAlgo() {
        inputMatrix();
        int[] parent = new int[matrix.length];
        int totalFlow = 0;
        while(isThereAnyPathPossible(source,destination,matrix,parent)) {
            int flow = Integer.MAX_VALUE;
            for(int val=destination;val!=source;val=parent[val]) {
                int up = parent[val];
                if(matrix[up][val]<flow) flow = matrix[up][val];
            }
            for(int val=destination;val!=source;val=parent[val]) {
                int up = parent[val];
                matrix[up][val]-=flow;
            }
            totalFlow+=flow;
        }
        System.out.println("Max Flow = "+totalFlow);
    }

    private static void inputMatrix() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of nodes = ");
        int n = sc.nextInt();
        System.out.print("Source = ");
        source = sc.nextInt() - 1;
        System.out.print("Destination = ");
        destination = sc.nextInt() - 1;
        matrix = new int[n][n];
        for(int i=0;i<n;i++) {
            if(i!=destination) for(int j=0;j<n;j++) {
                if(i==j || j==source) matrix[i][j] = 0;
                else {
                    System.out.print("Capacity from "+(i+1)+" to "+(j+1)+" (0 if not connected) = ");
                    matrix[i][j] = sc.nextInt();
                }
            }
        }
    }

    private static boolean isThereAnyPathPossible(int source, int destination, int[][] matrix, int[] parent) {
        parent[source] = -1;
        boolean[] visited = new boolean[matrix.length];
        ArrayList<Integer> queue = new ArrayList<>();
        queue.add(source);
        while(queue.size()>0) {
            int up = queue.get(0);
            queue.remove(0);
            for(int i=0;i<matrix.length;i++) {
                if(matrix[up][i]>0 && !visited[i]) {
                    visited[i] = true;
                    parent[i] = up;
                    if(i==destination) return true;
                    queue.add(i);
                }
            }
        }
        return false;
    }
}
