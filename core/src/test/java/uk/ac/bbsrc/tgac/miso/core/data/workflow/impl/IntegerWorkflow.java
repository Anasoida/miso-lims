package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

//public class IntegerWorkflow implements Workflow {
public class IntegerWorkflow extends AbstractWorkflow {
  private static final Workflow.WorkflowName WORKFLOW_NAME = null;
  private WorkflowStep workflowStep = new IntegerWorkflowStep("Input an integer.");
//  private Progress progress;

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

    getProgress().getSteps().add(step);
  }

  private void clearStepsAfter(int stepNumber) {
    List<ProgressStep> steps = new ArrayList<>(getProgress().getSteps());
    steps.subList(stepNumber - 1, steps.size()).clear();

    getProgress().setSteps(steps);
  }

  @Override
  public void cancelInput() {
    getProgress().setSteps(Collections.emptyList());
  }

  @Override
  protected WorkflowName getWorkflowName() {
    return WORKFLOW_NAME;
  }

//  @Override
//  public void setProgress(Progress progress) {
//    validateProgress(progress);
//
//    this.progress = new ProgressImpl();
//    this.progress.setId(progress.getId());
//    this.progress.setUser(progress.getUser());
//    this.progress.setCreationTime(progress.getCreationTime());
//    this.progress.setLastModified(progress.getLastModified());
//    this.progress.setSteps(Collections.emptyList());
//    this.progress.setWorkflowName(progress.getWorkflowName());
//
//    processInputs(new ArrayList<>(progress.getSteps()));
//  }
//
//  private void processInputs(List<ProgressStep> steps) {
//    for (ProgressStep step : steps) {
//      processInput(step);
//    }
//  }
//
//  /**
//   * Validate all Progress fields, but not ProgressSteps
//   */
//  private void validateProgress(Progress progress) {
//    if (progress.getWorkflowName() != WORKFLOW_NAME) {
//      throw new IllegalArgumentException(
//          String.format("WorkflowName %s is not expected for Workflow %s", progress.getWorkflowName(), IntegerWorkflow.class.toString()));
//    } else if (progress.getCreationTime().after(progress.getLastModified())) {
//      throw new IllegalArgumentException("Progress creation time is after last modification time");
//    }
//  }

  private int nextStepNumber() {
    return getProgress().getSteps().size() + 1;
  }

  private void assignStepNumber(ProgressStep step) {
    // Step numbers begin at 1
    step.setStepNumber(nextStepNumber());
  }

  private void assignProgress(ProgressStep step) {
    step.setProgress(getProgress());
  }
}
