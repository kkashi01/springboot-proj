package com.example.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testRestDownloadControllerWithNoParam() {
        // Invoke service WITHOUT passing any parameters. It should fail with message
        // such as "Please specify META data...."
        ResponseEntity<String> fact = template.getForEntity("/rest/download", String.class);

        assertThat(fact.getBody(), containsString("Please specify"));

    }
}
