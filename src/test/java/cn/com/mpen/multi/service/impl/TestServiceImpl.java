package cn.com.mpen.multi.service.impl;

import cn.com.mpen.core.common.constant.DatasourceEnum;
import cn.com.mpen.core.kernel_core.mutidatasource.annotion.DataSource;
import cn.com.mpen.multi.entity.Test;
import cn.com.mpen.multi.mapper.TestMapper;
import cn.com.mpen.multi.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fengshuonan
 * @since 2018-07-10
 */
public class TestServiceImpl implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    @DataSource(name = DatasourceEnum.DATA_SOURCE_BIZ)
    @Transactional
    public void testBiz() {
        Test test = new Test();
        test.setBbb("bizTest");
        testMapper.insert(test);
    }

    @Override
    @DataSource(name = DatasourceEnum.DATA_SOURCE_GUNS)
    @Transactional
    public void testGuns() {
        Test test = new Test();
        test.setBbb("gunsTest");
        testMapper.insert(test);
    }
}
