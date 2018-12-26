package com.akr.leadiq.test;

import com.akr.leadIq.datastore.JobUrlLists;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestUtils {
    public static ResultMatcher jobStatusIsCorrect(JobUrlLists expected) {
        return jobStatusIsCorrect(Long.valueOf(expected.getId()), expected);
    }

    private static ResultMatcher jobStatusIsCorrect(Long expectedId, JobUrlLists expected) {
        return new CombinedMatcher().addMatcher(jsonPath("$.id").value(expectedId))
                .addMatcher(jsonPath("$.created").value(expected.getCreated()))
                .addMatcher(jsonPath("$.status").value(expected.getStatus()))
                .addMatcher(jsonPath("$.uploaded.pending").value(expected.getPending()));
    }
}
