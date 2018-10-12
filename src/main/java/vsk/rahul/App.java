package vsk.rahul;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.LookAndFeel;

/**
 * 
 *
 */
public class App {
	
	static final Pattern regex = Pattern.compile("[$][{]([\\w\\d])*([${\\w\\d}])*[}]");
	static final String endRegex = "[}]{1}$";
	static final String startRegex = "^[$][{]{1}";
	
	public static void main(String[] args) throws Exception {
		
		Properties properties = new Properties();
		properties.load(App.class.getResourceAsStream("/properties-parser.properties"));
		System.out.println(properties);
		compile(properties);
		System.out.println(properties);
		
	}
	
	static void compile(final Properties props) {
		props.keySet().stream().map(k -> (String)k).forEach(k -> compile(props, k));
	}
	
	static String compile(Properties props, String k) {
		String v = (String)props.getProperty(k);
		
		if(Objects.isNull(v)) {
			return k;
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
		actualValue = !actualValue.equals(lookupKey) 
						? actualValue : props.getProperty(k);
		props.setProperty(k, actualValue);
		return props.getProperty(k);
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
}