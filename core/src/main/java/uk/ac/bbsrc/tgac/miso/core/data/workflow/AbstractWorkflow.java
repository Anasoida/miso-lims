package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWorkflow implements Workflow {
  private Progress progress;

  @Override
  final public Progress getProgress() {
    List<ProgressStep> steps = getCompletedSteps().stream().map(WorkflowStep::getProgressStep).collect(Collectors.toList());
    for (ProgressStep step : steps) {
      step.setProgress(progress);
    }
    progress.setSteps(steps);
    return progress;
  }

  @Override
  final public void setProgress(Progress progress) {
    if (this.progress != null) throw new IllegalStateException("Progress is already set");
    if (progress.getWorkflowName() != getWorkflowName()) throw new IllegalArgumentException("Invalid WorkflowName");

    processInputs(new ArrayList<>(progress.getSteps()));
    this.progress = progress;
  }

  private void processInputs(List<ProgressStep> steps) {
    for (ProgressStep step : steps) {
      processInput(step);
    }
  }

  @Override
  final public List<String> getLog() {
    return getCompletedSteps().stream().map(WorkflowStep::getLogMessage).collect(Collectors.toList());
  }

  protected abstract List<WorkflowStep> getCompletedSteps();

  protected abstract WorkflowName getWorkflowName();
}
