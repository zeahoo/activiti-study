package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class ProcessTestReciveTask {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/ReciveTask.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ReciveTask", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        TaskService taskService = activitiRule.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        assertEquals(0, list.size());

        HistoryService historyService = activitiRule.getHistoryService();

        // 读取所有的activity
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery().executionId(processInstance.getId()).list();
        for (HistoricActivityInstance historicActivityInstance : activityList) {
            System.out.println("task of :" + historicActivityInstance.getActivityName() + "\t" + historicActivityInstance.getActivityType());
        }

        // 只读取receive task
        activityList = historyService.createHistoricActivityInstanceQuery().activityType("receiveTask").executionId(processInstance.getId()).list();
        assertEquals(1, activityList.size());

        // 触发receive task
        System.out.println("begin invoke receive task...");
        runtimeService.signal(processInstance.getId());

        // 验证是否已经结束
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }

}