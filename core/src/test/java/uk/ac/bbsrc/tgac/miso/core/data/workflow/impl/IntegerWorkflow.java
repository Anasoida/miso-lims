package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow extends AbstractWorkflow {
  private static final WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");

  IntegerWorkflow(Progress progress) {
    super(progress);
  }

  @Override
  public WorkflowStepPrompt getNextStep() {
    return (nextStepNumber() == 1) ? workflowStep.getPrompt() : null;
  }

  @Override
  public WorkflowStepPrompt getStep(int stepNumber) {
    if (stepNumber == 1) return workflowStep.getPrompt();

    throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
  }

  // todo: make method in abstract class
  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    if (isExistingStepNumber(stepNumber) || (!isComplete() && stepNumber == nextStepNumber())) {
      clearStepsAfter(stepNumber);

      step.accept(workflowStep);

      step.setProgress(getProgress());
      step.setStepNumber(nextStepNumber());

      getProgress().getSteps().add(step);
    } else {
      throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
    }
  }

  // todo: take parameters instead of calling higher-level methods
  @Override
  public boolean isComplete() {
    return nextStepNumber() == 2;
  }

  @Override
  protected WorkflowName getWorkflowName() {
    return null;
  }
}
