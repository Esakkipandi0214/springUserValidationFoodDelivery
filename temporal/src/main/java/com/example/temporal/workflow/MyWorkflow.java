package com.example.temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MyWorkflow {

    @WorkflowMethod
    String executeWorkflow(); // The main workflow method

    // Define other methods if needed, e.g., signals or queries
}
