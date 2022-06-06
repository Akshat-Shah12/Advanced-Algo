

import java.util.Scanner;

public class HungarianAlgorithm {

    int[][] matrix;
    boolean[] rows_taken;
    boolean[] cols_taken;
    int[][] copy;
    int[][] bipartite_graph;
    int[] result;

    public HungarianAlgorithm() {
        getMatrixAsInput();
        rows_taken = new boolean[matrix.length];
        cols_taken = new boolean[matrix.length];
        copy = new int[matrix.length][matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, matrix.length);
        }
        subtractMinimumFromEachRow(copy);
        if(isEveryTaskAvailableForEveryCorrespondent(copy)) {
            generateBipartiteGraph(copy);
            return;
        }
        subtractMinimumFromEachColumn(copy);
        if(isEveryTaskAvailableForEveryCorrespondent(copy)) {
            generateBipartiteGraph(copy);
            return;
        }
        subtractMinimumFromRemainingElements(copy);
        generateBipartiteGraph(copy);
    }

    private void getMatrixAsInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of tasks = number of correspondents = ");
        int n = sc.nextInt();
        matrix = new int[n][n];
        System.out.println("Enter matrix elements");
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }
        sc.close(); 
    }

    private boolean isEveryTaskAvailableForEveryCorrespondent(int[][] input) {
        int count = 0;
        for(boolean b: rows_taken) if(b) count++;
        for(boolean b: cols_taken) if(b) count++;
        return count == input.length;
    }

    private void subtractMinimumFromEachRow(int[][] input) {
        for(int i = 0; i < input.length; i++) {
            int min = input[i][0];
            for(int j = 1; j < input.length; j++) {
                if(min > input[i][j]) min = input[i][j];
            }
            for(int j = 0; j < input.length; j++) {
                input[i][j]-= min;
            }
        }
        plotLines(input);
    }

    private void subtractMinimumFromEachColumn(int[][] input) {
        for(int i = 0; i < input.length; i++) {
            int min = input[0][i];
            for(int j = 1; j < input.length; j++) {
                if(min > input[j][i]) min = input[j][i];
            }
            for(int j = 0; j < input.length; j++) {
                input[j][i]-= min;
            }
        }
        plotLines(input);
    }

    private void subtractMinimumFromRemainingElements(int[][] input) {
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < input.length; i++) {
            if(!rows_taken[i]) {
                for (int j = 0; j < input.length; j++) {
                    if(!cols_taken[j] && min > input[i][j]) min = input[i][j];
                }
            }
        }
        for(int i = 0; i < input.length; i++) {
            if(!rows_taken[i]) {
                for (int j = 0; j < input.length; j++) {
                    if(!cols_taken[j]) input[i][j]-= min;
                }
            }
        }
        plotLines(input);

    }

    private void generateBipartiteGraph(int[][] input) {
        bipartite_graph = new int[input.length][];
        for(int i = 0; i < input.length; i++) {
            int count = 0;
            for(int j = 0; j < input.length; j++) {
                if(input[i][j] == 0) count++;
            }
            bipartite_graph[i] = new int[count];
            count = 0;
            for(int j = 0; j < input.length; j++) {
                if(input[i][j] == 0) bipartite_graph[i][count++] = j;
            }
        }
        computeResults(bipartite_graph);
    }

    private void computeResults(int[][] bipartite_graph) {
        boolean[] isCorrespondentHavingATask = new boolean[bipartite_graph.length];
        boolean[] isTaskAssigned = new boolean[bipartite_graph.length];
        result = new int[bipartite_graph.length];
        for(int i = 0; i < bipartite_graph.length; i++) {
            int min = Integer.MAX_VALUE;
            int currentCorrespondent = 0;
            for(int j = 0; j < bipartite_graph.length; j++) {
                if(!isCorrespondentHavingATask[j] && bipartite_graph[j].length < min) {
                    min = bipartite_graph[j].length;
                    currentCorrespondent = j;
                }
            }
            isCorrespondentHavingATask[currentCorrespondent] = true;
            for(int j = 0; j < bipartite_graph[currentCorrespondent].length; j++) {
                if(!isTaskAssigned[bipartite_graph[currentCorrespondent][j]]) {
                    isTaskAssigned[bipartite_graph[currentCorrespondent][j]] = true;
                    result[currentCorrespondent] = bipartite_graph[currentCorrespondent][j];
                    break;
                }
            }
        }
        for(int i = 0; i < result.length; i++) {
            System.out.print(result[i]+"\t");
        }
        System.out.println();
        int totalEffort = 0;
        System.out.print("Total Effort = ");
        for(int i = 0; i < result.length; i++) {
            totalEffort+=matrix[i][result[i]];
            System.out.print(matrix[i][result[i]]);
            if(i== result.length-1) System.out.print(" = ");
            else System.out.print(" + ");
        }
        System.out.println(totalEffort);
    }

    private void plotLines(int[][] input) {
        for(int i = 0; i < input.length; i++) {
            rows_taken[i] = false;
            cols_taken[i] = false;
        }
        for(int i = 0; i < input.length; i++) {
            for(int j = 0; j < input.length; j++) {
                if(input[i][j] == 0) {
                    int row_count_of_zeros = 0;
                    int col_count_of_zeros = 0;
                    for(int k = 0; k < input.length; k++) if(input[i][k] == 0) row_count_of_zeros++;
                    for(int k = 0; k < input.length; k++) if(input[k][j] == 0) col_count_of_zeros++;
                    if(row_count_of_zeros > col_count_of_zeros) rows_taken[i] = true;
                    else cols_taken[j] = true;
                }
            }
        }
    }
}
