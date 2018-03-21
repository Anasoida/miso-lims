package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;

public class TestWorkflow extends AbstractWorkflow {
  private static final WorkflowName WORKFLOW_NAME = null;
  private static final WorkflowStep[] steps = { new IntegerWorkflowStep("Input an integer."), new PoolWorkflowStep("Input a pool.") };

  private int currentStepNumber = 1;

  TestWorkflow(Progress progress) {
    super(progress);
  }

  @Override
  protected WorkflowStep getWorkflowStep(int stepNumber) {
    return steps[stepNumber - 1];
  }

  @Override
  protected void transition(int stepNumber, ProgressStep step) {
    step.accept(getWorkflowStep(stepNumber));
    currentStepNumber = stepNumber;
  }

  @Override
  public boolean isComplete() {
    return currentStepNumber == 2;
  }

  @Override
  protected WorkflowName getWorkflowName() {
    return WORKFLOW_NAME;
  }
}
