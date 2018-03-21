package uk.ac.bbsrc.tgac.miso.core.data.workflow.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.eaglegenomics.simlims.core.User;
import com.google.common.collect.Sets;

import uk.ac.bbsrc.tgac.miso.core.data.Pool;
import uk.ac.bbsrc.tgac.miso.core.data.impl.PoolImpl;
import uk.ac.bbsrc.tgac.miso.core.data.impl.UserImpl;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Progress;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.ProgressStep.InputType;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.WorkflowStepPrompt;

public class TestWorkflowTest {
  private static final long PROGRESS_ID = 1;
  private static final long USER_ID = 2;
  private static final int INT_1 = 3;
  private static final int INT_2 = 4;
  private static final long POOL_ID = 5;

  // Use null for WorkflowName since we can't create an Enum value for a test workflow
  private static final WorkflowName WORKFLOW_NAME = null;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private Workflow workflow;

  @Test
  public void testCreateNewWorkflow() {
    assertNoInput(makeNewWorkflow());
  }

  private void assertNoInput(Workflow workflow) {
    assertEquivalent(makeEmptyProgress(), workflow.getProgress());
    assertIntegerPrompt(workflow.getNextStep());
    assertIntegerPrompt(workflow.getStep(0));
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(1);
    exception.expect(IllegalArgumentException.class);
    assertFalse(workflow.isComplete());
    assertEquals(Collections.emptyList(), workflow.getLog());
  }

  @Test
  public void processInvalidInput() {
    workflow = makeNewWorkflow();
    exception.expect(IllegalArgumentException.class);
    workflow.processInput(makePoolStep(POOL_ID));
  }

  @Test
  public void processValidInput() {
    workflow = makeNewWorkflow();
    workflow.processInput(makeIntegerStep(INT_1));
    assertReceivedOneInput(INT_1);
  }

  private void assertReceivedOneInput(int input) {
    assertEquivalent(makeProgress(makeIntegerStep(input, 0)), workflow.getProgress());
    assertPoolPrompt(workflow.getNextStep());
    assertIntegerPrompt(workflow.getStep(0));
    assertPoolPrompt(workflow.getStep(1));
    exception.expect(IllegalArgumentException.class);
    workflow.getStep(2);
    assertFalse(workflow.isComplete());
    assertEquals(Collections.singletonList(String.format("Processed integer %d", INT_1)), workflow.getLog());
  }

  // @Test
  // public void testCancelInputWithoutInput() {
  // workflow = makeNewWorkflow();
  // workflow.cancelInput();
  // assertNoInput(workflow);
  // }

