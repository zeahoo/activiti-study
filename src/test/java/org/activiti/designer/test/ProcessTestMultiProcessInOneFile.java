package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiProcessInOneFile {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/MultiProcessInOneFile.bpmn" })
    public void startProcess() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(2, count);

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        assertEquals("process1", list.get(0).getKey());
        assertEquals("leave", list.get(1).getKey());
    }
}