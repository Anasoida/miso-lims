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

  boolean processInput(ProgressStep step);

  /**
   * @param stepNumber 1-index step number
   * @return true if the Workflow is complete
   */
  boolean processInput(int stepNumber, ProgressStep step);

  /**
   * Remove the last ProgressStep
   * If no input has been processed so far, do nothing
   */
  void cancelInput();

  void setProgress(Progress progress);

  Progress getProgress();
}