  // @Test
  // public void testCancelInputWithoutInput() {
  // makeNewWorkflow().cancelInput();
  // }
  //
  // @Test
  // public void testIsNotCompleteWithoutInput() {
  // assertFalse(makeNewWorkflow().isComplete());
  // }
  //
  // @Test
  // public void testGetProgressWithoutInput() {
  // assertEquivalent(makeProgress(), makeNewWorkflow().getProgress());
  // }
  //
  // @Test
  // public void testGetStepZero() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().getStep(0);
  // }
  //
  // @Test
  // public void testGetStepAfterCurrentStep() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().getStep(2);
  // }
  //
  // @Test
  // public void testGetFirstStep() {
  // assertIntegerPrompt(makeNewWorkflow().getStep(1));
  // }
  //
  // @Test
  // public void testGetNextStep() {
  // assertIntegerPrompt(makeNewWorkflow().getNextStep());
  // }
  //
  // @Test
  // public void testProcessInvalidInput() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().processInput(new PoolProgressStep());
  // }
  //
  // @Test
  // public void testProcessOneValidInput() {
  // workflow = makeNewWorkflow();
  //
  // workflow.processInput(makeIntegerStep(INT_1));
  // assertFalse(workflow.isComplete());
  // assertEquivalent(makeProgress(makeIntegerStep(INT_1)), workflow.getProgress());
  // }
  //
  // @Test
  // public void testProcessInputThenCancelInput() {
  // IntegerProgressStep step = makeIntegerStep(INT_1);
  //
  // workflow = makeNewWorkflow();
  // workflow.processInput(step);
  // workflow.cancelInput();
  //
  // assertEquivalent(makeProgress(), workflow.getProgress());
  // }
  //
  // @Test
  // public void testProcessInputAtStepZero() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().processInput(0, makeIntegerStep(INT_1));
  // }
  //
  // @Test
  // public void testProcessInputAtStepAfterCurrentStep() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().processInput(2, makeIntegerStep(INT_1));
  // }
  //
  // @Test
  // public void testProcessInvalidInputAtFirstStep() {
  // exception.expect(IllegalArgumentException.class);
  // makeNewWorkflow().processInput(1, new PoolProgressStep());
  // }
  //
  // @Test
  // public void testProcessValidInputAtFirstStep() {
  // workflow = makeNewWorkflow();
  // workflow.processInput(1, makeIntegerStep(INT_1));
  // assertFalse(workflow.isComplete());
  // assertEquivalent(makeProgress(makeIntegerStep(INT_1)), workflow.getProgress());
  // }
  //
  // @Test
  // public void setProgressWithInvalidName() {
  // Progress progress = makeEmptyProgress();
  // progress.setWorkflowName(LOADSEQUENCER);
  //
  // workflow = new TestWorkflow();
  // exception.expect(IllegalArgumentException.class);
  // workflow.setProgress(progress);
  // }
  //
  // @Test
  // public void setProgressTwiceThrowsError() {
  // workflow = makeNewWorkflow();
  // exception.expect(IllegalStateException.class);
  // workflow.setProgress(makeEmptyProgress());
  // }
  //
  // @Test
  // public void testInitalizeWorkflowWithInvalidStep() {
  // exception.expect(IllegalArgumentException.class);
  // makeExistingWorkflow(new PoolProgressStep());
  // }
  //
  // @Test
  // public void testFailedProcessInputDoesNotChangeProgress() {
  // workflow = makeExistingWorkflow(makeIntegerStep(INT_1));
  //
  // try {
  // // Try to set an invalid input at step 1
  // workflow.processInput(1, new PoolProgressStep());
  // } catch (Exception ignored) {
  // }
  //
  // assertEquivalent(makeProgress(makeIntegerStep(INT_1)), workflow.getProgress());
  // }
  //
  // @Test
  // public void testSecondInputInvalid() {
  // workflow = makeNewWorkflow();
  // workflow.processInput(makeIntegerStep(INT_1));
  // exception.expect(IllegalArgumentException.class);
  // workflow.processInput(makeIntegerStep(INT_1));
  // }

  // @Test
  // public void testGetNextStepAfterInput() {
  // workflow = new TestWorkflow(makeProgress(makeIntegerStep(INT_1)));
  // WorkflowStepPrompt prompt = workflow.getNextStep();
  // assertEquals(Collections.singleton(InputType.POOL), prompt.getDataTypes());
  // assertEquals("Input a pool.", prompt.getMessage());
  // }
  //
  // @Test
  // public void testCancelSecondInput() {
  // workflow = new TestWorkflow(makeProgress(makeIntegerStep(INT_1), makePoolStep(POOL_ID)));
  // workflow.cancelInput();
  // assertFalse(workflow.isComplete());
  // assertEquivalent(makeProgress(makeIntegerStep(INT_1)), workflow.getProgress());
  // }
  //
  // @Test
  // public void testProcessSecondValidInput() {
  // workflow = new TestWorkflow(makeProgress(makeIntegerStep(INT_1)));
  // workflow.processInput(makePoolStep(POOL_ID));
  // assertTrue(workflow.isComplete());
  // assertEquivalent(makeProgress(makeIntegerStep(INT_1), makePoolStep(POOL_ID)), workflow.getProgress());
  // }
  //
  // @Test
  // public void testProcessInputAtFirstStepAfterTwoInputs() {
  // workflow = new TestWorkflow(makeProgress(makeIntegerStep(INT_1), makePoolStep(POOL_ID)));
  // workflow.processInput(1, makeIntegerStep(INT_2));
  // assertFalse(workflow.isComplete());
  // assertEquivalent(makeProgress(makeIntegerStep(INT_2)), workflow.getProgress());
  // }
  //
  // @Test
  // public void testProcessInputAfterComplete() {
  // workflow = new TestWorkflow(makeProgress(makeIntegerStep(INT_1), makePoolStep(POOL_ID)));
  // exception.expect(IllegalArgumentException.class);
  // workflow.processInput(makeIntegerStep(INT_2));
  // }

