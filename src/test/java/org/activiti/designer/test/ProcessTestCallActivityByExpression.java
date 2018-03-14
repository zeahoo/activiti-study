package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestCallActivityByExpression {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/CallActivityByExpression.bpmn", "diagrams/Gateway.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("activityName", "gateway");

        IdentityService identityService = activitiRule.getIdentityService();
        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("CallActivityByExpression", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task singleResult = taskService.createTaskQuery().singleResult();
        identityService.setAuthenticatedUserId("henryyan");
        taskService.complete(singleResult.getId());

        long count = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().count();
        assertEquals(2, count);

        List<HistoricProcessInstance> list = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            assertEquals("henryyan", historicProcessInstance.getStartUserId());
        }
    }
}