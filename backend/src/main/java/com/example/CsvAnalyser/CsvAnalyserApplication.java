package com.example.CsvAnalyser;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CsvAnalyserApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {
		SpringApplication.run(CsvAnalyserApplication.class, args);
	}

	@Autowired
	public void runJob() throws Exception {
		JobExecution execution = jobLauncher.run(job, new JobParameters());
		System.out.println("Job finished with status: " + execution.getStatus());
	}

}
