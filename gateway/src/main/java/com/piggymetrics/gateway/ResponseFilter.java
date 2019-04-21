package com.piggymetrics.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ResponseFilter extends ZuulFilter {

	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;

	@Autowired
	private Tracer tracer;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.getResponse().addHeader("tmx-correlation-id", tracer.getCurrentSpan().getName());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public String filterType() {
		return "post";
	}
}
