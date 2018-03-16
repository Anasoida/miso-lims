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

  IntegerWorkflow(User user) {
    this.progress = new ProgressImpl();
    progress.setWorkflowName(null);
    progress.setUser(user);
    progress.setSteps(Collections.emptyList());
  }

  @Override
  public WorkflowStepPrompt getNextStep() {
    return (nextStepNumber() == 1) ? firstStep.getPrompt() : null;
  }

  @Override
  public WorkflowStepPrompt getStep(int stepNumber) {
    if (stepNumber == 1) return firstStep.getPrompt();

    throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
  }

  @Override
  public void processInput(ProgressStep step) {
    processInput(nextStepNumber(), step);
  }

  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    if (stepNumber != 1) throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));

    assignProgress(step);
    assignStepNumber(step);
    step.accept(firstStep);

    progress.getSteps().add(step);
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

  private int nextStepNumber() {
    return progress.getSteps().size() + 1;
  }

  private void assignStepNumber(ProgressStep step) {
    // Step numbers begin at 1
    step.setStepNumber(nextStepNumber());
  }

  private void assignProgress(ProgressStep step) {
    step.setProgress(progress);
  }

}
