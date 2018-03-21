package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import java.util.Collections;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class PoolWorkflowStep implements WorkflowStep {
  private final String message;

  PoolWorkflowStep(String message) {
    this.message = message;
  }

  @Override
  public WorkflowStepPrompt getPrompt() {
    return new WorkflowStepPrompt(Collections.singleton(ProgressStep.InputType.POOL), message);
  }

  @Override
  public void processInput(PoolProgressStep step) {
  }

  @Override
  public String getLogMessage() {
    return null;
  }
}
