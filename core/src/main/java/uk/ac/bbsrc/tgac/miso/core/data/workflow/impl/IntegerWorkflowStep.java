package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflowStep implements WorkflowStep {
  private String message;

  public IntegerWorkflowStep(String message) {
    this.message = message;
  }

  @Override
  public WorkflowStepPrompt getPrompt() {
    return new WorkflowStepPrompt(Sets.newHashSet(ProgressStep.InputType.INTEGER), message);
  }

  @Override
  public void processInput(IntegerProgressStep step) {
  }

  @Override
  public String getLogMessage() {
    return null;
  }
}
