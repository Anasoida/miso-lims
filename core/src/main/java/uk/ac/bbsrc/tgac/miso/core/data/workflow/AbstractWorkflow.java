package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWorkflow implements Workflow {
  private Progress progress;

  @Override
  public Progress getProgress() {
    progress.setSteps(getCompletedSteps().stream().map(WorkflowStep::getProgressStep).collect(Collectors.toList()));
    return progress;
  }

  @Override
  public void setProgress(Progress progress) {
    processInputs(new ArrayList<ProgressStep>(progress.getSteps()));
    this.progress = progress;
  }

  private void processInputs(List<ProgressStep> steps) {
    for (ProgressStep step : steps) {
      processInput(step);
    }
  }

  @Override
  public List<String> getLog() {
    return getCompletedSteps().stream().map(WorkflowStep::getLogMessage).collect(Collectors.toList());
  }

  protected abstract List<WorkflowStep> getCompletedSteps();
}
// public abstract class AbstractWorkflow implements Workflow {
// private Progress progress;
//
// /**
// * @param progress Progress object with all fields set
// */
// public AbstractWorkflow(Progress progress) {
// validateProgress(progress);
//
// this.progress = new ProgressImpl();
// this.progress.setId(progress.getId());
// this.progress.setWorkflowName(progress.getWorkflowName());
// this.progress.setUser(progress.getUser());
// this.progress.setCreationTime(progress.getCreationTime());
// this.progress.setLastModified(progress.getLastModified());
// this.progress.setSteps(Collections.emptyList());
//
// processInputs(new ArrayList<>(progress.getSteps()));
// }
//
// @Override
// public Progress getProgress() {
// return progress;
// }
//
// @Override
// public void processInput(ProgressStep step) {
// processInput(nextStepNumber(), step);
// }
//
// @Override
// public void cancelInput() {
// progress.setSteps(Collections.emptyList());
// }
//
// @Override
// public WorkflowStepPrompt getNextStep() {
// return getStep(nextStepNumber());
// }
//
// @Override
// public WorkflowStepPrompt getStep(int stepNumber) {
// if (validStepNumber(stepNumber)) return getWorkflowStep(stepNumber).getPrompt();
//
// throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
// }
//
// @Override
// public void processInput(int stepNumber, ProgressStep step) {
// if (!validStepNumber(stepNumber)) throw new IllegalArgumentException(String.format("Invalid step number: %d", stepNumber));
//
// transition(stepNumber, step);
//
// clearStepsAfter(stepNumber);
// addStepToProgress(step);
// }
//
// private void addStepToProgress(ProgressStep step) {
// step.setProgress(progress);
// step.setStepNumber(nextStepNumber());
// progress.getSteps().add(step);
// }
//
// private void clearStepsAfter(int stepNumber) {
// List<ProgressStep> steps = new ArrayList<>(progress.getSteps());
// steps.subList(stepNumber - 1, steps.size()).clear();
//
// progress.setSteps(steps);
// }
//
// private boolean isExistingStepNumber(int stepNumber) {
// return 1 <= stepNumber && stepNumber <= currentStepNumber();
// }
//
// private boolean validStepNumber(int stepNumber) {
// return isExistingStepNumber(stepNumber) || (stepNumber == nextStepNumber() && !isComplete());
// }
//
// private void processInputs(List<ProgressStep> steps) {
// for (ProgressStep step : steps) {
// processInput(step);
// }
// }
//
// /**
// * Validate all Progress fields, but not ProgressSteps
// */
// private void validateProgress(Progress progress) {
// if (progress.getWorkflowName() != getWorkflowName()) {
// throw new IllegalArgumentException(
// String.format("WorkflowName %s is not expected for Workflow %s", progress.getWorkflowName(), this.getClass().getName()));
// } else if (progress.getCreationTime().after(progress.getLastModified())) {
// throw new IllegalArgumentException("Progress creation time is after last modification time");
// }
// }
//
// private int currentStepNumber() {
// return progress.getSteps().size();
// }
//
// private int nextStepNumber() {
// return currentStepNumber() + 1;
// }
//
// protected abstract WorkflowName getWorkflowName();
//
// protected abstract WorkflowStep getWorkflowStep(int stepNumber);
//
// /**
// * Process the step input at stepNumber, discarding steps if necessary
// */
// protected abstract void transition(int stepNumber, ProgressStep step);
// }
