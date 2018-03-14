package uk.ac.bbsrc.tgac.miso.core.data.workflow;

public interface Workflow {
  enum WorkflowName {
    LOADSEQUENCER
  }

  WorkflowStepPrompt getNextStep();

  WorkflowStepPrompt getStep(int step);

  void processInput(ProgressStep step);

  void cancelInput();

  void setProgress(Progress progress);

  Progress getProgress();
}
