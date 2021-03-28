package site.bulibucai.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @see <a href="https://my.oschina.net/u/4344785/blog/3808710">代码创建 Druid 数据源</a>
 */
public class MyDruidDataSourceFactory extends UnpooledDataSourceFactory {

    public MyDruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }
}
