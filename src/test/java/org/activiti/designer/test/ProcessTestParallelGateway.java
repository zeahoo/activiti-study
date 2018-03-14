package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestParallelGateway {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/ParallelGateway.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ParallelGateway", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
        taskService.complete(task.getId());

        Task task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        assertNotNull(task2);
        Task task3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult();
        assertNotNull(task3);

        taskService.complete(task2.getId());
        task2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        assertNull(task2);

        taskService.complete(task3.getId());
        task3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult();
        assertNull(task3);

        task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
        taskService.complete(task.getId());

        // 验证历史数据
        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance activity : list) {
            System.out.println(activity.getActivityName() + " = " + activity.getEndTime());
        }
    }
}