package com.k66.cxzt.web.handler;

import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.IOException;

@Component
public class MessageI18NProcessor {
	private static final String PATH = "classpath*:i18n/*.%s.properties";
	private static Resource[] resources;
	private static ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
	private CompositeConfiguration config;

	public String getMessage(String k){
		if(null == resources){
			init(getRequestLocale());
		}
		String v = config.getString(k);
		return null == v ? k : v;
	}

	private String getRequestLocale(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getLocale().toLanguageTag();
	}

	private void init(String locale){
		locale = locale.replace("-" , "_");
		String path = String.format(PATH , locale.toLowerCase());
		try {
			resources = resourceResolver.getResources(path);
			config = read(resources);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.ERROR , e.getMessage() , e);
		}
	}

	private CompositeConfiguration read(Resource[] resources) throws IOException, ConfigurationException {
		CompositeConfiguration configuration = new CompositeConfiguration();
		for(Resource r : resources){
			PropertiesConfiguration props = new PropertiesConfiguration();
			props.read(new FileReader(r.getFile()));
			configuration.addConfiguration(props);
		}
		return configuration;
	}
}
