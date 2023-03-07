
# rundeck-sns-notification-plugin

## Overview
A Rundeck notification plugin that is used to send messages to an AWS Simple Notification Service (SNS) topic when triggered.  

The plugin has the following fields, which support standard expressions so that job and execution metadata (see: https://docs.rundeck.com/docs/manual/job-workflows.html#context-variables), and custom global variables can be used:

 - Subject - the subject of the SNS message
 - Message - the body of the SNS message
 - AWS SNS Topic ARN - the topic to deliver to

The message can be simple text, just like the body of an email, which is suitable for topic subscriptions configured 
for email delivery, or can be a JSON structure that is more useful where there may be a webhook/http subscription that forms part of an integration with another service.


![](https://github.com/companieshouse/rundeck-sns-notification-plugin/raw/main/plugin-screenshot.png)
For example, if there was an integration that relied on the message format being similar to messages received from Cloudwatch, then the following message body might be used: 

    {
	    "AlarmName": "Rundeck ${job.project} - ${job.group}/${job.name} : ${execution.status}",
	    "Region": "eu-west-2",
	    "StateChangeTime": "${execution.dateEndedW3c}",
	    "AlarmDescription": "${job.group}/${job.name} - see ${execution.href}",
	    "NewStateValue": "ALARM",
	    "OldStateValue": "OK",
	    "NewStateReason": "Job ${execution.status}",
	    "AWSAccountId": "",
	    "Trigger": {
		    "ComparisonOperator": "",
		    "Dimensions": "",
		    "EvaluationPeriods": "",
		    "MetricName": "",
		    "Namespace": "",
		    "Period": "",
		    "Statistic": "",
		    "StatisticType": "",
		    "Threshold": "",
		    "TreatMissingData": "",
		    "Unit": "",
		    "EvaluateLowSampleCountPercentile": ""
	    }
    }

Depending on the integration in question, many of those fields (such as the trigger attributes) might not be needed, and may be left blank.

### Region and Credentials
The region is automatically determined, via use of https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/regions/providers/DefaultAwsRegionProviderChain.html, and credentials loaded via use of https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html


## Deployment
The plugin jar needs to be placed inside `<RUNDECK_BASE>/libext` and it should then become available as an option for job notifications. 

## Build
The build requires Java 8+ and Maven - e.g.:

    mvn clean test package versions:set -DnewVersion=1.0.1
