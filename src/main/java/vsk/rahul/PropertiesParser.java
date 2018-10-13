package vsk.rahul;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Class to parse the property file which has dynamic references with each other properties.
 * 
 * <p>
 * E.g.;
 * <ol>
 * 	<li>a=1
 * 	<li>b=${a}+2
 *  <li>c=${d}
 *  <li>env=dev
 *  <li>conection.dev=100
 *  <li>max.connection=${connection.${env}}*1/2
 * </ol>
 * 
 * <b>After parsing it will be like...</b>
 * <ol>
 * 	<li>a=1
 * 	<li>b=1+2
 *  <li>c=${d}
 *  <li>env=dev
 *  <li>conection.dev=100
 *  <li>max.connection=100*1/2
 * </ol>
 *  <p>
 *  Note: It has been assumed that there are no circular referencing properties.
 *  
 */
public class PropertiesParser {
	
	static Logger logger = LoggerFactory.getLogger(PropertiesParser.class);
	
	static final Pattern regex = Pattern.compile("[$][{]([\\w\\d\\.])*([${\\w\\d\\.}])*[}]");
	static final String endRegex = "[}]{1}$";
	static final String startRegex = "^[$][{]{1}";
	
	static void compile(final Properties props) {
		Objects.nonNull(props);
		props.keySet().stream().map(k -> (String)k).forEach(k -> compile(props, k));
	}
	
	static String compile(Properties props, String k) {
		String v = (String)props.getProperty(k);
		
		if(Objects.isNull(v)) {
			v = k;
		}
		
		if(!regex.matcher(v).find()) {
			return v;
		}
		String[] group = matcher(v);
		String actualValue = v;
		String lookupKey = "";
		for(String s : group) {
			/*
			 * Now we got actual key to compile again.
			 */
			lookupKey = s.replaceAll(startRegex, "").replaceAll(endRegex, "");
			actualValue = actualValue.replace(s, compile(props, lookupKey));
		}
		if(!Objects.isNull(props.getProperty(k))) props.setProperty(k, actualValue);
		return !Objects.isNull(props.getProperty(k)) ? props.getProperty(k) : props.getProperty(actualValue);
	}
	
	static String[] matcher(String v) {
		Matcher m = regex.matcher(v);
		List<String> list = new ArrayList<>();
		while(m.find()) {
			list.add(m.group());
		}
		String[] s = new String[list.size()];
		return list.toArray(s);
	}
	
	public static void main(String[] args) throws Exception {
		
		Properties properties = new Properties();
		properties.load(PropertiesParser.class.getResourceAsStream("/properties-parser.properties"));
		System.out.println(properties);
		compile(properties);
		System.out.println(properties);
		
	}
}