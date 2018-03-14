package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestLeaveqjsq {


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/qun/qjsq.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("user", null);
        variableMap.put("users", Arrays.asList("user2", "user3"));
        variableMap.put("userGroup", new ArrayList<String>());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-qjsq", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        assertEquals(1, taskService.createTaskQuery().count());

        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskService.createTaskQuery().singleResult().getId());
        assertEquals(2, identityLinksForTask.size());
    }
}