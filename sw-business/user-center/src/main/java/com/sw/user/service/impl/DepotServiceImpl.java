package com.sw.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.user.Depot;
import com.sw.user.mapper.DepotMapper;
import com.sw.user.service.IDepotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("depotService")
@Transactional(rollbackFor = Exception.class)
public class DepotServiceImpl extends ServiceImpl<DepotMapper, Depot> implements IDepotService {

}
