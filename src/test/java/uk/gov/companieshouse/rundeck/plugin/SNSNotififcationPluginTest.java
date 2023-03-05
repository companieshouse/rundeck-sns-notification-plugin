package uk.gov.companieshouse.rundeck.plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@RunWith(MockitoJUnitRunner.class)
public class SNSNotififcationPluginTest {
    
    private static final String TEST_SUBJECT = "test subject";  
    private static final String TEST_MESSAGE = "test message";
    private static final String TEST_TOPIC_ARN = "test topic arn";
    
    private SNSNotificationPlugin plugin;
    
    @Mock
    SnsClient snsClient;
    
    private ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
    
    @Before
    public void setUp() {
        plugin = new SNSNotificationPlugin() {
            protected SnsClient getSnsClient() {
                return snsClient;
            }
        };
        
        plugin.setSubject(TEST_SUBJECT);
        plugin.setMessage(TEST_MESSAGE);
        plugin.setAws_sns_topic_arn(TEST_TOPIC_ARN);
    }

    @Test
    public void postNotificationWithSuccess() throws Exception {
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(getPublishResponse(200));
        
        assertTrue(plugin.postNotification("Start", null, null));
        verifySNSRequest();
    }
    
    @Test
    public void postNotificationWithFailure() throws Exception {
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(getPublishResponse(500));

        assertFalse(plugin.postNotification("Start", null, null));
        verifySNSRequest();
    }
    
    private PublishResponse getPublishResponse(int code) {
        SdkHttpResponse sdkHttpResponse = SdkHttpResponse.builder().statusCode(code).build();
        return (PublishResponse) PublishResponse.builder().sdkHttpResponse(sdkHttpResponse).build();
    }
    
    private void verifySNSRequest() {
        verify(snsClient, times(1)).publish(captor.capture());
        PublishRequest publishRequest = captor.getValue();
        assertEquals(TEST_SUBJECT, publishRequest.subject());
        assertEquals(TEST_MESSAGE, publishRequest.message());
        assertEquals(TEST_TOPIC_ARN, publishRequest.topicArn());
    }
}
