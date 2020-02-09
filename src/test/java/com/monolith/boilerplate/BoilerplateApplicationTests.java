package com.monolith.boilerplate;

import com.monolith.boilerplate.config.AppProperties;
import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoilerplateApplication.class)
class BoilerplateApplicationTests {

	@Autowired
	private AppProperties appProperties;

	@Test
	void contextLoads() {
		Assert.assertNotNull(appProperties.getAuth());
		Assert.assertNotNull(appProperties.getAuth().getTokenSecret());
		Assert.assertNotNull(appProperties.getAuth().getTokenExpirationMsec());
		Assert.assertNotNull(appProperties.getAuth().getIssuer());
		Assert.assertNotNull(appProperties.getOAuth2());
		Assert.assertNotNull(appProperties.getOAuth2().getAuthorizedRedirectUris());
		Assert.assertNotEquals(0, appProperties.getOAuth2().getAuthorizedRedirectUris().size());
	}

}
