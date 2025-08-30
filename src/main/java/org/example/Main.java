package org.example;

import java.util.Arrays;

public class Main {

    /**
     * 解决最小机器重量设计问题
     * @param n 部件数量
     * @param m 供应商数量
     * @param maxCost 最大允许的总成本
     * @param w 部件重量数组 w[i][j] 是部件i从供应商j处购得的重量
     * @param c 部件价格数组 c[i][j] 是部件i从供应商j处购得的价格
     */
    public static void solve(int n, int m, int maxCost, int[][] w, int[][] c) {
        // dp[i][j] 表示组装前 i 个部件（0到i-1）且总成本恰好为 j 时的最小重量
        int[][] dp = new int[n][maxCost + 1];
        // path[i][j] 记录为达到 dp[i][j] 的状态，第 i-1 个部件选择了哪个供应商
        int[][] path = new int[n][maxCost + 1];

        // 初始化dp表为无穷大，path表为-1
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
            Arrays.fill(path[i], -1);
        }

        // 基础情况：对于第一个部件（部件0）
        for (int k = 0; k < m; k++) {
            int partCost = c[0][k];
            int partWeight = w[0][k];
            if (partCost <= maxCost) {
                // 如果当前供应商提供的重量更小，则更新
                if (partWeight < dp[0][partCost]) {
                    dp[0][partCost] = partWeight;
                    path[0][partCost] = k;
                }
            }
        }

        // 动态规划：填充剩余的部件
        for (int i = 1; i < n; i++) { // 对于每个部件
            for (int j = 0; j <= maxCost; j++) { // 对于每个可能的总成本
                for (int k = 0; k < m; k++) { // 对于当前部件的每个供应商
                    int partCost = c[i][k];
                    int partWeight = w[i][k];
                    int prevCost = j - partCost;

                    // 如果前一个状态是可达的
                    if (prevCost >= 0 && dp[i - 1][prevCost] != Integer.MAX_VALUE) {
                        int newWeight = dp[i - 1][prevCost] + partWeight;
                        // 如果找到了更轻的组合
                        if (newWeight < dp[i][j]) {
                            dp[i][j] = newWeight;
                            path[i][j] = k;
                        }
                    }
                }
            }
        }

        // 寻找总成本不超过 maxCost 的最小重量
        int minWeight = Integer.MAX_VALUE;
        int finalCost = -1;
        for (int j = 0; j <= maxCost; j++) {
            if (dp[n - 1][j] < minWeight) {
                minWeight = dp[n - 1][j];
                finalCost = j;
            }
        }

        if (finalCost == -1) {
            System.out.println("在给定的成本约束下找不到解决方案。");
            return;
        }

        // 回溯以重建解决方案
        int[] solution = new int[n];
        int currentCost = finalCost;
        for (int i = n - 1; i >= 1; i--) {
            int supplier = path[i][currentCost];
            solution[i] = supplier;
            currentCost -= c[i][supplier];
        }
        solution[0] = path[0][currentCost];

        // 打印结果
        for (int i = 0; i < n; i++) {
            System.out.println(i + ": " + solution[i]);
        }
        System.out.println("sum weight = " + minWeight);
        System.out.println("cost = " + finalCost);
    }

    public static void main(String[] args) {
        int n = 3, m = 3, cost = 7;
        int[][] w = {{1, 2, 3}, {3, 2, 1}, {2, 3, 2}};
        int[][] c = {{1, 2, 3}, {5, 4, 2}, {2, 1, 2}};

        solve(n, m, cost, w, c);
    }
}
