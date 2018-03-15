package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow implements Workflow {
  private IntegerWorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer");
  private Progress progress;

  @Override
  public WorkflowStepPrompt getNextStep() {
    return workflowStep.getPrompt();
  }

  @Override
  public WorkflowStepPrompt getStep(int step) {
    return workflowStep.getPrompt();
  }

  @Override
  public void processInput(ProgressStep step) {
    step.accept(workflowStep);
  }

  @Override
  public void cancelInput() {
    // todo
  }

  @Override
  public Progress getProgress() {
    return progress;
  }

  @Override
  public void setProgress(Progress progress) {
    this.progress = progress;
  }
}
