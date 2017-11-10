
package com.sprinbootsamples.restsample.util.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

public class LoggingFilter extends OncePerRequestFilter {

    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQ_PRFX = "REQUEST < ";
    private static final String RESP_PRFX = "RESPONSE > ";
    private AtomicLong logId = new AtomicLong(0);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        long requestId = logId.incrementAndGet();
        request = new RequestWrapper(requestId, request);
        response = new ResponseWrapper(requestId, response);

        try {
            filterChain.doFilter(request, response);
        } finally {
            logRequest(request);
            logResponse((ResponseWrapper) response);
        }

    }

    private void logRequest(final HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(REQ_PRFX);
        if (request instanceof RequestWrapper) {
            msg.append("request logId=").append(((RequestWrapper) request).getId()).append("; ");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append("session logId=").append(session.getId()).append("; ");
        }
        if (request.getMethod() != null) {
            msg.append("method=").append(request.getMethod()).append("; ");
        }
        if (request.getContentType() != null) {
            msg.append("content type=").append(request.getContentType()).append("; ");
        }
        msg.append("uri=").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            msg.append('?').append(request.getQueryString());
        }

        if (request instanceof RequestWrapper && !isMultipart(request) && !isBinaryContent(request)) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            try {
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
                        "UTF-8";
                msg.append("; msgBody=").append(new String(requestWrapper.toByteArray(), charEncoding));
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse request msgBody", e);
            }

        }
        logger.trace(msg.toString());
    }

    private boolean isBinaryContent(final HttpServletRequest request) {
        if (request.getContentType() == null) {
            return false;
        }
        return request.getContentType().startsWith("image") || request.getContentType().startsWith("video") || request.getContentType().startsWith("audio");
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
    }

    private void logResponse(final ResponseWrapper response) {
        StringBuilder msg = new StringBuilder();
        msg.append(RESP_PRFX);
        msg.append("request logId=").append((response.getId()));
        try {
            msg.append("; msgBody=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to parse response msgBody", e);
        }
        logger.trace(msg.toString());
    }

}
