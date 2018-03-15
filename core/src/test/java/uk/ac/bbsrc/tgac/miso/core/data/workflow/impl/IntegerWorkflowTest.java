package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
  }

  @Test
  public void testCancelInputWithoutInput() {
    // Should not throw any exceptions
    workflow.cancelInput();
  }

  @Test
  public void testGetProgressWithoutInput() {
    assertNull(workflow.getProgress());
  }

  @Test
  public void testGetProgressAfterSetEmptyProgress() {
    Progress expected = new ProgressImpl();

    workflow.setProgress(expected);
    assertEquals(expected, workflow.getProgress());
  }

  @Test
  public void testNextStep() {
    assertIntegerPrompt(workflow.getNextStep());
  }

  @Test
  public void testGetFirstStep() {
    assertIntegerPrompt(workflow.getStep(1));
  }

  @Test
  public void testProcessInvalidInput() {
    exception.expect(IllegalArgumentException.class);
    // Give an instance of an invalid ProgressStep, and expect an exception
    workflow.processInput(new PoolProgressStep());
  }

  @Test
  @Ignore
  public void testProcessValidInput() {
    IntegerProgressStep step = new IntegerProgressStep();
    step.setInput(1);

    workflow.processInput(step);

    List<ProgressStep> steps = new ArrayList<>(workflow.getProgress().getSteps());
    assertEquals(1, steps.size());

    IntegerProgressStep actualStep = (IntegerProgressStep) steps.get(0);
    assertEquals(1, actualStep.getInput());
  }

  private void assertIntegerPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.INTEGER), prompt.getDataTypes());
    assertEquals("Input an integer", prompt.getMessage());
  }
}
