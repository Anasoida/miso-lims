package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.IntegerProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.PoolProgressStep;

/**
 * Represents a possibly incomplete step in a Workflow.
 * Responsible for describing, validating, and storing the input for a step.
 *
 * The default implementation of setProgressStep is to throw an exception.
 * Subclasses will override this implementation for the ProgressSteps they expect
 */
public interface WorkflowStep {
  WorkflowStepPrompt getPrompt();

  ProgressStep getProgressStep();

  default void setProgressStep(ProgressStep step) {
    throwUnexpectedInput();
  }

  default void setProgressStep(PoolProgressStep step) {
    throwUnexpectedInput();
  }

  default void setProgressStep(IntegerProgressStep step) {
    throwUnexpectedInput();
  }

  /**
   * @return message describing the data processed by this step
   */
  String getLogMessage();

  /**
   * Private helper method
   */
  default void throwUnexpectedInput() {
    throw new IllegalArgumentException("Unexpected input");
  }
}
