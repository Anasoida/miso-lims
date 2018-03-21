package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import java.util.List;

public interface Workflow {
  Progress getProgress();

  void setProgress(Progress progress);

  WorkflowStepPrompt getNextStep();

  WorkflowStepPrompt getStep(int stepNumber);

  boolean isComplete();

  List<String> getLog();

  void processInput(ProgressStep step);

  void processInput(int stepNumber, ProgressStep step);

  /**
   * Remove the last ProgressStep If no input has been processed so far, do nothing
   */
  void cancelInput();

  enum WorkflowName {
    LOADSEQUENCER
  }
}
