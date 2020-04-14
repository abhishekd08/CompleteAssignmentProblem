package com.abhishek.deshpande;

public class CompleteAssignmentProblem {
    private int[][] costMat;
    private int l;
    private int[] rowAvailable;
    private int[] colAvailable;
    private int[][] stack;
    private int stackPointer;

    private SolutionSet[] solutionSet;
    private int solutionSetPointer;

    private int[][] zeros;
    private int[][] rowZeroCount;
    private int[][] colZeroCount;
    private int[] rowIsSelected;
    private int[] colIsSelected;
    private int[] rowOrderByZero;
    private int[] colOrderByZero;
    int zerosPointer;

    private int[][] orderOfSelection;

    private int[][] originalCostMat;

    public CompleteAssignmentProblem(int[][] costMat) {
        this.costMat = costMat;
        l = costMat.length;

        originalCostMat = new int[l][l];
        for (int i=0;i<l;i++){
            for (int j=0;j<l;j++){
                originalCostMat[i][j] = costMat[i][j];
            }
        }

        rowAvailable = new int[l];
        colAvailable = new int[l];
        stackPointer = -1;
        stack = new int[10][2];

        setArrayValues(rowAvailable, 1);
        setArrayValues(colAvailable, 1);

        solutionSet = new SolutionSet[l];
        solutionSetPointer = 0;

        zeros = new int[l * l][2];
        rowZeroCount = new int[l][2];
        colZeroCount = new int[l][2];

        rowOrderByZero = new int[l];
        colOrderByZero = new int[l];

        setArrayValues(rowOrderByZero, -1);
        setArrayValues(colOrderByZero, -1);

        orderOfSelection = new int[l*2][3];
    }

    public void solve() {
        System.out.println("original matrix : ");
        printMatrix();
        reduce();
        drawLines();


        getPoints(0, 0);

        System.out.println("Reduced final Matrix : ");
        printMatrix();

        System.out.println("---------------- Solutions -----------------\n");
        for (int i=0;i<solutionSetPointer;i++){
            solutionSet[i].printSolutionSet();
        }
        System.out.println("-------------------END-------------------");

        int minCost = Integer.MAX_VALUE;
        int minCostIndex = -1;
        for (int i=0;i<solutionSetPointer;i++){
            int cost = 0;
            for (int j=0;j<l;j++){
                cost += originalCostMat[solutionSet[i].solutionStack[j][0]][solutionSet[i].solutionStack[j][1]];
            }
            if (cost < minCost){
                minCost = cost;
                minCostIndex = i;
            }
            System.out.println("cost -->" + cost);
        }

        System.out.println("Min Cost : "+minCost);
        solutionSet[minCostIndex].printSolutionSet();
    }

    private void drawLines() {

        setZerosArrays();
        //sort order of selection
        sort_with_row();
        int linesWithRowFirst = getSelectedLinesCount();
        sort_with_col();
        int linesWithColsFirst = getSelectedLinesCount();

        while(linesWithColsFirst != l || linesWithRowFirst != l){

            reduceAgain();
            setZerosArrays();
            sort_with_row();
            linesWithRowFirst = getSelectedLinesCount();

            sort_with_col();
            linesWithColsFirst = getSelectedLinesCount();

            if (linesWithRowFirst < l){
                setZerosArrays();
                sort_with_row();
                linesWithRowFirst = getSelectedLinesCount();
                reduceAgain();
                printMatrix();

            }else if (linesWithColsFirst < l){
                setZerosArrays();
                sort_with_col();
                linesWithColsFirst = getSelectedLinesCount();
                reduceAgain();
            }else   if (linesWithRowFirst == l){
                setZerosArrays();
                sort_with_row();
                linesWithRowFirst = getSelectedLinesCount();
                return;
            }else if (linesWithColsFirst == l){
                return;
            }
        }
    }

    private void setZerosArrays(){
        zeros = new int[l*l][2];
        rowZeroCount = new int[l][2];
        colZeroCount = new int[l][2];
        zerosPointer = 0;
        for (int i=0;i<l;i++){
            for (int j=0;j<l;j++){
                if (costMat[i][j] == 0){
                    zeros[zerosPointer][0] = i;
                    zeros[zerosPointer][1] = j;
                    zerosPointer++;

                    rowZeroCount[i][0]++;
                    rowZeroCount[i][1] = i;
                    colZeroCount[j][0]++;
                    colZeroCount[j][1] = j;
                }
            }
        }
    }

    private void sort_with_row() {
        for (int i=0;i<l;i++){
            orderOfSelection[i][0] = 0;
            orderOfSelection[i][1] = rowZeroCount[i][0];
            orderOfSelection[i][2] = rowZeroCount[i][1];

            orderOfSelection[i+l][0] = 1;
            orderOfSelection[i+l][1] = colZeroCount[i][0];
            orderOfSelection[i+l][2] = colZeroCount[i][1];
        }

        insertionSort();
    }

    private void sort_with_col() {
        for (int i=0;i<l;i++){
            orderOfSelection[i+l][0] = 0;
            orderOfSelection[i+l][1] = rowZeroCount[i][0];
            orderOfSelection[i+l][2] = rowZeroCount[i][1];

            orderOfSelection[i][0] = 1;
            orderOfSelection[i][1] = colZeroCount[i][0];
            orderOfSelection[i][2] = colZeroCount[i][1];
        }

        insertionSort();
    }

