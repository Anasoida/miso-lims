package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep.InputType;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflowTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  Workflow workflow;

  @Before
  public void setUp() {
    workflow = new IntegerWorkflow();
    workflow.setProgress(new ProgressImpl());
  }

  @Test
  public void testNoInput() {
  }

  @Test
  public void testCancelInputWithoutInput() {
    // Should not throw any exceptions
    workflow.cancelInput();
  }

  @Test
  public void testNextStep() {
    assertIntegerStep(workflow.getNextStep());
  }

  @Test
  public void testGetFirstStep() {
    assertIntegerStep(workflow.getStep(1));
  }

  @Test
  public void testProcessInvalidInput() {
    exception.expect(IllegalArgumentException.class);
    // Give an instance of an invalid ProgressStep, and expect an exception
    workflow.processInput(new PoolProgressStep());
  }

  @Test
  public void testProcessValidInput() {
    IntegerProgressStep step = new IntegerProgressStep();
    step.setInput(1);

    workflow.processInput(step);

    List<ProgressStep> steps = new ArrayList<>(workflow.getProgress().getSteps());
    assertEquals(1, steps.size());

    IntegerProgressStep actualStep = (IntegerProgressStep) steps.get(0);
    assertEquals(1, actualStep.getInput());
  }

  private void assertIntegerStep(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.INTEGER), prompt.getDataTypes());
    assertEquals("Input an integer", prompt.getMessage());
  }
}
