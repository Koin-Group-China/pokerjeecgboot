package org.jeecg.modules.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@TableName("commodity_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="commodity_type对象", description="商品类型")
public class CommodityType {

    /**商品类型id*/
    @Excel(name = "商品类型id", width = 15)
    @ApiModelProperty(value = "商品类型id")
    @TableId(type=IdType.UUID)
    private String typeId;
    /**类型名称*/
    @Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
    private String typeName;
    /**滤芯规格*/
    @Excel(name = "滤芯规格", width = 15)
    @ApiModelProperty(value = "滤芯规格")
    private Integer specification;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**更新人*/
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新时间*/
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
