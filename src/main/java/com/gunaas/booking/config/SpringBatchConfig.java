package com.gunaas.booking.config;

import com.gunaas.booking.batch.BookingDtoFieldSetMapper;
import com.gunaas.booking.batch.BookingProcessor;
import com.gunaas.booking.batch.DBWriter;
import com.gunaas.booking.model.Booking;
import com.gunaas.booking.multithreading.ConnectionPool;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * All the necessary beans are created here
 * Spring batch beans & Application beans
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public BookingDtoFieldSetMapper bookingDtoFieldSetMapper;

    @Autowired
    public DBWriter writer;

    @Autowired
    public BookingProcessor bookingProcessor;

    private static final String[] FORMAT = new String[]{"hotel_id", "booking_id", "customer_id", "selling_price_local_currency", "currency", "to_usd_exchange_rate"};

    @Bean
    public Job job() {
        return jobBuilderFactory.get("Booking-loader").incrementer(new RunIdIncrementer()).flow(step()).end().build();
    }

    @Bean
    public Step step() {
        Step step = stepBuilderFactory.get("Booking-loader-step")
                .<Booking, Booking>chunk(1000)
                .reader(flatFileItemReader())
                .processor(bookingProcessor)
                .writer(writer)
                .build();
        return step;
    }

    @Bean
    public FlatFileItemReader<Booking> flatFileItemReader() {
        FlatFileItemReaderBuilder<Booking> flatFileItemReaderBuilder = new FlatFileItemReaderBuilder<>();
        flatFileItemReaderBuilder.name("Booking Data CSV Reader");
        flatFileItemReaderBuilder.resource(new FileSystemResource("/Users/gunaalan.s/Workspace/gunaas-agoda-booking-service/src/main/resources/BookingData.csv"));
        flatFileItemReaderBuilder.delimited().names(FORMAT).linesToSkip(1);
        flatFileItemReaderBuilder.lineMapper(lineMapper());
        flatFileItemReaderBuilder.fieldSetMapper(bookingDtoFieldSetMapper);
        return flatFileItemReaderBuilder.build();
    }

    @Bean
    public LineMapper<Booking> lineMapper() {

        DefaultLineMapper<Booking> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(FORMAT);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(bookingDtoFieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(dataSourceUrl, dataSourceUsername, dataSourcePassword);
    }
}
