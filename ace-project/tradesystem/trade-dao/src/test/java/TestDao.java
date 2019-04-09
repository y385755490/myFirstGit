import com.ace.trade.entity.TradeUser;
import com.ace.trade.mapper.TradeUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-dao.xml")
public class TestDao {
    @Autowired
    private TradeUserMapper tradeUserDao;

    @Test
    public void test(){
        TradeUser record = new TradeUser();
        record.setUserName("zhangsan");
        record.setUserPassword("123456");
        int i = tradeUserDao.insert(record);
        System.out.println("i=" + i);
    }

}
