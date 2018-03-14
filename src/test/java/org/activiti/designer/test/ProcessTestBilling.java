package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestBilling {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/billing.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
        taskService.claim(task.getId(), "aa");

        variableMap = new HashMap<String, Object>();
        variableMap.put("approved", true);
        taskService.complete(task.getId(), variableMap);

        HistoryService historyService = activitiRule.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }

    @Test
    @Deployment(resources = { "diagrams/billing.bpmn" })
    public void startProcess11() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
        taskService.claim(task.getId(), "aa");

        variableMap = new HashMap<String, Object>();
        variableMap.put("approved", false);
        variableMap.put("applyer", "bb");
        taskService.complete(task.getId(), variableMap);

        task = taskService.createTaskQuery().taskAssignee("bb").singleResult();
        variableMap = new HashMap<String, Object>();
        variableMap.put("reApply", false);
        taskService.complete(task.getId(), variableMap);

        HistoryService historyService = activitiRule.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }

    @Test
    @Deployment(resources = { "diagrams/billing.bpmn" })
    public void startProcess22() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
        taskService.claim(task.getId(), "aa");

        variableMap = new HashMap<String, Object>();
        variableMap.put("approved", false);
        variableMap.put("applyer", "bb");
        taskService.complete(task.getId(), variableMap);

        task = taskService.createTaskQuery().taskAssignee("bb").singleResult();
        variableMap = new HashMap<String, Object>();
        variableMap.put("reApply", true);
        taskService.complete(task.getId(), variableMap);

        task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
        assertNotNull(task);
    }

}