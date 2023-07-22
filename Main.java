package com.company;
import java.util.Scanner;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Main {

    static ArrayList<Integer> available = new ArrayList<Integer>();
    static ArrayList<Integer> temp_available = new ArrayList<Integer>();
    static ArrayList<ArrayList<Integer>> allocation = new ArrayList<ArrayList<Integer>>();
    static ArrayList<ArrayList<Integer>> temp_allocation = new ArrayList<ArrayList<Integer>>();
    static ArrayList<ArrayList<Integer>> maximum = new ArrayList<ArrayList<Integer>>();
    static ArrayList<ArrayList<Integer>> temp_maximum = new ArrayList<ArrayList<Integer>>();
    static ArrayList<ArrayList<Integer>> need = new ArrayList<ArrayList<Integer>>();
    static ArrayList<ArrayList<Integer>> temp_need = new ArrayList<ArrayList<Integer>>();
    static int n;
    static int m;
    static ArrayList<Integer> finished = new ArrayList<>();
    static boolean deadlock;
    static ArrayList<Integer> victims = new ArrayList<Integer>();

    public static void Release2(int i)
    {
        for (int j = 0; j < m; j++)
        {
            temp_available.set(j,temp_available.get(j) + temp_allocation.get(i).get(j));
        }

        for (int k = 0; k < m; k++)
        {
            temp_allocation.get(i).set(k,0);
            temp_need.get(i).set(k,0);
        }
    }

    public static void Banker()
    {
        int counter = n - finished.size();
        int counter2 = n - finished.size();
        int flag = 0;
        while (counter2 > 0)
        {
            for (int i = 0; i < n; i++)
            {
                if (!finished.contains(i))
                {
                    if (temp_need.get(i).get(0) <= temp_available.get(0))
                    {
                        for (int j = 1; j < m; j++)
                        {
                            if (temp_need.get(i).get(j) > temp_available.get(j))
                                flag = 1;
                        }

                        if (flag == 1)
                        {
                            flag = 0;
                            continue;
                        }
                        Release2(i);
                        counter2--;
                        finished.add(i);
                        System.out.println("process " + i + " executed");
                        System.out.println();
                        print();
                    }
                }
            }
            if (counter2 < counter)
            {
                counter = counter2;
            }

            else
            {
                System.out.println("Deadlock detected.");
                deadlock = true;
                return;
            }
        }

        for (int i = 0; i < finished.size(); i++)
        {
            System.out.print(finished.get(i) + " ");
        }
        System.out.println();
        System.out.println("Processes executed in safe state.");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                temp_maximum.get(i).set(j,maximum.get(i).get(j));
                temp_allocation.get(i).set(j,allocation.get(i).get(j));
                temp_need.get(i).set(j,need.get(i).get(j));
            }
        }

        for (int k = 0; k < m; k++)
        {
            temp_available.set(k,available.get(k));
        }

        finished.removeAll(finished);

        deadlock = false;
    }

    public static void Request(int i, int[] r)
    {

        for (int j = 0; j < m; j++)
        {
            temp_need.get(i).set(j, temp_need.get(i).get(j) + r[j]);
        }

        print();
        Banker();
    }

    public static void print()
    {
        int i,j;

        System.out.println("available:");
        for (i = 0; i < m; i++)
        {
            System.out.print(temp_available.get(i)+ " ");
        }

        System.out.println();
        System.out.println();

        System.out.println("allocation:");
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < m; j++)
            {
                System.out.print(temp_allocation.get(i).get(j) + " ");
            }
            System.out.println();
        }

        System.out.println();

        System.out.println("need:");
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < m; j++)
            {
                System.out.print(temp_need.get(i).get(j) + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    public static void Release(int i, int[] r)
    {
            if (!finished.contains(i))
            {
                for (int j = 0; j < m; j++) {
                    if (r[j] > allocation.get(i).get(j)) {
                        System.out.println("Error, process " + i + " has only " + allocation.get(i).get(j) + " instances of resource " + j);
                        return;
                    }
                }

                System.out.println("process " + i + " released resources.");
                System.out.println();

                for (int k = 0; k < m; k++) {
                    allocation.get(i).set(k, allocation.get(i).get(k) - r[k]);
                    temp_allocation.get(i).set(k, temp_allocation.get(i).get(k) - r[k]);
                    available.set(k, available.get(k) + r[k]);
                    temp_available.set(k, temp_available.get(k) + r[k]);
                }

                print();
            }


        else
        {
            System.out.println("process has already been released.");
            System.out.println();
        }

    }

    public static void Recover() {
        int index = 0, total = 0, min = 1000;

        for (int i = 0; i < n; i++)
        {
            if (!finished.contains(i) && !victims.contains(i))
            {
                for (int j = 0; j < m; j++) {
                    total += temp_allocation.get(i).get(j);
                }

                if (total < min) {
                    index = i;
                    min = total;
                }
                total = 0;
            }

            for (int j = 0; j < m; j++)
            {
                temp_available.set(j,temp_available.get(j) + temp_allocation.get(index).get(j));
            }

            for (int k = 0; k < m; k++)
            {
                temp_allocation.get(index).set(k,0);
            }
        }
        victims.add(index);
        System.out.println("process " + index + " was the chosen victim because it allocated the least resources out of all other processes.");
    }

    public static void main(String[] args) {

	Scanner input = new Scanner(System.in);

    System.out.println("How many processes are there?");
    int n2 = input.nextInt();
    n = n2;
    System.out.println("How many resources are there?");
    int m2 = input.nextInt();
    m = m2;
    for (int i = 0; i < n; i++)
    {
        allocation.add(new ArrayList<Integer>());
        temp_allocation.add(new ArrayList<Integer>());
        maximum.add(new ArrayList<Integer>());
        temp_maximum.add(new ArrayList<Integer>());
        need.add(new ArrayList<Integer>());
        temp_need.add(new ArrayList<Integer>());
    }

    int num;

        System.out.println("How many instances of each resource are available?");
        for (int i = 0; i < m; i++)
        {
            num = input.nextInt();
            available.add(num);
            temp_available.add(num);
        }

        System.out.println("How many resources will each process take?");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                num = input.nextInt();
                maximum.get(i).add(num);
                temp_maximum.get(i).add(num);
            }
        }

        System.out.println("How many instances of each resource are allocated to each process?");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                num = input.nextInt();
                allocation.get(i).add(num);
                temp_allocation.get(i).add(num);
            }
        }


        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                num = maximum.get(i).get(j) - allocation.get(i).get(j);
                need.get(i).add(num);
                temp_need.get(i).add(num);
            }
        }

        print();

        Banker();

        while (true)
        {
            System.out.println();
            String command;
            command = input.next();

            switch(command)
            {
                case "RQ":
                {
                    int ind,number;
                    int[] r = new int[m];

                    ind = input.nextInt();
                    for (int i = 0; i < m; i++)
                    {
                        number = input.nextInt();
                        r[i] = number;
                    }

                    Request(ind,r);

                    break;
                 }

                case "RL":
                {
                    int ind,number;
                    int[] r = new int[m];

                    ind = input.nextInt();
                    for (int i = 0; i < m; i++)
                    {
                        number = input.nextInt();
                        r[i] = number;
                    }

                    System.out.println();
                    Release(ind,r);

                    if (deadlock)
                        Banker();

                    break;
                }

                case "Recover":
                {
                    if (!deadlock)
                    {
                        System.out.println("No deadlock, The system is in safe state.");
                    }

                    else
                    {
                        Recover();
                        Banker();
                    }

                    break;
                }

                case "Quit":
                {
                    exit(0);
                }
            }
        }
    }
}
