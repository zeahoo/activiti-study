package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestListenerAndDelegate {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/listenerAndDelegate.bpmn" })
    public void startProcess() throws Exception {
        TaskService taskService = activitiRule.getTaskService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        ProcessInstance processInstance = null;
        try {
            processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
        } catch (RuntimeException e) {
            System.err.println("cache runtimeexception");
            return;
        }
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("henryyan");
        System.out.println(ToStringBuilder.reflectionToString(taskAssignee));
    }
}