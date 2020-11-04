package com.ams;

import java.util.*;

/**
 * Created by shimon on 8/26/18.
 */
public class ExamScheduler {
    private ArrayList<Student> students;
    private Map<String, ArrayList<Student>> enrollMap;
    private Map<String, Integer> adjacencyMatrix;
    private Map<Integer, Set<String>> slots;
    private InputReader inputReader;
    private List<String> candidates;

    public ExamScheduler(String fileName) {
        inputReader = new InputReader(fileName);
        slots = new HashMap<Integer, Set<String>>();
        adjacencyMatrix = new HashMap<String, Integer>();
    }

    public Map<Integer, Set<String>> getSlots() {
        return slots;
    }

    public void report() {
        for(Integer index: slots.keySet()) {
            StringJoiner joiner = new StringJoiner(",");
            int studentCount = 0;
            for(String s:slots.get(index)) {
                joiner.add(s);
                studentCount += enrollMap.get(s).size();
            }
            System.out.println("Slot " + Integer.toString(index + 1 ) + ": " + joiner.toString() + "\t" + Integer.toString(studentCount));
        }
    }

    public void schedule() {
        // step 1: read input and construct adjacency matrix
        readInput();

        // report for step 1
        System.out.println("Total " + Integer.toString(students.size()) + " student/s has/have enrolled in total " + Integer.toString(enrollMap.size()) + " subjects.");

        // step 2: construct candidate list of subjects
        candidates = new ArrayList<String>(enrollMap.keySet());

        // check if no subject at all
        if (candidates.isEmpty()) {
            System.out.println("No subject found to schedule");
            return;
        }

        // step 3: order the candidates in descending order by total number of inconnectivity
        // meaning less number of students enrolled in it. This means
        // ordering the candidate list of subjects in ascending order of the number of enrolled
        // students. it is using a bubble sort.
        System.out.println("Ordering candidate list of subjects...");
        adjacencyMatrix = inputReader.getAdjacencyMatrix();

        /*
        System.out.println("Adjacency Matrix:");
        for(String s:adjacencyMatrix.keySet()) {
            System.out.println(s + " : " + Integer.toString(adjacencyMatrix.get(s)));
        }
        */

        for (int i = 0; i < candidates.size() - 1; i++) {
            for (int j = 0; j < candidates.size() - 1; j++) {
                if (adjacencyMatrix.get(candidates.get(j + 1)) > adjacencyMatrix.get(candidates.get(j))) {
                    // swap candidate position
                    String tmp = candidates.get(j);
                    candidates.set(j, candidates.get(j + 1));
                    candidates.set(j + 1, tmp);
                }
            }
        }

        // step 4: run greedy algorithm to schedule exams
        int slotCount = 0;
        while (true) {
            // break the loop if all subjects are scheduled
            if (candidates.isEmpty()) {
                break;
            }

            // peek the first candidate subject if exists
            String subject = candidates.get(0);

            // put it in a new slot
            Set<String> slot = new HashSet<String>();
            slot.add(subject);
            slots.put(slotCount, slot);

            // remove the subject from candidates
            candidates.remove(subject);

            // search for another inconnected candidate subject
            greedySearch(slot, 0);

            // increase slot index/count
            slotCount++;
        }
    }

    private void greedySearch(Set<String> slot, int searchIndex) {
        // check for empty candidate list
        if (candidates.isEmpty()) {
            return;
        }

        // take the candidate subject
        String subject = candidates.get(searchIndex);

        // search for connectivity
        boolean connected = false;
        for (Student st : enrollMap.get(subject)) {
            for (String candidate : slot) {
                if (st.subjects.contains(candidate)) {
                    connected = true;
                    break;
                }
            }
            if (connected) {
                // no need to iterate further
                break;
            }
        }

        if (connected) {
            // connection found, increase search index and move on
            if (searchIndex + 1 == candidates.size()) {
                return;
            }
            greedySearch(slot, searchIndex + 1);
        } else {
            // connectivity found
            slot.add(subject);
            candidates.remove(subject);

            // start over
            greedySearch(slot, 0);
        }
    }

    private void readInput() {
        System.out.println("Reading input...");
        inputReader.readInput();
        students = inputReader.getStudents();
        enrollMap = inputReader.getEnrollMap();
    }
}
