import com.ace.trade.common.exception.AceMQException;
import com.ace.trade.common.rocketmq.AceMQConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-rocketmq-consumer.xml")
public class ConsumerTest {
    @Autowired
    private AceMQConsumer aceMQConsumer;

    @Test
    public void testConsumer() throws InterruptedException, AceMQException {
        Thread.sleep(1000000L);
    }
}
