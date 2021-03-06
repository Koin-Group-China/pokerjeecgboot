package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.Relationship;
import org.jeecg.modules.system.vo.RelationshipVO;

import java.util.List;

/**
 * @Description: 商品滤芯关联
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
public interface IRelationshipService extends IService<Relationship> {
    /**
     * 根据商品id查询滤芯
     * @param id
     * @return
     */
    IPage<RelationshipVO> selectItemsByMainId(Page page, String commodityId);
    /**
     * 根据商品id查询已添加滤芯总数
     * @param commodityId
     * @return
     */
    Integer selectBycommodityId(String commodityId);

    /**
     * 根据类型id删除类型对应的滤芯记录
     * @param typeId
     * @return
     */
    void deletetypeId(String typeId);

    /**
     * 添加类型对应的滤芯
     * @param list
     * @return
     */
    boolean insertByfilterelementid(List<Relationship> list);

    /**
     * 根据商品id查询，类型中配置的滤芯id集合
     * @param SPid
     * @return
     */
    List<String> selectLXid(String SPid);

    /**
     * 根据滤芯id查询关联的数量
     * @return
     */
    Integer SelectById(@Param("LvId") String LvId);

}
