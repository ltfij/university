package com.hjwylde.uni.swen221.lab10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.hjwylde.uni.swen221.lab10.swen221.concurrent.Job;
import com.hjwylde.uni.swen221.lab10.swen221.concurrent.ThreadPool;

/*
 * Code for Laboratory 10, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class ParSort {

    // =======================================================================
    // SWEN221: Look at this
    // =======================================================================

    public static boolean checkValid(List<Integer> data) {
        for (int i = 1; i != data.size(); ++i) {
            int previous = data.get(i - 1);
            int current = data.get(i);
            if (previous > current)
                // invalid sort
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean displayMode = false;
        boolean parallelMode = false;
        String filename = null;

        // First, some rudimentary command-line argument processing.
        if (args.length == 0) {
            System.out.println("Usage: java Main [-gui] input.dat");
            System.exit(1);
        }
        int index = 0;

        while (args[index].startsWith("--")) {
            String arg = args[index++];
            if (arg.equals("--gui"))
                displayMode = true;
            else if (arg.equals("--parallel"))
                parallelMode = true;
            else {
                System.out.println("Unrecognised argument encountered: " + arg);
                System.exit(1);
            }
        }
        filename = args[index];

        // Second, read in the data and sort it.
        try {
            int numProcessors = Runtime.getRuntime().availableProcessors();
            System.out.println("Executing on machine with " + numProcessors + " processor(s).");

            List<Integer> data;
            try (FileReader fr = new FileReader(filename)) {
                data = ParSort.readInput(fr);
            }

            System.out.println("Read " + data.size() + " data items.");

            if (displayMode) {
                System.out.println("Running in Display Mode (so ignore timings).");
                data = new DisplayList<>(data);
            }

            long start = System.currentTimeMillis();

            if (parallelMode) {
                // do a parallel quick sort
                System.out.println("Performing a PARALLEL quicksort...");
                ParSort.parallelSort(data, numProcessors);
            } else {
                // perform a sequential quick sort
                System.out.println("Performing a SEQUENTIAL quicksort...");
                ParSort.sequentialSort(data, 0, data.size());
            }

            long time = System.currentTimeMillis() - start;

            System.out.println("Sorted data in " + time + "ms");
            if (ParSort.checkValid(data))
                System.out.println("Data was sorted correctly!");
            else
                System.out.println("!!! ERROR: data not sorted correctly");
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * The following implements a parallel quick sort.
     */
    public static void parallelSort(List<Integer> data, int numProcessors) {
        if (((data.size() / numProcessors) <= 1) || (numProcessors <= 1)) {
            ParSort.sequentialSort(data, 0, data.size());
            return;
        }

        int splitDepth = (int) (Math.log(numProcessors) / Math.log(2));

        ThreadPool pool = new ThreadPool(numProcessors);
        List<Job> jobs = new ArrayList<>();

        ParSort.parallelSort(data, 0, data.size(), jobs, 0, splitDepth);

        for (Job job : jobs)
            pool.submit(job);

        for (Job job : jobs)
            job.waitUntilFinished();
    }

    public static void parallelSort(List<Integer> data, int start, int end, List<Job> jobs,
            int depth, int splitDepth) {
        if (start >= end)
            return;

        // now sort into two sections so stuff in lower section is less than
        // pivot, and remainder is in upper section.
        int pivot = data.get((start + end) / 2);
        int lower = start;
        int upper = end - 1;

        while (lower < upper) {
            int lowerItem = data.get(lower);
            int upperItem = data.get(upper);
            if (lowerItem < pivot)
                // this item is not out of place
                lower++;
            else if (upperItem > pivot)
                // this item is not out of place
                upper--;
            else {
                // both items are out of place --- so swap them.
                data.set(lower, upperItem);
                data.set(upper, lowerItem);
                if (upperItem != pivot)
                    lower++;
                if (lowerItem != pivot)
                    upper--;
            }
        }

        data.set(lower, pivot);

        ParSort.pause(data, 100); // to make animation in display mode better

        // A this point, lower == upper.
        if (depth == splitDepth) {
            jobs.add(new SortJob(data, start, lower));
            jobs.add(new SortJob(data, lower + 1, end));
        } else {
            ParSort.parallelSort(data, start, lower, jobs, depth + 1, splitDepth);
            ParSort.parallelSort(data, lower + 1, end, jobs, depth + 1, splitDepth);
        }
    }

    // don't worry about what this method does.
    public static void pause(List<Integer> data, int delay) {
        if (data instanceof DisplayList)
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
    }

    public static ArrayList<Integer> readInput(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);

        String line;
        ArrayList<Integer> data = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (line.equals(""))
                continue;
            data.add(Integer.parseInt(line));
        }
        return data;
    }

    /**
     * The following implements a sequential quick sort.
     * 
     * @param list data to be sorted.
     * @param start start position within data for sort.
     * @param end end position within data for sort.
     */
    public static void sequentialSort(List<Integer> list, int start, int end) {
        if (start >= end)
            return;
        // recursive case

        // now sort into two sections so stuff in lower section is less than
        // pivot, and remainder is in upper section.
        int pivot = list.get((start + end) / 2);
        int lower = start;
        int upper = end - 1;

        while (lower < upper) {
            int lowerItem = list.get(lower);
            int upperItem = list.get(upper);
            if (lowerItem < pivot)
                // this item is not out of place
                lower++;
            else if (upperItem > pivot)
                // this item is not out of place
                upper--;
            else {
                // both items are out of place --- so swap them.
                list.set(lower, upperItem);
                list.set(upper, lowerItem);
                if (upperItem != pivot)
                    lower++;
                if (lowerItem != pivot)
                    upper--;
            }
        }

        list.set(lower, pivot);

        ParSort.pause(list, 100); // to make animation in display mode better

        // A this point, lower == upper.
        ParSort.sequentialSort(list, start, lower);
        ParSort.sequentialSort(list, lower + 1, end);
    }

    public static class SortJob extends Job {

        private final List<Integer> data;
        private final int start;
        private final int end;

        public SortJob(List<Integer> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            ParSort.sequentialSort(data, start, end);
        }
    }
}
