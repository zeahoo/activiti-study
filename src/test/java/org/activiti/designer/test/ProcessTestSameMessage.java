package org.activiti.designer.test;

import static org.junit.Assert.fail;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestSameMessage {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = { "diagrams/Message1.bpmn", "diagrams/Message2.bpmn" })
    public void startProcess() throws Exception {
        try {
            fail("should be thrown Cannot deploy process definition 'Message2.bpmn20.xml': there already is a message event subscription for the message with name 'SameMessage'.");
        } catch (ActivitiException e) {
        }
    }
}