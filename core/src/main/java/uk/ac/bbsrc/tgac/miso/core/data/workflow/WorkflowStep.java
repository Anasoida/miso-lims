package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.IntegerProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.PoolProgressStep;

public interface WorkflowStep {
  WorkflowStepPrompt getPrompt();

  /**
   * The default implementation of processInput is to throw an exception. Subclasses will override this implementation for the ProgressSteps
   * they expect
   */
  default void processInput(ProgressStep step) {
    throwUnexpectedInput();
  }

  default void processInput(PoolProgressStep step) {
    throwUnexpectedInput();
  }

  default void processInput(IntegerProgressStep step) {
    throwUnexpectedInput();
  }

  String getLogMessage();

  ProgressStep getProgressStep();

  /**
   * Private helper method
   */
  default void throwUnexpectedInput() {
    throw new IllegalArgumentException("Unexpected input");
  }
}
