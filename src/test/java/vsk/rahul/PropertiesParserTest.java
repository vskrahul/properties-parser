package vsk.rahul;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PropertiesParser}.
 */
public class PropertiesParserTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test(alwaysRun=true, priority=1, skipFailedInvocations=false)
	public void matcher() throws Exception {
		
		Method method = PropertiesParser.class.getDeclaredMethod("matcher", String.class);
		method.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>)method.invoke(PropertiesParser.class, 
				"${wdwew}.shgfhf.${}.khk.${hkj}.kjhk.${${}cjh${}hg}");
		
		Assert.assertNotNull(list);
		Assert.assertEquals(list, Arrays.asList(new String[]{"${wdwew}", "${}", "${hkj}", "${${}cjh${}hg}"}));
		logger.info("matcher is working fine : {}", list);
		method.setAccessible(false);
	}
	
	@Test(alwaysRun=true, priority=2, skipFailedInvocations=false, dataProvider="properties")
	public void compileKey(Object[] data) throws Exception {
		
		Properties p = (Properties)data[0];
		
		Method method = PropertiesParser.class.getDeclaredMethod("compile", Properties.class, String.class);
		method.setAccessible(true);
		
		String value = (String)method.invoke(PropertiesParser.class, p, "name");
		
		Assert.assertNotNull(value);
		Assert.assertEquals(value, String.format("%s %s", p.getProperty("fname"), p.getProperty("lname")));
		logger.info("compiled key : {}", value);
		method.setAccessible(false);
	}
	
	@Test(alwaysRun=true, priority=3, skipFailedInvocations=false, dataProvider="compiledProperties")
	public void compiledProperties(Properties p, Properties actualP) throws Exception {
		
		Method method = PropertiesParser.class.getDeclaredMethod("compile", Properties.class);
		method.setAccessible(true);
		
		method.invoke(PropertiesParser.class, p);
		
		Assert.assertEquals(actualP, p);
		logger.info("compiled properties : {}", p);
		method.setAccessible(false);
	}
	
	@DataProvider(name="properties")
	private Object[] properties() {
		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream("/properties-parser.properties"));
		} catch(Exception e) {
			Assert.fail("Can not load property file: /test/resources/properties-parser.properties");
		}
		return new Object[]{p};
	}
	
	@DataProvider(name="compiledProperties")
	private Object[][] compiledProperties() {
		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream("/compiled-properties-parser.properties"));
		} catch(Exception e) {
			Assert.fail("Can not load property file: /test/resources/compiled-properties-parser.properties");
		}
		Object[][] result =  new Object[][]{{(Properties)properties()[0], p}};
		return result;
	}
}