    private int getSelectedLinesCount() {
        int zerosCounter=zerosPointer;
        rowIsSelected = new int[l];
        colIsSelected = new int[l];

        int[][] tempZeros = new int[l*l][2];
        for (int i=0;i<l*2;i++){
            tempZeros[i][0] = zeros[i][0];
            tempZeros[i][1] = zeros[i][1];
        }
        System.out.println();
        for(int i=0;i<l*2;i++){
            for (int j=0;j<zerosPointer;j++){
                if (orderOfSelection[i][0] == 0) {
                    if (tempZeros[j][0] == orderOfSelection[i][2] && tempZeros[j][1] > -1) {
                        rowIsSelected[orderOfSelection[i][2]] = 1;
                        tempZeros[j][0] = -1;
                        tempZeros[j][1] = -1;
                        zerosCounter--;
                    }
                }else{
                    if (tempZeros[j][1] == orderOfSelection[i][2] && tempZeros[j][0] > -1) {
                        colIsSelected[orderOfSelection[i][2]] = 1;
                        tempZeros[j][0] = -1;
                        tempZeros[j][1] = -1;
                        zerosCounter--;
                    }
                }
            }

            if (zerosCounter==0){
                break;
            }
        }
        int lines = 0;
        for (int i=0;i<l;i++){
            if (rowIsSelected[i] == 1){
                lines++;
            }
            if (colIsSelected[i] == 1){
                lines++;
            }
        }
        return lines;
    }

    private void insertionSort(){
        for (int i=1;i<l*2;++i){
            int key = orderOfSelection[i][1];
            int key0 = orderOfSelection[i][0];
            int key2 = orderOfSelection[i][2];

            int j = i-1;
            while(j >= 0 && orderOfSelection[j][1] < key){
                orderOfSelection[j+1][1] = orderOfSelection[j][1];
                orderOfSelection[j+1][0] = orderOfSelection[j][0];
                orderOfSelection[j+1][2] = orderOfSelection[j][2];
                j = j-1;
            }
            orderOfSelection[j + 1][1] = key;
            orderOfSelection[j + 1][0] = key0;
            orderOfSelection[j + 1][2] = key2;
        }
    }

    private boolean getPoints(int row, int start) {
        boolean zeroAdded = false;
        for (int i = start; i < l; i++) {
            if (costMat[row][i] == 0 && rowAvailable[row] == 1 && colAvailable[i] == 1) {
                rowAvailable[row] = 0;
                colAvailable[i] = 0;
                stackPointer++;
                stack[stackPointer][0] = row;
                stack[stackPointer][1] = i;
                zeroAdded = true;

                if (row == l-1 && zeroAdded == true){
                    //copy 1 possible solution stack
                    int[][] solution = new int[l][2];
                    for (int j=0;j<l;j++){
                        solution[j][0] = stack[j][0];
                        solution[j][1] = stack[j][1];
                    }
                    solutionSet[solutionSetPointer] = new SolutionSet(solution);
                    solutionSetPointer++;

                    rowAvailable[row] = 1;
                    colAvailable[i] = 1;
                    stackPointer--;
                }

                if (row + 1 < l) {
                    if (!getPoints(row + 1, 0)) {
                        rowAvailable[row] = 1;
                        colAvailable[i] = 1;
                        stackPointer--;
                        zeroAdded = false;
                    }else{
                        rowAvailable[row] = 1;
                        colAvailable[i] = 1;
                        stackPointer--;
                        continue;
                    }
                }
            }
        }
        if (zeroAdded) {
            return true;
        }
        return false;
    }

    private void reduceAgain() {
        int minValue = Integer.MAX_VALUE;
        for (int i=0;i<l;i++){
            for (int j=0;j<l;j++){
                if (rowIsSelected[i] == 0 && colIsSelected[j] == 0 && costMat[i][j] < minValue){
                    minValue = costMat[i][j];
                }
            }
        }
        for (int i=0;i<l;i++) {
            for (int j = 0; j < l; j++) {
                if (rowIsSelected[i] == 0 && colIsSelected[j] == 0){
                    costMat[i][j] = costMat[i][j] - minValue;
                }else if (rowIsSelected[i] == 1 && colIsSelected[j] == 1){
                    costMat[i][j] = costMat[i][j] + minValue;
                }
            }
        }
    }

    private void reduce() {
        for (int i = 0; i < l; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < l; j++) {
                if (costMat[i][j] < min) {
                    min = costMat[i][j];
                }
            }
            for (int j = 0; j < l; j++) {
                costMat[i][j] -= min;
            }
        }

        for (int i = 0; i < l; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < l; j++) {
                if (costMat[j][i] < min) {
                    min = costMat[j][i];
                }
            }
            for (int j = 0; j < l; j++) {
                costMat[j][i] -= min;
            }
        }
    }

    private void setArrayValues(int[] arr, int value){
        for (int i=0;i<arr.length;i++){
            arr[i] = value;
        }
    }

    private void printMatrix() {
        System.out.println("\n--------");
        for (int i=0;i<l;i++){
            for (int j=0;j<l;j++){
                System.out.print(costMat[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    class SolutionSet{
        public int[][] solutionStack;

        public SolutionSet(int[][] solution){
            solutionStack = new int[l][2];
            for (int i=0;i<l;i++){
                solutionStack[i][0] = solution[i][0];
                solutionStack[i][1] = solution[i][1];
            }
        }

        public void printSolutionSet(){
            System.out.println("\ni\t\tj\n");
            for (int i=0;i<l;i++){
                System.out.println("( "+ solutionStack[i][0]+", "+solutionStack[i][1]+" )");
            }
        }
    }
}
