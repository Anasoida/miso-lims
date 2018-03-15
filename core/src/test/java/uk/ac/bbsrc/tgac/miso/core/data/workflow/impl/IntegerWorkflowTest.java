package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.eaglegenomics.simlims.core.User;
import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.core.data.impl.UserImpl;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep.InputType;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflowTest {
  private static final long USER_ID = 1;
  private static final int INPUT = 0;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private Workflow workflow;

  @Before
  public void setUp() {
    workflow = new IntegerWorkflow(makeUser());
  }

  private User makeUser() {
    User user = new UserImpl();
    user.setUserId(USER_ID);
    return user;
  }

  @Test
  public void testCancelInputWithoutInput() {
    // Should not throw any exceptions
    workflow.cancelInput();
  }

  @Test
  public void testGetProgressWithoutInput() {
    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
  }


  @Test
  public void testGetStepZero() {
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(0);
  }

  @Test
  public void testGetStepAfterCurrentStep() {
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(2);
  }

  @Test
  public void testGetFirstStep() {
    assertIntegerPrompt(workflow.getStep(1));
  }

  @Test
  public void testGetNextStep() {
    assertIntegerPrompt(workflow.getNextStep());
  }

  @Test
  public void testProcessInvalidInput() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(new PoolProgressStep());
  }

  @Test
  public void testProcessValidInput() {
    IntegerProgressStep step = makeIntegerProgressStep(INPUT);

    workflow.processInput(step);

    assertValidProgress(workflow.getProgress(), INPUT);
  }

  @Test
  public void testProcessInputThenCancelInput() {
    IntegerProgressStep step = makeIntegerProgressStep(INPUT);

    workflow.processInput(step);
    workflow.cancelInput();

    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
  }

  @Test
  public void testGetNextStepAfterProcessInput() {
    workflow.processInput(new IntegerProgressStep());
    assertEndPrompt(workflow.getNextStep());
  }

  @Test
  public void testGetStepAfterProcessInput() {
    workflow.processInput(new IntegerProgressStep());
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(2);
  }

  @Test
  public void testProcessInputAtStepZero() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(0, new IntegerProgressStep());
  }

  @Test
  public void testProcessInputAtStepAfterCurrentStep() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(2, new IntegerProgressStep());
  }

  @Test
  public void testProcessInvalidInputAtFirstStep() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(1, new PoolProgressStep());
  }

  @Test
  public void testProcessValidInputAtFirstStep() {
    IntegerProgressStep step = makeIntegerProgressStep(INPUT);

    workflow.processInput(1, step);

    assertValidProgress(workflow.getProgress(), INPUT);
  }

  /**
   * Validate that {@code progress} contains 1 step with {@code input} as its input
   */
  private void assertValidProgress(Progress progress, int input) {
    List<ProgressStep> steps = new ArrayList<>(progress.getSteps());
    assertEquals(1, steps.size());

    IntegerProgressStep actualStep = (IntegerProgressStep) steps.get(0);
    assertEquals(input, actualStep.getInput());
    assertEquals(workflow.getProgress(), actualStep.getProgress());
    assertEquals(1, actualStep.getStepNumber());
  }

  private IntegerProgressStep makeIntegerProgressStep(int input) {
    IntegerProgressStep step = new IntegerProgressStep();
    step.setInput(input);
    return step;
  }

  private void assertEndPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Collections.emptySet(), prompt.getDataTypes());
    assertEquals("Workflow is complete.", prompt.getMessage());
  }

  private void assertIntegerPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.INTEGER), prompt.getDataTypes());
    assertEquals("Input an integer.", prompt.getMessage());
  }

  /**
   * Assert that actualProgress and expectedProgress match based on a subset of fields
   */
  private void assertEquivalent(Progress expectedProgress, Progress actualProgress) {
    assertEquals(expectedProgress.getWorkflowName(), actualProgress.getWorkflowName());
    assertEquals(expectedProgress.getUser(), actualProgress.getUser());
    assertEquals(expectedProgress.getSteps(), actualProgress.getSteps());
  }

  private Progress makeEmptyProgress() {
    Progress expectedProgress = new ProgressImpl();
    expectedProgress.setSteps(Collections.emptyList());
    expectedProgress.setUser(makeUser());
    expectedProgress.setWorkflowName(null);
    return expectedProgress;
  }
}
