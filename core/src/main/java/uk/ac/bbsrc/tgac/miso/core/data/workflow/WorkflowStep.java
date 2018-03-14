package uk.ac.bbsrc.tgac.miso.core.data.workflow;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.IntegerProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.PoolProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.impl.SampleProgressStep;

public interface WorkflowStep {
  WorkflowStepPrompt getPrompt();

  default void processInput(SampleProgressStep step) {
    throwUnexpectedInput();
  }

  default void processInput(PoolProgressStep step) {
    throwUnexpectedInput();
  }

  default void processInput(IntegerProgressStep step) {
    throwUnexpectedInput();
  }

  String getLogMessage();

  /**
   * Private helper method
   */
  default void throwUnexpectedInput() {
    throw new IllegalArgumentException("Unexpected input");
  }
}
