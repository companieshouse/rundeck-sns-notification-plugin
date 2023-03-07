package uk.gov.companieshouse.rundeck.plugin;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.descriptions.TextArea;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;

import java.util.Map;

@Plugin(service = ServiceNameConstants.Notification, name = "SNSNotificationPlugin")
@PluginDescription(title = "AWS SNS Notification Plugin", description = "AWS SNS Notification Plugin.")
public class SNSNotificationPlugin implements NotificationPlugin {

    @PluginProperty(title = "Subject", description = "Subject of message.  Variables are supported.", required = true, defaultValue = "")
    private String subject;

    @PluginProperty(title = "Message", description = "Message to send.  Variables are supported.", required = true, defaultValue = "")
    @TextArea
    private String message;

    @PluginProperty(title = "AWS SNS Topic ARN", description = "AWS SNS Topic ARN", required = true)
    private String awsSnsTopicArn;

    public boolean postNotification(String trigger, Map executionData, Map config) {
        PublishRequest publishRequest = PublishRequest.builder().topicArn(awsSnsTopicArn).message(message)
                .subject(subject).build();
        PublishResponse publishResponse = getSnsClient().publish(publishRequest);
        return publishResponse.sdkHttpResponse().isSuccessful();
    }

    protected SnsClient getSnsClient() {
        return SnsClient.create();
    }

    protected void setSubject(String subject) {
        this.subject = subject;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected void setAwsSnsTopicArn(String awsSnsTopicArn) {
        this.awsSnsTopicArn = awsSnsTopicArn;
    }

}