package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class IntegerWorkflowTest {
  private static final long PROGRESS_ID = 1;
  private static final long USER_ID = 2;
  private static final int INPUT_1 = 3;
  private static final int INPUT_2 = 4;
  // Use null for WorkflowName since we can't create an Enum value for a test workflow
  private static final WorkflowName WORKFLOW_NAME = null;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private Workflow workflow;

  @Before
  public void setUp() {
    workflow = new IntegerWorkflow();
    workflow.setProgress(makeProgress());
  }

  @Test
  public void testCancelInputWithoutInput() {
    // Should not throw any exceptions
    workflow.cancelInput();
  }

  @Test
  public void testGetProgressWithoutInput() {
    assertEquivalent(makeProgress(), workflow.getProgress());
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

    assertEquivalent(makeProgress(INPUT_1), workflow.getProgress());
  }

  @Test
  public void testProcessInputThenCancelInput() {
    IntegerProgressStep step = makeIntegerProgressStep(INPUT_1);

    workflow.processInput(step);
    workflow.cancelInput();

    assertEquivalent(makeProgress(), workflow.getProgress());
  }

  @Test
  public void testGetNextStepAfterProcessInputReturnsNull() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    assertNull(workflow.getNextStep());
  }

  @Test
  public void testGetStepAfterProcessInput() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(2);
  }

  @Test
  public void testProcessInputAfterComplete() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));

    exception.expect(IllegalArgumentException.class);
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
  }

  @Test
  public void testProcessInputAtStepZero() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(0, makeIntegerProgressStep(INPUT_1));
  }

  @Test
  public void testProcessInputAtStepAfterCurrentStep() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(2, makeIntegerProgressStep(INPUT_1));
  }

  @Test
  public void testProcessInvalidInputAtFirstStep() {
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(1, new PoolProgressStep());
  }

  @Test
  public void testProcessValidInputAtFirstStep() {
    workflow.processInput(1, makeIntegerProgressStep(INPUT_1));

    assertEquivalent(makeProgress(INPUT_1), workflow.getProgress());
  }

  @Test
  public void testProcessInputAtSecondStepAfterProcessInput() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(2, makeIntegerProgressStep(INPUT_1));
  }

  @Test
  public void testReprocessInputAtFirstStep() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    workflow.processInput(1, makeIntegerProgressStep(INPUT_2));

    assertEquivalent(makeProgress(INPUT_2), workflow.getProgress());
  }

  @Test
  public void testSetEmptyProgressWithoutInput() {
    workflow.setProgress(makeProgress());
    assertEquivalent(makeProgress(), workflow.getProgress());
  }

  @Test
  public void testSetEmptyProgressAfterInput() {
    workflow.processInput(makeIntegerProgressStep(INPUT_1));
    workflow.setProgress(makeProgress());
    assertEquivalent(makeProgress(), workflow.getProgress());
  }

  @Test
  public void testSetProgressWithInput() {
    workflow.setProgress(makeProgress(INPUT_1));
    assertEquivalent(makeProgress(INPUT_1), workflow.getProgress());
  }

  @Test
  public void testSetProgressWithInvalidName() {
    Progress progress = makeProgress();
    progress.setWorkflowName(LOADSEQUENCER);

    exception.expect(IllegalArgumentException.class);
    workflow.setProgress(progress);
  }

  @Test
  public void testSetProgressWithInvalidDates() {
    Progress progress = makeProgress();
    progress.setCreationTime(new Date(1));
    progress.setLastModified(new Date(0));

    exception.expect(IllegalArgumentException.class);
    workflow.setProgress(progress);
  }

  @Test
  public void testSetInvalidProgress() {
    Progress progress = makeProgress();

    progress.setSteps(Collections.emptyList());
    progress.getSteps().add(new PoolProgressStep());

    exception.expect(IllegalArgumentException.class);
    workflow.setProgress(progress);
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

  private void assertEquivalent(Progress expectedProgress, Progress actualProgress) {
    assertEquals(expectedProgress.getId(), actualProgress.getId());
    assertEquals(expectedProgress.getWorkflowName(), actualProgress.getWorkflowName());
    assertEquals(expectedProgress.getUser(), actualProgress.getUser());
    assertEquals(expectedProgress.getSteps(), actualProgress.getSteps());
    assertSimilarDates(expectedProgress.getCreationTime(), actualProgress.getCreationTime());
    assertSimilarDates(expectedProgress.getLastModified(), actualProgress.getLastModified());
  }

  /**
   * Assert that two dates are within 1 hour of each other
   */
  private void assertSimilarDates(Date expected, Date actual) {
    int oneHourInMs = 3600000;

    assertTrue(Math.abs(expected.getTime() - actual.getTime()) < oneHourInMs);
  }

  private Progress makeProgress(int... inputs) {
    return makeProgress(PROGRESS_ID, WORKFLOW_NAME, makeUser(USER_ID), inputs);
  }

  private Progress makeProgress(long id, WorkflowName workflowName, User user, int... inputs) {
    Progress progress = new ProgressImpl();

    progress.setId(id);
    progress.setWorkflowName(workflowName);
    progress.setUser(user);

    Date now = new Date();
    progress.setCreationTime(now);
    progress.setLastModified(now);

    List<ProgressStep> steps = new ArrayList<>();
    if (inputs != null) {
      for (int i = 0; i < inputs.length; ++i) {
        ProgressStep step = makeIntegerProgressStep(inputs[i]);
        step.setStepNumber(i + 1);
        step.setProgress(progress);
        steps.add(step);
      }
    }
    progress.setSteps(steps);

    return progress;
  }

  private User makeUser(long id) {
    User user = new UserImpl();
    user.setUserId(id);
    return user;
  }
}
