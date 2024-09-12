package com.example.temporal.controller;

import com.example.temporal.workflow.MyWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temporal")
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);
    private final WorkflowClient workflowClient;

    public MyController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @GetMapping("/test")
    public String test() {
        logger.info("Testing Temporal Workflow Client");
        return "Temporal Workflow Client is ready!";
    }

    @GetMapping("/start-workflow")
    public ResponseEntity<String> startWorkflow() {
        try {
            // Define workflow options
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setTaskQueue("YOUR_TASK_QUEUE") // Replace with your task queue
                    .build();

            // Create a new workflow stub
            MyWorkflow myWorkflow = workflowClient.newWorkflowStub(MyWorkflow.class, options);
            // Start the workflow
            WorkflowExecution workflowStub = WorkflowClient.start(myWorkflow::executeWorkflow);
            String workflowId = workflowStub.getWorkflowId();
            String runId = workflowStub.getRunId();

            return ResponseEntity.ok("Workflow started with ID: " + workflowId + " and Run ID: " + runId);
        } catch (Exception e) {
            logger.error("Failed to start workflow", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start workflow: " + e.getMessage());
        }
    }

    @GetMapping("/workflow-status")
    public ResponseEntity<String> getWorkflowStatus(@RequestParam String workflowId) {
        try {
            // Create a workflow stub for the given workflow ID
            WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub(workflowId);
            // Retrieve status (Placeholder logic)
            String status = "Status retrieval not implemented"; // Implement actual status retrieval
            return ResponseEntity.ok("Workflow Status: " + status);
        } catch (Exception e) {
            logger.error("Failed to get workflow status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get workflow status: " + e.getMessage());
        }
    }

    @GetMapping("/workflow-result")
    public ResponseEntity<String> getWorkflowResult(@RequestParam String workflowId) {
        try {
            // Create a workflow stub for the given workflow ID
            WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub(workflowId);
            // Retrieve result (Placeholder logic)
            String result = "Result retrieval not implemented"; // Implement actual result retrieval
            return ResponseEntity.ok("Workflow Result: " + result);
        } catch (Exception e) {
            logger.error("Failed to get workflow result", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get workflow result: " + e.getMessage());
        }
    }
}
