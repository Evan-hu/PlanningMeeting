package com.test;

/**
 * Created by Evan on 2016/4/12.
 */
public class test {

    public static void main(String args[]) {
        test t = new test();
        for (int i = 0; i < new String[] { "A", "B", "C", "D" }.length; i++)
            t.combination(new String[] { "A", "B", "C", "D" }, i);
    }

    /**
     *
     * @param a记录组合序列数组
     * @param n总数
     * @param r选取的个数
     * @param k1数组坐标
     *            (初始传入0)
     * @param k2辅助参数
     *            (初始传入0)
     */
    public void combination(int record[], String info[], int n, int r, int k1,
                            int k2) {
        if (k1 == r) { // 输出当前序列
            for (int i = 0; i < r; ++i)
                System.out.print(info[record[i] - 1] + " ");
            System.out.println();
        } else
            for (int i = k2; i < n; ++i) {
                record[k1] = i + 1; // 子序列赋值
                combination(record, info, n, r, k1 + 1, i + 1); // 递归回溯
            }
    }

    public void combination(String info[], int r) {
        int record[] = new int[r];
        int n = info.length;
        combination(record, info, n, r, 0, 0);
    }

}
