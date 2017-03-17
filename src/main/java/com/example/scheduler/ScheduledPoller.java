package com.example.scheduler;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Simple scheduler. Currently using fixedRate of every 5 seconds.
 * Cron expression setup (commented) for hourly.
 * To disable the scchedulers see SpringBootWebApplication
 *
 * @see com.example.SpringBootWebApplication
 */
@Component
public class ScheduledPoller {

    private static final Logger log = LoggerFactory.getLogger(ScheduledPoller.class);
    private static String UPLOADED_FOLDER = "/tmp/uploadFolder/";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // cron expression below is set to run for every hour. Uncomment to schedule.
    // For demo purpose, I'm setting to fixRate of every 5 seconds
    // @Scheduled(cron = "0 0 0/1 1/1 * ? *")
    @Scheduled(fixedRate = 5000)
    public void dirPoller() {

        // Haven't yet tested. Setup a filter to get all files updated in past 1 hour
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                BasicFileAttributes attr = Files.readAttributes(entry, BasicFileAttributes.class);
                // get file creating time
                long fileCreationTime = attr.creationTime().toMillis();

                // get the current time and set it back 1 hour
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, -1);
                return fileCreationTime > cal.getTimeInMillis();
            }
        };

        // now get all the filtered files
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(UPLOADED_FOLDER), filter);
            StringBuilder result = new StringBuilder();
            for (Path path : stream) {
                result.append(path.getFileName().toString());
            }
            stream.close();

            // todo: now these files can be reported via email, etc.

        } catch (IOException e) {
            log.warn("Error reading directory: {}", e.getMessage());
        }


        log.info("Scheduler. The time is now {}", dateFormat.format(new Date()));
    }
}
