package redcoder.quartzextenddemo;

import redcoder.quartzextendcore.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@QuartzJobScan("redcoder.quartzextenddemo.job")
public class QuartzExtendDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzExtendDemoApplication.class, args);
    }

}
