package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow extends AbstractWorkflow {
  private static final WorkflowName WORKFLOW_NAME = null;
  private static final WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");

  IntegerWorkflow(Progress progress) {
    super(progress);
  }

  @Override
  protected WorkflowStep getWorkflowStep(int stepNumber) {
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
}
