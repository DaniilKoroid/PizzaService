package ua.rd.pizzaservice.service.order;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/repositoryTestContext.xml"
})
@ActiveProfiles("dev")
public class RepositoryTestConfig {

}
