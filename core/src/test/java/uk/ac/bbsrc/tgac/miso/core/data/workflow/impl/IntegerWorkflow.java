package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow extends AbstractWorkflow {
  private static final WorkflowName WORKFLOW_NAME = null;
  private static final WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");

  IntegerWorkflow(Progress progress) {
    super(progress);
  }

  @Override
  protected WorkflowStep getWorkflowStep(int stepNumber, Progress progress) {
    return workflowStep;
  }

  @Override
  protected boolean isComplete(Progress progress) {
    return progress.getSteps().size() == 1;
  }

  @Override
  protected WorkflowName getWorkflowName() {
    return WORKFLOW_NAME;
  }

  @Override
  protected WorkflowStepPrompt getStep(int stepNumber, Progress progress) {
    if (stepNumber == 1) return workflowStep.getPrompt();

    throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
  }

//  @Override
//  protected Progress processInput(int stepNumber, ProgressStep step, Progress progress) {
//    clearStepsAfter(stepNumber);
//
//    step.accept(workflowStep);
//
//    step.setProgress(progress);
//    step.setStepNumber(nextStepNumber());
//
//    progress.getSteps().add(step);
//
//    return progress;
//  }
}
