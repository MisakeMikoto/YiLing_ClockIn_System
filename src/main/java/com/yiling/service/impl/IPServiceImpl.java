package com.yiling.service.impl;

import com.yiling.dao.IPDao;
import com.yiling.domain.IP;
import com.yiling.service.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/28 20:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IPServiceImpl implements IPService {

    @Autowired
    private IPDao ipDao;
    @Override
    public List<IP> getIps() {
        return ipDao.selectList(null);
    }
}
