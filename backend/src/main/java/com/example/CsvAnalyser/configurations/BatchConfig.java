//package com.example.CsvAnalyser.configurations;
//
//import com.example.CsvAnalyser.models.LogEntry;
//import com.example.CsvAnalyser.services.LogItemProcessor;
//import com.example.CsvAnalyser.services.ReportWriter;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.validation.BindException;
//
//import java.io.File;
//
//@Configuration
//public class BatchConfig {
//
//    // === Resources ===
//    @Bean
//    public ClassPathResource logCsv() {
//        return new ClassPathResource("input/logs.csv");
//    }
//
//
//
//
//    @Bean
//    public ItemProcessor<LogEntry, LogEntry> processor() throws Exception {
//        return new LogItemProcessor();
//    }
//
//    @Bean
//    public ItemWriter<LogEntry> writer() {
//        return new ReportWriter();
//    }
//
//    // === Transaction Manager ===
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }
//
//    // === Data Source for JobRepository (H2 embedded) ===
//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
//                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
//                .build();
//    }
//
//    // === Job Repository ===
//    @Bean
//    public JobRepository jobRepository() throws Exception {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setDataSource(dataSource());
//        factory.setTransactionManager(transactionManager());
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }
//
//    // === Job Launcher ===
//    @Bean
//    public JobLauncher jobLauncher() throws Exception {
//        var launcher = new org.springframework.batch.core.launch.support.TaskExecutorJobLauncher();
//        launcher.setJobRepository(jobRepository());
//        launcher.setTaskExecutor(taskExecutor());
//        launcher.afterPropertiesSet();
//        return launcher;
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor("batch_executor");
//    }
//
//    // === Step ===
//    @Bean
//    public Step step1(JobRepository jobRepository,
//                      PlatformTransactionManager transactionManager,
//                      FlatFileItemReader<LogEntry> reader,
//                      ItemProcessor<LogEntry, LogEntry> processor,
//                      ItemWriter<LogEntry> writer) {
//
//        return new org.springframework.batch.core.step.builder.StepBuilder("step1", jobRepository)
//                .<LogEntry, LogEntry>chunk(100, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//
//    // === Job ===
//    @Bean
//    public Job logAnalysisJob(JobRepository jobRepository, Step step1) {
//        return new JobBuilder("logAnalysisJob", jobRepository)
//                .start(step1)
//                .preventRestart()
//                .build();
//    }
//
//
//    @Bean
//    public FlatFileItemReader<LogEntry> reader(Resource logCsv) {
//        FlatFileItemReader<LogEntry> reader = new FlatFileItemReader<>();
//        reader.setResource(logCsv);
//        reader.setLinesToSkip(1); // skip header
//
//        // Configure tokenizer
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setNames("IP","Time","Path","Response"); // adjust according to your CSV columns
//
//        // Configure mapper
//        DefaultLineMapper<LogEntry> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(tokenizer);
//        lineMapper.setFieldSetMapper((FieldSetMapper<LogEntry>) new LogEntryFieldSetMapper());
//
//        reader.setLineMapper(lineMapper);
//        return reader;
//    }
//
//    // FieldSetMapper to map CSV row to LogEntry
//    public static class LogEntryFieldSetMapper implements FieldSetMapper<LogEntry> {
//        @Override
//        public LogEntry mapFieldSet(FieldSet fieldSet) throws BindException {
//            LogEntry entry = new LogEntry();
//            entry.setTime(fieldSet.readString("IP"));
//            entry.setCountry(fieldSet.readString("Time"));
//            entry.setPath(fieldSet.readString("Path"));
//            entry.setResponseStatus(Integer.parseInt(fieldSet.readString("Response")));
//            return entry;
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//}
//



package com.example.CsvAnalyser.configurations;

import com.example.CsvAnalyser.models.LogEntry;
import com.example.CsvAnalyser.services.LogItemProcessor;
import com.example.CsvAnalyser.services.MetricsCollector;
import com.example.CsvAnalyser.services.ReportWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

@Configuration
public class BatchConfig {

    // === CSV resource ===
    @Bean
    public Resource logCsv() {
        return new ClassPathResource("logs.csv");
    }

    // === Reader ===
    @Bean
    public FlatFileItemReader<LogEntry> reader(Resource logCsv, MetricsCollector metricsCollector) {
        FlatFileItemReader<LogEntry> reader = new FlatFileItemReader<>();
        reader.setResource(logCsv);
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("IP", "Time", "Path", "Response");

        DefaultLineMapper<LogEntry> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new LogEntryFieldSetMapper(metricsCollector));

        reader.setLineMapper(lineMapper);
        return reader;
    }


    // === FieldSetMapper ===
    public static class LogEntryFieldSetMapper implements FieldSetMapper<LogEntry> {

        private final MetricsCollector metrics;

        public LogEntryFieldSetMapper(MetricsCollector metrics) {
            this.metrics = metrics;
        }

        @Override
        public LogEntry mapFieldSet(FieldSet fieldSet) throws BindException {
            LogEntry entry = new LogEntry();

            String ip = fieldSet.readString("IP");
            if ("-".equals(ip)) {
                metrics.incrementInvalid("IP");
                entry.setIp(null);
            } else {
                entry.setIp(ip);
            }

            String time = fieldSet.readString("Time");
            if ("-".equals(time)) {
                metrics.incrementInvalid("Time");
                entry.setTime(null);
            } else {
                entry.setTime(time);
            }

            String path = fieldSet.readString("Path");
            if ("-".equals(path)) {
                metrics.incrementInvalid("Path");
                entry.setPath(null);
            } else {
                entry.setPath(path);
            }

            String response = fieldSet.readString("Response");
            if ("-".equals(response)) {
                metrics.incrementInvalid("Response");
                entry.setResponseStatus(0); // assuming Integer type
            } else {
                try {
                    entry.setResponseStatus(Integer.parseInt(response));
                } catch (NumberFormatException e) {
                    metrics.incrementInvalid("Response");
                    entry.setResponseStatus(0);
                }
            }

            return entry;
        }
    }


    // === Processor ===
    @Bean
    public ItemProcessor<LogEntry, LogEntry> processor() {
        return new LogItemProcessor();
    }

    // === Writer ===
    @Bean
    public ItemWriter<LogEntry> writer() {
        return new ReportWriter();
    }

    // === Step ===
    @Bean
    public Step step1(FlatFileItemReader<LogEntry> reader,
                      ItemProcessor<LogEntry, LogEntry> processor,
                      ItemWriter<LogEntry> writer,
                      org.springframework.batch.core.repository.JobRepository jobRepository,
                      org.springframework.transaction.PlatformTransactionManager transactionManager) {

        return new StepBuilder("step1", jobRepository)
                .<LogEntry, LogEntry>chunk(10000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // === Job ===
    @Bean
    public Job logAnalysisJob(org.springframework.batch.core.repository.JobRepository jobRepository,
                              Step step1) {
        return new JobBuilder("logAnalysisJob", jobRepository)
                .start(step1)
                .build();
    }
}
