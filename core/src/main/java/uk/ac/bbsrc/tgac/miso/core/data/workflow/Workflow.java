package uk.ac.bbsrc.tgac.miso.core.data.workflow;

public interface Workflow {
  enum WorkflowName {
    LOADSEQUENCER
  }

  WorkflowStepPrompt getNextStep();

  WorkflowStepPrompt getStep(int stepNumber);

  void processInput(ProgressStep step);

  void processInput(int stepNumber, ProgressStep step);

  /**
   * Remove the last ProgressStep
   * If no input has been processed so far, do nothing
   */
  void cancelInput();

  void setProgress(Progress progress);

  Progress getProgress();
}
