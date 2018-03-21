package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.AbstractWorkflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class TestWorkflow extends AbstractWorkflow {
  private List<WorkflowStep> steps = Arrays.asList(new IntegerWorkflowStep("Input an integer."), new PoolWorkflowStep("Input a pool."));
  private int nextStepNumber = 0;

  @Override
  public WorkflowStepPrompt getNextStep() {
    return getStep(0);
  }

  @Override
  public WorkflowStepPrompt getStep(int stepNumber) {
    return steps.get(stepNumber).getPrompt();
  }

  @Override
  public boolean isComplete() {
    return nextStepNumber == 2;
  }

  @Override
  public void processInput(ProgressStep step) {
    processInput(nextStepNumber, step);
  }

  @Override
  public void processInput(int stepNumber, ProgressStep step) {
    step.accept(steps.get(stepNumber));
    nextStepNumber++;
  }

  @Override
  public void cancelInput() {
    nextStepNumber--;
  }

  private int currentStepNumber() {
    return nextStepNumber - 1;
  }

  @Override
  protected List<WorkflowStep> getCompletedSteps() {
    return steps.subList(0, nextStepNumber);
  }

  public static class PoolWorkflowStep implements WorkflowStep {
    private final String message;
    private ProgressStep progressStep;

    PoolWorkflowStep(String message) {
      this.message = message;
    }

    @Override
    public WorkflowStepPrompt getPrompt() {
      return new WorkflowStepPrompt(Collections.singleton(ProgressStep.InputType.POOL), message);
    }

    @Override
    public void processInput(PoolProgressStep step) {
      this.progressStep = step;
    }

    @Override
    public String getLogMessage() {
      // todo
      return null;
    }

    @Override
    public ProgressStep getProgressStep() {
      return progressStep;
    }
  }

  public static class IntegerWorkflowStep implements WorkflowStep {
    private final String message;
    private ProgressStep progressStep;

    IntegerWorkflowStep(String message) {
      this.message = message;
    }

    @Override
    public WorkflowStepPrompt getPrompt() {
      return new WorkflowStepPrompt(Sets.newHashSet(ProgressStep.InputType.INTEGER), message);
    }

    @Override
    public void processInput(IntegerProgressStep step) {
      this.progressStep = step;
    }

    @Override
    public String getLogMessage() {
      // todo
      return null;
    }

    @Override
    public ProgressStep getProgressStep() {
      return progressStep;
    }

  }
}
