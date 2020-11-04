package com.ams;

public class Main {
    public static void main(String[] args) {
	    if(args.length < 1) {
	        System.out.println("Usage: java -jar ExamScheduler.jar <input_file_path>");
	        return;
        }

        ExamScheduler scheduler = new ExamScheduler(args[0]);
	    scheduler.schedule();
	    System.out.println("Scheduling is complete.");
	    scheduler.report();
    }
}
