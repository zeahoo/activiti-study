package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestJuelMap {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/qun/juelMap.bpmn" })
    public void startProcess() throws Exception {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        //		ArrayList<String> value = new ArrayList<String>();
        //		value.add("3333");
        Map<String, List<String>> names = new HashMap<String, List<String>>();
        names.put("henry", Arrays.asList("Henry Yan", "咖啡兔"));
        variableMap.put("names", names);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("juelMap", variableMap);
        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

        HistoryService historyService = activitiRule.getHistoryService();
        System.out.println(historyService.createHistoricVariableInstanceQuery().variableName("a").singleResult().getValue());
        ;
    }
}