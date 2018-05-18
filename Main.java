package lab5_mpj;

import mpi.*;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        MPI.Init(args);
        int[] result = new int[1];
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int total = 10;

        int[] d = new int[1];

        d[0] = total / size + 1;

        int[] a = new int[d[0]];
        int[] b = new int[d[0]];
        Random r = new Random();

      //  MPI.COMM_WORLD.Bcast(d, 1, 0, MPI.INT, 0);

        if (rank == 0) {
            int[] tmp1 = new int[d[0]];
            int[] tmp2 = new int[d[0]];

            for (int dest = 1; dest < size; dest++) {
                for (int i = 0; i < d[0]; i++) {
                    tmp1[i] = r.nextInt(10);
                    tmp2[i] = r.nextInt(10);
                }

                MPI.COMM_WORLD.Send(tmp1, 0, a.length, MPI.INT, dest, 0);
                MPI.COMM_WORLD.Send(tmp2, 0, b.length, MPI.INT, dest, 0);
            }


            d[0] = total - d[0] * (size - 1);
            for (int i = 0; i < d[0]; i++) {
                a[i] = r.nextInt(10);
                b[i] = r.nextInt(10);

            }
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));

        } else {

            MPI.COMM_WORLD.Recv(a, 0, d[0], MPI.INT, 0, 0);
            MPI.COMM_WORLD.Recv(b, 0, d[0], MPI.INT, 0, 0);
            System.out.println("a = " + Arrays.toString(a));
            System.out.println("b = " + Arrays.toString(b));
        }

        int[] sum = new int[1];

        for (int i = 0; i < d[0]; i++) {
            sum[0] += a[i] * b[i];

        }
        System.out.println(sum[0]);

        MPI.COMM_WORLD.Reduce(sum, 0, result, 0, 1, MPI.INT, MPI.SUM, 0);

        if (rank == 0) {
            System.out.println("answer is " + result[0]);
        }
        MPI.Finalize();
    }
}
