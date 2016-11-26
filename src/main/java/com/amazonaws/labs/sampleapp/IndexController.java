package com.amazonaws.labs.sampleapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

@Controller
public class IndexController {
    private final static Logger LOGGER = Logger.getLogger(IndexController.class.getName());

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    @Value("${DEPLOYMENT_GROUP_NAME}")
    private String deploymentGroupName;

    @Autowired
    private AmazonCloudWatch amazonCloudWatch;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displayIndex(Model model) {
        LOGGER.info("Application name set to: " + applicationName);
        LOGGER.info("Deployment Group Name set to: " + deploymentGroupName);
        LOGGER.info("Returning metrics for Namespace:" + applicationName + "-" + deploymentGroupName);
        return "/index";
    }

    private void emitMetrics(String namespace) {
        final PutMetricDataRequest request = new PutMetricDataRequest();
        request.setNamespace(namespace);

        MetricDatum metricDatum = new MetricDatum();
        metricDatum.setMetricName("Request");
        metricDatum.setTimestamp(new Date());
        metricDatum.setValue(1.0);
        metricDatum.setUnit(StandardUnit.Count);

        final List<MetricDatum> metrics = new ArrayList<>();
        metrics.add(metricDatum);
        request.setMetricData(metrics);

        amazonCloudWatch.putMetricData(request);
    }
}
