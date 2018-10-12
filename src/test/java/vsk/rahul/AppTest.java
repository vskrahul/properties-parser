package vsk.rahul;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void matcher() {
		String[] g = App.matcher("${wdwew}.shgfhf.${}.khk.${hkj}.kjhk.${${}cjh${}hg}");
		logger.info(Arrays.toString(g));
		Assert.assertNotNull(g);
		Assert.assertEquals(Arrays.toString(g), "[${wdwew}, ${}, ${hkj}, ${${}cjh${}hg}]");
	}
	
}