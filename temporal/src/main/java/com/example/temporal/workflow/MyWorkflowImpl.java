package com.example.temporal.workflow;

import io.temporal.workflow.Workflow;

public class MyWorkflowImpl implements MyWorkflow {

    @Override
    public String executeWorkflow() {
        // Implement your workflow logic here
        return "Workflow executed";
    }
}
