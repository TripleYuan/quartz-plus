package redcoder.quartzplus.schedcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redcoder.quartzplus.core.annotation.QuartzJobScan;

@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.schedcenter.job")
public class QuartzPlusSchedulerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusSchedulerCenterApplication.class, args);
    }

}
