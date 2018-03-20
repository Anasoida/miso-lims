package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;

public class TestWorkflow extends AbstractWorkflow {
  private static final WorkflowName WORKFLOW_NAME = null;
  private static final WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");

  private boolean complete = false;

  TestWorkflow(Progress progress) {
    super(progress);
  }

  @Override
  protected WorkflowStep getWorkflowStep(int stepNumber) {
    return workflowStep;
  }

  @Override
  protected void transition(int stepNumber, ProgressStep step) {
    step.accept(workflowStep);
    complete = true;
  }

  @Override
  public boolean isComplete() {
    return complete;
  }

  @Override
  protected WorkflowName getWorkflowName() {
    return WORKFLOW_NAME;
  }
}
