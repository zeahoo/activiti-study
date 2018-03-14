package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestExecutionIdNotEqualsProcessInstanceId {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/ExecutionIdNotEqualsProcessInstanceId.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        variableMap.put("users", Arrays.asList("one", "two", "three"));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        List<Execution> list = runtimeService.createExecutionQuery().processDefinitionKey("process1").list();
        for (Execution execution : list) {
            ExecutionEntity entity = (ExecutionEntity) execution;
            System.out.println("executionId=" + execution.getId() + "\tPID=" + execution.getProcessInstanceId() + "\t" + entity.getProcessDefinitionId() + "\t execution-parnet_id="
                    + entity.getParentId());
        }

        List<ProcessInstance> list2 = runtimeService.createProcessInstanceQuery().processDefinitionKey("process1").list();
        System.out.println("processInstance.size=" + list2.size());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("tid=" + task.getId() + "\t eid=" + task.getExecutionId() + "\t pid=" + task.getProcessInstanceId());
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().singleResult();
        System.out.println("tid=" + task.getId() + "\t eid=" + task.getExecutionId() + "\t pid=" + task.getProcessInstanceId());
        taskService.complete(task.getId());

        // one,two,three
        List<Task> list3 = taskService.createTaskQuery().list();
        assertEquals(3, list3.size());
        for (Task task2 : list3) {
            System.out.println(task2.getAssignee());
        }
        /*
         * assertEquals("one", list3.get(0).getAssignee()); assertEquals("two", list3.get(1).getAssignee()); assertEquals("three", list3.get(2).getAssignee());
         */}
}