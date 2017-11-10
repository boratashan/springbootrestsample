package com.sprinbootsamples.restsample.configuration;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final String prfx;
    public ApiVersionRequestMappingHandlerMapping(String prfx) {
        this.prfx = prfx;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if(info == null) return null;
        VersionedApi methodAnnotation = AnnotationUtils.findAnnotation(method, VersionedApi.class);
        if(methodAnnotation != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            info = createVerInfo(methodAnnotation, methodCondition).combine(info);
        } else {
            VersionedApi typeAnnotation = AnnotationUtils.findAnnotation(handlerType, VersionedApi.class);
            if(typeAnnotation != null) {
                RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                info = createVerInfo(typeAnnotation, typeCondition).combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createVerInfo(VersionedApi annotation, RequestCondition<?> customCondition) {
        int[] values = annotation.value();
        String[] patterns = new String[values.length];
        for(int i=0; i<values.length; i++) {
            patterns[i] = prfx+values[i];
        }

        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }

}
