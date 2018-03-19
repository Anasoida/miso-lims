package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.ProgressImpl;

public abstract class AbstractWorkflow implements Workflow {
  private Progress progress;

  public AbstractWorkflow(Progress progress) {
    this.setProgress(progress);
  }

  @Override
  public Progress getProgress() {
    return progress;
  }

  @Override
  public void setProgress(Progress progress) {
    validateProgress(progress);

    this.progress = new ProgressImpl();
    this.progress.setId(progress.getId());
    this.progress.setWorkflowName(progress.getWorkflowName());
    this.progress.setUser(progress.getUser());
    this.progress.setCreationTime(progress.getCreationTime());
    this.progress.setLastModified(progress.getLastModified());
    this.progress.setSteps(Collections.emptyList());

    processInputs(new ArrayList<>(progress.getSteps()));
  }

  @Override
  public void processInput(ProgressStep step) {
    processInput(nextStepNumber(), step);
  }

  @Override
  public boolean isComplete() {
    return isComplete(progress);
  }

  @Override
  public void cancelInput() {
    progress.setSteps(Collections.emptyList());
  }

  @Override
  public WorkflowStepPrompt getNextStep() {
    return getStep(nextStepNumber());
  }

  @Override
  public WorkflowStepPrompt getStep(int stepNumber) {
    return getStep(stepNumber, progress);
  }

  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    if (isExistingStepNumber(stepNumber) || (stepNumber == nextStepNumber() && !isComplete())) {
      clearStepsAfter(stepNumber);
      step.accept(getWorkflowStep(stepNumber, progress));

      step.setProgress(progress);
      step.setStepNumber(nextStepNumber());

      progress.getSteps().add(step);
    } else {
      throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
    }
  }

  private void clearStepsAfter(int stepNumber) {
    List<ProgressStep> steps = new ArrayList<>(progress.getSteps());
    steps.subList(stepNumber - 1, steps.size()).clear();

    progress.setSteps(steps);
  }

  private boolean isExistingStepNumber(int stepNumber) {
    return 1 <= stepNumber && stepNumber <= currentStepNumber();
  }

  private void processInputs(List<ProgressStep> steps) {
    for (ProgressStep step : steps) {
      processInput(step);
    }
  }

  /**
   * Validate all Progress fields, but not ProgressSteps
   */
  private void validateProgress(Progress progress) {
    if (progress.getWorkflowName() != getWorkflowName()) {
      throw new IllegalArgumentException(
          String.format("WorkflowName %s is not expected for Workflow %s", progress.getWorkflowName(), this.getClass().getName()));
    } else if (progress.getCreationTime().after(progress.getLastModified())) {
      throw new IllegalArgumentException("Progress creation time is after last modification time");
    }
  }

  private int currentStepNumber() {
    return progress.getSteps().size();
  }

  private int nextStepNumber() {
    return progress.getSteps().size() + 1;
  }

  protected abstract boolean isComplete(Progress progress);

  protected abstract WorkflowName getWorkflowName();

  protected abstract WorkflowStepPrompt getStep(int stepNumber, Progress progress);

  protected abstract WorkflowStep getWorkflowStep(int stepNumber, Progress progress);
}
