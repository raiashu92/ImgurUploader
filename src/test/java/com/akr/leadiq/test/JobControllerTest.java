package com.akr.leadiq.test;

import com.akr.imgur.datastore.DatabaseMap;
import com.akr.imgur.datastore.JobUrlLists;
import com.akr.imgur.domain.UrlObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.akr.leadiq.test.TestUtils.jobStatusIsCorrect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobControllerTest extends MockGetPostGenerator {
	
	private static final String INVALID_JOB_SUBMITTED = "";
	private static final String TEST_JOB_MISSING_URL = "{\"nothing\": \"here\"}";
	
	@Autowired
	private DatabaseMap databaseMap;

	@Before
	public void setUp() {
		databaseMap.getJobs().clear();
	}

    @Test
    public void testToGetEmptyList() throws Exception {
		assertNoJobs();
		getJobList()
        	.andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"urls\":[]}")));
    }

	private ResultActions getJobList() throws Exception {
		return get("/v1/images");
	}

	private ResultActions getJobList(long id) throws Exception {
		return get("/v1/images/upload/{jobId}", id);
	}

	private ResultActions createNewJob(String jsonData) throws Exception {
		return post("/v1/images/upload", jsonData);
	}
    
    private void assertNoJobs() {
    	assertJobsCounter(0);
    }
    
    private void assertJobsCounter(int count) {
    	Assert.assertEquals(count, databaseMap.getJobs().size());
    }

    @Test
    public void testToGetNonexistentJobAndCheckNotFoundResponse() throws Exception {
		assertNoJobs();
		getJobList(1000)
        	.andExpect(status().isNotFound());
    }

	@Test
    public void testToGetExistingJobAndCheckOKResponse() throws Exception {
		JobUrlLists injectedJob = injectJob();
		assertJobsCounter(1);
		getJobList(Long.parseLong(injectedJob.getId()))
        	.andExpect(status().isOk())
        	.andExpect(jobStatusIsCorrect(injectedJob));
    }

	private JobUrlLists injectJob() {
		UrlObject urlObject = new UrlObject();
		List<String> list = new ArrayList<>();
		list.add("test");
		urlObject.setUrls(list);

		return databaseMap.create(urlObject);
	}
    
    @Test
    public void testToCreateInvalidNewOrderAndEnsureBadResponse() throws Exception {
		assertNoJobs();
		createNewJob(INVALID_JOB_SUBMITTED)
    		.andExpect(status().isBadRequest());
    }
}
