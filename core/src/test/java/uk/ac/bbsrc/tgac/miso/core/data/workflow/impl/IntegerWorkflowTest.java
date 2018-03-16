package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflowTest {
  private static final long PROGRESS_ID = 1;
  private static final long USER_ID = 2;
  private static final int INPUT_1 = 3;
  private static final int INPUT_2 = 4;
  private static final WorkflowName WORKFLOW_NAME = null;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private Workflow workflow;

  @Before
  public void setUp() {
    workflow = new IntegerWorkflow(makeUser(USER_ID));
  }

  @Test
  public void testCancelInputWithoutInput() {
    // Should not throw any exceptions
    workflow.cancelInput();
  }

  @Test
  public void testGetProgressWithoutInput() {
    assertEquivalent2(makeEmptyProgress(), workflow.getProgress());
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
    workflow.processInput(makeIntegerProgressStep(INPUT_1));

    validateProgress(workflow.getProgress(), INPUT_1);
//    assertEquivalent2(makeProgress(INPUT_1), workflow.getProgress());
  }

  @Test
  public void testProcessInputThenCancelInput() {
    IntegerProgressStep step = makeIntegerProgressStep(INPUT_1);

    workflow.processInput(step);
    workflow.cancelInput();

    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
  }

  @Test
  public void testGetNextStepAfterProcessInputReturnsNull() {
    workflow.processInput(new IntegerProgressStep());
    assertNull(workflow.getNextStep());
  }

  @Test
  public void testGetStepAfterProcessInput() {
    workflow.processInput(new IntegerProgressStep());
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(2);
  }

  @Test
  public void testProcessInputAfterComplete() {
    workflow.processInput(new IntegerProgressStep());

    exception.expect(IllegalArgumentException.class);
    workflow.processInput(new IntegerProgressStep());
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
    IntegerProgressStep step = makeIntegerProgressStep(INPUT_1);

    workflow.processInput(1, step);

    validateProgress(workflow.getProgress(), INPUT_1);
  }

  @Test
  public void testProcessInputAtSecondStepAfterProcessInput() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(2, makeIntegerProgressStep(INPUT_1));
  }

  @Test
  @Ignore
  public void testReprocessInputAtFirstStep() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    workflow.processInput(1, makeIntegerProgressStep(INPUT_2));
    assertEquivalent(makeProgressWithInput(INPUT_2), workflow.getProgress());
  }

  @Test
  public void testSetEmptyProgressWithoutInput() {
    workflow.setProgress(makeEmptyProgress());
    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
  }

  @Test
  public void testSetEmptyProgressAfterInput() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    workflow.setProgress(makeEmptyProgress());
    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
  }

  @Test
  public void testSetProgressWithInput() {
    workflow.setProgress(makeProgressWithInput(INPUT_1));
    assertEquivalent(makeProgressWithInput(INPUT_1), workflow.getProgress());
  }

  /**
   * Validate that {@code progress} contains 1 step with {@code input} as its input
   */
  private void validateProgress(Progress progress, int input) {
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

  private void assertIntegerPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.INTEGER), prompt.getDataTypes());
    assertEquals("Input an integer.", prompt.getMessage());
  }

  /**
   * Assert that actualProgress and expectedProgress match based on a subset of fields
   */
  private void assertEquivalent2(Progress expectedProgress, Progress actualProgress) {
    //    assertEquals(expectedProgress.getId(), actualProgress.getId());
    assertEquals(expectedProgress.getWorkflowName(), actualProgress.getWorkflowName());
    assertEquals(expectedProgress.getUser(), actualProgress.getUser());
    assertEquals(expectedProgress.getSteps(), actualProgress.getSteps());
    //    assertSimilarDates(expectedProgress.getCreationTime(), actualProgress.getCreationTime());
    //    assertSimilarDates(expectedProgress.getLastModified(), actualProgress.getLastModified());
  }

  /**
   * Assert that actualProgress and expectedProgress match based on a subset of fields
   */
  private void assertEquivalent(Progress expectedProgress, Progress actualProgress) {
//    assertEquals(expectedProgress.getId(), actualProgress.getId());
    assertEquals(expectedProgress.getWorkflowName(), actualProgress.getWorkflowName());
    assertEquals(expectedProgress.getUser(), actualProgress.getUser());
    assertEquals(expectedProgress.getSteps(), actualProgress.getSteps());
//    assertSimilarDates(expectedProgress.getCreationTime(), actualProgress.getCreationTime());
//    assertSimilarDates(expectedProgress.getLastModified(), actualProgress.getLastModified());
  }

  /**
   * Assert that two dates are within 1 hour of each other
   */
  private void assertSimilarDates(Date expected, Date actual) {
    int oneHourInMs = 3600000;

    assertTrue(Math.abs(expected.getTime() - actual.getTime()) < oneHourInMs);
  }

  private Progress makeProgress(int... inputs) {
    Progress progress = new ProgressImpl();

    progress.setId(PROGRESS_ID);
    progress.setWorkflowName(WORKFLOW_NAME);
    progress.setUser(makeUser(USER_ID));

    Date now = new Date();
    progress.setCreationTime(now);
    progress.setLastModified(now);

    List<ProgressStep> steps = new ArrayList<>();
    for (int i = 0; i < inputs.length; ++i) {
      ProgressStep step = makeIntegerProgressStep(inputs[i]);
      step.setStepNumber(i + 1);
      steps.add(step);
    }
    progress.setSteps(steps);

    return progress;
  }

  private Progress makeEmptyProgress() {
    Progress progress = new ProgressImpl();
    progress.setSteps(Collections.emptyList());
    progress.setUser(makeUser(USER_ID));
    progress.setWorkflowName(WORKFLOW_NAME);
    return progress;
  }

  private Progress makeProgressWithInput(int input) {
    Progress progress = new ProgressImpl();
    progress.setSteps(Collections.singleton(makeIntegerProgressStep(input)));
    progress.setUser(makeUser(USER_ID));
    progress.setWorkflowName(WORKFLOW_NAME);
    return progress;
  }

  private User makeUser(long id) {
    User user = new UserImpl();
    user.setUserId(id);
    return user;
  }
}
