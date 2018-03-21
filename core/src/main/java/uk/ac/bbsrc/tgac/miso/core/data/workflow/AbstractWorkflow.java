package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWorkflow implements Workflow {
  private Progress progress;

  @Override
  public Progress getProgress() {
    List<ProgressStep> steps = getCompletedSteps().stream().map(WorkflowStep::getProgressStep).collect(Collectors.toList());
    for (ProgressStep step : steps) {
      step.setProgress(progress);
    }
    progress.setSteps(steps);
    return progress;
  }

  @Override
  public void setProgress(Progress progress) {
    processInputs(new ArrayList<>(progress.getSteps()));
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
