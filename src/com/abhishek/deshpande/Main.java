package com.abhishek.deshpande;

public class Main {

    public static void main(String[] args) {

        /*int[][] costMat = new int[][3];

        costMat[0] = new int[]{2500,4000,3500};
        costMat[1] = new int[]{4000,6000,3500};
        costMat[2] = new int[]{2000,4000,2500};*/

        /*costMat[0] = new int[]{40,23,42};
        costMat[1] = new int[]{65,84,67};
        costMat[2] = new int[]{12,5,8};*/

        /*int[][] costMat = new int[4][4];
        costMat[0] = new int[]{82,83,69,92};
        costMat[1] = new int[]{77,37,49,92};
        costMat[2] = new int[]{11,69, 5,86};
        costMat[3] = new int[]{ 8, 9,98,23};*/

        /*int[][] costMat = new int[4][4];
        costMat[0] = new int[]{38,35,6,7};
        costMat[1] = new int[]{16,97,98,73};
        costMat[2] = new int[]{86,43,72,58};
        costMat[3] = new int[]{ 63,27,48,4};*/

        /*int[][] costMat = new int[4][4];
        costMat[0] = new int[]{36,99,32,69};
        costMat[1] = new int[]{7,37,76,56};
        costMat[2] = new int[]{93,20,25,32};
        costMat[3] = new int[]{ 86,7,87,33};*/

        /*
        Worked with this data

        int[][] costMat = new int[4][4];
        costMat[0] = new int[]{70,67,44,43};
        costMat[1] = new int[]{59,52,19,99};
        costMat[2] = new int[]{10,10,42,18};
        costMat[3] = new int[]{49,58,27,6};*/

        /*int[][] costMat = new int[4][4];
        costMat[0] = new int[]{14,99,95,29};
        costMat[1] = new int[]{84,1,23,1};
        costMat[2] = new int[]{39,93,42,12};
        costMat[3] = new int[]{18,91,63,79};*/

        /*int[][] costMat = new int[3][3];
        costMat[0] = new int[]{2500,4000,3500};
        costMat[1] = new int[]{4000,6000,3500};
        costMat[2] = new int[]{2000,4000,2500};*/


        int[][] costMat = new int[5][5];
        costMat[0] = new int[]{34,21,81,85,12};
        costMat[1] = new int[]{45,76,77,15,13};
        costMat[2] = new int[]{1,86,46,31,14};
        costMat[3] = new int[]{26,98,87,81,15};
        costMat[4] = new int[]{34,12,76,36,90};

        CompleteAssignmentProblem completeAssignmentProblem = new CompleteAssignmentProblem(costMat);
        completeAssignmentProblem.solve();

        /*AssignmentProblem assignmentProblem = new AssignmentProblem(costMat);
        assignmentProblem.solve();*/
    }
}

class AssignmentProblem {
    private int[][] costMat;
    private int l;
    private int[] rowAvailable;
    private int[] colAvailable;
    private int[][] stack;
    private int stackPointer;

    public AssignmentProblem(int[][] costMat){
        this.costMat = costMat;
        l = costMat.length;

        rowAvailable = new int[l];
        colAvailable = new int[l];
        stackPointer = -1;
        stack = new int[10][2];

        for (int i=0;i<l;i++){
            rowAvailable[i] = 1;
            colAvailable[i] = 1;
        }
    }

    public void solve(){
        reduce();
        getPoints(0,0);

        System.out.println("i j");
        for (int i=0;i<l;i++){
            System.out.print(stack[i][0] + " " + stack[i][1] + "\n");
        }


    }

    private boolean getPoints(int row,int start){
        boolean zeroAdded=false;
        for (int i=start;i<l;i++){
            if (costMat[row][i] == 0 && rowAvailable[row] == 1 && colAvailable[i] == 1){
                rowAvailable[row] = 0;
                colAvailable[i] = 0;
                stackPointer++;
                stack[stackPointer][0] = row;
                stack[stackPointer][1] = i;
                zeroAdded = true;

                if (row+1 < l) {
                    if (!getPoints(row + 1, 0)) {
                        rowAvailable[row] = 1;
                        colAvailable[i] = 1;
                        stackPointer--;
                        zeroAdded = false;
                    }
                }
            }
        }
        if (zeroAdded){
            return true;
        }
        return false;
    }


    private void reduce(){
        for(int i=0;i<l;i++){
            int min=Integer.MAX_VALUE;
            for(int j=0;j<l;j++) {
                if (costMat[i][j] < min){
                    min = costMat[i][j];
                }
            }
            for (int j=0;j<l;j++){
                costMat[i][j] -= min;
            }
        }

        for (int i=0;i<l;i++){
            int min = Integer.MAX_VALUE;
            for (int j=0;j<l;j++){
                if (costMat[j][i] < min){
                    min  = costMat[j][i];
                }
            }
            for (int j=0;j<l;j++){
                costMat[j][i] -= min;
            }
        }
    }
}