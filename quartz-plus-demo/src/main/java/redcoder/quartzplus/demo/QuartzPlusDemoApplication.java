package redcoder.quartzplus.demo;

import redcoder.quartzplus.core.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.demo.job")
public class QuartzPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusDemoApplication.class, args);
    }

}