  private Workflow makeNewWorkflow() {
    Workflow workflow = new TestWorkflow();
    workflow.setProgress(makeEmptyProgress());
    return workflow;
  }

  private Progress makeEmptyProgress() {
    Progress progress = new ProgressImpl();
    progress.setWorkflowName(WORKFLOW_NAME);
    progress.setSteps(Collections.emptyList());
    return progress;
  }

  private PoolProgressStep makePoolStep(long poolId, int stepNumber) {
    PoolProgressStep step = makePoolStep(poolId);
    step.setStepNumber(stepNumber);
    return step;
  }

  private PoolProgressStep makePoolStep(long poolId) {
    PoolProgressStep step = new PoolProgressStep();
    Pool pool = new PoolImpl();
    pool.setId(poolId);
    step.setInput(pool);
    return step;
  }

  private IntegerProgressStep makeIntegerStep(int input, int stepNumber) {
    IntegerProgressStep step = makeIntegerStep(input);
    step.setStepNumber(stepNumber);
    return step;
  }

  private IntegerProgressStep makeIntegerStep(int input) {
    IntegerProgressStep step = new IntegerProgressStep();
    step.setInput(input);
    return step;
  }

  private void assertIntegerPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.INTEGER), prompt.getDataTypes());
    assertEquals("Input an integer.", prompt.getMessage());
  }

  private void assertPoolPrompt(WorkflowStepPrompt prompt) {
    assertEquals(Sets.newHashSet(InputType.POOL), prompt.getDataTypes());
    assertEquals("Input a pool.", prompt.getMessage());
  }

  /**
   * Match Progress object based on workflowName, stepNumber, and input fields
   */
  private void assertEquivalent(Progress expectedProgress, Progress actualProgress) {
    assertEquals(expectedProgress.getWorkflowName(), actualProgress.getWorkflowName());

    List<ProgressStep> expectedSteps = new ArrayList<>(expectedProgress.getSteps());
    List<ProgressStep> actualSteps = new ArrayList<>(actualProgress.getSteps());
    assertEquals(expectedSteps.size(), actualSteps.size());
    for (int i = 0; i < expectedSteps.size(); ++i) {
      assertEquals(expectedSteps.get(i).getStepNumber(), actualSteps.get(i).getStepNumber());
      if (expectedSteps.get(i) instanceof IntegerProgressStep) {
        assertEquals(((IntegerProgressStep) expectedSteps.get(i)).getInput(), ((IntegerProgressStep) actualSteps.get(i)).getInput());
      } else {
        assertEquals(((PoolProgressStep) expectedSteps.get(i)).getInput(), ((PoolProgressStep) actualSteps.get(i)).getInput());
      }
    }
  }

  private Progress makeProgress(ProgressStep... steps) {
    return makeProgress(PROGRESS_ID, WORKFLOW_NAME, makeUser(USER_ID), steps);
  }

  private Progress makeProgress(long id, WorkflowName workflowName, User user, ProgressStep... steps) {
    Progress progress = new ProgressImpl();

    progress.setId(id);
    progress.setWorkflowName(workflowName);
    progress.setUser(user);

    Date now = new Date();
    progress.setCreationTime(now);
    progress.setLastModified(now);
    progress.setSteps(Arrays.asList(steps));

    return progress;
  }

  private User makeUser(long id) {
    User user = new UserImpl();
    user.setUserId(id);
    return user;
  }
}
