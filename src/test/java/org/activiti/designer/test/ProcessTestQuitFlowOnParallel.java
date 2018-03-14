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

public class ProcessTestQuitFlowOnParallel {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 没有信号事件会导致任务“444444”重复创建
     */
    @Test
    @Deployment(resources = { "diagrams/qun/quitFlowOnParallelWithSignal.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("quitFlowOnParallel", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        // task 1111111
        TaskService taskService = activitiRule.getTaskService();
        Task task111111 = taskService.createTaskQuery().taskName("11111").singleResult();
        taskService.complete(task111111.getId());

        // 两个并行任务
        long count = taskService.createTaskQuery().count();
        assertEquals(2, count);

        // 完成任务：22222222，回到任务11111
        Task task22222222 = taskService.createTaskQuery().taskName("22222222").singleResult();
        variableMap = new HashMap<String, Object>();
        variableMap.put("type", "back");
        taskService.complete(task22222222.getId(), variableMap);

        // 统计任务数量
        count = taskService.createTaskQuery().count();
        assertEquals(2, count);

        // 再次完成任务111111
        task111111 = taskService.createTaskQuery().taskName("11111").singleResult();
        taskService.complete(task111111.getId());

        count = taskService.createTaskQuery().count();
        assertEquals(3, count);

        // 清理未完成的流程，避免影响其他测试方法
        runtimeService.deleteProcessInstance(processInstance.getId(), "");
        HistoryService historyService = activitiRule.getHistoryService();
        historyService.deleteHistoricProcessInstance(processInstance.getId());
    }

    /**
     * 利用信号事件避免任务“444444”重复创建
     */
    @Test
    @Deployment(resources = { "diagrams/qun/quitFlowOnParallel.bpmn" })
    public void startProcessWithSignal() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("quitFlowOnParallelWithSignal", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        // task 1111111
        TaskService taskService = activitiRule.getTaskService();
        Task task111111 = taskService.createTaskQuery().taskName("11111").singleResult();
        taskService.complete(task111111.getId());

        // 两个并行任务
        long count = taskService.createTaskQuery().count();
        assertEquals(2, count);

        // 完成任务：22222222，回到任务11111
        Task task22222222 = taskService.createTaskQuery().taskName("22222222").singleResult();
        variableMap = new HashMap<String, Object>();
        variableMap.put("type", "back");
        taskService.complete(task22222222.getId(), variableMap);

        // 统计任务数量
        count = taskService.createTaskQuery().count();
        assertEquals(1, count);

        // 再次完成任务111111
        task111111 = taskService.createTaskQuery().taskName("11111").singleResult();
        taskService.complete(task111111.getId());

        count = taskService.createTaskQuery().count();
        assertEquals(2, count);
    }
}