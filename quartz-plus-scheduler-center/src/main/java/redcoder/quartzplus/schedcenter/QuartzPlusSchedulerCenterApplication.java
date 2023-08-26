package redcoder.quartzplus.schedcenter;

import redcoder.quartzplus.core.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.schedcenter.job")
public class QuartzPlusSchedulerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusSchedulerCenterApplication.class, args);
    }

}
