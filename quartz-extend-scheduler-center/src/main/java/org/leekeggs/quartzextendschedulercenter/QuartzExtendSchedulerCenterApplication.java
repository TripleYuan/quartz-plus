package org.leekeggs.quartzextendschedulercenter;

import org.leekeggs.quartzextendcore.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.leekeggs.quartzextendschedulercenter.mapper")
@QuartzJobScan("org.leekeggs.quartzextendschedulercenter.job")
public class QuartzExtendSchedulerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzExtendSchedulerCenterApplication.class, args);
    }

}
