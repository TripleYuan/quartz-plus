package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerMenu;

import java.util.List;

public interface MenuRepository extends CrudRepository<QuartzSchedulerMenu, Integer> {

    List<QuartzSchedulerMenu> findByMenuStatus(int menuStatus);
}
