package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import java.util.Collections;

import com.eaglegenomics.simlims.core.User;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow implements Workflow {
  private WorkflowStep firstStep = new IntegerWorkflowStep("Input an integer.");
  private WorkflowStepPrompt endPrompt = new WorkflowStepPrompt(Collections.emptySet(), "Workflow is complete.");
  private Progress progress;

  public IntegerWorkflow(User user) {
    this.progress = new ProgressImpl();
    progress.setWorkflowName(null);
    progress.setUser(user);
    progress.setSteps(Collections.emptyList());
  }

  @Override
  public WorkflowStepPrompt getNextStep() {
    if (progress.getSteps().size() == 0) return firstStep.getPrompt();

    return endPrompt;
  }

  @Override
  public WorkflowStepPrompt getStep(int step) {
    if (step == 1) return firstStep.getPrompt();

    throw new IllegalArgumentException(String.format("Invalid step number: %d", step));
  }

  @Override
  public void processInput(ProgressStep step) {
    assignProgress(step);
    assignStepNumber(step);
    step.accept(firstStep);

    progress.getSteps().add(step);
  }

  private void assignStepNumber(ProgressStep step) {
    // Step numbers begin at 1
    step.setStepNumber(progress.getSteps().size() + 1);
  }

  private void assignProgress(ProgressStep step) {
    step.setProgress(progress);
  }

  @Override
  public void cancelInput() {
    progress.setSteps(Collections.emptyList());
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
