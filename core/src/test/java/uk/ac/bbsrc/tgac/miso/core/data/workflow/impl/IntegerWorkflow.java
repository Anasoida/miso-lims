package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflow implements Workflow {
  private WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");
  private Progress progress;

  @Override
  public WorkflowStepPrompt getNextStep() {
    return (nextStepNumber() == 1) ? workflowStep.getPrompt() : null;
  }

  @Override
  public WorkflowStepPrompt getStep(int stepNumber) {
    if (stepNumber == 1) return workflowStep.getPrompt();

    throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
  }

  @Override
  public void processInput(ProgressStep step) {
    processInput(nextStepNumber(), step);
  }

  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    if (stepNumber != 1) throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));

    clearStepsAfter(stepNumber);

    assignProgress(step);
    assignStepNumber(step);
    step.accept(workflowStep);

    progress.getSteps().add(step);
  }

  private void clearStepsAfter(int stepNumber) {
    List<ProgressStep> steps = new ArrayList<>(progress.getSteps());
    steps.subList(stepNumber - 1, steps.size()).clear();

    progress.setSteps(steps);
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
