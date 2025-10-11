package com.example.CsvAnalyser;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;






@SpringBootApplication
public class CsvAnalyserApplication  {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {
		SpringApplication.run(CsvAnalyserApplication.class, args);
	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		if (args.length < 2) {
//			System.err.println("Usage: java -jar CsvAnalyser-0.0.1-SNAPSHOT.jar inputFile=<path> outputFolder=<path>");
//			return;
//		}
//
//		String inputFile = args[0].split("=")[1];
//		String outputFolder = args[1].split("=")[1];
//
//		System.out.println("Input file:  " + inputFile);
//		System.out.println("Output dir:  " + outputFolder);
//
//		JobParameters jobParameters = new JobParametersBuilder()
//				.addString("inputFile", inputFile)
//				.addString("outputFolder", outputFolder)
//				.addLong("timestamp", System.currentTimeMillis()) // ensures uniqueness
//				.toJobParameters();
//
//		JobExecution execution = jobLauncher.run(job, jobParameters);
//		System.out.println("Job finished with status: " + execution.getStatus());
//	}
}



