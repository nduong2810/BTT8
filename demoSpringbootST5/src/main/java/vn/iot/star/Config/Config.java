package vn.iot.star.Config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class Config implements WebMvcConfigurer {

	// Định nghĩa LocaleResolver lưu trữ trong Cookie
	@Bean(name = "localeResolver")
	public LocaleResolver getLocaleResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setCookieName("iot.star.lang");
		resolver.setDefaultLocale(Locale.ENGLISH); // Ngôn ngữ mặc định
		return resolver;
	}

	// Đọc file messages trong thư mục resources/i18n
	@Bean(name = "messageSource")
	public MessageSource getMessageResource() {
		ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
		// không cần ghi đuôi _en hay _vi, Spring sẽ tự động chọn theo locale
		messageResource.setBasename("classpath:i18n/messages");
		messageResource.setDefaultEncoding("UTF-8");
		return messageResource;
	}

	// Cho phép đổi ngôn ngữ qua tham số ?language=xx
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("language");
		registry.addInterceptor(localeInterceptor).addPathPatterns("/**");
	}
}
