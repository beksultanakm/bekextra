import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Benchmark {
    static final int REPEAT = 10;

    public static void main(String[] args) throws IOException {
        checkCorrectness();

        int[] sizes = {100, 1000, 10000};
        String[] orders = {"random", "sorted"};

        FileWriter csv = new FileWriter("results.csv");
        csv.write("order,n,structure,insert_ms,search_ms,delete_ms,bst_height\n");

        for (int i = 0; i < 20; i++) {
            runOnce(makeKeys(1000, "random"));
        }

        for (String order : orders) {
            System.out.println("=== Деректер: " + order + " ===");
            for (int n : sizes) {
                int[] keys = makeKeys(n, order);

                double[] bstSum = new double[3];
                double[] hashSum = new double[3];
                int height = 0;

                for (int r = 0; r < REPEAT; r++) {
                    double[] res = runOnce(keys);
                    for (int k = 0; k < 3; k++) {
                        bstSum[k] += res[k];
                        hashSum[k] += res[k + 3];
                    }
                    height = (int) res[6];
                }

                for (int k = 0; k < 3; k++) {
                    bstSum[k] /= REPEAT;
                    hashSum[k] /= REPEAT;
                }

                System.out.printf("n=%-6d BST : insert=%8.3f  search=%8.3f  delete=%8.3f  (height=%d)%n",
                        n, bstSum[0], bstSum[1], bstSum[2], height);
                System.out.printf("         Hash: insert=%8.3f  search=%8.3f  delete=%8.3f%n",
                        hashSum[0], hashSum[1], hashSum[2]);

                csv.write(order + "," + n + ",BST," + bstSum[0] + "," + bstSum[1] + "," + bstSum[2] + "," + height + "\n");
                csv.write(order + "," + n + ",Hash," + hashSum[0] + "," + hashSum[1] + "," + hashSum[2] + ",-\n");
            }
            System.out.println();
        }
        csv.close();
        System.out.println("Нәтиже results.csv файлына жазылды (уақыт миллисекундпен).");
    }

    static int[] makeKeys(int n, String order) {
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) {
            keys[i] = i + 1;
        }
        if (order.equals("random")) {
            Random rnd = new Random(42);
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int tmp = keys[i];
                keys[i] = keys[j];
                keys[j] = tmp;
            }
        }
        return keys;
    }

    static double[] runOnce(int[] keys) {
        double[] out = new double[7];

        BST bst = new BST();
        long t = System.nanoTime();
        for (int k : keys) bst.insert(k);
        out[0] = (System.nanoTime() - t) / 1e6;
        out[6] = bst.height();

        t = System.nanoTime();
        int found = 0;
        for (int k : keys) if (bst.search(k)) found++;
        out[1] = (System.nanoTime() - t) / 1e6;

        t = System.nanoTime();
        for (int k : keys) bst.delete(k);
        out[2] = (System.nanoTime() - t) / 1e6;

        Hashtable ht = new Hashtable();
        t = System.nanoTime();
        for (int k : keys) ht.insert(k);
        out[3] = (System.nanoTime() - t) / 1e6;

        t = System.nanoTime();
        for (int k : keys) if (ht.search(k)) found++;
        out[4] = (System.nanoTime() - t) / 1e6;

        t = System.nanoTime();
        for (int k : keys) ht.delete(k);
        out[5] = (System.nanoTime() - t) / 1e6;

        if (found != keys.length * 2) {
            System.out.println("ҚАТЕ: кейбір кілттер табылмады!");
        }
        return out;
    }

    static void checkCorrectness() {
        BST bst = new BST();
        Hashtable ht = new Hashtable();
        int[] keys = makeKeys(500, "random");
        for (int k : keys) {
            bst.insert(k);
            ht.insert(k);
        }
        for (int k = 2; k <= 500; k += 2) {
            bst.delete(k);
            ht.delete(k);
        }
        for (int k = 1; k <= 500; k++) {
            boolean expected = (k % 2 == 1);
            if (bst.search(k) != expected || ht.search(k) != expected) {
                throw new RuntimeException("Тест құлады, кілт: " + k);
            }
        }
        if (bst.search(9999) || ht.search(9999)) {
            throw new RuntimeException("Жоқ кілт табылды");
        }
        System.out.println("Дұрыстық тесті өтті.\n");
    }
}