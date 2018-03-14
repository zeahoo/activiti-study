package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestEventGateway {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/EventGateway.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("EventGateway", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
        Thread.currentThread().wait(2000);
    }
}