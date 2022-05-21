package redcoder.quartzextendschedulercenter;

import redcoder.quartzextendcore.annotation.QuartzJobScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("redcoder.quartzextendschedulercenter.mapper")
@QuartzJobScan("redcoder.quartzextendschedulercenter.job")
public class QuartzExtendSchedulerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzExtendSchedulerCenterApplication.class, args);
    }

}
