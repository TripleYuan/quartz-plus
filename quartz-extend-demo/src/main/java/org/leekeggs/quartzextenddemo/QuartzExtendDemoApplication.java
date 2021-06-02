package org.leekeggs.quartzextenddemo;

import org.leekeggs.quartzextendcore.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@QuartzJobScan("org.leekeggs.quartzextenddemo.job")
public class QuartzExtendDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzExtendDemoApplication.class, args);
    }

}
