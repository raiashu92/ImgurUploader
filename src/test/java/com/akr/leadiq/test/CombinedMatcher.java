package com.akr.leadiq.test;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

public class CombinedMatcher implements ResultMatcher {
	
	private List<ResultMatcher> matchers = new ArrayList<>();

	@Override
	public void match(MvcResult result) throws Exception {

		for (ResultMatcher matcher: matchers) {
			matcher.match(result);
		}
	}
	
	public CombinedMatcher addMatcher(ResultMatcher matcher) {
		matchers.add(matcher);
		return this;
	}

}
