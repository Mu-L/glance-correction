package io.github.rovingsea.glancecorrection.sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Haixin Wu
 * @since 2022-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class From2 extends Model<From2> {

    /**
     * 主键唯一id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
