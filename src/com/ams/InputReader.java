package com.ams;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shimon on 8/26/18.
 */
public class InputReader {
    private File inputFile;
    private FileReader inputReader;
    private ArrayList<Student> students;
    private Map<String, ArrayList<Student>> enrollMap;
    private Map<String, Integer> adjacencyMatrix;

    public InputReader(String fileName) {
        inputFile = new File(fileName);
        students = new ArrayList<Student>();
        enrollMap = new HashMap<String, ArrayList<Student>>();
        adjacencyMatrix = new HashMap<String, Integer>();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Map<String, ArrayList<Student>> getEnrollMap() {
        return enrollMap;
    }

    public Map<String, Integer> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public boolean readInput() {
        try {
            inputReader = new FileReader(inputFile);
            BufferedReader bufferReader = new BufferedReader(inputReader);
            String line = bufferReader.readLine();
            int lineCount = 0;
            while (line != null) {
                line = line.trim();
                if (line.length() < 1) {
                    System.out.println("Skipping empty line at line no. " + lineCount);
                    lineCount++;
                    line = bufferReader.readLine();
                    continue;
                }

                // first line with student count
                if (lineCount == 0) {
                    line = bufferReader.readLine();
                    lineCount++;
                    continue;
                }

                // student name and subject count
                String[] stInfo = line.split(",");
                if (stInfo.length < 2) {
                    throw new IOException("Error reading student info at line " + Integer.toString(lineCount));
                }

                // construct student object, add to arraylist
                Student st = new Student(stInfo[0]);
                int v = Integer.parseInt(stInfo[1].trim());
                for (int i = 0; i < v; i++) {
                    line = bufferReader.readLine();
                    lineCount++;
                    st.subjects.add(line);
                }
                students.add(st);

                // read next student info
                lineCount++;
                line = bufferReader.readLine();
            }

            // construct enroll map
            for (Student st : students) {
                for (String subject : st.subjects) {
                    if (!enrollMap.containsKey(subject)) {
                        enrollMap.put(subject, new ArrayList<Student>());
                    }
                    enrollMap.get(subject).add(st);
                }
            }

            // construct adjacency matrix
            for(String subject: enrollMap.keySet()) {
                int edgeCount = 0;
                for(Student st:enrollMap.get(subject)) {
                    edgeCount += st.subjects.size();
                }
                adjacencyMatrix.put(subject, edgeCount);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Could not open " + inputFile.getName() + " to read [" + ex.getMessage() + "]\n");
            return false;
        } catch (IOException ex) {
            System.out.println("Could not read input file! [" + ex.getMessage() + "]");
            return false;
        }
        return true;
    }
}
