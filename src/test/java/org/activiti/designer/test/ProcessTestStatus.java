package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestStatus {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/Gateway.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("gateway", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        // suspend process instance
        runtimeService.suspendProcessInstanceById(processInstance.getId());

        // verify suspended process instance
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().active().list();
        assertEquals(0, list.size());

        list = runtimeService.createProcessInstanceQuery().list();
        assertEquals(1, list.size());

        /*
         * // bug：suspended process instance should get a empty result of task, but one. TaskService taskService = activitiRule.getTaskService(); List<Task> list2 =
         * taskService.createTaskQuery().processInstanceId(processInstance.getId()).list(); assertEquals(0, list2.size());
         */

        // reactive process instance
        runtimeService.activateProcessInstanceById(processInstance.getId());

        // verify active process instance
        list = runtimeService.createProcessInstanceQuery().active().list();
        assertEquals(1, list.size());

        list = runtimeService.createProcessInstanceQuery().list();
        assertEquals(1, list.size());
    }
}