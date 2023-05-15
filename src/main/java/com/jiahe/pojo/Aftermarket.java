package com.jiahe.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author  
 * @since 2023-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("aftermarket")
public class Aftermarket implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String type;
    private String cause;
    private String description;
    private String image;
    @TableField(exist = false)
    private int status;
    private Integer orderId;
    private Integer orderCommodityId;
    private Integer isDeleted;

}
