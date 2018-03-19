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
  protected boolean isComplete(Progress progress) {
    return progress.getSteps().size() == 1;
  }

  // todo: make method in abstract class
  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    if (isExistingStepNumber(stepNumber) || (!(nextStepNumber() == 2) && stepNumber == nextStepNumber())) {
      clearStepsAfter(stepNumber);

      step.accept(workflowStep);

      step.setProgress(getProgress());
      step.setStepNumber(nextStepNumber());

      getProgress().getSteps().add(step);
    } else {
      throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
    }
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
}
