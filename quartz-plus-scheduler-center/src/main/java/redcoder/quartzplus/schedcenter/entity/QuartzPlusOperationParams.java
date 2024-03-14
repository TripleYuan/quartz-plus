package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "QuartzPlusOperationParams")
@Table(name = "quartz_plus_operation_params")
@Data
public class QuartzPlusOperationParams {

    @Id
    @Column(name = "`params_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paramsId;

    @Column(name = "`in_params`")
    private String inParams;

    @Column(name = "`out_params`")
    private String outParams;
}
