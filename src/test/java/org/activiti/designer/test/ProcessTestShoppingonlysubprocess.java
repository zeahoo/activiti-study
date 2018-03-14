package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestShoppingonlysubprocess {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/shopping-only-subprocess.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("shopping-only-subprocess", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();

        variableMap = new HashMap<String, Object>();
        variableMap.put("amount", 328d);
        taskService.complete(task.getId(), variableMap);

        task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertEquals("银行付款", task.getName());
        Object variable = taskService.getVariable(task.getId(), "amount");
        assertNotNull(variable);
        assertEquals(variable, 328d);
    }
}