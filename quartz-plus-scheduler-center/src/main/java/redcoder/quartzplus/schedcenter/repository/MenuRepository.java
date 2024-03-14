package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusMenu;

import java.util.List;

public interface MenuRepository extends CrudRepository<QuartzPlusMenu, Integer> {

    List<QuartzPlusMenu> findByMenuStatus(int menuStatus);
}
