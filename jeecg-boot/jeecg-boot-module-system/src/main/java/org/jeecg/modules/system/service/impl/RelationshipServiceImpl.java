package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.Relationship;
import org.jeecg.modules.system.mapper.RelationshipMapper;
import org.jeecg.modules.system.service.IRelationshipService;
import org.jeecg.modules.system.vo.RelationshipVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 商品滤芯关联
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, RelationshipVO> implements IRelationshipService {
    private RelationshipMapper relationshipMapper;

    @Override
    public IPage<RelationshipVO> selectItemsByMainId(Page page, String commodityId) {
        return page.setRecords(relationshipMapper.selectItemsByMainId(commodityId));
    }
}
