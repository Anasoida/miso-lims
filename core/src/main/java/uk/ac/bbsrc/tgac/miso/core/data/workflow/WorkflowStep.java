package uk.ac.bbsrc.tgac.miso.core.data.workflow;

public interface WorkflowStep {
  WorkflowStepPrompt getPrompt();

  void processInput(ProgressStep step);

  String getLogMessage();
}
