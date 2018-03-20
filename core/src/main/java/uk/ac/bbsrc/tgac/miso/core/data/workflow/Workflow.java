package uk.ac.bbsrc.tgac.miso.core.data.workflow;

public interface Workflow {
  enum WorkflowName {
    LOADSEQUENCER
  }

  WorkflowStepPrompt getNextStep();

  /**
   * @param stepNumber 1-indexed step number
   */
  WorkflowStepPrompt getStep(int stepNumber);

  void processInput(ProgressStep step);

  /**
   * @param stepNumber 1-indexed step number
   */
  void processInput(int stepNumber, ProgressStep step);

  /**
   * Remove the last ProgressStep
   * If no input has been processed so far, do nothing
   */
  void cancelInput();

  Progress getProgress();

  boolean isComplete();
}
